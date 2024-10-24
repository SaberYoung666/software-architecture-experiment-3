package org.young;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ContactClient {
	private final ContactService contactService;
	private JTextField txtName;
	private JTextField txtPhoneNumber;
	private JTable contactTable;

	public ContactClient() {
		contactService = new ContactService();
		initializeUI();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ContactClient();
			}
		});
	}

	private void initializeUI() {
		JFrame frame = new JFrame("通讯录");
		frame.setBounds(100, 100, 460, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		txtName = new JTextField(5);
		txtPhoneNumber = new JTextField(9);

		inputPanel.add(new JLabel("姓名："));
		inputPanel.add(txtName);
		inputPanel.add(new JLabel("电话号码："));
		inputPanel.add(txtPhoneNumber);

		JButton btnAdd = new JButton("添加");
		JButton btnUpdate = new JButton("更新");
		JButton btnDelete = new JButton("删除");

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					contactService.addContact(new Contact(txtName.getText(), txtPhoneNumber.getText()));
					refreshContactList();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = contactTable.getSelectedRow();
				if (selectedRow >= 0) {
					String name = (String) contactTable.getValueAt(selectedRow, 0);
					txtName.setText(name);
					String phoneNumber = txtPhoneNumber.getText();
					try {
						contactService.updateContact(new Contact(name, phoneNumber));
						refreshContactList();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = contactTable.getSelectedRow();
				if (selectedRow >= 0) {
					String name = (String) contactTable.getValueAt(selectedRow, 0);
					try {
						if (contactService.deleteContact(name)) {
							refreshContactList();
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		inputPanel.add(btnAdd);
		inputPanel.add(btnUpdate);
		inputPanel.add(btnDelete);

		frame.add(inputPanel, BorderLayout.NORTH);

		contactTable = new JTable();
		frame.add(new JScrollPane(contactTable), BorderLayout.CENTER);

		refreshContactList();

		frame.setVisible(true);
	}

	private void refreshContactList() {
		List<Contact> contacts = contactService.getAllContacts();
		ContactTableModel model = new ContactTableModel(contacts);
		contactTable.setModel(model);
	}
}

class ContactTableModel extends AbstractTableModel {
	private final List<Contact> contacts;

	public ContactTableModel(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public int getRowCount() {
		return contacts.size();
	}

	@Override
	public int getColumnCount() {
		return 2; // Name, Phone
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Contact contact = contacts.get(rowIndex);
		return switch (columnIndex) {
			case 0 -> contact.getName();
			case 1 -> contact.getPhoneNumber();
			default -> null;
		};
	}

	@Override
	public String getColumnName(int column) {
		return switch (column) {
			case 0 -> "姓名";
			case 1 -> "手机号码";
			default -> super.getColumnName(column);
		};
	}
}

