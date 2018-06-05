package muksihs.steem.postbrowser.client;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.TextResource;

import muksihs.steem.postbrowser.shared.NsfwVerifiedList;

public interface BundledData extends ClientBundle {
	static BundledData INSTANCE = GWT.create(BundledData.class);

	@Source("muksihs/steem/postbrowser/shared/nsfw-verified-list.json")
	@DataResource.MimeType(value = "")
	TextResource nsfwVerifiedList();

	static class Data {
		public static NsfwVerifiedList getNsfwVerifiedList() {
			NsfwVerifiedListMapper mapper = GWT.create(NsfwVerifiedListMapper.class);
			return mapper.read(BundledData.INSTANCE.nsfwVerifiedList().getText());
		}

		interface NsfwVerifiedListMapper extends ObjectMapper<NsfwVerifiedList> {
		}
	}
}
