package org.young;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
	private static final String INSERT_CONTACTS_SQL = "INSERT INTO linkman (name, phone_number) VALUES (?, ?);";
	private static final String SELECT_CONTACT_BY_ID = "SELECT name, phone_number FROM linkman WHERE name = ?";
	private static final String SELECT_ALL_CONTACTS = "SELECT name, phone_number FROM linkman";
	private static final String DELETE_CONTACT_SQL = "DELETE FROM linkman WHERE name = ?;";
	private static final String UPDATE_CONTACT_SQL = "UPDATE linkman SET name = ?, phone_number = ? WHERE name = ?;";

	protected Connection getConnection() {
		Connection connection = null;
		try {
			String jdbcURL = "jdbc:mysql://localhost:3306/address_list_experiment?useSSL=false";
			String jdbcPassword = "1234";
			String jdbcUsername = "root";
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public void insertContact(Contact contact) throws SQLException {
		try (Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CONTACTS_SQL)) {
			preparedStatement.setString(1, contact.getName());
			preparedStatement.setString(2, contact.getPhoneNumber());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public Contact selectContact(String name) throws SQLException {
		Contact contact = null;
		try (Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTACT_BY_ID);) {
			preparedStatement.setString(1, name);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String phoneNumber = rs.getString("phone_number");
				contact = new Contact(name, phoneNumber);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return contact;
	}

	public List<Contact> selectAllContacts() {
		List<Contact> contacts = new ArrayList<>();
		try (Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CONTACTS);) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String phoneNumber = rs.getString("phone_number");
				contacts.add(new Contact(name, phoneNumber));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return contacts;
	}

	public boolean deleteContact(String name) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();PreparedStatement statement = connection.prepareStatement(DELETE_CONTACT_SQL);) {
			statement.setString(1, name);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateContact(Contact contact) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();PreparedStatement statement = connection.prepareStatement(UPDATE_CONTACT_SQL);) {
			statement.setString(1, contact.getName());
			statement.setString(2, contact.getPhoneNumber());
			statement.setString(3, contact.getName());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}

