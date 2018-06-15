package steem.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class FollowingList {
	private final List<Following> list;

	public List<Following> getList() {
		return list;
	}

	public FollowingList() {
		list = new ArrayList<>();
	}

	@JsonCreator
	protected static FollowingList jsonCreator(List<Following> following) {
		FollowingList f = new FollowingList();
		if (following != null) {
			f.getList().addAll(following);
		}
		return f;
	}

	@JsonValue
	protected List<Following> jsonValue() {
		return list;
	}

	public static class Following {
		private String follower;
		private String following;
		private List<String> what;

		public String getFollower() {
			return follower;
		}

		public void setFollower(String follower) {
			this.follower = follower;
		}

		public String getFollowing() {
			return following;
		}

		public void setFollowing(String following) {
			this.following = following;
		}

		public List<String> getWhat() {
			return what;
		}

		public void setWhat(List<String> what) {
			this.what = what;
		}
	}
}
