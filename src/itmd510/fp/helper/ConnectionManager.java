package itmd510.fp.helper; 


import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Akshay Patil
 * Student ID A20356673 Current Date:11/11/2015
 * This class is used to manage the connection attributes. 
 * This is a common class which will be refereed by all the classes to get the connection object.
 */
public class ConnectionManager
{

	String databaseUrl;
	String userName;
	String password;
	String driver;

	/**
	 * ConnectionManager constructor to initialize all the connection attributes.
	 */
	public ConnectionManager()
	{
		//database URL veriable
		//databaseUrl = "jdbc:mysql://localhost/javaproject";
		databaseUrl = "jdbc:mysql://www.papademas.net/dbfp?";
		//database user name credentials
		//userName = "root";
		userName = "fpuser";
		//database password credentials
		//password = "";
		password = "510";
		//mySQL database driver
		driver ="com.mysql.jdbc.Driver";
	}




	/**
	 * This method is used to get the connection object
	 * @return connection object to the mySQL database.
	 */
	public Connection getNewConnection()
	{
		Connection connection = null;

		try
		{
			//driver which will be used for which the connection will be drawn
			Class.forName(driver);
			//gets connection from the driver manager
			connection = DriverManager.getConnection(databaseUrl, userName, password);

		}
		catch(SQLException sqle)
		{
			System.err.println("SQLException: "+sqle);
			return null;
		}
		catch(ClassNotFoundException cnfe)
		{
			System.err.println("ClassNotFoundException: "+cnfe);
			return null;
		}
		//return connection
		return connection;
	}//end getNewConnection


}//end class