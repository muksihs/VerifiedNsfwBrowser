package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.GWT;

public class Util {
	private static Boolean isSdm;
	public static boolean isSdm() {
		if (isSdm==null) {
			GWT.log("isSdm: "+(isSdm=true));
		}
		return isSdm;
	}
}
