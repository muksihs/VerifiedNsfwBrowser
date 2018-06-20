package muksihs.steem.postbrowser.eventbus;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.GenericEvent;

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
			_eventBus = new AsyncEventBus();
		}
	}

	default void fireEvent(GenericEvent event) {
		getEventBus().fireEvent(event);
	}
}
