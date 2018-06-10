package muksihs.steem.postbrowser.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gwt.user.client.Timer;

import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;

/**
 * Store and lookup blog entries by tag and author. Simple case-insensitive
 * index.
 * 
 * @author muksihs
 *
 */
public class BlogIndex implements GlobalAsyncEventBus {
	private final Map<String, Set<BlogIndexEntry>> BY_TAG;

	// deduped values for serialization
	@JsonValue
	protected Collection<BlogIndexEntry> jsonValue() {
		Set<BlogIndexEntry> values = new HashSet<>();
		for (Set<BlogIndexEntry> entries : BY_TAG.values()) {
			values.addAll(entries);
		}
		return values;
	}

	// always create new index upon creation from deduped serialized list
	@JsonCreator
	protected static BlogIndex jsonCreate(Collection<BlogIndexEntry> entries) {
		BlogIndex index = new BlogIndex();
		index.addAll(entries);
		return index;
	}

	public static enum FilteredListMode {
		AND, OR;
	}

	public List<BlogIndexEntry> getFilteredList(//
			FilteredListMode mode, //
			Collection<String> includeTags, //
			Collection<String> excludeTags) {
		List<BlogIndexEntry> list = new ArrayList<>();
		if (mode == null) {
			mode = FilteredListMode.AND;
		}
		if (includeTags == null || includeTags.isEmpty()) {
			// include ALL posts if no tags provided
			for (Set<BlogIndexEntry> entrySets : BY_TAG.values()) {
				list.addAll(entrySets);
			}
		} else {
			ensureTagEntriesExist(includeTags);
			Iterator<String> iTags = includeTags.iterator();
			list.addAll(BY_TAG.get(iTags.next()));
			includeTagLoop: while (iTags.hasNext()) {
				String tag = iTags.next();
				Set<BlogIndexEntry> tmp = BY_TAG.get(tag);
				switch (mode) {
				case AND:
					list.retainAll(tmp);
					if (list.isEmpty()) {
						break includeTagLoop;
					}
					break;
				case OR:
					list.addAll(tmp);
					break;
				}
			}
			if (excludeTags != null && !excludeTags.isEmpty()) {
				Iterator<String> iExclude = excludeTags.iterator();
				excludeTagLoop: while (iExclude.hasNext()) {
					String tag = iExclude.next();
					list.removeAll(BY_TAG.get(tag));
					if (list.isEmpty()) {
						break excludeTagLoop;
					}
				}
			}
		}
		return list;
	}

	public BlogIndex() {
		BY_TAG = new HashMap<>();
	}

	public void add(BlogIndexEntry entry) {
		if (entry == null) {
			return;
		}
		Set<String> tags = new HashSet<>();
		if (entry.getTags() != null) {
			for (String tag : entry.getTags()) {
				tag = tag.trim().toLowerCase();
				if (tag.isEmpty()) {
					continue;
				}
				tags.add(tag);
			}
		}
		if (entry.getAuthor() != null) {
			String author = entry.getAuthor();
			author = author.trim().toLowerCase();
			if (!author.isEmpty()) {
				tags.add(author);
			}
		}
		ensureTagEntriesExist(tags);
		for (String tag : tags) {
			BY_TAG.get(tag).add(entry);
		}
	}

	private void ensureTagEntriesExist(Collection<String> tags) {
		for (String tag : tags) {
			if (!BY_TAG.containsKey(tag)) {
				BY_TAG.put(tag, new HashSet<BlogIndexEntry>());
			}
		}
	}

	public void addAll(Collection<BlogIndexEntry> entries) {
		if (entries == null || entries.isEmpty()) {
			return;
		}
		fireEvent(new Event.Indexing(true));
		List<BlogIndexEntry> tmp = new ArrayList<>(entries);
		Iterator<BlogIndexEntry> iEntries = tmp.iterator();
		int count = 0;
		while (count++ < 10 && iEntries.hasNext()) {
			add(iEntries.next());
			iEntries.remove();
		}
		if (tmp.isEmpty()) {
			fireEvent(new Event.Indexing(false));
			return;
		}
		new Timer() {
			@Override
			public void run() {
				addAll(tmp);
			}
		}.schedule(0);
	}

	public Set<String> getTags() {
		return new HashSet<>(BY_TAG.keySet());
	}

	public List<BlogIndexEntry> getEntries() {
		return new ArrayList<>(jsonValue());
	}
}
