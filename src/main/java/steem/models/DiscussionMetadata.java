package steem.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscussionMetadata implements HasJsonAnyGetterSetter {
	
	private String thumbnail;
	private List<String> image;
	private List<String> links;
	private String app;
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	private String format;
	
	private List<String> tags=new ArrayList<>();
	private List<String> users=new ArrayList<>();
	public List<String> getTags() {
		return tags;
	}
	public List<String> getUsers() {
		return users;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
	private Map<String, Object> _properties=new HashMap<>();
	@Override
	public Map<String, Object> anyGetterSetterMap() {
		return _properties;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public List<String> getLinks() {
		return links;
	}
	public void setLinks(List<String> links) {
		this.links = links;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}