package muksihs.steem.postbrowser.client;

import com.github.mvp4g.mvp4g2.core.eventbus.IsEventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Debug;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Event;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.EventBus;
import com.github.mvp4g.mvp4g2.core.eventbus.annotation.Start;

@Debug
@EventBus(shell = ShellPresenter.class)
public interface AppEventBus extends IsEventBus {
	@Start
	@Event
	void start();
	
	@Event
	void onSetAppVersion(String versionTxt);
}
