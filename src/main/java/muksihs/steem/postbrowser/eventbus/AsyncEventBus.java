package muksihs.steem.postbrowser.eventbus;

import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AsyncEventBus extends SimpleEventBus {
	private static void async(Runnable run) {
		new Timer() {
			@Override
			public void run() {
				run.run();
			}
		}.schedule(0);
	}

	public AsyncEventBus() {
		super();
	}

	@Override
	public void fireEvent(Event<?> event) {
		if (event==null) {
			throw new IllegalArgumentException("NULL EVENT");
		}
		async(() -> super.fireEvent(event));
	}

	@Override
	public void fireEventFromSource(Event<?> event, Object source) {
		if (event==null) {
			throw new IllegalArgumentException("NULL EVENT");
		}
		async(() -> super.fireEventFromSource(event, source));
	}

}
