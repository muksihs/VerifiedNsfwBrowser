package steem;

import java.util.Date;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;

import steem.SteemApi.UserAccountInfoList;
import steem.models.DiscussionMetadata;
import steem.models.Discussions;
import steem.models.FollowingList;
import steem.models.TrendingTags;

public class MapperCallback {
	private MapperCallback() {
	};
	
	public static interface SteemDateMapper extends ObjectMapper<Date> {}
	public static interface SteemDateCallback extends SteemTypedCallback<Date, SteemDateMapper> {
		@Override
		default SteemDateMapper mapper() {
			SteemDateMapper mapper = GWT.create(SteemDateMapper.class);
			return mapper;
		}
	}
	
	public static interface DiscussionMetadataMapper extends ObjectMapper<DiscussionMetadata> {
	}

	public static DiscussionMetadataMapper discussionMetadataMapper = GWT.create(DiscussionMetadataMapper.class);


	public static interface UserAccountInfoListMapper extends ObjectMapper<UserAccountInfoList> {
	}
	
	public static interface UserAccountInfoListCallback
			extends SteemTypedListCallback<UserAccountInfoList, UserAccountInfoListMapper> {
		@Override
		default UserAccountInfoListMapper mapper() {
			return GWT.create(UserAccountInfoListMapper.class);
		}
	}


	public static interface FollowingListMapper extends ObjectMapper<FollowingList> {
	}

	public static interface FollowingListCallback extends SteemTypedCallback<FollowingList, FollowingListMapper> {
		@Override
		default FollowingListMapper mapper() {
			return GWT.create(FollowingListMapper.class);
		}
	}

	public static interface DiscussionsMapper extends ObjectMapper<Discussions> {
	}

	public static interface DiscussionsCallback extends SteemTypedListCallback<Discussions, DiscussionsMapper> {
		@Override
		default DiscussionsMapper mapper() {
			return GWT.create(DiscussionsMapper.class);
		}
	}

	public static interface TrendingTagsMapper extends ObjectMapper<TrendingTags> {
	}

	public static interface TrendingTagsCallback extends SteemTypedListCallback<TrendingTags, TrendingTagsMapper> {
		@Override
		default TrendingTagsMapper mapper() {
			return GWT.create(TrendingTagsMapper.class);
		}
	}
}
