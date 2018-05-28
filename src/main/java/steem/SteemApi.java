package steem;

import java.util.HashMap;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;
import steem.model.accountinfo.AccountInfo;
import steem.models.Discussion;
import steem.models.Discussions;
import steem.models.TrendingTags;

@JsType(namespace = "steem", name = "api", isNative = true)
public class SteemApi {
	public static native void getAccounts(String[] username, SteemCallbackArray<AccountInfo> callback);

	@JsMethod(name = "getContentReplies")
	private static native void _getContentReplies(String username, String permlink,
			SteemCallback_old<JavaScriptObject> discussion);

	@JsOverlay
	public static void getContentReplies(String username, String permlink, SteemCallback_old<Discussions> cb) {

		SteemCallback_old<JavaScriptObject> _cb = new SteemCallback_old<JavaScriptObject>() {
			@Override
			public void onResult(JavaScriptObject error, JavaScriptObject result) {
				if (error != null || result == null) {
					cb.onResult(error, null);
					return;
				}
				String json = JsonUtils.stringify(result);
				json = "{\"discussions\":" + json + "}";
				Discussions discussions;
				try {
					discussions = Util.discussionsCodec.read(json);
				} catch (Exception e) {
					DomGlobal.console.log("=== " + e.getMessage());
					GWT.log(e.getMessage(), e);
					cb.onResult(error, null);
					return;
				}
				cb.onResult(error, discussions);
			}
		};

		_getContentReplies(username, permlink, _cb);
	}

	@JsMethod(name = "getContent")
	private static native void _getContent(String username, String permlink, SteemCallback_old<JavaScriptObject> cb);

	@JsOverlay
	public static void getContent(String username, String permlink, SteemCallback_old<Discussion> cb) {
		SteemCallback_old<JavaScriptObject> parseCb = new SteemCallback_old<JavaScriptObject>() {
			@Override
			public void onResult(JavaScriptObject error, JavaScriptObject result) {
				if (error != null) {
					cb.onResult(error, null);
					return;
				}
				if (result == null) {
					DomGlobal.console.log("getDiscussionsByBlog: NULL RESPONSE.");
					return;
				}
				try {
					String stringify = JsonUtils.stringify(result);
					Discussion d = Util.discussionCodec.read(stringify);
					if (d == null) {
						DomGlobal.console.log("Decoding FAIL: d == null!");
						cb.onResult(error, null);
						return;
					}
					cb.onResult(error, d);
				} catch (Exception e) {
					DomGlobal.console.log("Exception: " + e.getMessage());
				}
			}
		};
		_getContent(username, permlink, parseCb);
	}

	@JsMethod(name = "getTrendingTags")
	private static native void _getTrendingTags(String afterTag, int limit, SteemJsCallback jsCallback);

	@JsOverlay
	public static void getTrendingTags(String afterTag, int limit, TrendingTagsCallback callback) {
		_getTrendingTags(afterTag, limit, (error, result) -> {
			callback.onResult(error, result);
		});
	}

	public static interface TrendingTagsMapper extends ObjectMapper<TrendingTags> {
	}

	public static interface TrendingTagsCallback extends SteemTypedListCallback<TrendingTags, TrendingTagsMapper> {
		@Override
		default TrendingTagsMapper mapper() {
			return GWT.create(TrendingTagsMapper.class);
		}
	}

	@JsMethod(name = "getDiscussionsByCreated")
	private static native void _getDiscussionsByCreated(JavaScriptObject query, //
			SteemJsCallback jsCallback);

	@JsOverlay
	public static void getDiscussionsByCreated(String tag, //
			int limit, //
			DiscussionsCallback callback) {
		getDiscussionsByCreated(tag, null, null, limit, callback);
	}
	@JsOverlay
	public static void getDiscussionsByCreated(String tag, //
			String startPermlink, //
			String startAuthor, //
			int limit, //
			DiscussionsCallback callback) {
		JSONObject json = new JSONObject();
		json.put("tag", new JSONString(tag));
		json.put("limit", new JSONNumber(limit));
		if (!(startPermlink==null||startPermlink.trim().isEmpty())) {
			json.put("start_permlink", new JSONString(startPermlink));
		}
		if (!(startAuthor==null||startAuthor.trim().isEmpty())) {
			json.put("start_author", new JSONString(startAuthor));
		}
		_getDiscussionsByCreated(json.getJavaScriptObject(),
				(error, result) -> callback.onResult(error, result));
	}
	
	public static interface HashMapMapper extends ObjectMapper<HashMap<String, Object>> {}

	public static interface DiscussionsMapper extends ObjectMapper<Discussions> {
	}

	public static interface DiscussionsCallback extends SteemTypedListCallback<Discussions, DiscussionsMapper> {
		@Override
		default DiscussionsMapper mapper() {
			return GWT.create(DiscussionsMapper.class);
		}
	}

	private static native void getDiscussionsByBlog(JavaScriptObject query, SteemCallback_old<JavaScriptObject> cb);

	@JsOverlay
	public static void getDiscussionsByBlog(String username, int count, SteemCallback_old<Discussions> cb) {
		if (username == null) {
			username = "";
		}
		username = username.replace("\"", "\\\"");
		JSONObject query = new JSONObject();
		query.put("tag", new JSONString(username));
		query.put("limit", new JSONNumber(count));
		SteemCallback_old<JavaScriptObject> parseCb = new SteemCallback_old<JavaScriptObject>() {
			@Override
			public void onResult(JavaScriptObject error, JavaScriptObject result) {
				if (error != null) {
					cb.onResult(error, null);
					return;
				}
				if (result == null) {
					DomGlobal.console.log("getDiscussionsByBlog: NULL RESPONSE.");
					return;
				}
				try {
					String stringify = "{\"discussions\":" + JsonUtils.stringify(result) + "}";
					Discussions d = Util.discussionsCodec.read(stringify);
					if (d == null) {
						DomGlobal.console.log("Decoding FAIL: d == null!");
						cb.onResult(error, null);
						return;
					}
					cb.onResult(error, d);
				} catch (Exception e) {
					DomGlobal.console.log("Exception: " + e.getMessage());
				}
			}
		};
		getDiscussionsByBlog(query.getJavaScriptObject(), parseCb);
	}

	public static class Util {
		// old
		public static interface DiscussionsCodec extends ObjectMapper<Discussions> {
		}

		public static DiscussionsCodec discussionsCodec = GWT.create(DiscussionsCodec.class);

		public static interface DiscussionCodec extends ObjectMapper<Discussion> {
		}

		public static DiscussionCodec discussionCodec = GWT.create(DiscussionCodec.class);

//		public static interface JsonMetadataCodec extends ObjectMapper<JsonMetadata> {
//		}

//		public static JsonMetadataCodec jsonMetadataCodec = GWT.create(JsonMetadataCodec.class);
	}
}