package muksihs.steem.postbrowser.eventbus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;

public abstract class EventBusComposite extends Composite implements GlobalAsyncEventBus {

	protected HandlerRegistration registration;

	public EventBusComposite() {
	}

	protected abstract <T extends EventBinder<EventBusComposite>> T getEventBinder();

	@Override
	protected void onLoad() {
		try {
			registration = getEventBinder().bindEventHandlers(this, getEventBus());
		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
		}
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		try {
			registration.removeHandler();
		} catch (Exception e) {
			GWT.log(e.getMessage(), e);
		}
		super.onUnload();
	};
}
