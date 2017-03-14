package it.caldesi.commandlineserverdb.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Provides CRUD operations on Server table
 */
public class ServerServiceH2 {

	private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS SERVER(id int primary key, name varchar(255), description varchar(255))";

	private static final String INSERT_QUERY = "INSERT INTO SERVER (id, name, description) values (?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE SERVER SET name=?, description=? WHERE id=?";
	private static final String DELETE_QUERY = "DELETE FROM SERVER WHERE id=?";
	private static final String SELECT_QUERY = "SELECT * FROM SERVER";

	private static final String COUNT_QUERY = "SELECT count(*) FROM SERVER";
	private static final String CLEAR_QUERY = "DELETE FROM SERVER";

	private Connection connection = createConnection();

	public ServerServiceH2() {
		connection = createConnection();
	}

	/**
	 * Drops the Server table
	 * 
	 */
	public void clearTable() throws Exception {
		PreparedStatement dropPreparedStatement = null;

		connection.setAutoCommit(false);

		dropPreparedStatement = connection.prepareStatement(CLEAR_QUERY);
		dropPreparedStatement.executeUpdate();
		dropPreparedStatement.close();

		connection.commit();

	}

	/**
	 * Creates the Server table if not exists
	 * 
	 */
	public void createServerTableIfNotExists() throws Exception {
		PreparedStatement createPreparedStatement = null;

		connection.setAutoCommit(false);

		createPreparedStatement = connection.prepareStatement(CREATE_TABLE_QUERY);
		createPreparedStatement.executeUpdate();
		createPreparedStatement.close();

		connection.commit();
	}

	/**
	 * Inserts a new Server given id, name and description
	 * 
	 * @param id
	 *            Unique id of the server (numerical)
	 * @param name
	 *            Name of the server
	 * @param description
	 *            Description of the server
	 * 
	 */
	public void insert(int id, String name, String description) throws Exception {
		PreparedStatement insertPreparedStatement = null;

		connection.setAutoCommit(false);

		insertPreparedStatement = connection.prepareStatement(INSERT_QUERY);
		insertPreparedStatement.setInt(1, id);
		insertPreparedStatement.setString(2, name);
		insertPreparedStatement.setString(3, description);
		insertPreparedStatement.executeUpdate();
		insertPreparedStatement.close();

		connection.commit();
	}

	/**
	 * Updates a Server given id, name and description
	 * 
	 * @param id
	 *            Unique id of the server (numerical)
	 * @param name
	 *            Name of the server
	 * @param description
	 *            Description of the server
	 * 
	 */
	public void update(int id, String name, String description) throws Exception {
		PreparedStatement insertPreparedStatement = null;

		connection.setAutoCommit(false);

		insertPreparedStatement = connection.prepareStatement(UPDATE_QUERY);
		insertPreparedStatement.setString(1, name);
		insertPreparedStatement.setString(2, description);
		insertPreparedStatement.setInt(3, id);
		insertPreparedStatement.executeUpdate();
		insertPreparedStatement.close();

		connection.commit();
	}

	/**
	 * Deletes a Server given id
	 * 
	 * @param id
	 *            Unique id of the server (numerical)
	 * 
	 */
	public void delete(int id) throws Exception {
		PreparedStatement insertPreparedStatement = null;

		connection.setAutoCommit(false);

		insertPreparedStatement = connection.prepareStatement(DELETE_QUERY);
		insertPreparedStatement.setInt(1, id);
		insertPreparedStatement.executeUpdate();
		insertPreparedStatement.close();

		connection.commit();
	}

	/**
	 * Lists all the servers
	 * 
	 */
	public void list() throws Exception {
		PreparedStatement selectPreparedStatement = null;

		selectPreparedStatement = connection.prepareStatement(SELECT_QUERY);
		ResultSet rs = selectPreparedStatement.executeQuery();
		System.out.println("Id\t\tName\t\tDescription");
		while (rs.next()) {
			System.out.println(rs.getInt("id") + "\t\t" + rs.getString("name") + "\t\t" + rs.getString("description"));
		}
		selectPreparedStatement.close();
	}

	/**
	 * Return the number of servers into the database
	 * 
	 */
	public void count() throws Exception {
		PreparedStatement countPreparedStatement = null;

		countPreparedStatement = connection.prepareStatement(COUNT_QUERY);
		ResultSet rs = countPreparedStatement.executeQuery();
		while (rs.next()) {
			System.out.println("Number of servers: " + rs.getInt(1));
		}
		countPreparedStatement.close();
	}

	// Connection management

	/**
	 * Create and return a new connection to the database
	 * 
	 */
	public Connection createConnection() {
		DBConnectorH2 dbConnector = DBConnectorH2.getInstance();
		connection = dbConnector.getDBConnection();

		return connection;
	}

	/**
	 * Close the connection to the database
	 * 
	 */
	public void connectionClose() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
