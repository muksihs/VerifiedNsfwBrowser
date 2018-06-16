package muksihs.steem.postbrowser.shared;

import java.util.Date;
import java.util.List;

public class BlogIndexEntry implements Comparable<BlogIndexEntry>{
	
	private String author;
	private Date created;
	private String permlink;
	private List<String> tags;
	
	/**
	 * Date descending, author ascending.
	 */
	@Override
	public int compareTo(BlogIndexEntry o) {
		Date created = getCreated();
		Date oCreated = o.created;
		if (created==null) {
			created=new Date(0);
		}
		if (oCreated==null) {
			created = new Date(0);
		}
		if (created.compareTo(oCreated)!=0) {
			return -created.compareTo(o.getCreated());
		}
		String author = getAuthor();
		String oAuthor = o.getAuthor();
		if (author==null) {
			author = "";
		}
		if (oAuthor==null) {
			oAuthor = "";
		}
		if (!author.equalsIgnoreCase(oAuthor)) {
			return author.compareToIgnoreCase(oAuthor);
		}
		String permlink = getPermlink();
		String oPermlink = o.getPermlink();
		if (permlink==null) {
			permlink="";
		}
		if (oPermlink==null) {
			oPermlink="";
		}
		return permlink.compareToIgnoreCase(oPermlink);
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