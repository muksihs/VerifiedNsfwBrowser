package steem.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class AuthorizationList {
	private List<AuthArray> keyAuths;
	public AuthorizationList(){
	}
	@JsonCreator
	public AuthorizationList(List<AuthArray> keyAuths) {
		this.setKeyAuths(keyAuths);
	}
	public List<AuthArray> getKeyAuths() {
		return keyAuths;
	}
	public void setKeyAuths(List<AuthArray> keyAuths) {
		this.keyAuths = keyAuths;
	}
	
	public static class AuthArray {
		private final int typeId;
		private final String publicKey;

		@JsonValue
		protected List<Object> _jsonValue(){
			List<Object> list = new ArrayList<Object>();
			list.add(publicKey);
			list.add(typeId);
			return list;
		}
		
		@JsonCreator
		public AuthArray(List<Object> list) {
			int typeId=-1;
			String publicKey=null;
			if (list==null) {
				this.typeId=typeId;
				this.publicKey=publicKey;
				return;
			}
			Iterator<Object> iList = list.iterator();
			while (iList.hasNext()) {
				Object next = iList.next();
				if (next instanceof Integer) {
					typeId=(Integer) next;
					break;
				}
			}
			iList = list.iterator();
			while (iList.hasNext()) {
				Object next = iList.next();
				if (next instanceof String) {
					publicKey=(String) next;
					break;
				}
			}
			this.typeId=typeId;
			this.publicKey=publicKey;
		}

		public int getTypeId() {
			return typeId;
		}

		public String getPublicKey() {
			return publicKey;
		}
	}
}
