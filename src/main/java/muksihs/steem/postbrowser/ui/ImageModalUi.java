package muksihs.steem.postbrowser.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialModal;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.EventBusComposite;
import muksihs.steem.postbrowser.shared.BlogIndexEntry;

public class ImageModalUi extends EventBusComposite {

	private static ImageModalUiUiBinder uiBinder = GWT.create(ImageModalUiUiBinder.class);

	interface ImageModalUiUiBinder extends UiBinder<Widget, ImageModalUi> {
	}
	
	@UiField
	protected MaterialModal modal;
	@UiField
	protected MaterialImage image;
	@UiField
	protected MaterialButton btnDismiss;
	private BlogIndexEntry preview;

	public ImageModalUi() {
		initWidget(uiBinder.createAndBindUi(this));
		modal.addCloseHandler((e)->this.removeFromParent());
		btnDismiss.addClickHandler((e)->modal.close());
		image.addClickHandler((e)->modal.close());
	}
	
	@EventHandler
	public void setZoomImage(Event.SetModalImage event) {
		this.preview = event.getZoomPreview();
		if (preview.getCombinedImages()==null||preview.getCombinedImages().isEmpty()) {
			modal.close();
			return;
		}
		String fullImageUrl = preview.getCombinedImages().get(0);
		image.setUrl(fullImageUrl);
	}
	
	public void open() {
		modal.open();
		fireEvent(new Event.GetModalImage());
	}
	
	interface MyEventBinder extends EventBinder<ImageModalUi>{}
	@Override
	protected <T extends EventBinder<EventBusComposite>> T getEventBinder() {
		return GWT.create(MyEventBinder.class);
	}

}
