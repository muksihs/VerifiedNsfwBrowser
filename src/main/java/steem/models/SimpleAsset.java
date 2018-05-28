package steem.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SimpleAsset {
	private BigDecimal value;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private String currency;

	@JsonValue
	protected String jsonValue() {
		return (value == null ? "" : value.toPlainString()) //
				+ (value == null || currency == null ? "" : " ")//
				+ (currency == null ? "" : currency);
	}

	@JsonCreator
	public static SimpleAsset getAsset(String assetString) {
		if (assetString == null || assetString.trim().isEmpty()) {
			return null;
		}
		SimpleAsset simpleAsset = new SimpleAsset();
		try {
			if (assetString.contains(" ")) {
				int i = assetString.indexOf(" ");
				simpleAsset.value = new BigDecimal(assetString.substring(0, i).trim());
				simpleAsset.currency = assetString.substring(i + 1).trim();
			} else {
				simpleAsset.value = new BigDecimal(assetString.trim());
				simpleAsset.currency = "UNKOWN";
			}
		} catch (Exception e) {
		}
		return simpleAsset;
	}
}
