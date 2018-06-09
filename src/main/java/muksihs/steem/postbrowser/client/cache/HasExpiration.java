package muksihs.steem.postbrowser.client.cache;

import java.util.Date;

public interface HasExpiration {

	void setExpires(Date expires);

	default boolean isExpired() {
		return new Date().after(getExpires());
	}

	Date getExpires();

}
