package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.ScriptInjector;

import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;

public class AppEntryPoint implements EntryPoint, GlobalAsyncEventBus {
	private static final String STEEMJS="https://cdn.steemjs.com/lib/latest/steem.min.js";
	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(handler);
		fireEvent((new Event.ShowLoading(true)));
		Callback<Void, Exception> loaded=new Callback<Void, Exception>() {
			@Override
			public void onSuccess(Void result) {
				ViewController.bind();
				AppController.bind();
				VerifiedNsfwBlogData.bind();
				fireEvent(new Event.AppStart());
			}
			
			@Override
			public void onFailure(Exception reason) {
				
			}
		};
		ScriptInjector.fromUrl(STEEMJS).setRemoveTag(false).setWindow(ScriptInjector.TOP_WINDOW).setCallback(loaded).inject();
	}

	private UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		@Override
		public void onUncaughtException(Throwable e) {
			GWT.log(e.getMessage() == null ? "" : e.getMessage(), e);
		}
	};
}
