package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTitle;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.Event.ShowMainView;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;
import muksihs.steem.postbrowser.ui.AboutUi;
import muksihs.steem.postbrowser.ui.LoginUi;
import muksihs.steem.postbrowser.ui.MainView;

public class ViewController implements GlobalAsyncEventBus {
	private static ViewController instance;

	public static void unbind() {
		if (instance == null) {
			return;
		}
		instance.registration.removeHandler();
		instance = null;
	}

	public static void bind() {
		if (instance == null) {
			instance = new ViewController();
		}
		HandlerRegistration registration = eventBinder.bindEventHandlers(instance, instance.getEventBus());
		instance.setRegistration(registration);
	}

	interface MyEventBinder extends EventBinder<ViewController> {
	}

	private static final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	private HandlerRegistration registration;

	private MainView mainView;

	private void setRegistration(HandlerRegistration registration) {
		this.registration = registration;
	}

	protected ViewController() {
	}
	
	@EventHandler
	protected void onShowLoginUi(Event.ShowLoginUi event) {
		LoginUi loginUi = new LoginUi();
		RootPanel.get().add(loginUi);
		loginUi.open();
	}
	
	@EventHandler
	protected void onShowLoading(Event.ShowLoading event) {
		MaterialLoader.loading(event.isLoading());	
	}
	
	@EventHandler
	protected void onShowMainView(ShowMainView event) {
		if (mainView == null) {
			mainView = new MainView();
		
		}
		RootPanel.get("app").clear();
		RootPanel.get("app").add(mainView);
	}
	
	@EventHandler
	protected void onShowAbout(Event.ShowAbout event) {
		AboutUi about = new AboutUi();
		RootPanel.get().add(about);
		about.open();
	}
	
	@EventHandler
	protected void alertMessage(Event.AlertMessage event) {
		MaterialModal modal = new MaterialModal();
		modal.setDismissible(true);
		modal.setTitle("Alert!");
		modal.addCloseHandler((e) -> modal.removeFromParent());
		MaterialTitle title = new MaterialTitle(event.getMessage());
		title.setMarginTop(-50);
		modal.add(title);
		MaterialButton btnOk = new MaterialButton("OK");
//		btnOk.getElement().getStyle().setBackgroundColor("DarkBlue");
		btnOk.setMargin(4);
		btnOk.addClickHandler((e) -> modal.close());
		modal.add(btnOk);
		RootPanel.get().add(modal);
		modal.open();
	}
}
