package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import muksihs.steem.postbrowser.eventbus.Event;
import muksihs.steem.postbrowser.eventbus.GlobalAsyncEventBus;

public class AppController implements GlobalAsyncEventBus {
	private static AppController instance;

	public static void unbind() {
		if (instance == null) {
			return;
		}
		instance.registration.removeHandler();
		instance = null;
	}

	public static void bind() {
		if (instance == null) {
			instance = new AppController();
		}
		HandlerRegistration registration = eventBinder.bindEventHandlers(instance, instance.getEventBus());
		instance.setRegistration(registration);
	}

	interface MyEventBinder extends EventBinder<AppController> {
	}

	private static final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	private HandlerRegistration registration;

	private void setRegistration(HandlerRegistration registration) {
		this.registration = registration;
	}

	protected AppController() {
	}
	
	@EventHandler
	protected void onAppStart(Event.AppStart event) {
		//init
		
		//load main view
		fireEvent(new Event.ShowMainView());
		
		//all done
		fireEvent(new Event.Loading(false));
	}
	
	@EventHandler
	protected void getAppVersion(Event.GetAppVersion event) {
		fireEvent(new Event.AppVersion("20180608-BETA"));
	}
}


//@Override
//public void execute() {
//NsfwVerifiedList users = BundledData.Data.getNsfwVerifiedList();
//final List<String> list = users.getList();
////lowercase
//ListIterator<String> li = list.listIterator();
//while (li.hasNext()) {
//String next = li.next();
//li.set(next.toLowerCase().trim());
//}
//Collections.sort(list);
//
//int maxSublistSize = Math.min(5, list.size());
//List<String> sublist = new ArrayList<>(list.subList(0, maxSublistSize));
//UserAccountInfoListCallback callback=new UserAccountInfoListCallback() {
//@Override
//public void onResult(String error, UserAccountInfoList result) {
//Set<String> receivedUsers = new TreeSet<>();
//for (UserAccountInfo user: result.getList()) {
//receivedUsers.add(user.getName().getName());
//}
//
//sublist.removeAll(receivedUsers);
//if (!sublist.isEmpty()) {
//GWT.log("=== BAD ACCOUNT(S): "+sublist.toString());
//}
//if (!list.isEmpty()) {
//int maxSublistSize = Math.min(5, list.size());
//sublist.clear();
//sublist.addAll(new ArrayList<>(list.subList(0, maxSublistSize)));
//list.subList(0, maxSublistSize).clear();
//SteemApi.getAccounts(sublist, this);
//} else {
//MaterialLoader.loading(false);
//}
//}
//};
////process in sets of 5
//SteemApi.getAccounts(sublist, callback);
//list.subList(0, maxSublistSize).clear();
//}
//public static interface Mapper extends ObjectMapper<Discussion>{}
