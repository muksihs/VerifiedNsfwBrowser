package muksihs.steem.postbrowser.eventbus;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.GenericEvent;

import elemental2.dom.DomGlobal;

public interface GlobalAsyncEventBus {
	default EventBus getEventBus() {
		return Internal.getEventBus();
	}
	class Internal {
		private static EventBus _eventBus;

		public static EventBus getEventBus() {
			if (_eventBus == null) {
				initEventBus();
			}
			return _eventBus;
		}

		private static void initEventBus() {
			_eventBus = new AsyncEventBus() {
				@Override
				public void fireEvent(com.google.web.bindery.event.shared.Event<?> event) {
					if (event != null) {
						DomGlobal.console.log("Event: " + event.getClass().getSimpleName());
					} else {
						DomGlobal.console.log("Null event!");
					}
					super.fireEvent(event);
				};

				@Override
				public void fireEventFromSource(com.google.web.bindery.event.shared.Event<?> event, Object source) {
					if (event != null) {
					} else {
						GWT.log("null event!");
					}
					super.fireEventFromSource(event, source);
				};
			};
		}
	}

	default void fireEvent(GenericEvent event) {
		getEventBus().fireEvent(event);
	}
}
