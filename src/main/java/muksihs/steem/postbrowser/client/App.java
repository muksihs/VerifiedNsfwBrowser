package muksihs.steem.postbrowser.client;

import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;

import gwt.material.design.client.ui.MaterialLoader;
import steem.SteemApi;
import steem.SteemApi.DiscussionsCallback;
import steem.models.Discussion;
import steem.models.Discussions;

public class App implements ScheduledCommand {

	@Override
	public void execute() {
		int[] counter = new int[] {0};
		MaterialLoader.loading(false);
		DiscussionsCallback callback = new DiscussionsCallback() {
			@Override
			public void onResult(String error, Discussions result) {
				if (error != null) {
					GWT.log("=== ERROR:");
					GWT.log(error);
				}
				if (result != null) {
					GWT.log("=== RESULT:");
					List<Discussion> list = result.getList();
					if (list == null) {
						GWT.log("--- NULL RESULTS");
						return;
					}
					GWT.log(" === ["+(++counter[0])+"]");
					for (Discussion discussion : list) {
						GWT.log(discussion.getAuthor() + ": " + discussion.getTitle()+"\n"+discussion.getPermlink());
					}
					if (list.size()>1) {
						Discussion discussion = list.get(list.size()-1);
						SteemApi.getDiscussionsByCreated("dlive-porn", discussion.getPermlink(), discussion.getAuthor(), 10, this);
					}
				}
			}
		};
		SteemApi.getDiscussionsByCreated("dlive-porn", 10, callback);
	}
	public static interface Mapper extends ObjectMapper<Discussion>{}

}
