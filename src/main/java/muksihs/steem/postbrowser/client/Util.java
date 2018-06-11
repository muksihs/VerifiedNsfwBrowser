package muksihs.steem.postbrowser.client;

import com.google.gwt.core.client.GWT;

public class Util {
	private static Boolean isSdm=null;
	public static boolean isSdm() {
		if (isSdm==null) {
			isSdm=false;
			GWT.log("isSdm: "+(isSdm=true));
		}
		return isSdm;
	}
}
