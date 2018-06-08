package muksihs.steem.postbrowser.eventbus;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public interface Event {

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
