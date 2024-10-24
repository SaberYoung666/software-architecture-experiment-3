package org.young;

import java.sql.SQLException;
import java.util.List;

public class ContactService {
	private final ContactDAO contactDAO;

	public ContactService() {
		contactDAO = new ContactDAO();
	}

	public void addContact(Contact contact) throws SQLException {
		contactDAO.insertContact(contact);
	}

	public Contact getContactById(String name) throws SQLException {
		return contactDAO.selectContact(name);
	}

	public List<Contact> getAllContacts() {
		return contactDAO.selectAllContacts();
	}

	public boolean deleteContact(String name) throws SQLException {
		return contactDAO.deleteContact(name);
	}

	public boolean updateContact(Contact contact) throws SQLException {
		return contactDAO.updateContact(contact);
	}
}

