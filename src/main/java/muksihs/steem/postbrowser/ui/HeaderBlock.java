package muksihs.steem.postbrowser.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialNavBrand;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.EventBusComposite;


public class HeaderBlock extends EventBusComposite {

	interface HeaderBlockUiBinder extends UiBinder<Widget, HeaderBlock> {
	}
	
	interface MyEventBinder extends EventBinder<HeaderBlock> {
	}

	static String versionTxt = "19000101";

	private static HeaderBlockUiBinder uiBinder = GWT.create(HeaderBlockUiBinder.class);

	@UiField
	protected MaterialNavBrand navBrand;
	
	@UiField
	protected MaterialLabel version;
	@UiField
	protected MaterialButton account;
	
	public HeaderBlock() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		account.addClickHandler((e)->fireEvent(new Event.LoginLogout()));
		navBrand.addClickHandler((e)->fireEvent(new Event.ShowAbout()));
	}

	@Override
	protected <T extends EventBinder<EventBusComposite>> T getEventBinder() {
		return GWT.create(MyEventBinder.class);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		fireEvent(new Event.GetAppVersion());
	}

	@EventHandler
	public void showLoggedInStatus(Event.LoginComplete event) {
		if (event.isLoggedIn()) {
			account.setText("LOGOUT");
		} else {
			account.setText("LOGIN");
		}
	}

	@EventHandler
	public final void setAppVersion(Event.AppVersion event) {
		version.setText(event.getVersionTxt());
		HeaderBlock.versionTxt = event.getVersionTxt();
	}
}
