package steem.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@SuppressWarnings("serial")
public class SteemDate extends Date {
	public SteemDate() {
		super();
	}
	public SteemDate(long date) {
		super(date);
	}
	@JsonValue
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	protected Date getJsonValue() {
		return this;
	}
	@JsonCreator
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	protected static SteemDate fromJsonValue(Date date) {
		if (date==null) {
			return null;
		}
		return new SteemDate(date.getTime());
	}
}
