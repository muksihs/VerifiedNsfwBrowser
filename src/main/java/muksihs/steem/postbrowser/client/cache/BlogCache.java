package muksihs.steem.postbrowser.client.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;

import blazing.chain.LZSEncoding;
import muksihs.steem.postbrowser.shared.SteemPostingInfo;

public class BlogCache {
	private static final String BLOG_INFO = "blog-info";

	// only deserialize the date property for the expires check loop
	protected interface ExpiresCodec extends ObjectMapper<CachedExpiration> {
	}

	protected final ExpiresCodec expiresCodec = GWT.create(ExpiresCodec.class);

	protected interface Codec extends ObjectMapper<CachedAccountInfo> {
	}

	protected final Codec codec = GWT.create(Codec.class);
	private final String prefix;
	private final Map<String, String> storage;
	private final Map<String, String> memCache;

	public BlogCache() {
		memCache = new HashMap<>();
		Storage localStorageIfSupported = Storage.getLocalStorageIfSupported();
		if (localStorageIfSupported!=null) {
			storage = new StorageMap(localStorageIfSupported);
		} else {
			storage = memCache;
		}
		prefix = BLOG_INFO + ":";
	}

	public void put(String key, SteemPostingInfo info) {
		_put(prefix + key, info);
	}

	private void _put(String prefixedKey, SteemPostingInfo info) {
		CachedAccountInfo value = new CachedAccountInfo(info);
		String jsonString = codec.write(value);
		jsonString = LZSEncoding.compressToUTF16(jsonString);
		memCache.put(prefixedKey, jsonString);
		try {
			storage.put(prefixedKey, jsonString);
			return;
		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
			try {
				storage.put(prefixedKey, jsonString);
				return;
			} catch (Exception e1) {
				GWT.log(e1.getMessage(), e1);
				storage.clear();
				storage.put(prefixedKey, jsonString);
				return;
			}
		}
	}

	public SteemPostingInfo get(String key) {
		String prefixedKey = prefix + key;
		SteemPostingInfo steemPostingInfo = _get(prefixedKey);
		return steemPostingInfo;
	}

	private SteemPostingInfo _get(String prefixedKey) {
		String jsonString = _getJsonString(prefixedKey);
		if (jsonString == null) {
			return null;
		}
		try {
			CachedAccountInfo decoded = codec.read(jsonString);
			if (decoded == null || decoded.isExpired()) {
				return null;
			}
			SteemPostingInfo steemPostingInfo = decoded.getAccountInfo();
			return steemPostingInfo;
		} catch (Exception e) {
			GWT.log("cache exception: " + prefixedKey, e);
			_remove(prefixedKey);
			return null;
		}
	}

	private String _getJsonString(String prefixedKey) {
		String jsonString = memCache.get(prefixedKey);
		if (jsonString == null) {
			jsonString = storage.get(prefixedKey);
			if (jsonString != null) {
				// copy into memCache the item it did not have
				memCache.put(prefixedKey, jsonString);
			}
		}
		if (jsonString == null) {
			return null;
		}
		// remove legacy data
		if (jsonString.contains("\"expires\"")) {
			GWT.log("remove legacy data: " + prefixedKey);
			return null;
		}
		try {
			return LZSEncoding.decompressFromUTF16(jsonString);
		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
			_remove(prefixedKey);
		}
		return null;
	}

	private Set<String> prefixedKeySet() {
		Set<String> set = new TreeSet<>(storage.keySet());
		set.addAll(memCache.keySet());
		return set;
	}

	public void clear() {
		for (String prefixedKey : prefixedKeySet()) {
			if (prefixedKey.startsWith(BLOG_INFO)) {
				_remove(prefixedKey);
			}
		}
	}

	public void remove(String key) {
		String prefixedKey = prefix + key;
		_remove(prefixedKey);
	}

	private void _remove(String prefixedKey) {
		storage.remove(prefixedKey);
		memCache.remove(prefixedKey);
	}
}
