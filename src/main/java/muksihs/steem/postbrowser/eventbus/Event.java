package muksihs.steem.postbrowser.eventbus;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public interface Event {

	public class Loading extends GenericEvent {
		private final boolean loading;
		public Loading(boolean loading) {
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
