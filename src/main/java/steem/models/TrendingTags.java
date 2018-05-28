package steem.models;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrendingTags {
	private List<TrendingTag> list;
	public static class TrendingTag {
		private String name;
		@JsonProperty("total_payouts")
		private String totalPayouts;
		@JsonProperty("net_votes")
		private BigInteger netVotes;
		@JsonProperty("top_posts")
		private BigInteger topPosts;
		private BigInteger comments;
		private BigInteger trending;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTotalPayouts() {
			return totalPayouts;
		}
		public void setTotalPayouts(String totalPayouts) {
			this.totalPayouts = totalPayouts;
		}
		public BigInteger getNetVotes() {
			return netVotes;
		}
		public void setNetVotes(BigInteger netVotes) {
			this.netVotes = netVotes;
		}
		public BigInteger getTopPosts() {
			return topPosts;
		}
		public void setTopPosts(BigInteger topPosts) {
			this.topPosts = topPosts;
		}
		public BigInteger getComments() {
			return comments;
		}
		public void setComments(BigInteger comments) {
			this.comments = comments;
		}
		public BigInteger getTrending() {
			return trending;
		}
		public void setTrending(BigInteger trending) {
			this.trending = trending;
		}
	}
	public List<TrendingTag> getList() {
		return list;
	}
	public void setList(List<TrendingTag> trendingTags) {
		this.list = trendingTags;
	}
}
