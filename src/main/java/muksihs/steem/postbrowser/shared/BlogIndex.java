package muksihs.steem.postbrowser.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	private final Map<String, Set<BlogIndexEntry>> byTag;
	private final Set<String> authors;
	private final Map<String, BlogIndexEntry> mostRecentByAuthor;

	// deduped values for serialization
	@JsonValue
	protected Collection<BlogIndexEntry> jsonValue() {
		Set<BlogIndexEntry> values = new HashSet<>();
		for (Set<BlogIndexEntry> entries : byTag.values()) {
			values.addAll(entries);
		}
		return values;
	}

	// always build new index upon deserialization
	@JsonCreator
	protected static BlogIndex jsonCreate(Collection<BlogIndexEntry> entries) {
		BlogIndex index = new BlogIndex();
		index.addAll(entries);
		return index;
	}

	public static enum FilteredListMode {
		AND, OR;
	}

	public BlogIndexEntry getMostRecentEntry(String author) {
		return mostRecentByAuthor.get(author);
	}

	public List<String> getDateSortedAuthors() {
		List<String> authors = new ArrayList<>(mostRecentByAuthor.keySet());
		Collections.sort(authors, (a, b) -> {
			BlogIndexEntry ea = mostRecentByAuthor.get(a);
			BlogIndexEntry eb = mostRecentByAuthor.get(b);
			if (ea != null && eb != null) {
				if (!ea.getCreated().equals(eb.getCreated())) {
					return -ea.getCreated().compareTo(eb.getCreated());
				}
			}
			return a.compareToIgnoreCase(b);
		});
		return authors;
	}
	
	public Set<String> getAvailableTags(){
		return new TreeSet<>(byTag.keySet());
	}
	
	public List<BlogIndexEntry> getFilteredList(//
			FilteredListMode mode, //
			Collection<String> includeTags, //
			Collection<String> excludeTags){
		return getFilteredList(mode, includeTags, excludeTags, false);
	}

	public List<BlogIndexEntry> getFilteredList(//
			FilteredListMode mode, //
			Collection<String> includeTags, //
			Collection<String> excludeTags, //
			boolean singleAuthors) {
		List<BlogIndexEntry> list = new ArrayList<>();
		if (mode == null) {
			mode = FilteredListMode.AND;
		}
		if (includeTags == null || includeTags.isEmpty()) {
			// include ALL posts if no tags provided
			for (Set<BlogIndexEntry> entrySets : byTag.values()) {
				list.addAll(entrySets);
			}
		} else {
			//Simplify logic by removing need for NULL checks.
			ensureTagEntriesExist(includeTags);
			Iterator<String> iTags = includeTags.iterator();
			list.addAll(byTag.get(iTags.next()));
			includeTagLoop: while (iTags.hasNext()) {
				String tag = iTags.next();
				Set<BlogIndexEntry> tmp = byTag.get(tag);
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
					list.removeAll(byTag.get(tag));
					if (list.isEmpty()) {
						break excludeTagLoop;
					}
				}
			}
		}
		/*
		 * posts generally show up under more than one tag, so dedupe
		 */
		dedupe(list);
		/*
		 * Sort by date descending, author ascending
		 */
		Collections.sort(list);
		if (singleAuthors) {
			//one post per author
			Set<String> already = new HashSet<>();
			Iterator<BlogIndexEntry> iList = list.iterator();
			while (iList.hasNext()) {
				String author = iList.next().getAuthor();
				if (already.contains(author)) {
					iList.remove();
					continue;
				}
				already.add(author);
			}
		}
		return list;
	}

	private void dedupe(List<BlogIndexEntry> list) {
		Iterator<BlogIndexEntry> iList = list.iterator(); 
		Set<BlogIndexEntry> already = new HashSet<>();
		while (iList.hasNext()) {
			BlogIndexEntry next = iList.next();
			if (already.contains(next)) {
				iList.remove();
				continue;
			}
			already.add(next);
		}
	}

	public BlogIndex() {
		byTag = new HashMap<>();
		authors = new TreeSet<>();
		mostRecentByAuthor = new HashMap<>();
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
		author: if (entry.getAuthor() != null) {
			String author = entry.getAuthor();
			author = author.trim().toLowerCase();
			if (author.isEmpty()) {
				break author;
			}
			authors.add(author);
			tags.add("@" + author);
			final Date created = entry.getCreated();
			if (created != null) {
				BlogIndexEntry prev = mostRecentByAuthor.get(author);
				if (prev == null || created.after(prev.getCreated())) {
					mostRecentByAuthor.put(author, entry);
				}
			}
		}
		ensureTagEntriesExist(tags);
		for (String tag : tags) {
			byTag.get(tag).add(entry);
		}
	}

	private void ensureTagEntriesExist(Collection<String> tags) {
		for (String tag : tags) {
			if (!byTag.containsKey(tag)) {
				byTag.put(tag, new HashSet<BlogIndexEntry>());
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
			fireEvent(new Event.SetAvailableTags(new TreeSet<>(byTag.keySet())));
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
		return new HashSet<>(byTag.keySet());
	}

	public List<BlogIndexEntry> getEntries() {
		return new ArrayList<>(jsonValue());
	}
}
