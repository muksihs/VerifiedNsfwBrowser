package muksihs.steem.postbrowser.client;

import com.github.mvp4g.mvp4g2.core.application.IsApplication;
import com.github.mvp4g.mvp4g2.core.application.annotation.Application;

@Application(encodeToken = true, //
		eventBus = AppEventBus.class, //
		loader = AppLoader.class, //
		historyOnStart = true)
public interface App extends IsApplication {
	
}
