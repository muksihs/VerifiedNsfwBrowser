package muksihs.steem.postbrowser.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

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
		NsfwVerifiedList users = BundledData.Data.getNsfwVerifiedList();
		final List<String> list = users.getList();
		//lowercase
		ListIterator<String> li = list.listIterator();
		while (li.hasNext()) {
			String next = li.next();
			li.set(next.toLowerCase().trim());
		}
		Collections.sort(list);
		
		int maxSublistSize = Math.min(5, list.size());
		List<String> sublist = new ArrayList<>(list.subList(0, maxSublistSize));
		UserAccountInfoListCallback callback=new UserAccountInfoListCallback() {
			@Override
			public void onResult(String error, UserAccountInfoList result) {
				Set<String> receivedUsers = new TreeSet<>();
				for (UserAccountInfo user: result.getList()) {
					receivedUsers.add(user.getName().getName());
				}
				
				sublist.removeAll(receivedUsers);
				if (!sublist.isEmpty()) {
					GWT.log("=== BAD ACCOUNT(S): "+sublist.toString());
				}
				if (!list.isEmpty()) {
					int maxSublistSize = Math.min(5, list.size());
					sublist.clear();
					sublist.addAll(new ArrayList<>(list.subList(0, maxSublistSize)));
					list.subList(0, maxSublistSize).clear();
					SteemApi.getAccounts(sublist, this);
				} else {
					MaterialLoader.loading(false);
				}
			}
		};
		//process in sets of 5
		SteemApi.getAccounts(sublist, callback);
		list.subList(0, maxSublistSize).clear();
	}
	public static interface Mapper extends ObjectMapper<Discussion>{}

}
