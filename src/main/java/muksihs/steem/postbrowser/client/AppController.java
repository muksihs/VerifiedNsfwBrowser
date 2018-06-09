package muksihs.steem.postbrowser.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
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
import gwt.material.design.client.ui.MaterialLoader;
import muksihs.steem.postbrowser.client.cache.AccountCache;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;
import muksihs.steem.postbrowser.shared.NsfwVerifiedList;
import muksihs.steem.postbrowser.shared.SteemPostingInfo;
import steem.SteemApi;
import steem.SteemApi.UserAccountInfoList;
import steem.SteemApi.UserAccountInfoListCallback;
import steem.SteemAuth;
import steem.models.AuthorizationList.AuthArray;
import steem.models.Authorizations;
import steem.models.Discussion;
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
	protected void onAppStart(Event.AppStart event) {
		// load main view
		fireEvent(new Event.ShowMainView());

		// init
		fireEvent(new Event.LoadNsfwVerifiedAccounts());
		
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
		fireEvent(new Event.AppVersion("20180608-BETA"));
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
	
	@EventHandler
	protected void onNsfwVerifiedAccountsLoaded(Event.NsfwVerifiedAccountsLoaded event) {
		fireEvent(new Event.ShowLoading(true));
		List<String> list = event.getList();
		int maxSublistSize = Math.min(5, list.size());
		List<String> sublist = new ArrayList<>(list.subList(0, maxSublistSize));
		UserAccountInfoListCallback callback = new UserAccountInfoListCallback() {
			@Override
			public void onResult(String error, UserAccountInfoList result) {
				Set<String> receivedUsers = new TreeSet<>();
				for (UserAccountInfo user : result.getList()) {
					receivedUsers.add(user.getName());
				}

				sublist.removeAll(receivedUsers);
				if (!sublist.isEmpty()) {
					GWT.log("=== BAD ACCOUNT(S): " + sublist.toString());
				}
				if (!list.isEmpty()) {
					int maxSublistSize = Math.min(5, list.size());
					sublist.clear();
					sublist.addAll(new ArrayList<>(list.subList(0, maxSublistSize)));
					list.subList(0, maxSublistSize).clear();
					SteemApi.getAccounts(sublist, this);
				} else {
					fireEvent(new Event.ShowLoading(false));
				}
			}
		};
		// process in sets of 5
		SteemApi.getAccounts(sublist, callback);
		list.subList(0, maxSublistSize).clear();
	}

	public static interface Mapper extends ObjectMapper<Discussion> {
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
						new AccountCache().remove(DEFAULT_USER);
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
			SteemApi.getAccounts(new String[] { username }, cb);
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
		if (event.isLoggedIn()) {
			History.fireCurrentHistoryState();
		} else {
			
		}
	}
}
