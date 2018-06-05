package muksihs.steem.postbrowser.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;

public class AboutUi extends Composite {

	private static AboutUiUiBinder uiBinder = GWT.create(AboutUiUiBinder.class);

	interface AboutUiUiBinder extends UiBinder<Widget, AboutUi> {
	}
	
	@UiField
	protected MaterialModal modal;
	@UiField
	protected MaterialButton btnOk;

	public AboutUi() {
		initWidget(uiBinder.createAndBindUi(this));
		btnOk.addClickHandler((e)->modal.close());
		modal.addCloseHandler((e)->this.removeFromParent());
	}
	
	public void open() {
		modal.open();
	}
}
