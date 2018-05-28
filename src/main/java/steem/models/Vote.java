package steem.models;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Vote {
	private String voter;
	private Long weight;
	private BigInteger rshares;
	private Long percent;
	private BigInteger reputation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone="UTC")
	private Date time;
	public String getVoter() {
		return voter;
	}
	public void setVoter(String voter) {
		this.voter = voter;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public BigInteger getRshares() {
		return rshares;
	}
	public void setRshares(BigInteger rshares) {
		this.rshares = rshares;
	}
	public Long getPercent() {
		return percent;
	}
	public void setPercent(Long percent) {
		this.percent = percent;
	}
	public BigInteger getReputation() {
		return reputation;
	}
	public void setReputation(BigInteger reputation) {
		this.reputation = reputation;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
