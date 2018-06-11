package muksihs.steem.postbrowser.client;

public class Util {
	private static Boolean isSdm = null;
	public static boolean isSdm() {
		if (isSdm==null) {
			isSdm = System.getProperty("superdevmode").equals("on");
		}
		return isSdm;
	}
}
