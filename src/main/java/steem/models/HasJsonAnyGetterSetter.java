package steem.models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public interface HasJsonAnyGetterSetter {
	@JsonAnyGetter
	Map<String, Object> anyGetterSetterMap();
	@JsonAnySetter
	default void setProperty(String key, Object value) {
		anyGetterSetterMap().put(key, value);
	}
	default Object getProperty(String key) {
		return anyGetterSetterMap().get(key);
	}
}
