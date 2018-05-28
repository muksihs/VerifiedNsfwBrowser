package muksihs.steem.postbrowser.client;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

import gwt.material.design.client.ui.MaterialLoader;
import muksihs.steem.postbrowser.shared.NsfwVerifiedList;
import steem.SteemApi;
import steem.SteemApi.UserAccountInfoList;
import steem.SteemApi.UserAccountInfoListCallback;
import steem.models.Discussion;
import steem.models.UserAccountInfo;

public class App implements ScheduledCommand {

	@Override
	public void execute() {
		MaterialLoader.loading(false);
		UserAccountInfoListCallback callback=new UserAccountInfoListCallback() {
			@Override
			public void onResult(String error, UserAccountInfoList result) {
				for (UserAccountInfo user: result.getList()) {
					GWT.log(user.getName().getName());
					GWT.log(" - "+user.getLastRootPost().toString());
				}
			}
		};
		NsfwVerifiedList users = BundledData.Data.getNsfwVerifiedList();
		SteemApi.getAccounts(users.getList(), callback);
	}
	public static interface Mapper extends ObjectMapper<Discussion>{}

}
