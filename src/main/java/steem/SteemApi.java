package steem;

import java.math.BigInteger;
import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dev.json.JsonNumber;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;
import steem.MapperCallback.CommentListCallback;
import steem.MapperCallback.DiscussionsCallback;
import steem.MapperCallback.FollowingListCallback;
import steem.MapperCallback.TrendingTagsCallback;
import steem.MapperCallback.UserAccountInfoListCallback;
import steem.models.Discussion;
import steem.models.Discussions;
import steem.models.UserAccountInfo;

@JsType(namespace = "steem", name = "api", isNative = true)
public class SteemApi {

	@JsMethod(name = "getContentReplies")
	private static native void _getContentReplies(String username, String permlink,
			SteemCallback_old<JavaScriptObject> discussion);

	@JsOverlay
	public static void getContentReplies(String username, String permlink, SteemCallback_old<Discussions> cb) {

		SteemCallback_old<JavaScriptObject> _cb = new SteemCallback_old<JavaScriptObject>() {
			@Override
			public void onResult(JavaScriptObject error, JavaScriptObject result) {
				if (error != null || result == null) {
					cb.onResult(error, null);
					return;
				}
				String json = JsonUtils.stringify(result);
				json = "{\"discussions\":" + json + "}";
				Discussions discussions;
				try {
					discussions = Util.discussionsCodec.read(json);
				} catch (Exception e) {
					DomGlobal.console.log("=== " + e.getMessage());
					GWT.log(e.getMessage(), e);
					cb.onResult(error, null);
					return;
				}
				cb.onResult(error, discussions);
			}
		};

		_getContentReplies(username, permlink, _cb);
	}

	@JsMethod(name = "getContent")
	private static native void _getContent(String username, String permlink, SteemCallback_old<JavaScriptObject> cb);

	@JsOverlay
	public static void getContent(String username, String permlink, SteemCallback_old<Discussion> cb) {
		SteemCallback_old<JavaScriptObject> parseCb = new SteemCallback_old<JavaScriptObject>() {
			@Override
			public void onResult(JavaScriptObject error, JavaScriptObject result) {
				if (error != null) {
					cb.onResult(error, null);
					return;
				}
				if (result == null) {
					DomGlobal.console.log("getContent: NULL RESPONSE.");
					return;
				}
				try {
					String stringify = JsonUtils.stringify(result);
					Discussion d = Util.discussionCodec.read(stringify);
					if (d == null) {
						DomGlobal.console.log("Decoding FAIL: d == null!");
						cb.onResult(error, null);
						return;
					}
					cb.onResult(error, d);
				} catch (Exception e) {
					DomGlobal.console.log("Exception: " + e.getMessage());
				}
			}
		};
		_getContent(username, permlink, parseCb);
	}

	@JsMethod(name = "getFollowing")
	private static native void _getFollowing(String username, String startFollowing, String followType, int limit,
			SteemJsCallback jsCallback);

	@JsOverlay
	public static void getFollowing(String username, String startFollowing, String followType, int limit,
			FollowingListCallback callback) {
		_getFollowing(username, startFollowing, followType, limit, (error, result) -> {
			callback.onResult(error, result);
		});
	}

	@JsMethod(name = "getTrendingTags")
	private static native void _getTrendingTags(String afterTag, int limit, SteemJsCallback jsCallback);

	@JsOverlay
	public static void getTrendingTags(String afterTag, int limit, TrendingTagsCallback callback) {
		_getTrendingTags(afterTag, limit, (error, result) -> {
			callback.onResult(error, result);
		});
	}
	
	@JsMethod(name = "getBlog")
	private static native void _getBlog(String account, String entryId, int limit, //
			SteemJsCallback cb);
	@JsOverlay
	public static void getBlog(String account, BigInteger entryId, int limit, CommentListCallback callback) {
		_getBlog(account, entryId.toString(), limit, (error, result) -> callback.onResult(error, result));
	}

	@JsMethod(name = "getDiscussionsByBlog")
	private static native void _getDiscussionsByBlog(JavaScriptObject query, //
			SteemJsCallback cb);

	@JsOverlay
	public static void getDiscussionsByBlog(String username, //
			int limit, //
			DiscussionsCallback callback) {
		JSONObject json = new JSONObject();
		json.put("tag", new JSONString(username));
		json.put("limit", new JSONNumber(limit));
		_getDiscussionsByBlog(json.getJavaScriptObject(), (error, result) -> callback.onResult(error, result));
	}

	/**
	 * Get blog entries from blog by username starting at post with
	 * author=postAuthor and permlink=postPermlink
	 * 
	 * @param username
	 * @param postAuthor
	 * @param postPermlink
	 * @param limit
	 * @param callback
	 */
	@JsOverlay
	public static void getDiscussionsByBlog(String username, //
			String postAuthor, //
			String postPermlink, //
			int limit, //
			DiscussionsCallback callback) {
		JSONObject json = new JSONObject();
		json.put("start_author", new JSONString(postAuthor));
		json.put("start_permlink", new JSONString(postPermlink));
		json.put("tag", new JSONString(username));
		json.put("limit", new JSONNumber(limit));
		_getDiscussionsByBlog(json.getJavaScriptObject(), (error, result) -> callback.onResult(error, result));
	}

	@JsMethod(name = "getDiscussionsByCreated")
	private static native void _getDiscussionsByCreated(JavaScriptObject query, //
			SteemJsCallback jsCallback);

	@JsOverlay
	public static void getDiscussionsByCreated(String tag, //
			int limit, //
			DiscussionsCallback callback) {
		getDiscussionsByCreated(tag, null, null, limit, callback);
	}

	@JsOverlay
	public static void getDiscussionsByCreated(String tag, //
			String startPermlink, //
			String startAuthor, //
			int limit, //
			DiscussionsCallback callback) {
		JSONObject json = new JSONObject();
		json.put("tag", new JSONString(tag));
		json.put("limit", new JSONNumber(limit));
		if (!(startPermlink == null || startPermlink.trim().isEmpty())) {
			json.put("start_permlink", new JSONString(startPermlink));
		}
		if (!(startAuthor == null || startAuthor.trim().isEmpty())) {
			json.put("start_author", new JSONString(startAuthor));
		}
		_getDiscussionsByCreated(json.getJavaScriptObject(), (error, result) -> callback.onResult(error, result));
	}

	@JsMethod(name = "getAccounts")
	private static native void _getAccounts(String[] username, SteemJsCallback callback);

	@JsOverlay
	public static void getAccounts(List<String> usernames, UserAccountInfoListCallback callback) {
		_getAccounts(usernames.toArray(new String[usernames.size()]),
				(error, result) -> callback.onResult(error, result));
	}

	@JsOverlay
	public static void getAccounts(String[] usernames, UserAccountInfoListCallback callback) {
		_getAccounts(usernames, (error, result) -> callback.onResult(error, result));
	}

	@JsOverlay
	public static void getAccount(String username, UserAccountInfoListCallback callback) {
		_getAccounts(new String[] { username }, (error, result) -> callback.onResult(error, result));
	}

	public static class UserAccountInfoList {
		private List<UserAccountInfo> list;

		public List<UserAccountInfo> getList() {
			return list;
		}

		public void setList(List<UserAccountInfo> list) {
			this.list = list;
		}
	}

	public static class Util {
		// old
		public static interface DiscussionsCodec extends ObjectMapper<Discussions> {
		}

		public static DiscussionsCodec discussionsCodec = GWT.create(DiscussionsCodec.class);

		public static interface DiscussionCodec extends ObjectMapper<Discussion> {
		}

		public static DiscussionCodec discussionCodec = GWT.create(DiscussionCodec.class);
	}
}