package com.udemy.converter;

import org.springframework.stereotype.Component;

import com.udemy.entity.Contact;
import com.udemy.model.ContactModel;

@Component("contactConverter")
public class ContactConverter {

	public Contact model2entity(ContactModel cm) {
		Contact c = new Contact();
		
		c.setId(cm.getId());
		c.setFirstname(cm.getFirstname());
		c.setLastname(cm.getLastname());
		c.setTelephone(cm.getTelephone());
		c.setCity(cm.getCity());
		
		return c;
	}
	
	public ContactModel entity2model(Contact c) {
		ContactModel cm = new ContactModel();
		
		cm.setId(c.getId());
		cm.setFirstname(c.getFirstname());
		cm.setLastname(c.getLastname());
		cm.setTelephone(c.getTelephone());
		cm.setCity(c.getCity());
		
		return cm;
	}
}
