package muksihs.steem.postbrowser.client;

import com.github.mvp4g.mvp4g2.core.ui.AbstractPresenter;
import com.github.mvp4g.mvp4g2.core.ui.IsShell;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Presenter;
import com.google.gwt.user.client.ui.RootPanel;

@Presenter(viewClass = ShellView.class, viewInterface = IShellView.class)
public class ShellPresenter extends AbstractPresenter<AppEventBus, IShellView> //
		implements IShellView.Presenter, IsShell<AppEventBus, IShellView> //
{
	@Override
	public void setShell() {
		RootPanel.get("app").add(view);
	}
}
