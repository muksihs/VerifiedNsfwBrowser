package steem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;

import steem.SteemApi.UserAccountInfoList;
import steem.models.BlogItem;
import steem.models.CommentItem;
import steem.models.Discussion;
import steem.models.DiscussionMetadata;
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

	public static interface DiscussionsMapper extends ObjectMapper<List<Discussion>> {
	}

	public static interface DiscussionsCallback extends SteemTypedCallback<List<Discussion>, DiscussionsMapper> {
		@Override
		default DiscussionsMapper mapper() {
			return GWT.create(DiscussionsMapper.class);
		}
	}
	
	public static interface MapMapper extends ObjectMapper<Map<String, Object>> {
	}

	public static interface MapMapperCallback extends SteemTypedListCallback<Map<String, Object>, MapMapper> {
		@Override
		default MapMapper mapper() {
			return GWT.create(MapMapper.class);
		}
	}
	
	public static interface CommentItemsMapper extends ObjectMapper<List<CommentItem>> {
	}

	public static interface CommentListCallback extends SteemTypedCallback<List<CommentItem>,CommentItemsMapper> {
		@Override
		default CommentItemsMapper mapper() {
			return GWT.create(CommentItemsMapper.class);
		}
	}
	
	public static interface BlogItemsMapper extends ObjectMapper<ArrayList<BlogItem>> {
	}

	public static interface BlogListCallback extends SteemTypedCallback<ArrayList<BlogItem>,BlogItemsMapper> {
		@Override
		default BlogItemsMapper mapper() {
			return GWT.create(BlogItemsMapper.class);
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
