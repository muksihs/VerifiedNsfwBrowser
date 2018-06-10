package muksihs.steem.postbrowser.shared;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

class BlogIndexEntries {
	private final Set<BlogIndexEntry> entries;

	public BlogIndexEntries() {
		entries = new TreeSet<>();
	}

	@JsonValue
	public Set<BlogIndexEntry> getEntries() {
		return entries;
	}

	@JsonCreator
	protected static BlogIndexEntries create(List<BlogIndexEntry> entriesList) {
		if (entriesList == null) {
			return null;
		}
		BlogIndexEntries entries = new BlogIndexEntries();
		entries.getEntries().addAll(entriesList);
		return entries;
	}
}