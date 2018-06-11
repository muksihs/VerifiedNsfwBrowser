package muksihs.steem.postbrowser.eventbus;

import java.util.List;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public interface Event {
	
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
