package org.helyx.app.j2me.lib.i18n;

public class Locale {

	private static final String CAT = "LOCALE";

	private String country;
	private String language;
	private String name;
	
	public Locale(String country, String language) {
		super();
		this.country = country;
		this.language = language;
		name = country + "_" + language;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

}
