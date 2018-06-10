package steem.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscussionMetadata implements HasJsonAnyGetterSetter {
	
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
}