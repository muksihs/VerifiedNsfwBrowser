package muksihs.steem.postbrowser.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.github.nmorel.gwtjackson.client.exception.JsonDeserializationException;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import elemental2.dom.DomGlobal;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;
import muksihs.steem.postbrowser.shared.BlogIndex;
import muksihs.steem.postbrowser.shared.BlogIndexEntry;
import muksihs.steem.postbrowser.shared.NsfwVerifiedList;
import steem.SteemApi;
import steem.SteemApi.DiscussionsCallback;
import steem.models.Discussion;
import steem.models.DiscussionMetadata;
import steem.models.Discussions;

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
		if (result==null) {
			return;
		}
		List<BlogIndexEntry> entries = new ArrayList<>();
		for (Discussion discussion: result.getList()) {
			BlogIndexEntry entry = new BlogIndexEntry();
			entry.setAuthor(discussion.getAuthor());
			entry.setCreated(discussion.getCreated());
			entry.setPermlink(discussion.getPermlink());
			parseMetadata: try {
				DiscussionMetadata metadata=SteemApi.discussionMetadataMapper.read(discussion.getJsonMetadata());
				if (metadata==null) {
					break parseMetadata;
				}
				if (metadata.getTags()==null) {
					break parseMetadata;
				}
				entry.setTags(metadata.getTags());
			} catch (JsonDeserializationException e) {
			}
			//add author as a special "@" prefixed tag.
			entry.getTags().add("@"+entry.getAuthor());
			entries.add(entry);
		}
		index.addAll(entries);
	};
	
	@EventHandler
	protected void onIndexing(Event.Indexing event) {
		DomGlobal.console.log("Indexing: "+String.valueOf(event.isIndexing()));
		if (!event.isIndexing()) {
			DomGlobal.console.log(" - Unique tags: "+index.getTags().size());
			DomGlobal.console.log(" - Unique entries: "+index.getEntries().size());
		}
	}
	
	@EventHandler
	protected void onAppStart(Event.AppStart event) {
		// init
		fireEvent(new Event.LoadNsfwVerifiedAccounts());
	}
	
	@EventHandler
	protected void onLoadNsfwVerifiedAccounts(Event.LoadNsfwVerifiedAccounts event) {
		NsfwVerifiedList users = BundledData.Data.getNsfwVerifiedList();
		final List<String> list = users.getList();
		// lowercase
		ListIterator<String> li = list.listIterator();
		while (li.hasNext()) {
			String next = li.next();
			li.set(next.toLowerCase().trim());
		}
		Collections.sort(list);
		fireEvent(new Event.NsfwVerifiedAccountsLoaded(list));
	}
	
	private void indexBlogs(Iterator<String> iList) {
		if (!iList.hasNext()) {
			fireEvent(new Event.ShowLoading(false));
			return;
		}
		DiscussionsCallback cb=new DiscussionsCallback() {
			@Override
			public void onResult(String error, Discussions result) {
				if (error!=null) {
					GWT.log("DiscussionsCallback#onResult-error: "+error);
				}
				indexBlogEntries(result);
				indexBlogs(iList);
			}
		};
		final String username = iList.next();
		GWT.log("#getDiscussionsByBlog: "+username);
		int count;
		if (Util.isSdm()) {
			count = 3;
		} else {
			count = 100;
		}
		SteemApi.getDiscussionsByBlog(username, count, cb);
		iList.remove();
	}
	
	@EventHandler
	protected void onNsfwVerifiedAccountsLoaded(Event.NsfwVerifiedAccountsLoaded event) {
		fireEvent(new Event.ShowLoading(true));
		List<String> list = event.getList();
		if (list==null||list.isEmpty()) {
			return;
		}
		final Iterator<String> iList = new ArrayList<>(list).iterator();
		indexBlogs(iList);
	}

	public static interface Mapper extends ObjectMapper<Discussion> {
	}
}
