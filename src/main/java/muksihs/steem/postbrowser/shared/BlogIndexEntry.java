package muksihs.steem.postbrowser.shared;

import java.util.Date;
import java.util.List;

public class BlogIndexEntry implements Comparable<BlogIndexEntry>{
	
	private String author;
	private Date created;
	private String permlink;
	private List<String> tags;
	
	@Override
	public int compareTo(BlogIndexEntry o) {
		if (getCreated().compareTo(o.created)!=0) {
			return getCreated().compareTo(o.getCreated());
		}
		if (!getAuthor().equalsIgnoreCase(o.getAuthor())) {
			return getAuthor().compareToIgnoreCase(o.getAuthor());
		}
		return getPermlink().compareToIgnoreCase(o.getPermlink());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BlogIndexEntry)) {
			return false;
		}
		BlogIndexEntry other = (BlogIndexEntry) obj;
		if (author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!author.equals(other.author)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (permlink == null) {
			if (other.permlink != null) {
				return false;
			}
		} else if (!permlink.equals(other.permlink)) {
			return false;
		}
		if (tags == null) {
			if (other.tags != null) {
				return false;
			}
		} else if (!tags.equals(other.tags)) {
			return false;
		}
		return true;
	}
	public String getAuthor() {
		return author;
	}
	public Date getCreated() {
		return created;
	}
	public String getPermlink() {
		return permlink;
	}
	public List<String> getTags() {
		return tags;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((permlink == null) ? 0 : permlink.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public void setPermlink(String permlink) {
		this.permlink = permlink;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}