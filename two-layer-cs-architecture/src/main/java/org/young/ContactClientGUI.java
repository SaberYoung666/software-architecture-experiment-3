package org.young;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ContactClientGUI {
	private final JTextField nameField;
	private final JTextField phoneField;
	private final JTextArea logArea;
	private PrintWriter out;
	private BufferedReader in;

	public ContactClientGUI() {
		JFrame frame = new JFrame("通讯录");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 300));
		frame.setLayout(new BorderLayout());

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		nameField = new JTextField(15);
		phoneField = new JTextField(15);
		JButton addButton = new JButton("添加新联系人");
		JButton updateButton = new JButton("修改联系人信息");
		JButton deleteButton = new JButton("删除联系人");
		JButton getButton = new JButton("查看联系人信息");

		inputPanel.add(new JLabel("姓名："));
		inputPanel.add(nameField);
		inputPanel.add(new JLabel("手机号："));
		inputPanel.add(phoneField);
		inputPanel.add(addButton);
		inputPanel.add(updateButton);
		inputPanel.add(deleteButton);
		inputPanel.add(getButton);

		logArea = new JTextArea();
		logArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(logArea);

		frame.add(inputPanel, BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCommand("ADD " + nameField.getText() + " " + phoneField.getText());
			}
		});

		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCommand("UPDATE " + nameField.getText() + " " + phoneField.getText());
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCommand("DELETE " + nameField.getText());
			}
		});

		getButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCommand("GET " + nameField.getText());
			}
		});

		frame.setVisible(true);

		connectToServer();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ContactClientGUI();
			}
		});
	}

	private void connectToServer() {
		try {
			Socket socket = new Socket("localhost", 1234);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			new Thread(new ResponseListener()).start();
		} catch (IOException e) {
			logArea.append("无法连接到服务器: " + e.getMessage() + "\n");
		}
	}

	private void sendCommand(String command) {
		out.println(command);
	}

	private class ResponseListener implements Runnable {
		public void run() {
			try {
				String response;
				while ((response = in.readLine()) != null) {
					logArea.append(response + "\n");
				}
			} catch (IOException e) {
				logArea.append("读取服务器响应时出错: " + e.getMessage() + "\n");
			}
		}
	}
}
