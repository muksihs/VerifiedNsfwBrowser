package steem;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.github.nmorel.gwtjackson.client.exception.JsonDeserializationException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;

import elemental2.dom.DomGlobal;

public interface SteemTypedListCallback <T, U extends ObjectMapper<T>> {
	default void onResult(JavaScriptObject error, JavaScriptObject result) {
		String jsonError = error == null ? null : JsonUtils.stringify(error);
		T jsonResult;
		try {
			jsonResult = result == null ? null : mapper().read("{'list':"+JsonUtils.stringify(result)+"}");
		} catch (JsonDeserializationException e) {
			DomGlobal.console.log(e.getMessage(), e);
			DomGlobal.console.log(JsonUtils.stringify(result, "\t"));
			jsonResult = null;
		}
		onResult(jsonError, jsonResult);
	}
	void onResult(String error, T result);
	U mapper();
}
