package com.udemy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.udemy.converter.ContactConverter;
import com.udemy.entity.Contact;
import com.udemy.model.ContactModel;
import com.udemy.repository.ContactJpaRepository;
import com.udemy.service.ContactService;

@Service("contactServiceImpl")
public class ContactServiceImpl implements ContactService {

	@Autowired
	@Qualifier("contactJpaRepository")
	private ContactJpaRepository cjr;
	
	@Autowired
	@Qualifier("contactConverter")
	private ContactConverter contactConverter;
	
	@Override
	public ContactModel addContact(ContactModel contactModel) {
		Contact contact = cjr.save(contactConverter.model2entity(contactModel));
		return contactConverter.entity2model(contact);
	}

	@Override
	public List<ContactModel> listAllContacts() {
		List<Contact> contacts = cjr.findAll();
		List<ContactModel> contactsModel = new ArrayList<ContactModel>();
		for (Contact c: contacts) {
			contactsModel.add(contactConverter.entity2model(c));
		}
		return contactsModel;
	}

	@Override
	public Contact findContactById(int id) {
		return cjr.findById(id);
	}

	@Override
	public void removeContact(int id) {
		Contact contact = findContactById(id);
		if (contact != null) {
			cjr.delete(contact);
		}
//		cjr.deleteById(id);
	}

	@Override
	public ContactModel findContactModelById(int id) {
		return contactConverter.entity2model(findContactById(id));
	}

}
