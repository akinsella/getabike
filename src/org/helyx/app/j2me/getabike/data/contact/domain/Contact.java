package org.helyx.app.j2me.getabike.data.contact.domain;

public class Contact {

	public String firstname;
	public String lastname;
	
	public String phoneNumber;

	public Contact() {
		super();
	}
	
	public String renderDefaultText() {
		if (firstname == null && lastname == null) {
			return "NON RENSEIGNE";
		}
		else if (firstname == null) {
			return lastname;
		}
		else {
			return firstname;
		}
	}
	
}
