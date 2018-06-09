package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;

public class AppEP implements EntryPoint, GlobalAsyncEventBus {

	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(handler);
		ViewController.bind();
		AppController.bind();
		fireEvent((new Event.Loading(true)));
		fireEvent(new Event.AppStart());
	}

	private UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		@Override
		public void onUncaughtException(Throwable e) {
			GWT.log(e.getMessage() == null ? "" : e.getMessage(), e);
		}
	};
}
