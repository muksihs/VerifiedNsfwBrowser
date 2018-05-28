package steem.models;

@FunctionalInterface
public interface SteemCallback<T> {
	void onResult(String error, T result);
}