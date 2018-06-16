package muksihs.steem.postbrowser.eventbus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.web.bindery.event.shared.binder.GenericEvent;

import muksihs.steem.postbrowser.shared.BlogIndex.FilteredListMode;
import muksihs.steem.postbrowser.shared.BlogIndexEntry;

public interface Event {
	
	public class EnableNextButton extends GenericEvent {
		private boolean enable;
		public EnableNextButton(boolean enable) {
			this.setEnable(enable);
		}
		public boolean isEnable() {
			return enable;
		}
		public void setEnable(boolean enable) {
			this.enable = enable;
		}

	}

	public class UpdatedPreviewList extends GenericEvent {
		private final List<BlogIndexEntry> list;
		public UpdatedPreviewList(List<BlogIndexEntry> list) {
			this.list=list;
		}
		public List<BlogIndexEntry> getList() {
			return list;
		}

	}

	public class LoadUpdatePreviewList extends GenericEvent {
		private final List<String> haveTags;
		private final List<String> notTags;
		public List<String> getHaveTags() {
			return haveTags;
		}
		public List<String> getNotTags() {
			return notTags;
		}
		public FilteredListMode getMode() {
			return mode;
		}
		private final FilteredListMode mode;
		public LoadUpdatePreviewList() {
			this.haveTags = new ArrayList<>();
			this.notTags = new ArrayList<>();
			this.mode=FilteredListMode.AND;
		}
		public LoadUpdatePreviewList(Collection<String> haveTags) {
			this.haveTags = new ArrayList<>(haveTags);
			this.notTags = new ArrayList<>();
			this.mode=FilteredListMode.AND;
		}
		public LoadUpdatePreviewList(Collection<String> haveTags, Collection<String> notTags) {
			this.haveTags = new ArrayList<>(haveTags);
			this.notTags = new ArrayList<>(notTags);
			this.mode=FilteredListMode.AND;
		}
		public LoadUpdatePreviewList(FilteredListMode mode, Collection<String> haveTags, Collection<String> notTags) {
			this.haveTags = new ArrayList<>(haveTags);
			this.notTags = new ArrayList<>(notTags);
			this.mode=mode;
		}
	}

	public class ZoomImage extends GenericEvent {
		private final BlogIndexEntry preview;
		public ZoomImage(BlogIndexEntry preview) {
			this.preview = preview;
		}
		public BlogIndexEntry getPreview() {
			return preview;
		}

	}

	public class ShowPreviews extends GenericEvent {
		private final List<BlogIndexEntry> previews;
		public ShowPreviews(Collection<BlogIndexEntry> previews) {
			this.previews=new ArrayList<>(previews);
		}
		public List<BlogIndexEntry> getPreviews() {
			return previews;
		}
	}

	public class AddToExcludeFilter extends GenericEvent {
private final String tag;
		public AddToExcludeFilter(String tag) {
			this.tag=tag;
		}
		public String getTag() {
			return tag;
		}

	}

	public class AddToIncludeFilter extends GenericEvent {
		private final String tag;
		public AddToIncludeFilter(String tag) {
			this.tag=tag;
		}
		public String getTag() {
			return tag;
		}

	}

	public class RemoveFromFilter extends GenericEvent {
		private final String tag;
		public RemoveFromFilter(String tag) {
			this.tag=tag;
		}
		public String getTag() {
			return tag;
		}

	}

	public class ShowFilterTags extends GenericEvent {
		private final List<String> tags;
		public ShowFilterTags(Collection<String> tags) {
			this.tags=new ArrayList<>(tags);
		}
		public List<String> getTags() {
			return tags;
		}
	}

	public class SetAvailableTags extends GenericEvent {
		private final List<String> tags;
		public SetAvailableTags(Collection<String> tags) {
			this.tags=new ArrayList<>(tags);
		}
		public List<String> getTags() {
			return tags;
		}
	}

	public class EnablePreviousButton extends GenericEvent {
		private final boolean enable;
		public EnablePreviousButton(boolean enable) {
			this.enable=enable;
		}
		public boolean isEnable() {
			return enable;
		}
	}

	public class BrowseViewLoaded extends GenericEvent {

	}

	public class SaveFilter extends GenericEvent {

	}

	public class LoadFilter extends GenericEvent {

	}

	public class ClearSearch extends GenericEvent {

	}

	public class MostRecentSet extends GenericEvent {

	}

	public class NextPreviewSet extends GenericEvent {

	}

	public class PreviousPreviewSet extends GenericEvent {

	}

	public class Indexing extends GenericEvent {

		private final boolean indexing;

		public Indexing(boolean indexing) {
			this.indexing=indexing;
		}

		public boolean isIndexing() {
			return indexing;
		}

	}

	public class NsfwVerifiedAccountsLoaded extends GenericEvent {
		private final List<String> list;
		public NsfwVerifiedAccountsLoaded(List<String> list) {
			this.list=list;
		}
		public NsfwVerifiedAccountsLoaded(Collection<String> list) {
			this.list=new ArrayList<>(list);
		}
		public List<String> getList() {
			return list;
		}

	}

	public class LoadNsfwVerifiedAccounts extends GenericEvent {

	}

	public class ShowLoginUi extends GenericEvent {

	}

	public class Login<T extends GenericEvent> extends GenericEvent {
		private final T refireEvent;

		public Login(T event) {
			this.refireEvent = event;
		}

		public T getRefireEvent() {
			return refireEvent;
		}
	}
	
	public class LoginLogout extends GenericEvent {

	}


	public class AlertMessage extends GenericEvent {
		private final String message;

		public AlertMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

	}

	
	public class LoginComplete extends GenericEvent {
		private final boolean loggedIn;

		public LoginComplete(boolean loggedIn) {
			this.loggedIn = loggedIn;
		}

		public boolean isLoggedIn() {
			return loggedIn;
		}
	}

	public class TryLogin extends GenericEvent {
		private final String username;
		private final String wif;
		private final boolean silent;

		public TryLogin(String username, String wif) {
			this(username, wif, false);
		}

		public TryLogin(String username, String wif, boolean silent) {
			this.username = username;
			this.wif = wif;
			this.silent = silent;
		}

		public String getUsername() {
			return username;
		}

		public String getWif() {
			return wif;
		}

		public boolean isSilent() {
			return silent;
		}
	}

	public class ShowLoading extends GenericEvent {
		private final boolean loading;
		public ShowLoading(boolean loading) {
			this.loading=loading;
		}
		public boolean isLoading() {
			return loading;
		}

	}

	public class ShowMainView extends GenericEvent {

	}

	public class AppStart extends GenericEvent {

	}

	public class GetAppVersion extends GenericEvent {

	}

	public class ShowAbout extends GenericEvent {

	}

	public class AppVersion extends GenericEvent {
		private final String versionTxt;

		public AppVersion(String versionTxt) {
			this.versionTxt = versionTxt;
		}

		public String getVersionTxt() {
			return versionTxt;
		}
	}

}
