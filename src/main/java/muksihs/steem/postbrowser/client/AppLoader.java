package muksihs.steem.postbrowser.client;

import com.github.mvp4g.mvp4g2.core.application.IsApplicationLoader;

import gwt.material.design.client.ui.MaterialLoader;

public class AppLoader implements IsApplicationLoader {

	@Override
	public void load(FinishLoadCommand finishLoadCommand) {
		MaterialLoader.loading(true);
		finishLoadCommand.finishLoading();
	}
}
