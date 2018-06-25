package muksihs.steem.postbrowser.client;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;

import elemental2.dom.DomGlobal;
import muksihs.steem.postbrowser.client.cache.AccountCache;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;
import muksihs.steem.postbrowser.shared.BlogIndex.FilteredListMode;
import muksihs.steem.postbrowser.shared.BlogIndexEntry;
import muksihs.steem.postbrowser.shared.SteemPostingInfo;
import steem.MapperCallback.UserAccountInfoListCallback;
import steem.SteemApi;
import steem.SteemApi.UserAccountInfoList;
import steem.SteemAuth;
import steem.models.AuthorizationList.AuthArray;
import steem.models.Authorizations;
import steem.models.UserAccountInfo;

public class AppController implements GlobalAsyncEventBus {
	private static final String DEFAULT_USER = "default-user";
	private static AppController instance;

	public static void unbind() {
		if (instance == null) {
			return;
		}
		instance.registration.removeHandler();
		instance = null;
	}

	public static void bind() {
		if (instance == null) {
			instance = new AppController();
		}
		HandlerRegistration registration = eventBinder.bindEventHandlers(instance, instance.getEventBus());
		instance.setRegistration(registration);
	}

	interface MyEventBinder extends EventBinder<AppController> {
	}

	private static final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	private HandlerRegistration registration;

	private void setRegistration(HandlerRegistration registration) {
		this.registration = registration;
	}

	protected AppController() {
	}
	
	@EventHandler
	protected void onAddToIncludeFilter(Event.AddToIncludeFilter event) {
		this.haveTags.add(event.getTag());
		this.notTags.remove(event.getTag());
		updateActiveTagsDisplay();
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
	}
	
	@EventHandler
	protected void onAddToExcludeFilter(Event.AddToExcludeFilter event) {
		this.notTags.add(event.getTag());
		this.haveTags.remove(event.getTag());
		updateActiveTagsDisplay();
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
	}
	
	@EventHandler
	protected void onRemoveFromFilter(Event.RemoveFromFilter event) {
		String tag = event.getTag();
		if (tag.startsWith("-")||tag.startsWith("+")) {
			tag=tag.substring(1);
		}
		this.haveTags.remove(tag);
		this.notTags.remove(tag);
		DomGlobal.console.log("#onRemoveFromFilter", "HAVE TAGS: ", haveTags.toString(), "NOT TAGS: ", notTags.toString(), filterMode.name());
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
		updateActiveTagsDisplay();
	}
	
	private void updateActiveTagsDisplay() {
		Collection<String> tags=new TreeSet<>();
		for (String tag: haveTags) {
			tags.add("+"+tag);
		}
		for (String tag: notTags) {
			tags.add("-"+tag);
		}
		fireEvent(new Event.ShowFilterTags(tags));
	}
	
	@EventHandler
	protected void onNextPreviewSet(Event.NextPreviewSet event) {
		pageNo++;
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
	}
	
	@EventHandler
	protected void onPreviousPreviewSet(Event.PreviousPreviewSet event) {
		if (pageNo>0) {
			pageNo--;
		}
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
	}

	@EventHandler
	protected void onAppStart(Event.AppStart event) {
		// load main view
		fireEvent(new Event.ShowMainView());

		// init
		// fireEvent(new Event.LoadNsfwVerifiedAccounts());

		// validate cached login credentials (if any)
		AccountCache cache = new AccountCache();
		SteemPostingInfo info = cache.get(DEFAULT_USER);
		if (info != null) {
			fireEvent(new Event.TryLogin(info.getUsername(), info.getWif(), true));
		} else {
			fireEvent(new Event.LoginComplete(false));
		}

		// all done
		fireEvent(new Event.ShowLoading(false));
	}

	private GenericEvent afterLoginPendingEvent;

	@EventHandler
	protected <T extends GenericEvent> void login(Event.Login<T> event) {
		afterLoginPendingEvent = event.getRefireEvent();
		fireEvent(new Event.ShowLoginUi());
	}

	private boolean loggedIn;

	private boolean isLoggedIn() {
		return loggedIn;
	}

	@EventHandler
	protected void onLoginLogout(Event.LoginLogout event) {
		if (isLoggedIn()) {
			AccountCache accountCache = new AccountCache();
			SteemPostingInfo steemPostingInfo = accountCache.get(DEFAULT_USER);
			steemPostingInfo.setWif(null);
			accountCache.put(DEFAULT_USER, steemPostingInfo);
			loggedIn = false;
			fireEvent(new Event.LoginComplete(false));
			Location.reload();
		} else {
			fireEvent(new Event.Login<GenericEvent>(null));
		}
	}

	@EventHandler
	protected void getAppVersion(Event.GetAppVersion event) {
		final String versionProperty;
		final String dateString = new java.sql.Date(System.currentTimeMillis()).toString();
		if (!Util.isSdm()) {
			versionProperty = System.getProperty("version", dateString + "-BETA");
		} else {
			versionProperty = String.valueOf(dateString+"-SDM");
		}
		fireEvent(new Event.AppVersion(versionProperty));
	}

	private String username = "";
	private String userWif = "";

	@EventHandler
	protected void tryLogin(Event.TryLogin event) {
		final String wif = event.getWif() == null ? "" : event.getWif().trim();
		if (wif.isEmpty()) {
			fireEvent(new Event.LoginComplete(false));
			return;
		}
		if (!wif.equals(wif.replaceAll("[^123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]", ""))) {
			if (!event.isSilent()) {
				fireEvent(new Event.AlertMessage("That is not a valid key."));
			}
			fireEvent(new Event.ShowLoading(false));
			return;
		}
		if (wif.startsWith("STM")) {
			if (!event.isSilent()) {
				fireEvent(new Event.AlertMessage(
						"That is not the PRIVATE posting key. Please visit your wallet => permissions => select \"show private key\" next to your posting key, to obtain your private posting key. It will not start with 'STM'."));
			}
			fireEvent(new Event.ShowLoading(false));
			return;
		}
		UserAccountInfoListCallback cb = new UserAccountInfoListCallback() {
			@Override
			public void onResult(String error, UserAccountInfoList result) {
				fireEvent(new Event.ShowLoading(false));
				if (error != null) {
					if (!event.isSilent()) {
						fireEvent(new Event.AlertMessage(error));
					}
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				if (result.getList().isEmpty()) {
					if (!event.isSilent()) {
						fireEvent(new Event.AlertMessage("Username not found!"));
					}
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				UserAccountInfo accountInfo = result.getList().iterator().next();
				if (accountInfo == null) {
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				Authorizations posting = accountInfo.getPosting();
				if (posting == null) {
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				List<AuthArray> keyAuths = posting.getKeyAuths();
				if (keyAuths == null || keyAuths.isEmpty()) {
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				AuthArray keylist = keyAuths.iterator().next();
				if (keylist.getPublicKey() == null) {
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				String publicWif = keylist.getPublicKey();
				try {
					if (!SteemAuth.wifIsValid(wif, publicWif)) {
						if (!event.isSilent()) {
							fireEvent(new Event.AlertMessage("THAT IS NOT YOUR PRIVATE POSTING KEY"));
						}
						fireEvent(new Event.LoginComplete(false));
						return;
					}
				} catch (JavaScriptException e) {
					DomGlobal.console.log("JavaScriptException: " + e.getMessage());
					if (!event.isSilent()) {
						fireEvent(new Event.AlertMessage(e.getMessage()));
					}
					fireEvent(new Event.LoginComplete(false));
					return;
				}
				AccountCache cache = new AccountCache();
				SteemPostingInfo info = new SteemPostingInfo();
				info.setUsername(accountInfo.getName());
				info.setWif(wif);
				cache.put(DEFAULT_USER, info);
				fireEvent(new Event.LoginComplete(true));
				DomGlobal.console.log("Logged in as: " + accountInfo.getName());
				username = accountInfo.getName();
				userWif = wif;
			}
		};
		fireEvent(new Event.ShowLoading(true));
		String username = event.getUsername();
		while (username.trim().startsWith("@")) {
			username = username.trim().substring(1);
		}
		try {
			SteemApi.getAccount(username, cb);
		} catch (Exception e) {
			DomGlobal.console.log(e);
			new Timer() {
				@Override
				public void run() {
					fireEvent(event);
				}
			}.schedule(500);
		}
	}

	@EventHandler
	protected void loginComplete(Event.LoginComplete event) {
		fireEvent(new Event.ShowLoading(false));
		loggedIn = event.isLoggedIn();
		if (afterLoginPendingEvent != null && event.isLoggedIn()) {
			fireEvent(afterLoginPendingEvent);
		}
		afterLoginPendingEvent = null;
		History.fireCurrentHistoryState();
	}
	
	private FilteredListMode filterMode=FilteredListMode.AND;
	private final Set<String> haveTags=new TreeSet<>();
	private final Set<String> notTags=new TreeSet<>();
	private Timer loadUpdatePreviewList;
	private int pageNo=0;
	private BlogIndexEntry zoomPreview;
	private static final int PAGE_SIZE=6;
	@EventHandler
	protected void onIndexing(Event.Indexing event) {
		if (loadUpdatePreviewList!=null) {
			loadUpdatePreviewList.cancel();
			loadUpdatePreviewList=null;
		}
		if (event.isIndexing()) {
			return;
		}
		loadUpdatePreviewList=new Timer() {
			@Override
			public void run() {
				fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
			}
		};
		loadUpdatePreviewList.schedule(100);
	}
	@EventHandler
	protected void onUpdatedPreviewList(Event.UpdatedPreviewList event) {
		List<BlogIndexEntry> list = event.getList();
		while (pageNo*PAGE_SIZE>list.size() && pageNo>0) {
			pageNo--;
		}
		int fromIndex = pageNo*PAGE_SIZE;
		int toIndex = Math.min((pageNo+1)*PAGE_SIZE, list.size());
		fireEvent(new Event.ShowPreviews(list.subList(fromIndex, toIndex)));
		fireEvent(new Event.EnableNextButton((pageNo+1)*PAGE_SIZE<list.size()));
		fireEvent(new Event.EnablePreviousButton(pageNo>0));
	}
	@EventHandler
	protected void onMostRecentSet(Event.MostRecentSet event) {
		pageNo=0;
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
	}
	@EventHandler
	protected void onClearSearch(Event.ClearSearch event) {
		haveTags.clear();
		notTags.clear();
		fireEvent(new Event.LoadUpdatePreviewList(filterMode, haveTags, notTags));
	}
	@EventHandler
	protected void zoomImage(Event.ZoomImage event) {
		this.zoomPreview = event.getPreview();
		fireEvent(new Event.ImageModal());
	}
	@EventHandler
	protected void getModalImage(Event.GetModalImage event) {
		fireEvent(new Event.SetModalImage(zoomPreview));
	}

}
