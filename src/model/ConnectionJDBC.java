package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionJDBC {

	public Connection databaseLink;

	public Connection getConnection() {
		String databaseName = "canil_banco";
		String databaseUser = "root";
		String databasePassword = "2001";
		String url = "jdbc:mysql://localhost/" + databaseName;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return databaseLink;
	}
}
