package org.helyx.app.j2me.velocite.data.language.domain;

public class Language {

	public Language() {
		super();
	}

	public String key;
	
	public String name;
	
	public String country;
	
	public String localeCountry;
	
	public String localeLanguage;


	public String toString() {
		StringBuffer sb = new StringBuffer("[Language]")
		.append(" key=").append(key)
		.append(", name=").append(name)
		.append(", country=").append(country)
		.append(", localeCountry=").append(localeCountry)
		.append(", localeLanguage=").append(localeLanguage);
		
		return sb.toString();
	}

}
