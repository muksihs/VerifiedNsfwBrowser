package muksihs.steem.postbrowser.client;

import com.github.mvp4g.mvp4g2.core.ui.LazyReverseView;
import com.google.gwt.user.client.ui.Widget;

import muksihs.steem.postbrowser.ui.MainView;

public class ShellView //
		extends LazyReverseView<IShellView.Presenter> //
		implements IShellView {

	protected MainView mainView;
	@Override
	public Widget asWidget() {
		return mainView;
	}

	@Override
	public void createView() {
		mainView = new MainView();
	}
}
