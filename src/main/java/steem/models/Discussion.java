package steem.models;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Discussion implements HasJsonAnyGetterSetter {
	@JsonProperty("first_reblogged_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date firstRebloggedOn;
	public Date getFirstRebloggedOn() {
		return firstRebloggedOn;
	}
	public void setFirstRebloggedOn(Date firstRebloggedOn) {
		this.firstRebloggedOn = firstRebloggedOn;
	}
	private BigInteger id;
	private String author;
	private String permlink;
	private String category;
	@JsonProperty("parent_author")
	private String parentAuthor;
	@JsonProperty("parent_permlink")
	private String parentPermlink;
	private String title;
	private String body;
	@JsonProperty("json_metadata")
	private String jsonMetadata;
	@JsonProperty("last_update")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date lastUpdate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date created;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date active;
	@JsonProperty("last_payout")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date lastPayout;
	private BigInteger depth;
	private BigInteger children;
	@JsonProperty("net_rshares")
	private BigInteger netRshares;
	@JsonProperty("abs_rshares")
	private BigInteger absRshares;
	@JsonProperty("vote_rshares")
	private BigInteger voteRshares;
	@JsonProperty("children_abs_rshares")
	private BigInteger childrenAbsRshares;
	@JsonProperty("cashout_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date cashoutTime;
	@JsonProperty("max_cashout_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Date maxCashoutTime;
	@JsonProperty("total_vote_weight")
	private BigInteger totalVoteWeight;
	@JsonProperty("reward_weight")
	private BigInteger rewardWeight;
	@JsonProperty("total_payout_value")
	private SimpleAsset totalPayoutValue;
	@JsonProperty("curator_payout_value")
	private SimpleAsset curatorPayoutValue;
	@JsonProperty("author_rewards")
	private BigInteger authorRewards;
	@JsonProperty("net_votes")
	private BigInteger netVotes;
	@JsonProperty("root_author")
	private String rootAuthor;
	@JsonProperty("root_permlink")
	private String rootPermlink;
	@JsonProperty("max_accepted_payout")
	private SimpleAsset maxAcceptedPayout;
	@JsonProperty("percent_steem_dollars")
	private BigInteger percentSteemDollars;
	@JsonProperty("allow_replies")
	private Boolean allowReplies;
	@JsonProperty("allow_votes")
	private Boolean allowVotes;
	@JsonProperty("allow_curation_rewards")
	private Boolean allowCurationRewards;
	private List<Beneficiary> beneficiaries;
	private String url;
	@JsonProperty("root_title")
	private String rootTitle;
	@JsonProperty("pending_payout_value")
	private SimpleAsset pendingPayoutValue;
	@JsonProperty("total_pending_payout_value")
	private SimpleAsset totalPendingPayoutValue;
	@JsonProperty("active_votes")
	private List<Vote> activeVotes;
	private List<Object> replies;
	@JsonProperty("author_reputation")
	private BigInteger authorReputation;
	private SimpleAsset promoted;
	@JsonProperty("body_length")
	private BigInteger bodyLength;
	@JsonProperty("reblogged_by")
	private List<String> rebloggedBy;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPermlink() {
		return permlink;
	}
	public void setPermlink(String permlink) {
		this.permlink = permlink;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getParentAuthor() {
		return parentAuthor;
	}
	public void setParentAuthor(String parentAuthor) {
		this.parentAuthor = parentAuthor;
	}
	public String getParentPermlink() {
		return parentPermlink;
	}
	public void setParentPermlink(String parentPermlink) {
		this.parentPermlink = parentPermlink;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getJsonMetadata() {
		return jsonMetadata;
	}
	public void setJsonMetadata(String jsonMetadata) {
		this.jsonMetadata = jsonMetadata;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getActive() {
		return active;
	}
	public void setActive(Date active) {
		this.active = active;
	}
	public Date getLastPayout() {
		return lastPayout;
	}
	public void setLastPayout(Date lastPayout) {
		this.lastPayout = lastPayout;
	}
	public BigInteger getDepth() {
		return depth;
	}
	public void setDepth(BigInteger depth) {
		this.depth = depth;
	}
	public BigInteger getChildren() {
		return children;
	}
	public void setChildren(BigInteger children) {
		this.children = children;
	}
	public BigInteger getNetRshares() {
		return netRshares;
	}
	public void setNetRshares(BigInteger netRshares) {
		this.netRshares = netRshares;
	}
	public BigInteger getAbsRshares() {
		return absRshares;
	}
	public void setAbsRshares(BigInteger absRshares) {
		this.absRshares = absRshares;
	}
	public BigInteger getVoteRshares() {
		return voteRshares;
	}
	public void setVoteRshares(BigInteger voteRshares) {
		this.voteRshares = voteRshares;
	}
	public BigInteger getChildrenAbsRshares() {
		return childrenAbsRshares;
	}
	public void setChildrenAbsRshares(BigInteger childrenAbsRshares) {
		this.childrenAbsRshares = childrenAbsRshares;
	}
	public Date getCashoutTime() {
		return cashoutTime;
	}
	public void setCashoutTime(Date cashoutTime) {
		this.cashoutTime = cashoutTime;
	}
	public Date getMaxCashoutTime() {
		return maxCashoutTime;
	}
	public void setMaxCashoutTime(Date maxCashoutTime) {
		this.maxCashoutTime = maxCashoutTime;
	}
	public BigInteger getTotalVoteWeight() {
		return totalVoteWeight;
	}
	public void setTotalVoteWeight(BigInteger totalVoteWeight) {
		this.totalVoteWeight = totalVoteWeight;
	}
	public BigInteger getRewardWeight() {
		return rewardWeight;
	}
	public void setRewardWeight(BigInteger rewardWeight) {
		this.rewardWeight = rewardWeight;
	}
	public SimpleAsset getTotalPayoutValue() {
		return totalPayoutValue;
	}
	public void setTotalPayoutValue(SimpleAsset totalPayoutValue) {
		this.totalPayoutValue = totalPayoutValue;
	}
	public SimpleAsset getCuratorPayoutValue() {
		return curatorPayoutValue;
	}
	public void setCuratorPayoutValue(SimpleAsset curatorPayoutValue) {
		this.curatorPayoutValue = curatorPayoutValue;
	}
	public BigInteger getAuthorRewards() {
		return authorRewards;
	}
	public void setAuthorRewards(BigInteger authorRewards) {
		this.authorRewards = authorRewards;
	}
	public BigInteger getNetVotes() {
		return netVotes;
	}
	public void setNetVotes(BigInteger netVotes) {
		this.netVotes = netVotes;
	}
	public String getRootAuthor() {
		return rootAuthor;
	}
	public void setRootAuthor(String rootAuthor) {
		this.rootAuthor = rootAuthor;
	}
	public String getRootPermlink() {
		return rootPermlink;
	}
	public void setRootPermlink(String rootPermlink) {
		this.rootPermlink = rootPermlink;
	}
	public SimpleAsset getMaxAcceptedPayout() {
		return maxAcceptedPayout;
	}
	public void setMaxAcceptedPayout(SimpleAsset maxAcceptedPayout) {
		this.maxAcceptedPayout = maxAcceptedPayout;
	}
	public BigInteger getPercentSteemDollars() {
		return percentSteemDollars;
	}
	public void setPercentSteemDollars(BigInteger percentSteemDollars) {
		this.percentSteemDollars = percentSteemDollars;
	}
	public Boolean getAllowReplies() {
		return allowReplies;
	}
	public void setAllowReplies(Boolean allowReplies) {
		this.allowReplies = allowReplies;
	}
	public Boolean getAllowVotes() {
		return allowVotes;
	}
	public void setAllowVotes(Boolean allowVotes) {
		this.allowVotes = allowVotes;
	}
	public Boolean getAllowCurationRewards() {
		return allowCurationRewards;
	}
	public void setAllowCurationRewards(Boolean allowCurationRewards) {
		this.allowCurationRewards = allowCurationRewards;
	}
	public List<Beneficiary> getBeneficiaries() {
		return beneficiaries;
	}
	public void setBeneficiaries(List<Beneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRootTitle() {
		return rootTitle;
	}
	public void setRootTitle(String rootTitle) {
		this.rootTitle = rootTitle;
	}
	public SimpleAsset getPendingPayoutValue() {
		return pendingPayoutValue;
	}
	public void setPendingPayoutValue(SimpleAsset pendingPayoutValue) {
		this.pendingPayoutValue = pendingPayoutValue;
	}
	public SimpleAsset getTotalPendingPayoutValue() {
		return totalPendingPayoutValue;
	}
	public void setTotalPendingPayoutValue(SimpleAsset totalPendingPayoutValue) {
		this.totalPendingPayoutValue = totalPendingPayoutValue;
	}
	public List<Vote> getActiveVotes() {
		return activeVotes;
	}
	public void setActiveVotes(List<Vote> activeVotes) {
		this.activeVotes = activeVotes;
	}
	public List<Object> getReplies() {
		return replies;
	}
	public void setReplies(List<Object> replies) {
		this.replies = replies;
	}
	public BigInteger getAuthorReputation() {
		return authorReputation;
	}
	public void setAuthorReputation(BigInteger authorReputation) {
		this.authorReputation = authorReputation;
	}
	public SimpleAsset getPromoted() {
		return promoted;
	}
	public void setPromoted(SimpleAsset promoted) {
		this.promoted = promoted;
	}
	public BigInteger getBodyLength() {
		return bodyLength;
	}
	public void setBodyLength(BigInteger bodyLength) {
		this.bodyLength = bodyLength;
	}
	public List<String> getRebloggedBy() {
		return rebloggedBy;
	}
	public void setRebloggedBy(List<String> rebloggedBy) {
		this.rebloggedBy = rebloggedBy;
	}
	private final Map<String, Object> _properties = new HashMap<>(); 
	@Override
	public Map<String, Object> anyGetterSetterMap() {
		return _properties;
	}

}
