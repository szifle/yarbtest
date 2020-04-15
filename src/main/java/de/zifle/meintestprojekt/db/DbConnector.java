package de.zifle.meintestprojekt.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnector {

	private static DbConnector instance = new DbConnector();
	
	private final Connection connection;

	public static DbConnector getInstance() {
		return instance;
	}

	private DbConnector() {
		try {
			String url = "jdbc:postgresql://192.168.178.32/yarb";
			Properties props = new Properties();
			props.setProperty("user", "yarb-restprovider");
			props.setProperty("password", "huch2hach");
			connection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			throw new RuntimeException("Could not create database connection", e);
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
}
