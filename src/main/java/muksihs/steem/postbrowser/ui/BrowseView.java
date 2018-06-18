package muksihs.steem.postbrowser.ui;

import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialAnchorButton;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Br;
import muksihs.steem.postbrowser.client.Util;
import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.EventBusComposite;
import muksihs.steem.postbrowser.shared.BlogIndexEntry;

public class BrowseView extends EventBusComposite {

	@UiField
	protected MaterialPanel posts;
	@UiField
	protected MaterialPanel filterTags;
	@UiField
	protected MaterialPanel availableTags;

	@UiField
	protected MaterialButton loadFilter;
	@UiField
	protected MaterialButton saveFilter;
	@UiField
	protected MaterialButton clearFilter;
	@UiField
	protected MaterialButton mostRecent;
	@UiField
	protected MaterialButton previous;
	@UiField
	protected MaterialButton next;

	@UiField
	protected MaterialButton previousBtm;
	@UiField
	protected MaterialButton nextBtm;

	private static BrowseViewUiBinder uiBinder = GWT.create(BrowseViewUiBinder.class);

	interface BrowseViewUiBinder extends UiBinder<Widget, BrowseView> {
	}

	private void getPrevious(ClickEvent e) {
		fireEvent(new Event.PreviousPreviewSet());
	}

	private void getNext(ClickEvent e) {
		fireEvent(new Event.NextPreviewSet());
	}

	public BrowseView() {
		initWidget(uiBinder.createAndBindUi(this));
		previous.addClickHandler(this::getPrevious);
		next.addClickHandler(this::getNext);
		previousBtm.addClickHandler(this::getPrevious);
		nextBtm.addClickHandler(this::getNext);
		mostRecent.addClickHandler((e) -> fireEvent(new Event.MostRecentSet()));
		clearFilter.addClickHandler((e) -> fireEvent(new Event.ClearSearch()));

		loadFilter.addClickHandler((e) -> fireEvent(new Event.LoadFilter()));
		saveFilter.addClickHandler((e) -> fireEvent(new Event.SaveFilter()));

		loadFilter.setDisplay(Display.NONE);
		saveFilter.setDisplay(Display.NONE);
	}

	interface MyEventBinder extends EventBinder<BrowseView> {
	}

	@Override
	protected <T extends EventBinder<EventBusComposite>> T getEventBinder() {
		return GWT.create(MyEventBinder.class);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		fireEvent(new Event.BrowseViewLoaded());
	}

	@UiField
	protected MaterialCollapsible tags;

	@EventHandler
	protected void onEnablePreviousButton(Event.EnablePreviousButton event) {
		this.previous.setEnabled(event.isEnable());
		this.previousBtm.setEnabled(event.isEnable());
	}

	@EventHandler
	protected void onEnableNextButton(Event.EnableNextButton event) {
		this.next.setEnabled(event.isEnable());
		this.nextBtm.setEnabled(event.isEnable());
	}

	@EventHandler
	protected void onSetAvailableTags(Event.SetAvailableTags event) {
		Scheduler.get().scheduleDeferred(() -> {
			tags.closeAll();
			MaterialPanel panel = new MaterialPanel();
			for (String tag : event.getTags()) {
				MaterialAnchorButton tagLabel = new MaterialAnchorButton(tag);
				if (tag.startsWith("-") || tag.startsWith("+")) {
					tagLabel.setEnabled(false);
				} else {
					tagLabel.addClickHandler((e) -> showAddToFilterDialog(tag));
				}
				tagLabel.setMargin(1);
				panel.add(tagLabel);
			}
			availableTags.clear();
			availableTags.add(panel);
		});
	}

	private final Set<String> activeFilterTags = new TreeSet<>();

	@EventHandler
	protected void showFilterTags(Event.ShowFilterTags event) {
		filterTags.clear();
		activeFilterTags.clear();
		tags.closeAll();
		Scheduler.get().scheduleDeferred(() -> {
			for (String tag : event.getTags()) {
				MaterialAnchorButton tagLabel = new MaterialAnchorButton(tag);
				tagLabel.addClickHandler((e) -> showRemoveFromFilterDialog(tag));
				tagLabel.setMargin(1);
				if (tag.startsWith("-")) {
					tagLabel.setBackgroundColor(Color.RED);
				}
				if (tag.startsWith("+")) {
					tagLabel.setBackgroundColor(Color.GREEN);
				}
				filterTags.add(tagLabel);
				activeFilterTags.add(tag);
			}
		});
	}

	private Void showRemoveFromFilterDialog(String tag) {
		MaterialModal dialog = new MaterialModal();
		dialog.addCloseHandler((e) -> dialog.removeFromParent());
		dialog.setTitle("Remove From Filter");
		MaterialPanel buttons = new MaterialPanel();
		buttons.setTextAlign(TextAlign.CENTER);
		MaterialButton remove = new MaterialButton("Remove From Filter: " + tag);
		MaterialButton cancel = new MaterialButton("Cancel");
		remove.addClickHandler((e) -> {
			GWT.log("DO remove from filter: " + tag);
			dialog.close();
			fireEvent(new Event.RemoveFromFilter(tag));
		});
		remove.setMargin(2);
		cancel.addClickHandler((e) -> dialog.close());
		cancel.setMargin(2);
		buttons.add(remove);
		buttons.add(cancel);
		dialog.add(buttons);
		RootPanel.get().add(dialog);
		dialog.open();
		return null;
	}

	private Void showAddToFilterDialog(String tag) {
		MaterialModal dialog = new MaterialModal();
		dialog.addCloseHandler((e) -> dialog.removeFromParent());
		dialog.setTitle("Add To Filter");
		MaterialPanel buttons = new MaterialPanel();
		buttons.setTextAlign(TextAlign.CENTER);
		MaterialButton include = new MaterialButton("Only Show Posts With " + tag);
		MaterialButton exclude = new MaterialButton("Do Not Show Posts With " + tag);
		MaterialButton cancel = new MaterialButton("Cancel");
		include.addClickHandler((e) -> {
			dialog.close();
			fireEvent(new Event.AddToIncludeFilter(tag));
		});
		include.setMargin(2);
		exclude.addClickHandler((e) -> {
			dialog.close();
			fireEvent(new Event.AddToExcludeFilter(tag));
		});
		exclude.setMargin(2);
		cancel.addClickHandler((e) -> dialog.close());
		cancel.setMargin(2);
		buttons.add(exclude);
		buttons.add(include);
		buttons.add(cancel);
		dialog.add(buttons);
		RootPanel.get().add(dialog);
		dialog.open();
		return null;
	}

	@EventHandler
	protected void showPreviews(Event.ShowPreviews event) {
		Window.scrollTo(0, 0);
		posts.clear();
		for (BlogIndexEntry preview : event.getPreviews()) {
			final String imgHref;
			if (preview.getCombinedImages() == null || preview.getCombinedImages().isEmpty()) {
				imgHref = Util.BROKEN_IMG;
			} else {
				imgHref = preview.getCombinedImages().get(0);
			}
			String steemImgHref = imgHref;
			if (!steemImgHref.contains(Util.STEEMIMAGES)) {
				steemImgHref = Util.STEEMIMAGES + Util.BROWSE_SCALE + imgHref;
			}
			if (steemImgHref.contains(Util.STEEMIMAGES + Util.NO_SCALE)) {
				steemImgHref = steemImgHref.replace(Util.STEEMIMAGES + Util.NO_SCALE,
						Util.STEEMIMAGES + Util.BROWSE_SCALE);
			}
			MaterialImage img = new MaterialImage(steemImgHref);
			img.addErrorHandler((e) -> {
				if (img.getUrl().equals(imgHref)) {
					img.setUrl(Util.BROKEN_IMG);
				} else {
					img.setUrl(imgHref);
				}
			});
			img.setWidth("100%");
			img.setMaxWidth("100%");
			img.setMargin(2);
			img.setHoverable(true);
			img.setTitle("#" + preview.getTitle() + " " + preview.getTags());
			img.addClickHandler((e) -> fireEvent(new Event.ZoomImage(preview)));
			MaterialButton viewTags = new MaterialButton();
			viewTags.setWidth("45%");
			viewTags.setMargin(2);
			viewTags.setText("TAGS VIEW");
			Set<String> previewTags = new TreeSet<>(preview.getTags());
			previewTags.add("@" + preview.getAuthor());
			viewTags.addClickHandler((e) -> showPostTags(previewTags));
			MaterialButton zoomImage = new MaterialButton();
			zoomImage.setWidth("45%");
			zoomImage.setMargin(2);
			zoomImage.setText("ZOOM IMAGE");
			zoomImage.addClickHandler((e) -> fireEvent(new Event.ZoomImage(preview)));

			MaterialLink postLink = new MaterialLink();
			String href = "https://steemit.com/" + preview.getTags().get(0) + "/@" + preview.getAuthor() + "/"
					+ preview.getPermlink();
			postLink.setWidth("45%");
			postLink.setMargin(2);
			postLink.setTarget("_blank");
			postLink.setHref(href);
			postLink.setText("STEEMIT POST");
			postLink.setType(ButtonType.RAISED);

			MaterialLink altPostLink = new MaterialLink();
			if (preview.getCustomUrl() != null && !preview.getCustomUrl().trim().isEmpty()) {
				String bhref = preview.getCustomUrl();
				altPostLink.setWidth("45%");
				altPostLink.setMargin(2);
				altPostLink.setTarget("_blank");
				altPostLink.setHref(bhref);
				altPostLink.setText(preview.getCustomUrlName());
				altPostLink.setType(ButtonType.RAISED);
				altPostLink.setBackgroundColor(Color.BLUE_GREY);
			} else {
				String bhref = "https://busy.org/" + preview.getTags().get(0) + "/@" + preview.getAuthor() + "/"
						+ preview.getPermlink();
				altPostLink.setWidth("45%");
				altPostLink.setMargin(2);
				altPostLink.setTarget("_blank");
				altPostLink.setHref(bhref);
				altPostLink.setText("BUSY.ORG POST");
				altPostLink.setType(ButtonType.RAISED);
			}

			MaterialButton channel = new MaterialButton();
			channel.setText("@" + preview.getAuthor());
			channel.setWidth("75%");
			channel.setMargin(2);
			channel.setBackgroundColor(Color.GREEN);
			channel.addClickHandler((e) -> fireEvent(new Event.AddToIncludeFilter("@" + preview.getAuthor())));

			MaterialLabel title = new MaterialLabel(preview.getTitle());
			title.setMargin(2);
			title.setFontWeight(FontWeight.BOLDER);

			MaterialPanel panel = new MaterialPanel();
			panel.getElement().getStyle().setVerticalAlign(VerticalAlign.TOP);
			panel.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
			panel.getElement().getStyle().setMargin(4, Style.Unit.PX);
			String style = panel.getElement().getAttribute("style");
			if (!style.endsWith(";") && !style.trim().isEmpty()) {
				style += ";";
			}
			style += "max-width: 400px; max-height: 100%;";
			panel.getElement().setAttribute("style", style);
			panel.add(img);
			panel.add(new Br());
			panel.add(title);
			panel.add(channel);
			panel.add(new Br());
			panel.add(viewTags);
			panel.add(zoomImage);
			panel.add(postLink);
			panel.add(altPostLink);
			posts.add(panel);
		}
	}

	private void showPostTags(Set<String> previewTags) {
		MaterialModal modal = new MaterialModal();
		modal.addCloseHandler((e) -> modal.removeFromParent());
		MaterialPanel panel = new MaterialPanel();
		for (String tag : previewTags) {
			MaterialAnchorButton tagLabel = new MaterialAnchorButton(tag);
			tagLabel.addClickHandler((e) -> showAddToFilterDialog(tag));
			tagLabel.addClickHandler((e) -> modal.close());
			tagLabel.setMargin(1);
			if (activeFilterTags.contains("+" + tag)) {
				tagLabel.setEnabled(false);
				tagLabel.setBackgroundColor(Color.LIGHT_GREEN);
			}
			panel.add(tagLabel);
		}
		MaterialButton cancel = new MaterialButton("DISMISS");
		cancel.setBackgroundColor(Color.GREEN_LIGHTEN_1);
		cancel.setTextColor(Color.WHITE);
		cancel.setMargin(1);
		cancel.addClickHandler((e) -> modal.close());
		panel.add(cancel);
		modal.add(panel);
		RootPanel.get().add(modal);
		modal.open();
	}

}
