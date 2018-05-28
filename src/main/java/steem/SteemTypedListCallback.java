package steem;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

public interface SteemTypedListCallback <T, U extends ObjectMapper<T>> {
	default void onResult(JavaScriptObject error, JavaScriptObject result) {
		String jsonError = error == null ? null : JsonUtils.stringify(error);
		T jsonResult = result == null ? null : mapper().read("{'list':"+JsonUtils.stringify(result)+"}");
		onResult(jsonError, jsonResult);
	}
	void onResult(String error, T result);
	U mapper();
}
