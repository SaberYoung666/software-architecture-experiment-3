package org.young;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class ContactServer {
	private final ServerSocket serverSocket;
	private final Connection connection;

	public ContactServer(int port) throws IOException, SQLException, ClassNotFoundException {
		serverSocket = new ServerSocket(port);
		// 初始化数据库连接
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/address_list_experiment", "root", "1234");
	}

	public static void main(String[] args) {
		try {
			ContactServer server = new ContactServer(1234);
			server.start();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		System.out.println("服务器已启动，等待连接...");
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				new ClientHandler(clientSocket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ClientHandler extends Thread {
		private final Socket clientSocket;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				String line;
				while ((line = in.readLine()) != null) {
					String[] tokens = line.split(" ");
					String command = tokens[0];

					try {
						switch (command) {
							case "GET":
								String name = tokens[1];
								String info = getContactInfo(name);
								out.println(info == null ? "联系人不存在" : info);
								break;
							case "ADD":
								addContact(tokens[1], tokens[2]);
								out.println("联系人添加成功");
								break;
							case "UPDATE":
								updateContact(tokens[1], tokens[2]);
								out.println("联系人信息更新成功");
								break;
							case "DELETE":
								deleteContact(tokens[1]);
								out.println("联系人删除成功");
								break;
							default:
								out.println("未知命令");
								break;
						}
					} catch (SQLException e) {
						out.println("数据库操作失败: " + e.getMessage());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private String getContactInfo(String name) throws SQLException {
			String sql = "SELECT phone_number FROM linkman WHERE name = ?";
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setString(1, name);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return rs.getString("phone_number");
				}
			}
			return null;
		}

		private void addContact(String name, String phone) throws SQLException {
			String sql = "INSERT INTO linkman (name, phone_number) VALUES (?, ?)";
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setString(1, name);
				stmt.setString(2, phone);
				stmt.executeUpdate();
			}
		}

		private void updateContact(String name, String phone) throws SQLException {
			String sql = "UPDATE linkman SET phone_number = ? WHERE name = ?";
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setString(1, phone);
				stmt.setString(2, name);
				stmt.executeUpdate();
			}
		}

		private void deleteContact(String name) throws SQLException {
			String sql = "DELETE FROM linkman WHERE name = ?";
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setString(1, name);
				stmt.executeUpdate();
			}
		}
	}
}
