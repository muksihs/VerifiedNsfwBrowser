package muksihs.steem.postbrowser.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import elemental2.dom.DomGlobal;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Text;
import steem.MapperCallback.BlogListCallback;
import steem.SteemApi;
import steem.models.BlogItem;
import steem.models.CommentItem;

public class TestEP implements EntryPoint {
	private static int line = 0;

	private static void log(String result) {
		Div div = new Div();
		div.add(new Text((++line) + ") " + result));
		RootPanel.get().insert(div, 0);
		DomGlobal.console.log(result);
	}

	private BlogListCallback callback = new BlogListCallback() {
		@Override
		public void onResult(String error, ArrayList<BlogItem> results) {
			Iterator<BlogItem> iResults = results.iterator();
			while (iResults.hasNext()) {
				BlogItem result = iResults.next();
				if (result.getBlog() == null || result.getBlog().trim().isEmpty()) {
					iResults.remove();
					continue;
				}
				if (result.getEntryId() == 0) {
					iResults.remove();
					continue;
				}
				//always remove the last entry from the previous results
				if (result.getEntryId()==lastEntryId) {
					iResults.remove();
					continue;
				}
			}
			log("Results: " + results.size());
			total += results.size();
			for (BlogItem result : results) {
				log("Item: " + result.getEntryId() //
				+ " @" + result.getBlog() //
//				+ " " + result.getReblogOn() //
				+ " " + result.getPermlink());
			}
			int newLastEntryId = Integer.MAX_VALUE;
			for (BlogItem result : results) {
				newLastEntryId = (int) Math.min(newLastEntryId, result.getEntryId());
			}
			if (newLastEntryId != lastEntryId && !results.isEmpty()) {
				log(" - new last entry id: "+newLastEntryId+" was "+lastEntryId);
				lastEntryId=newLastEntryId;
				SteemApi.getBlogEntries("muksihs", lastEntryId, 10, callback);
			} else {
				log(" === TOTAL ENTRIES: "+total);
			}
		}

	};

	int lastEntryId = -1;
	int total = 0;

	@Override
	public void onModuleLoad() {
		SteemApi.getBlogEntries("muksihs", Integer.MAX_VALUE, 10, callback);
	}

}
