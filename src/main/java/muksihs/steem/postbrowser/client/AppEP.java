package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

import gwt.material.design.client.ui.MaterialLoader;

public class AppEP implements EntryPoint {

	@Override
	public void onModuleLoad() {
		MaterialLoader.loading(true);
		GWT.log(this.getClass().getSimpleName() + "#onModuleLoad");
		GWT.setUncaughtExceptionHandler(handler);
		App app = new AppImpl();
	}

	private UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
		@Override
		public void onUncaughtException(Throwable e) {
			GWT.log(e.getMessage() == null ? "" : e.getMessage(), e);
		}
	};
}
