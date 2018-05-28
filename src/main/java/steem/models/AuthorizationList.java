package steem.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AuthorizationList {
	private List<Object> keyAuths;
	public AuthorizationList(){
	}
	@JsonCreator
	public AuthorizationList(List<Object> keyAuths) {
		this.setKeyAuths(keyAuths);
	}
	public List<Object> getKeyAuths() {
		return keyAuths;
	}
	public void setKeyAuths(List<Object> keyAuths) {
		this.keyAuths = keyAuths;
	}
}
