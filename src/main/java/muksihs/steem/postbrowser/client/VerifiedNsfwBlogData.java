package muksihs.steem.postbrowser.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
	}

	protected void indexBlogEntries(Discussions result) {
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
				if (!metadata.getTags().contains("nsfw") && !metadata.getTags().contains("NSFW")) {
					GWT.log("NOT NSFW post skipped: " + entry.getAuthor() + ", " + entry.getPermlink());
					continue;
				}
				entry.setTags(metadata.getTags());
				entry.setImage(metadata.getImage());
				entry.setThumbnail(metadata.getThumbnail());
			} catch (JsonDeserializationException e) {
				GWT.log(e.getMessage(), e);
			}
			entries.add(entry);
		}
		index.addAll(entries);
	};

	@EventHandler
	protected void onLoadUpdatePreviewList(Event.LoadUpdatePreviewList event) {
		if (!event.getHaveTags().isEmpty() || !event.getNotTags().isEmpty()) {
			List<BlogIndexEntry> list=index.getFilteredList(event.getMode(), event.getHaveTags(), event.getNotTags());
			//TODO: update available tags based on filtered posts
			fireEvent(new Event.UpdatedPreviewList(list));
			return;
		}
		List<BlogIndexEntry> list = new ArrayList<>();// index.getFilteredList(FilteredListMode.AND, empty, empty);
		for (String author : index.getDateSortedAuthors()) {
			BlogIndexEntry entry = index.getMostRecentEntry(author);
			if (entry.getImage() == null || entry.getImage().isEmpty()) {
				continue;
			}
			list.add(entry);
		}
		fireEvent(new Event.UpdatedPreviewList(list));
	}

	@EventHandler
	protected void onIndexing(Event.Indexing event) {
		if (!event.isIndexing()) {
			DomGlobal.console.log(" - Unique tags: " + index.getTags().size());
			DomGlobal.console.log(" - Unique entries: " + index.getEntries().size());
		}
	}

	@EventHandler
	protected void onAppStart(Event.AppStart event) {
		// init
		fireEvent(new Event.LoadNsfwVerifiedAccounts());
	}

	@EventHandler
	protected void onLoadNsfwVerifiedAccounts(Event.LoadNsfwVerifiedAccounts event) {
		Set<String> list = new TreeSet<>();
		int limit = 10;
		FollowingListCallback cb = new FollowingListCallback() {
			private final FollowingListCallback cb = this;

			@Override
			public void onResult(String error, FollowingList result) {
				if (error != null) {
					fireEvent(new Event.AlertMessage("STEEM API ERROR: " + error));
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
					return;
				}
				String last = followingList.get(followingList.size() - 1).getFollowing();
				SteemApi.getFollowing("verifiednsfw", last, "blog", limit, cb);
			}
		};
		SteemApi.getFollowing("verifiednsfw", "", "blog", limit, cb);
	}

	private void indexBlogs(Iterator<String> iList) {
		if (!iList.hasNext()) {
			fireEvent(new Event.ShowLoading(false));
			return;
		}
		final String username = iList.next();
		fireEvent(new Event.ShowLoading(true));
		DiscussionsCallback cb = new DiscussionsCallback() {
			@Override
			public void onResult(String error, Discussions result) {
				if (error != null) {
					fireEvent(new Event.AlertMessage("STEEM API ERROR: " + error));
				}
				if (result != null) {
					// filter out "reblogs"
					Iterator<Discussion> iResult = result.getList().iterator();
					while (iResult.hasNext()) {
						Discussion next = iResult.next();
						if (next.getAuthor().equalsIgnoreCase(username)) {
							continue;
						}
						iResult.remove();
					}
					indexBlogEntries(result);
				}
				indexBlogs(iList);
			}
		};
		GWT.log("#getDiscussionsByBlog: " + username);
		int count;
		if (Util.isSdm()) {
			count = 5;
		} else {
			count = 100;
		}
		SteemApi.getDiscussionsByBlog(username, count, cb);
		iList.remove();
	}

	@EventHandler
	protected void onNsfwVerifiedAccountsLoaded(Event.NsfwVerifiedAccountsLoaded event) {
		List<String> list = event.getList();
		if (list == null || list.isEmpty()) {
			return;
		}
		final Iterator<String> iList = new ArrayList<>(list).iterator();
		indexBlogs(iList);
	}

	public static interface Mapper extends ObjectMapper<Discussion> {
	}
}
