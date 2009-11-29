package org.helyx.app.j2me.getabike.data.contact.domain;

public class Contact {

	private String name;
	private String phoneNumber;

	public Contact() {
		super();
	}
	
	public String getName() {
		if (name == null || name.length() <= 0) {
			return "NON RENSEIGNE";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getFormattedPhoneNumber() {
		return dropSpaces(phoneNumber);
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	private String dropSpaces(String phoneNumber) {
		StringBuffer sbPhoneNumber = new StringBuffer();
		
		int charLength = phoneNumber.length();
		for (int i = 0 ; i < charLength ; i++) {
			char c = phoneNumber.charAt(i);
			if (c != ' ') {
				sbPhoneNumber.append(c);
			}
		}
		
		String newPhoneNumber = sbPhoneNumber.toString();
		
		return newPhoneNumber;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Contact]")
		
		.append(" name=").append(name)
		.append(", phoneNumber=").append(phoneNumber);

		return sb.toString();
	}

}
