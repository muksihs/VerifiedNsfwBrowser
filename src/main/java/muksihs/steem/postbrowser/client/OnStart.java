package muksihs.steem.postbrowser.client;

import com.github.mvp4g.mvp4g2.core.ui.AbstractHandler;
import com.github.mvp4g.mvp4g2.core.ui.annotation.EventHandler;
import com.github.mvp4g.mvp4g2.core.ui.annotation.Handler;
import com.google.gwt.core.shared.GWT;

@Handler
public class OnStart extends AbstractHandler<AppEventBus> {
	@EventHandler
	public void onStart() {
		GWT.log("onStart");
	}
}


// @Override
// public void execute() {
// NsfwVerifiedList users = BundledData.Data.getNsfwVerifiedList();
// final List<String> list = users.getList();
// //lowercase
// ListIterator<String> li = list.listIterator();
// while (li.hasNext()) {
// String next = li.next();
// li.set(next.toLowerCase().trim());
// }
// Collections.sort(list);
//
// int maxSublistSize = Math.min(5, list.size());
// List<String> sublist = new ArrayList<>(list.subList(0, maxSublistSize));
// UserAccountInfoListCallback callback=new UserAccountInfoListCallback() {
// @Override
// public void onResult(String error, UserAccountInfoList result) {
// Set<String> receivedUsers = new TreeSet<>();
// for (UserAccountInfo user: result.getList()) {
// receivedUsers.add(user.getName().getName());
// }
//
// sublist.removeAll(receivedUsers);
// if (!sublist.isEmpty()) {
// GWT.log("=== BAD ACCOUNT(S): "+sublist.toString());
// }
// if (!list.isEmpty()) {
// int maxSublistSize = Math.min(5, list.size());
// sublist.clear();
// sublist.addAll(new ArrayList<>(list.subList(0, maxSublistSize)));
// list.subList(0, maxSublistSize).clear();
// SteemApi.getAccounts(sublist, this);
// } else {
// MaterialLoader.loading(false);
// }
// }
// };
// //process in sets of 5
// SteemApi.getAccounts(sublist, callback);
// list.subList(0, maxSublistSize).clear();
// }
// public static interface Mapper extends ObjectMapper<Discussion>{}
