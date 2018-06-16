package muksihs.steem.postbrowser.eventbus;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AsyncEventBus extends SimpleEventBus {
	public AsyncEventBus() {
		super();
	}

	@Override
	public void fireEvent(Event<?> event) {
		async(()->super.fireEvent(event));
	}

	@Override
	public void fireEventFromSource(Event<?> event, Object source) {
		async(()->super.fireEventFromSource(event, source));
	}

	protected void async(ScheduledCommand cmd) {
//		Scheduler.get().scheduleDeferred(cmd);
		Timer t = new Timer() {
			@Override
			public void run() {
				cmd.execute();
			}
		};
		t.schedule(0);
	}

}
