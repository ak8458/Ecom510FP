package itmd510.fp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import itmd510.fp.dao.ProductDAO.ProductTable;
import itmd510.fp.helper.ConnectionManager;
import itmd510.fp.model.Customer;
import itmd510.fp.model.Employee;
import itmd510.fp.model.User;

/**
 * 
 * @author Akshay Patil 
 *This DAO class performs CRUD operations for the User object
 */
public class UserDAO {
	private Connection connection;

	public int AddUser(User customer) {
		// TODO Auto-generated method stub

		connection = new ConnectionManager().getNewConnection();

		// Query to insert a record to the bank table
		String query = "INSERT INTO ak_users (fName,lName, uPWD, uRole, uAddress) VALUES (?, ?, ?, ?, ?) ;";
		// Use prepared statements to avoid SQL injection attacks
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			// Set the parameters to the query
			statement.setString(1, customer.getuFirstName());
			statement.setString(2, customer.getuLastName());
			statement.setString(3, customer.getuPassword());
			statement.setString(4, String.valueOf(customer.getuRole()));
			statement.setString(5, customer.getuAddress());

			// Execute the insert
			statement.executeUpdate();
			// To get the primary key (id) of the newly inserted record
			ResultSet resultSet = statement.getGeneratedKeys();


			if(resultSet.next())
				return resultSet.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error Creating: " + e.getMessage());
		}
		// Close the connection to the database - Very important!!!
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			System.out.println("Error closing connection: " + e);
		}
		// Return the bank object that was inserted with the id field set.
		return -1;

	}

	public User authenticateUser(int uID, String password) {
		try{
			connection = new ConnectionManager().getNewConnection();

			// TODO Auto-generated method stub
			if(uID>0){

				// Query to insert a record to the bank table
				String query = "SELECT * from ak_users where userID = ? and uPWD = ?;";
				// Use prepared statements to avoid SQL injection attacks
				PreparedStatement prepStmt = connection.prepareStatement(query);
				prepStmt.setInt(1, uID);
				prepStmt.setString(2, password);


				// To get the primary key (id) of the newly inserted record
				ResultSet resultSet = prepStmt.executeQuery();


				if(resultSet.next()){
					if(resultSet.getString("uRole").equalsIgnoreCase("c")){
						Customer customer=new Customer();
						customer.setuFirstName(resultSet.getString("fName"));
						customer.setuLastName(resultSet.getString("lName"));
						customer.setuPassword(resultSet.getString("uPWD"));
						customer.setuRole(resultSet.getString("uRole").charAt(0));
						customer.setUserID(resultSet.getInt("userID"));
						customer.setuAddress(resultSet.getString("uAddress"));
						connection.close();
						return customer;
					}else{
						Employee employee=new Employee();
						employee.setuFirstName(resultSet.getString("fName"));
						employee.setuLastName(resultSet.getString("lName"));
						employee.setuPassword(resultSet.getString("uPWD"));
						employee.setuRole(resultSet.getString("uRole").charAt(0));
						employee.setUserID(resultSet.getInt("userID"));
						employee.setuAddress(resultSet.getString("uAddress"));
						connection.close();
						return employee;
					}

				}

			}

		}catch(Exception e){

		}
		return null;
	}
}
