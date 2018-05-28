package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;

import gwt.material.design.client.ui.MaterialLoader;

public class VerifiedNsfwBrowserEP implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MaterialLoader.loading(true);
		GWT.log(this.getClass().getSimpleName() + "#onModuleLoad");
		GWT.setUncaughtExceptionHandler(handler);
		Scheduler.get().scheduleDeferred(new App());
	}

	private UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		@Override
		public void onUncaughtException(Throwable e) {
			GWT.log(e.getMessage() == null ? "" : e.getMessage(), e);
		}
	};
}
