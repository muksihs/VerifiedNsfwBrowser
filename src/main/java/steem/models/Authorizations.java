package steem.models;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import steem.models.AuthorizationList.AuthArray;

public class Authorizations {
	@JsonProperty("weight_threshold")
	private BigInteger weightThreshold;
	@JsonProperty("account_auths")
	private List<AuthArray> accountAuths;
	@JsonProperty("key_auths")
	private List<AuthArray> keyAuths;
	public BigInteger getWeightThreshold() {
		return weightThreshold;
	}
	public void setWeightThreshold(BigInteger weightThreshold) {
		this.weightThreshold = weightThreshold;
	}
	public List<AuthArray> getAccountAuths() {
		return accountAuths;
	}
	public void setAccountAuths(List<AuthArray> accountAuths) {
		this.accountAuths = accountAuths;
	}
	public List<AuthArray> getKeyAuths() {
		return keyAuths;
	}
	public void setKeyAuths(List<AuthArray> keyAuths) {
		this.keyAuths = keyAuths;
	}
}
