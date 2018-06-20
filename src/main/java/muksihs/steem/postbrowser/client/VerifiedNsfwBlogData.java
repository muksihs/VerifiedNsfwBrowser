package muksihs.steem.postbrowser.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.github.nmorel.gwtjackson.client.exception.JsonDeserializationException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import elemental2.dom.DomGlobal;
import gwt.material.design.client.ui.MaterialToast;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;
import muksihs.steem.postbrowser.shared.BlogIndex;
import muksihs.steem.postbrowser.shared.BlogIndexEntry;
import steem.SteemApi;
import steem.SteemApi.DiscussionsCallback;
import steem.SteemApi.FollowingListCallback;
import steem.models.Discussion;
import steem.models.DiscussionMetadata;
import steem.models.Discussions;
import steem.models.FollowingList;
import steem.models.FollowingList.Following;

public class VerifiedNsfwBlogData implements GlobalAsyncEventBus {
	private static final String URL_PATTERN_DPORN1 = "[\\s\\S]*<a[^>]*href=[\"']?(https?://(.*?\\.)?dporn.co/[^/]*?/[^/]*?/[^/]*?)[\"']?[^>]*>[\\s\\S]*";
	private static final String URL_PATTERN_DPORN2 = "[\\s\\S]*<a[^>]*href=[\"']?(https?://(.*?\\.)?dporn.co/[^/]*?/@[^/]*?/[^/]*?)[\"']?[^>]*>[\\s\\S]*";
	private static final String URL_PATTERN_DLIVE = "[\\s\\S]*\\[DLive\\]\\((https?://dlive.io[^\\)]*?)\\)[\\s\\S]*";
	private static final String URL_PATTERN_DTUBE = "[\\s\\S]*<a[^>]*href=[\"']?(https?://(.*?\\.)?d.tube/#!/[^/]*?/[^/]*?/[^/]*?)[\"']?[^>]*>[\\s\\S]*";
	private final BlogIndex index;
	private static VerifiedNsfwBlogData instance;

	public static void unbind() {
		if (instance == null) {
			return;
		}
		instance.registration.removeHandler();
		instance = null;
	}

	public static void bind() {
		if (instance == null) {
			instance = new VerifiedNsfwBlogData();
		}
		HandlerRegistration registration = eventBinder.bindEventHandlers(instance, instance.getEventBus());
		instance.setRegistration(registration);
	}

	interface MyEventBinder extends EventBinder<VerifiedNsfwBlogData> {
	}

	private static final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	private HandlerRegistration registration;

	private void setRegistration(HandlerRegistration registration) {
		this.registration = registration;
	}

	protected VerifiedNsfwBlogData() {
		index = new BlogIndex();
		oldestByAuthor = new HashMap<>();
	}

	/**
	 * keep copy of "oldest" by list order post so that indexing can continue where
	 * it left off
	 */
	private final Map<String, BlogIndexEntry> oldestByAuthor;

	protected void indexBlogEntries(String username, Discussions result) {
		if (result == null) {
			return;
		}
		List<BlogIndexEntry> entries = new ArrayList<>();
		for (Discussion discussion : result.getList()) {
			BlogIndexEntry entry = new BlogIndexEntry();
			entry.setAuthor(discussion.getAuthor());
			entry.setCreated(discussion.getCreated());
			entry.setPermlink(discussion.getPermlink());
			entry.setTitle(discussion.getTitle());
			parseMetadata: try {
				DiscussionMetadata metadata = SteemApi.discussionMetadataMapper.read(discussion.getJsonMetadata());
				if (metadata == null) {
					break parseMetadata;
				}
				if (metadata.getTags() == null) {
					break parseMetadata;
				}
				entry.setTags(metadata.getTags());
				entry.setImage(metadata.getImage());
				entry.setThumbnail(metadata.getThumbnail());
				entry.addToCombinedImages(trimAndStripQuotes(metadata.getThumbnail()));
				entry.addToCombinedImages(trimAndStripQuotes(metadata.getImage()));
			} catch (JsonDeserializationException e) {
				DomGlobal.console.log("BAD BLOG DATA: " + discussion.getAuthor() + "/" + discussion.getPermlink());
				DomGlobal.console.log(e.getMessage(), e);
				// prevent NPEs
				entry.setTags(new ArrayList<>());
			}
			final String body = discussion.getBody();

			// add body extracted images next (Markdown)
			// ![wo3n2ln78t.jpg](https://img.esteem.ws/wo3n2ln78t.jpg)
			// ![alt
			// text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png
			// "Logo Title Text 1")
			if (body.matches("[\\s\\S]*!\\[[^\\]]*\\]\\s*\\([^\\)]*\\)[\\s\\S]*")) {
				String tmp = body;
				// strip all but MD image tags
				tmp = tmp.replaceAll("[^\\)]+!", "!");
				tmp = tmp.replaceAll("\\)[^!]+", ")");

				// convert all to bare urls
				tmp = tmp.replaceAll("!\\[[^\\]]*\\]", "");
				tmp = tmp.replaceAll("\\(([^\\)\\s]+)[^\\)]*", "\n$1\n");

				// remove any remaining and hence invalid paren sets
				tmp = tmp.replaceAll("\\([^\\)]*\\)", "");

				// strip possible single or double surrounding quotes
				tmp = ("\n" + tmp + "\n").replaceAll("\n['\"]+", "");
				tmp = ("\n" + tmp + "\n").replaceAll("['\"]+\n", "");
				// cleanup removing blank lines
				tmp = tmp.replaceAll("\n+", "\n");
				// split on inner "\n" and add to combined images
				String[] tmpUrls = tmp.trim().split("\n");
				if (tmpUrls != null && tmpUrls.length > 0) {
					entry.addToCombinedImages(trimAndStripQuotes(Arrays.asList(tmpUrls)));
				}
			}

			// add body extracted images last (HTML <img ...>

			if (body.matches("[\\s\\S]*<[iI][mM][gG][^>]+>[\\s\\S]*")) {
				// strip all but tags
				String tmp = body;
				tmp = tmp.replaceAll("[^>]+<", "<");
				tmp = tmp.replaceAll(">[^<]+", ">");
				// reduce down to only img tags
				tmp = tmp.replaceAll("(<>)", "");
				tmp = tmp.replaceAll("(<[^iI][^>]*>)", "");
				tmp = tmp.replaceAll("(<[iI][^mM][^>]*>)", "");
				tmp = tmp.replaceAll("(<[iI][mM][^gG]\\s+[^>]*>)", "");
				// convert all img tags with a src component to bare urls
				tmp = tmp.replaceAll("<[^>]+?src[ \n]*=[ \n]*([^> \n]+)[^>]*>", "\n$1\n");
				// remove any remaining and hence invalid tags
				tmp = tmp.replaceAll("(<[^>]*>)", "");
				// strip possible single or double surrounding quotes
				tmp = ("\n" + tmp + "\n").replaceAll("\n['\"]+", "");
				tmp = ("\n" + tmp + "\n").replaceAll("['\"]+\n", "");
				// cleanup removing blank lines
				tmp = tmp.replaceAll("\n+", "\n");
				// split on inner "\n" and add to combined images
				String[] tmpUrls = tmp.trim().split("\n");
				if (tmpUrls != null && tmpUrls.length > 0) {
					entry.addToCombinedImages(trimAndStripQuotes(Arrays.asList(tmpUrls)));
				}
			}
			if (entry.getThumbnail() != null) {
				if (entry.getThumbnail().matches("https?://.*?dlive.io/.*")) {
					if (body.matches(URL_PATTERN_DLIVE)) {
						entry.setCustomUrlName("DLive");
						entry.setCustomUrl(body.replaceAll(URL_PATTERN_DLIVE, "$1"));
					}
				}
			}
			dporn: if (body.contains("dporn.co/") && discussion.getJsonMetadata().contains("dporn")) {
				if (body.matches(URL_PATTERN_DPORN2)) {
					entry.setCustomUrlName("DPORN");
					String tmp = body;
					entry.setCustomUrl(tmp.replaceAll(URL_PATTERN_DPORN2, "$1"));
					break dporn;
				}
				if (body.matches(URL_PATTERN_DPORN1)) {
					entry.setCustomUrlName("DPORN");
					String tmp = body;
					entry.setCustomUrl(tmp.replaceAll(URL_PATTERN_DPORN1, "$1"));
					break dporn;
				}
			}
			if (body.contains("https://d.tube/#!") && discussion.getJsonMetadata().contains("videohash")) {
				if (body.matches(URL_PATTERN_DTUBE)) {
					entry.setCustomUrlName("DTUBE");
					String tmp = body;
					entry.setCustomUrl(tmp.replaceAll(URL_PATTERN_DTUBE, "$1"));
				}
			}
			entries.add(entry);
		}
		if (!entries.isEmpty()) {
			BlogIndexEntry oldestBlogIndexEntry = entries.get(entries.size()-1);
			oldestByAuthor.put(username, oldestBlogIndexEntry);
			index.addAll(username, entries);
		}
	};

	private List<String> trimAndStripQuotes(List<String> image) {
		if (image == null) {
			return null;
		}
		List<String> tmp = new ArrayList<>();
		for (String thumbnail : image) {
			tmp.add(trimAndStripQuotes(thumbnail));
		}
		return tmp;
	}

	private String trimAndStripQuotes(String thumbnail) {
		if (thumbnail == null) {
			return thumbnail;
		}
		thumbnail = thumbnail.trim();
		while (thumbnail.length() > 0 && thumbnail.startsWith("\"")) {
			thumbnail = thumbnail.substring(1).trim();
		}
		while (thumbnail.length() > 0 && thumbnail.startsWith("'")) {
			thumbnail = thumbnail.substring(1).trim();
		}
		while (thumbnail.length() > 0 && thumbnail.endsWith("\"")) {
			thumbnail = thumbnail.substring(0, thumbnail.length() - 1).trim();
		}
		while (thumbnail.length() > 0 && thumbnail.endsWith("'")) {
			thumbnail = thumbnail.substring(0, thumbnail.length() - 1).trim();
		}
		return thumbnail;
	}

	@EventHandler
	protected void onLoadUpdatePreviewList(Event.LoadUpdatePreviewList event) {
		if (!event.getHaveTags().isEmpty() || !event.getNotTags().isEmpty()) {
			List<BlogIndexEntry> list = index.getFilteredList(event.getMode(), event.getHaveTags(), event.getNotTags());
			fireEvent(new Event.UpdatedPreviewList(list));
			Set<String> availableTags = new TreeSet<>();
			for (BlogIndexEntry preview : list) {
				List<String> tags = preview.getTags();
				if (tags != null) {
					availableTags.addAll(tags);
				}
			}
			fireEvent(new Event.SetAvailableTags(availableTags));
			return;
		}
		List<BlogIndexEntry> list = new ArrayList<>();// index.getFilteredList(FilteredListMode.AND, empty, empty);
		for (String author : index.getNewestDateSortedAuthors()) {
			BlogIndexEntry entry = index.getMostRecentEntry(author);
			list.add(entry);
		}
		fireEvent(new Event.UpdatedPreviewList(list));
		Set<String> tags = index.getTags();
//		for (String author : index.getAuthors()) {
//			tags.add("@" + author);
//		}
		fireEvent(new Event.SetAvailableTags(tags));
	}

	@EventHandler
	protected void onAppStart(Event.AppStart event) {
		fireEvent(new Event.LoadNsfwVerifiedAccounts());
	}

	@EventHandler
	protected void onLoadNsfwVerifiedAccounts(Event.LoadNsfwVerifiedAccounts event) {
		MaterialToast.fireToast("Loading Verified NSFW Account List", 1000);
		fireEvent(new Event.ShowLoading(true));
		Set<String> list = new TreeSet<>();
		int limit = 10;
		FollowingListCallback cb = new FollowingListCallback() {
			private final FollowingListCallback cb = this;

			@Override
			public void onResult(String error, FollowingList result) {
				if (error != null) {
					MaterialToast.fireToast("STEEM API ERROR: [Getting NSFW Verified Account List]" + error, 1000);
					new Timer() {
						@Override
						public void run() {
							fireEvent(new Event.LoadNsfwVerifiedAccounts());
						}
					}.schedule(500);
					return;
				}
				if (result == null) {
					fireEvent(new Event.NsfwVerifiedAccountsLoaded(list));
					return;
				}
				final List<Following> followingList = result.getList();
				for (Following f : followingList) {
					if (list.contains(f.getFollowing())) {
						continue;
					}
					list.add(f.getFollowing());
				}
				if (followingList.size() < 2) {
					fireEvent(new Event.NsfwVerifiedAccountsLoaded(list));
					fireEvent(new Event.ShowLoading(false));
					return;
				}
				String last = followingList.get(followingList.size() - 1).getFollowing();
				SteemApi.getFollowing("verifiednsfw", last, "blog", limit, cb);
			}
		};
		SteemApi.getFollowing("verifiednsfw", "", "blog", limit, cb);
	}

	@EventHandler
	protected void onStartBackgroundIndexing(Event.StartBackgroundIndexing event) {
		List<String> list = getOldestDateSortedAuthors();
		ListIterator<String> iList = list.listIterator();
		while (iList.hasNext()) {
			if (index.isIndexingComplete(iList.next())) {
				iList.remove();
			}
		}
		if (list.isEmpty()) {
			return;
		}
		additionalIndexBlogs(list.listIterator());
	}
	
	public List<String> getOldestDateSortedAuthors() {
		List<String> authors = new ArrayList<>(oldestByAuthor.keySet());
		Collections.sort(authors, (a, b) -> {
			BlogIndexEntry ea = oldestByAuthor.get(a);
			BlogIndexEntry eb = oldestByAuthor.get(b);
			if (ea != null && eb != null) {
				if (!ea.getCreated().equals(eb.getCreated())) {
					return -ea.getCreated().compareTo(eb.getCreated());
				}
			}
			return a.compareToIgnoreCase(b);
		});
		return authors;
	}

	/**
	 * in case of single api call crash
	 */
	private Timer timerGetDiscussionsByBlogFailsafe = null;
	/**
	 * in case of total steem js crash
	 */
	private Timer timerRestartAdditionalIndexBlogs = null;

	private void additionalIndexBlogs(ListIterator<String> iList) {
		if (timerGetDiscussionsByBlogFailsafe != null) {
			timerGetDiscussionsByBlogFailsafe.cancel();
		}
		if (!iList.hasNext()) {
			return;
		}
		if (timerRestartAdditionalIndexBlogs != null) {
			timerRestartAdditionalIndexBlogs.cancel();
		}
		timerRestartAdditionalIndexBlogs = new Timer() {
			@Override
			public void run() {
				fireEvent(new Event.StartBackgroundIndexing());
			}
		};
		timerRestartAdditionalIndexBlogs.schedule(10000);
		final String username = iList.next();
		iList.remove();
		if (index.isIndexingComplete(username)) {
			additionalIndexBlogs(iList);
			return;
		}
		DomGlobal.console.log("=== additionalIndexBlogs: @" + username);
		timerGetDiscussionsByBlogFailsafe = new Timer() {
			@Override
			public void run() {
				DomGlobal.console.log("=== FIRE: timerGetDiscussionsByBlogFailsafe");
				additionalIndexBlogs(iList);
			}
		};
		timerGetDiscussionsByBlogFailsafe.schedule(10000);
		BlogIndexEntry oldestPost = oldestByAuthor.get(username);
		if (oldestPost == null) {
			additionalIndexBlogs(iList);
			return;
		}
		DiscussionsCallback cb = new DiscussionsCallback() {
			@Override
			public void onResult(String error, Discussions result) {
				if (error != null) {
					additionalIndexBlogs(iList);
					return;
				}
				if (result != null) {
					indexBlogEntries(username, result);
					if (result.getList().size() < 2) {
						index.setIndexingComplete(username, true);
						DomGlobal.console.log("=====================");
						DomGlobal.console.log("Indexing complete for @" + username);
						DomGlobal.console.log("=====================");
					}
				}
				new Timer() {
					@Override
					public void run() {
						additionalIndexBlogs(iList);
					}
				}.schedule(1000);
			}
		};
		int count;
		count = 100;
		SteemApi.getDiscussionsByBlog(username, oldestPost.getAuthor(), oldestPost.getPermlink(), count, cb);
	}

	private void indexBlogs(ListIterator<String> iList) {
		if (!iList.hasNext()) {
			fireEvent(new Event.ShowLoading(false));
			fireEvent(new Event.StartBackgroundIndexing());
			MaterialToast.fireToast("Starting Additional Indexing in the Background", 1000);
			return;
		}
		final String username = iList.next();
		iList.remove();
		DomGlobal.console.log("indexBlog: @" + username);
		DiscussionsCallback cb = new DiscussionsCallback() {
			@Override
			public void onResult(String error, Discussions result) {
				if (error != null) {
					MaterialToast.fireToast("STEEM API ERROR [@" + username + "]: " + error, 1000);
					iList.add(username);
					iList.previous();
					indexBlogs(iList);
					return;
				}
				if (result != null) {
					indexBlogEntries(username, result);
				}
				indexBlogs(iList);
			}
		};
		int count = 10;
		SteemApi.getDiscussionsByBlog(username, count, cb);
	}

	@EventHandler
	protected void onNsfwVerifiedAccountsLoaded(Event.NsfwVerifiedAccountsLoaded event) {
		List<String> list = event.getList();
		if (list == null || list.isEmpty()) {
			return;
		}
		fireEvent(new Event.ShowLoading(true));
		final ListIterator<String> iList = new ArrayList<>(list).listIterator();
		indexBlogs(iList);
		MaterialToast.fireToast("Loading and Tag Indexing Blogs", 1000);
	}

	public static interface Mapper extends ObjectMapper<Discussion> {
	}
}
