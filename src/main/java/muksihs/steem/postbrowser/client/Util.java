package muksihs.steem.postbrowser.client;

public class Util {
	
	public static final String STEEMIMAGES="https://steemitimages.com/";
	public static final String NO_SCALE = "0x0/";
	public static final String BROWSE_SCALE = "400x400/";
	public static final String BROKEN_IMG = "https://steemitimages.com/0x0/https://openclipart.org/image/800px/svg_to_png/298822/missingImageanim3.png";
	
	private static Boolean isSdm = null;
	public static boolean isSdm() {
		if (isSdm==null) {
			isSdm = System.getProperty("superdevmode").equals("on");
		}
		return isSdm;
	}
}
