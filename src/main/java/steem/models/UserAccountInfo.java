package steem.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountInfo implements HasJsonAnyGetterSetter {
	private BigInteger id;
	private AccountName name;
	private Authorizations owner;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public AccountName getName() {
		return name;
	}
	public void setName(AccountName name) {
		this.name = name;
	}
	public Authorizations getOwner() {
		return owner;
	}
	public void setOwner(Authorizations owner) {
		this.owner = owner;
	}
	public Authorizations getActive() {
		return active;
	}
	public void setActive(Authorizations active) {
		this.active = active;
	}
	public Authorizations getPosting() {
		return posting;
	}
	public void setPosting(Authorizations posting) {
		this.posting = posting;
	}
	public String getMemoKey() {
		return memoKey;
	}
	public void setMemoKey(String memoKey) {
		this.memoKey = memoKey;
	}
	public String getJsonMetadata() {
		return jsonMetadata;
	}
	public void setJsonMetadata(String jsonMetadata) {
		this.jsonMetadata = jsonMetadata;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public Date getLastOwnerUpdate() {
		return lastOwnerUpdate;
	}
	public void setLastOwnerUpdate(Date lastOwnerUpdate) {
		this.lastOwnerUpdate = lastOwnerUpdate;
	}
	public Date getLastAccountUpdate() {
		return lastAccountUpdate;
	}
	public void setLastAccountUpdate(Date lastAccountUpdate) {
		this.lastAccountUpdate = lastAccountUpdate;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Boolean getMined() {
		return mined;
	}
	public void setMined(Boolean mined) {
		this.mined = mined;
	}
	public String getRecoveryAccount() {
		return recoveryAccount;
	}
	public void setRecoveryAccount(String recoveryAccount) {
		this.recoveryAccount = recoveryAccount;
	}
	public String getResetAccount() {
		return resetAccount;
	}
	public void setResetAccount(String resetAccount) {
		this.resetAccount = resetAccount;
	}
	public BigInteger getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(BigInteger commentCount) {
		this.commentCount = commentCount;
	}
	public BigInteger getLifetimeVoteCount() {
		return lifetimeVoteCount;
	}
	public void setLifetimeVoteCount(BigInteger lifetimeVoteCount) {
		this.lifetimeVoteCount = lifetimeVoteCount;
	}
	public BigInteger getPostCount() {
		return postCount;
	}
	public void setPostCount(BigInteger postCount) {
		this.postCount = postCount;
	}
	public Boolean getCanVote() {
		return canVote;
	}
	public void setCanVote(Boolean canVote) {
		this.canVote = canVote;
	}
	public BigInteger getVotingPower() {
		return votingPower;
	}
	public void setVotingPower(BigInteger votingPower) {
		this.votingPower = votingPower;
	}
	public Date getLastVoteTime() {
		return lastVoteTime;
	}
	public void setLastVoteTime(Date lastVoteTime) {
		this.lastVoteTime = lastVoteTime;
	}
	public SimpleAsset getBalance() {
		return balance;
	}
	public void setBalance(SimpleAsset balance) {
		this.balance = balance;
	}
	public SimpleAsset getSavingsBalance() {
		return savingsBalance;
	}
	public void setSavingsBalance(SimpleAsset savingsBalance) {
		this.savingsBalance = savingsBalance;
	}
	public SimpleAsset getSbdBalance() {
		return sbdBalance;
	}
	public void setSbdBalance(SimpleAsset sbdBalance) {
		this.sbdBalance = sbdBalance;
	}
	public BigInteger getSbdSeconds() {
		return sbdSeconds;
	}
	public void setSbdSeconds(BigInteger sbdSeconds) {
		this.sbdSeconds = sbdSeconds;
	}
	public Date getSbdSecondsLastUpdate() {
		return sbdSecondsLastUpdate;
	}
	public void setSbdSecondsLastUpdate(Date sbdSecondsLastUpdate) {
		this.sbdSecondsLastUpdate = sbdSecondsLastUpdate;
	}
	public Date getSbdLastInterestPayment() {
		return sbdLastInterestPayment;
	}
	public void setSbdLastInterestPayment(Date sbdLastInterestPayment) {
		this.sbdLastInterestPayment = sbdLastInterestPayment;
	}
	public SimpleAsset getSavingsSbdBalance() {
		return savingsSbdBalance;
	}
	public void setSavingsSbdBalance(SimpleAsset savingsSbdBalance) {
		this.savingsSbdBalance = savingsSbdBalance;
	}
	public BigInteger getSavings_sbd_seconds() {
		return savings_sbd_seconds;
	}
	public void setSavings_sbd_seconds(BigInteger savings_sbd_seconds) {
		this.savings_sbd_seconds = savings_sbd_seconds;
	}
	public Date getSavingsSbdSecondsLastUpdate() {
		return savingsSbdSecondsLastUpdate;
	}
	public void setSavingsSbdSecondsLastUpdate(Date savingsSbdSecondsLastUpdate) {
		this.savingsSbdSecondsLastUpdate = savingsSbdSecondsLastUpdate;
	}
	public Date getSavingsSbdLastInterestPayment() {
		return savingsSbdLastInterestPayment;
	}
	public void setSavingsSbdLastInterestPayment(Date savingsSbdLastInterestPayment) {
		this.savingsSbdLastInterestPayment = savingsSbdLastInterestPayment;
	}
	public BigInteger getSavings_withdraw_requests() {
		return savings_withdraw_requests;
	}
	public void setSavings_withdraw_requests(BigInteger savings_withdraw_requests) {
		this.savings_withdraw_requests = savings_withdraw_requests;
	}
	public SimpleAsset getRewardSbdBalance() {
		return rewardSbdBalance;
	}
	public void setRewardSbdBalance(SimpleAsset rewardSbdBalance) {
		this.rewardSbdBalance = rewardSbdBalance;
	}
	public SimpleAsset getRewardSteemBalance() {
		return rewardSteemBalance;
	}
	public void setRewardSteemBalance(SimpleAsset rewardSteemBalance) {
		this.rewardSteemBalance = rewardSteemBalance;
	}
	public SimpleAsset getRewardVestingBalance() {
		return rewardVestingBalance;
	}
	public void setRewardVestingBalance(SimpleAsset rewardVestingBalance) {
		this.rewardVestingBalance = rewardVestingBalance;
	}
	public SimpleAsset getRewardVestingSteem() {
		return rewardVestingSteem;
	}
	public void setRewardVestingSteem(SimpleAsset rewardVestingSteem) {
		this.rewardVestingSteem = rewardVestingSteem;
	}
	public SimpleAsset getVestingShares() {
		return vestingShares;
	}
	public void setVestingShares(SimpleAsset vestingShares) {
		this.vestingShares = vestingShares;
	}
	public SimpleAsset getDelegatedVestingShares() {
		return delegatedVestingShares;
	}
	public void setDelegatedVestingShares(SimpleAsset delegatedVestingShares) {
		this.delegatedVestingShares = delegatedVestingShares;
	}
	public SimpleAsset getReceivedVestingShares() {
		return receivedVestingShares;
	}
	public void setReceivedVestingShares(SimpleAsset receivedVestingShares) {
		this.receivedVestingShares = receivedVestingShares;
	}
	public SimpleAsset getVestingWithdrawRate() {
		return vestingWithdrawRate;
	}
	public void setVestingWithdrawRate(SimpleAsset vestingWithdrawRate) {
		this.vestingWithdrawRate = vestingWithdrawRate;
	}
	public Date getNextVestingWithdrawal() {
		return nextVestingWithdrawal;
	}
	public void setNextVestingWithdrawal(Date nextVestingWithdrawal) {
		this.nextVestingWithdrawal = nextVestingWithdrawal;
	}
	public BigDecimal getWithdrawn() {
		return withdrawn;
	}
	public void setWithdrawn(BigDecimal withdrawn) {
		this.withdrawn = withdrawn;
	}
	public BigDecimal getToWithdraw() {
		return toWithdraw;
	}
	public void setToWithdraw(BigDecimal toWithdraw) {
		this.toWithdraw = toWithdraw;
	}
	public BigDecimal getWithdrawRoutes() {
		return withdrawRoutes;
	}
	public void setWithdrawRoutes(BigDecimal withdrawRoutes) {
		this.withdrawRoutes = withdrawRoutes;
	}
	public BigDecimal getCurationRewards() {
		return curationRewards;
	}
	public void setCurationRewards(BigDecimal curationRewards) {
		this.curationRewards = curationRewards;
	}
	public BigDecimal getPostingRewards() {
		return postingRewards;
	}
	public void setPostingRewards(BigDecimal postingRewards) {
		this.postingRewards = postingRewards;
	}
	public List<BigDecimal> getProxiedVsfVotes() {
		return proxiedVsfVotes;
	}
	public void setProxiedVsfVotes(List<BigDecimal> proxiedVsfVotes) {
		this.proxiedVsfVotes = proxiedVsfVotes;
	}
	public BigInteger getWitnesses_voted_for() {
		return witnesses_voted_for;
	}
	public void setWitnesses_voted_for(BigInteger witnesses_voted_for) {
		this.witnesses_voted_for = witnesses_voted_for;
	}
	public Date getLastPost() {
		return lastPost;
	}
	public void setLastPost(Date lastPost) {
		this.lastPost = lastPost;
	}
	public Date getLastRootPost() {
		return lastRootPost;
	}
	public void setLastRootPost(Date lastRootPost) {
		this.lastRootPost = lastRootPost;
	}
	public BigInteger getAverageBandwidth() {
		return averageBandwidth;
	}
	public void setAverageBandwidth(BigInteger averageBandwidth) {
		this.averageBandwidth = averageBandwidth;
	}
	public BigInteger getLifetimeBandwidth() {
		return lifetimeBandwidth;
	}
	public void setLifetimeBandwidth(BigInteger lifetimeBandwidth) {
		this.lifetimeBandwidth = lifetimeBandwidth;
	}
	public Date getLastBandwidthUpdate() {
		return lastBandwidthUpdate;
	}
	public void setLastBandwidthUpdate(Date lastBandwidthUpdate) {
		this.lastBandwidthUpdate = lastBandwidthUpdate;
	}
	public BigInteger getAverageMarketBandwidth() {
		return averageMarketBandwidth;
	}
	public void setAverageMarketBandwidth(BigInteger averageMarketBandwidth) {
		this.averageMarketBandwidth = averageMarketBandwidth;
	}
	public BigInteger getLifetimeMarketBandwidth() {
		return lifetimeMarketBandwidth;
	}
	public void setLifetimeMarketBandwidth(BigInteger lifetimeMarketBandwidth) {
		this.lifetimeMarketBandwidth = lifetimeMarketBandwidth;
	}
	public Date getLastMarketBandwidthUpdate() {
		return lastMarketBandwidthUpdate;
	}
	public void setLastMarketBandwidthUpdate(Date lastMarketBandwidthUpdate) {
		this.lastMarketBandwidthUpdate = lastMarketBandwidthUpdate;
	}
	public SimpleAsset getVestingBalance() {
		return vestingBalance;
	}
	public void setVestingBalance(SimpleAsset vestingBalance) {
		this.vestingBalance = vestingBalance;
	}
	public BigInteger getReputation() {
		return reputation;
	}
	public void setReputation(BigInteger reputation) {
		this.reputation = reputation;
	}
	public List<Object> getTransferHistory() {
		return transferHistory;
	}
	public void setTransferHistory(List<Object> transferHistory) {
		this.transferHistory = transferHistory;
	}
	public List<Object> getMarketHistory() {
		return marketHistory;
	}
	public void setMarketHistory(List<Object> marketHistory) {
		this.marketHistory = marketHistory;
	}
	public List<Object> getPostHistory() {
		return postHistory;
	}
	public void setPostHistory(List<Object> postHistory) {
		this.postHistory = postHistory;
	}
	public List<Object> getVoteHistory() {
		return voteHistory;
	}
	public void setVoteHistory(List<Object> voteHistory) {
		this.voteHistory = voteHistory;
	}
	public List<Object> getOtherHistory() {
		return otherHistory;
	}
	public void setOtherHistory(List<Object> otherHistory) {
		this.otherHistory = otherHistory;
	}
	public List<String> getWitnessVotes() {
		return witnessVotes;
	}
	public void setWitnessVotes(List<String> witnessVotes) {
		this.witnessVotes = witnessVotes;
	}
	public List<Object> getTagsUsage() {
		return tagsUsage;
	}
	public void setTagsUsage(List<Object> tagsUsage) {
		this.tagsUsage = tagsUsage;
	}
	public List<String> getGuestBloggers() {
		return guestBloggers;
	}
	public void setGuestBloggers(List<String> guestBloggers) {
		this.guestBloggers = guestBloggers;
	}
	private Authorizations active;
	private Authorizations posting;
	@JsonProperty("memo_key")
	private String memoKey;
	@JsonProperty("json_metadata")
	private String jsonMetadata;
	private String proxy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_owner_update")
	private Date lastOwnerUpdate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_account_update")
	private Date lastAccountUpdate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_account_recovery")
	private Date lastAccountRecovery;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("created")
	private Date created;

	private Boolean mined;

	@JsonProperty("recovery_account")
	private String recoveryAccount;

	@JsonProperty("reset_account")
	private String resetAccount;
	@JsonProperty("comment_count")
	private BigInteger commentCount;
	@JsonProperty("lifetime_vote_count")
	private BigInteger lifetimeVoteCount;
	@JsonProperty("post_count")
	private BigInteger postCount;
	@JsonProperty("can_vote")
	private Boolean canVote;
	@JsonProperty("voting_power")
	private BigInteger votingPower;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_vote_time")
	private Date lastVoteTime;
	private SimpleAsset balance;
	@JsonProperty("savings_balance")
	private SimpleAsset savingsBalance;
	@JsonProperty("sbd_balance")
	private SimpleAsset sbdBalance;
	@JsonProperty("sbd_seconds")
	private BigInteger sbdSeconds;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("sbd_seconds_last_update")
	private Date sbdSecondsLastUpdate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("sbd_last_interest_payment")
	private Date sbdLastInterestPayment;
	@JsonProperty("savings_sbd_balance")
	private SimpleAsset savingsSbdBalance;
	@JsonProperty("savings_sbd_seconds")
	private BigInteger savings_sbd_seconds;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("savings_sbd_seconds_last_update")
	private Date savingsSbdSecondsLastUpdate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("savings_sbd_last_interest_payment")
	private Date savingsSbdLastInterestPayment;
	@JsonProperty("savings_withdraw_requests")
	private BigInteger savings_withdraw_requests;
	@JsonProperty("reward_sbd_balance")
	private SimpleAsset rewardSbdBalance;
	@JsonProperty("reward_steem_balance")
	private SimpleAsset rewardSteemBalance;
	@JsonProperty("reward_vesting_balance")
	private SimpleAsset rewardVestingBalance;
	@JsonProperty("reward_vesting_steem")
	private SimpleAsset rewardVestingSteem;
	@JsonProperty("vesting_shares")
	private SimpleAsset vestingShares;
	@JsonProperty("delegated_vesting_shares")
	private SimpleAsset delegatedVestingShares;
	@JsonProperty("received_vesting_shares")
	private SimpleAsset receivedVestingShares;
	@JsonProperty("vesting_withdraw_rate")
	private SimpleAsset vestingWithdrawRate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("next_vesting_withdrawal")
	private Date nextVestingWithdrawal;
	private BigDecimal withdrawn;
	@JsonProperty("to_withdraw")
	private BigDecimal toWithdraw;
	@JsonProperty("withdraw_routes")
	private BigDecimal withdrawRoutes;
	@JsonProperty("curation_rewards")
	private BigDecimal curationRewards;
	@JsonProperty("posting_rewards")
	private BigDecimal postingRewards;
	@JsonProperty("proxied_vsf_votes")
	private List<BigDecimal> proxiedVsfVotes;
	@JsonProperty("witnesses_voted_for")
	private BigInteger witnesses_voted_for;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_post")
	private Date lastPost;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_root_post")
	private Date lastRootPost;
	@JsonProperty("average_bandwidth")
	private BigInteger averageBandwidth;
	@JsonProperty("lifetime_bandwidth")
	private BigInteger lifetimeBandwidth;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_bandwidth_update")
	private Date lastBandwidthUpdate;
	@JsonProperty("average_market_bandwidth")
	private BigInteger averageMarketBandwidth;
	@JsonProperty("lifetime_market_bandwidth")
	private BigInteger lifetimeMarketBandwidth;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	@JsonProperty("last_market_bandwidth_update")
	private Date lastMarketBandwidthUpdate;
	@JsonProperty("vesting_balance")
	private SimpleAsset vestingBalance;
	private BigInteger reputation;
	@JsonProperty("transfer_history")
	private List<Object> transferHistory;
	@JsonProperty("market_history")
	private List<Object> marketHistory;
	@JsonProperty("post_history")
	private List<Object> postHistory;
	@JsonProperty("vote_history")
	private List<Object> voteHistory;
	@JsonProperty("other_history")
	private List<Object> otherHistory;
	@JsonProperty("witness_votes")
	private List<String> witnessVotes;
	@JsonProperty("tags_usage")
	private List<Object> tagsUsage;
	@JsonProperty("guest_bloggers")
	private List<String> guestBloggers;
	
	private final Map<String, Object> _map = new HashMap<String, Object>();
	@Override
	public Map<String, Object> anyGetterSetterMap() {
		return _map;
	}
}
