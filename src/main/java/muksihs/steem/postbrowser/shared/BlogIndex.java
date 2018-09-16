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
	public Set<String> getAuthors() {
		return authors;
	}

	private final Map<String, BlogIndexEntry> mostRecentByAuthor;
	private final Map<String, Boolean> indexingComplete;

	public static enum FilteredListMode {
		AND, OR;
	}

	public BlogIndexEntry getMostRecentEntry(String author) {
		return mostRecentByAuthor.get(author);
	}
	
	public boolean isIndexingComplete(String author) {
		return indexingComplete.containsKey(author)?indexingComplete.get(author):false;
	}
	
	public void setIndexingComplete(String author, boolean isIndexingComplete) {
		indexingComplete.put(author, isIndexingComplete);
	}

	/**
	 * List of authors sorted by their newest NSFW only indexed posts, date descending
	 * @return
	 */
	public List<String> getNewestDateSortedAuthors() {
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
			String lcFirstTag = iTags.next().toLowerCase();
			list.addAll(byTag.get(lcFirstTag));
			includeTagLoop: while (iTags.hasNext()) {
				String lcTag = iTags.next().toLowerCase();
				Set<BlogIndexEntry> tmp = byTag.get(lcTag);
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
		}
		if (excludeTags != null && !excludeTags.isEmpty()) {
			Iterator<String> iExclude = excludeTags.iterator();
			excludeTagLoop: while (iExclude.hasNext()) {
				String lcTag = iExclude.next().toLowerCase();
				list.removeAll(byTag.get(lcTag));
				if (list.isEmpty()) {
					break excludeTagLoop;
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
		indexingComplete = new HashMap<>();
	}

	public void add(String username, BlogIndexEntry entry) {
		if (entry == null) {
			return;
		}
		Set<String> tags = new HashSet<>();
		if (entry.getTags() != null) {
			for (String tag : entry.getTags()) {
				String lcTag = tag.trim().toLowerCase();
				if (tag.isEmpty()) {
					continue;
				}
				tags.add(lcTag);
			}
		}
		authors.add(username);
		final Date created = entry.getCreated();
		//don't index reblogs
		if (username.equalsIgnoreCase(entry.getAuthor())) {
			tags.add("@" + username.toLowerCase());
			//Only use the most recent NSFW post for recent by author listing
			if (created != null && tags.contains("nsfw")) {
				BlogIndexEntry prev = mostRecentByAuthor.get(username);
				if (prev == null || created.after(prev.getCreated())) {
					mostRecentByAuthor.put(username, entry);
				}
			}
			ensureTagEntriesExist(tags);
			for (String tag : tags) {
				String lcTag = tag.toLowerCase();
				byTag.get(lcTag).add(entry);
			}
		}
	}

	private void ensureTagEntriesExist(Collection<String> tags) {
		for (String tag : tags) {
			String lcTag = tag.toLowerCase();
			if (!byTag.containsKey(lcTag)) {
				byTag.put(lcTag, new HashSet<BlogIndexEntry>());
			}
		}
	}

	public void addAll(String username, Collection<BlogIndexEntry> entries) {
		if (entries == null || entries.isEmpty()) {
			return;
		}
		fireEvent(new Event.Indexing(true));
		List<BlogIndexEntry> tmp = new ArrayList<>(entries);
		Iterator<BlogIndexEntry> iEntries = tmp.iterator();
		int count = 0;
		while (count++ < 10 && iEntries.hasNext()) {
			add(username, iEntries.next());
			iEntries.remove();
		}
		if (tmp.isEmpty()) {
			fireEvent(new Event.Indexing(false));
			return;
		}
		new Timer() {
			@Override
			public void run() {
				addAll(username, tmp);
			}
		}.schedule(0);
	}

	public Set<String> getTags() {
		return new TreeSet<>(byTag.keySet());
	}
}
