package muksihs.steem.postbrowser.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class BlogIndexEntry implements Comparable<BlogIndexEntry>{
	
	private long entryId;
	private String title;
	private String author;
	private Date created;
	private String permlink;
	private String thumbnail;
	private List<String> image;
	private List<String> tags;
	private List<String> combinedImages;
	private String customUrl;
	private String customUrlName;
	
	public String getCustomUrlName() {
		return customUrlName;
	}

	public void setCustomUrlName(String customUrlName) {
		this.customUrlName = customUrlName;
	}

	public void setCombinedImages(List<String> combinedImages) {
		this.combinedImages = combinedImages;
	}

	public BlogIndexEntry() {
		combinedImages = new ArrayList<>();
	}
	
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
		if (entryId != other.getEntryId()) {
			return false;
		}
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
		this.tags = new ArrayList<>(tags);
		ListIterator<String> iter = this.tags.listIterator();
		while (iter.hasNext()) {
			String next = iter.next().trim();
			if (next.isEmpty()) {
				iter.remove();
				continue;
			}
			iter.set(next);
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public List<String> getCombinedImages() {
		return combinedImages==null?new ArrayList<>():combinedImages;
	}

	public void addToCombinedImages(List<String> images) {
		if (images==null||images.isEmpty()) {
			return;
		}
		for (String image: images) {
			addToCombinedImages(image);
		}
	}

	public void addToCombinedImages(String image) {
		if (image==null||image.trim().isEmpty()) {
			return;
		}
		if (!combinedImages.contains(image)) {
			combinedImages.add(image);
		}
	}

	public String getCustomUrl() {
		return customUrl;
	}

	public void setCustomUrl(String customUrl) {
		this.customUrl = customUrl;
	}

	public long getEntryId() {
		return entryId;
	}

	public void setEntryId(long entryId) {
		this.entryId = entryId;
	}
}