package itmd510.fp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import itmd510.fp.dao.ProductDAO.ProductTable;
import itmd510.fp.helper.ConnectionManager;
import itmd510.fp.model.CartItem;
import itmd510.fp.model.User;

/**
 * 
 * @author Akshay Patil 
 *This DAO class performs CRUD operations for the Cart object
 */
public class CartDAO {
	private Connection connection;

	/**
	 * 
	 * @author Akshay Patil
	 * This inner class creates cart observable objects which is included in the cart table
	 *
	 */
	public class CartTable{
		public SimpleStringProperty id = new SimpleStringProperty();
		public SimpleStringProperty name = new SimpleStringProperty();
		public SimpleStringProperty price = new SimpleStringProperty();
		public SimpleStringProperty noOfItems = new SimpleStringProperty();
		public String getPrice() {
			return price.get();
		}
		public String getId() {
			return id.get();
		}
		public String getName() {
			return name.get();
		}


		public String getNoOfItems() {
			return noOfItems.get();
		}
	}
	/**
	 * Adds product to cart
	 * @param cartItem product to be added
	 */
	public void addCartItem(CartItem cartItem) {
		boolean exists=false;
		int quantity = 0;
		float price = 0;
		// TODO Auto-generated method stub
		connection = new ConnectionManager().getNewConnection();

		//query which selects existing cart item.
		String selectQuery = "Select * from ak_cart_items where cartID=? and productID=?;";
		try{
			PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
			selectStmt.setInt(1, cartItem.getCartID());
			selectStmt.setInt(2, cartItem.getProduct().getProductID());
			ResultSet selectRS = selectStmt.executeQuery();

			//if the cart item already exists then get the current quantity
			if(selectRS.next()){
				if(selectRS.getInt("cartID")==cartItem.getCartID() && selectRS.getInt("productID")==cartItem.getProduct().getProductID()){
					exists=true;
					quantity=selectRS.getInt("noOfItems");
					price=selectRS.getFloat("tPrice");
				}

			}
			selectRS.close();
		}catch(Exception e){
			System.out.println("Error " +this.toString()+ e);
		}

		//if cart item already exists then updates the quantity
		if(exists){

			String updateQuery = "UPDATE ak_cart_items SET noOfItems = ?,tPrice=? WHERE productID = ? and cartID=?;";
			try{
				PreparedStatement updateStmt = connection.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS);
				updateStmt.setInt(1, (cartItem.getQuantity()+quantity));
				updateStmt.setFloat(2, (cartItem.getPrice()+price));
				updateStmt.setInt(3, cartItem.getProduct().getProductID());
				updateStmt.setInt(4, cartItem.getCartID());
				updateStmt.executeUpdate();
				ResultSet updateRS = updateStmt.getGeneratedKeys();
				updateRS.close();

			}catch(Exception e){
				System.out.println("Error " +this.toString()+ e);
			}
		}
		else{

			// if cart item doesnot exist, then we add a new record
			String query = "INSERT INTO ak_cart_items (cartID,productID,noOfItems, tPrice,productName) VALUES (?, ?, ?, ?,?) ;";
			// Use prepared statements to avoid SQL injection attacks
			try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				// Set the parameters to the query
				statement.setInt(1, cartItem.getCartID());
				statement.setInt(2, cartItem.getProduct().getProductID());
				statement.setInt(3, cartItem.getQuantity());
				statement.setFloat(4, cartItem.getPrice());
				statement.setString(5,cartItem.getProduct().getProductName());

				// Execute the insert
				statement.executeUpdate();

			} catch (SQLException e) {
				System.out.println("Error " +this.toString()+ e);
			}
			// Close the connection to the database - Very important!!!
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				System.out.println("Error " +this.toString()+ e);
			}
		}

	}


	private ObservableList<CartTable> allCartList;
	/**
	 * gets a list of cart items for a particular user
	 * @param user user whos cart elements are to be displayed
	 * @return cart elements
	 */
	public ObservableList<CartTable> getAllCartItems(User user) {

		allCartList = FXCollections.observableArrayList();
		try {
			connection =new ConnectionManager().getNewConnection();

			//query to select the cart items
			String query = "Select * from ak_cart_items where cartID=?;";

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, user.getUserID());
			ResultSet resultSet = statement.executeQuery();



			while (resultSet.next()) {

				CartTable cartTable = new CartTable();
				cartTable.id.set(resultSet.getString("productID"));  
				cartTable.name.set(resultSet.getString("productName"));
				cartTable.price.set(resultSet.getString("tPrice"));
				cartTable.noOfItems.set(resultSet.getString("noOfItems"));

				allCartList.add(cartTable);  


			}
			//close result set and connection
			resultSet.close();
			connection.close();
		} catch (Exception e) {

			System.out.println("Error " +this.toString()+ e);
		}
		return allCartList;
	}

	/**
	 * removes selected product from the cart
	 * @param cartItem cart item to be removed
	 * @return boolean acknowledgement
	 */
	public boolean removeFromCart(CartItem cartItem) {

		// TODO Auto-generated method stub
		try {
			connection = new ConnectionManager().getNewConnection();

			//query to delete the record from table
			String query = "delete from ak_cart_items where productID=? and cartID=?;";

			// Use prepared statements to avoid SQL injection attacks
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1,cartItem.getProduct().getProductID());
			statement.setInt(2,cartItem.getCartID());
			// Execute the insert
			statement.executeUpdate();


		} catch (SQLException e) {

			System.out.println("Error " +this.toString()+ e);
			return false;
		}
		// Close the connection to the database - Very important!!!
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			System.out.println("Error " +this.toString()+ e);
			return false;
		}
		return true;
	}


}
