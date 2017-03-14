package it.caldesi.commandlineserverdb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.jdbc.JdbcSQLException;

public class DBConnectorH2 {

	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_NAME = "serverdb";
	private static final String DB_CONNECTION = "jdbc:h2:~/" + DB_NAME;
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";

	private static DBConnectorH2 instance = null;

	private DBConnectorH2() {
	}

	/**
	 * Get the singleton DBConnectorH2 instance
	 * 
	 * @return the instance of connector
	 */
	public static DBConnectorH2 getInstance() {
		if (instance == null)
			instance = new DBConnectorH2();

		return instance;
	}

	/**
	 * Create a new connection to H2 Database
	 * 
	 * @return connection to the H2 database
	 */
	public Connection getDBConnection() {
		Connection dbConnection = null;

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}

		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

			return dbConnection;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return dbConnection;
	}

	public static void main(String[] args) throws Exception {
		ServerServiceH2 serverServiceH2 = new ServerServiceH2();
		try {
			serverServiceH2.clearTable();
			serverServiceH2.createServerTableIfNotExists();
			serverServiceH2.insert(2, "Name2", "Description2");
			serverServiceH2.count();
			serverServiceH2.list();
		} catch (JdbcSQLException jdbcSQLException) {
			if (jdbcSQLException.getMessage().contains("Unique "))
				System.err.println("Id already exists!");
			serverServiceH2.connectionClose();
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			serverServiceH2.connectionClose();
			System.exit(-1);
		}
	}

}