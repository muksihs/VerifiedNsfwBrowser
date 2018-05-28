package steem;

import com.google.gwt.core.client.JavaScriptObject;

import jsinterop.annotations.JsFunction;

@FunctionalInterface
@JsFunction
public interface SteemCallback_old<T> {
	void onResult(JavaScriptObject error, T result);
}