package steem.models;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authorizations {
	@JsonProperty("weight_threshold")
	private BigInteger weightThreshold;
	@JsonProperty("account_auths")
	private List<AuthorizationList> accountAuths;
	@JsonProperty("key_auths")
	private List<AuthorizationList> keyAuths;
	
}
