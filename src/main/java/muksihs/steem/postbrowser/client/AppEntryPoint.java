package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;

public class AppEntryPoint implements EntryPoint, GlobalAsyncEventBus {
	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(handler);
		fireEvent((new Event.ShowLoading(true)));
		ViewController.bind();
		AppController.bind();
		VerifiedNsfwBlogData.bind();
		fireEvent(new Event.AppStart());
	}

	private UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		@Override
		public void onUncaughtException(Throwable e) {
			GWT.log(e.getMessage() == null ? "" : e.getMessage(), e);
		}
	};
}
