package itmd510.fp.dao;

import itmd510.fp.helper.ConnectionManager;
import itmd510.fp.model.Customer;
import itmd510.fp.model.Employee;
import itmd510.fp.model.Order;
import itmd510.fp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Akshay Patil 
 *This DAO class performs CRUD operations for the Order object
 */
public class OrderDAO {
	private Connection connection;
	/**
	 * 
	 * @author Akshay Patil
	 *This inner class creates order observable objects which is included in the order table
	 */
	public class OrderTable{
		public SimpleStringProperty orderID = new SimpleStringProperty();
		public SimpleStringProperty custID = new SimpleStringProperty();
		public SimpleStringProperty prodID = new SimpleStringProperty();
		public SimpleStringProperty prodName = new SimpleStringProperty();
		public SimpleStringProperty status = new SimpleStringProperty();
		public SimpleStringProperty price = new SimpleStringProperty();
		public SimpleStringProperty quantity = new SimpleStringProperty();
		public SimpleStringProperty date = new SimpleStringProperty();
		public String getPrice() {
			return price.get();
		}
		public String getProdName() {
			return prodName.get();
		}
		public String getStatus() {
			return status.get();
		}
		public String getOrderID() {
			return orderID.get();
		}
		public String getCustID() {
			return custID.get();
		}
		public String getProdID() {
			return prodID.get();
		}
		public String getQuantity() {
			return quantity.get();
		}
		public String getDate() {
			return date.get();
		}
	}

	/**
	 * Adds customer order to database
	 * @param order order to be added
	 * @return Auto generated order ID to be displayed to the customer as a part of confirmation
	 */
	public Integer AddOrder(Order order) 
	{
		// TODO Auto-generated method stub
		int orderID=-1;
		//check for stock before order
		int productStock=getProductStock(order);
		if(productStock>=order.getQuantity())
		{

			try {
				connection =new ConnectionManager().getNewConnection();

				//inserts order to database
				String query = "INSERT INTO ak_orders (userID,productID, noOfItems, tPrice,pName,oStatus) "
						+ "VALUES (?, ?, ?, ?, ?, ?) ;";
				// Use prepared statements to avoid SQL injection attacks
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				// Set the parameters to the query
				statement.setInt(1, order.getCustomerID());
				statement.setInt(2, order.getProductID());
				statement.setInt(3, order.getQuantity());
				statement.setFloat(4, order.getPrice());
				statement.setString(5, order.getProductName());
				statement.setString(6, String.valueOf(order.getStatus()));


				// Execute the insert
				statement.executeUpdate();
				// To get the primary key (id) of the newly inserted record
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()){
					orderID= resultSet.getInt(1);
					int newQty=productStock-order.getQuantity();
					updateProductQty(newQty,order.getProductID(),connection);
				}
			} catch (SQLException e) {

				System.out.println("Error Creating Book: " + e.getMessage());
			}
			// Close the connection to the database - Very important!!!
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				System.out.println("Error closing connection: " + e);
			}
			return orderID;
		}
		//return -1 if order is out of stock
		return orderID;
		
	}

	/**
	 * this method updates the quantity of the products in stock after buy/cancel of order
	 * @param newQuantity updated quantity
	 * @param productID product to be updated
	 * @param connection connection object
	 */
	private void updateProductQty(int newQuantity, Integer productID, Connection connection) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		try {

			// Query to update the number of products in store
			String query = "UPDATE ak_products SET pInStore=? WHERE productID = ?;";
			// Use prepared statements to avoid SQL injection attacks
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			// Set the parameters to the query
			statement.setInt(1, newQuantity);
			statement.setInt(2, productID);


			// Execute the update
			statement.executeUpdate();
		

		} catch (SQLException e) {

			System.out.println("Error "+this.toString() + e.getMessage());
		}


	}

	/**
	 * This method gets the latest stock of the product
	 * @param order order object containing the productID for which the stock is to be calculated
	 * @return returns stock
	 */
	private int getProductStock(Order order) {
		// TODO Auto-generated method stub
		int quantity=-1;
		try {
			connection = new ConnectionManager().getNewConnection();


			String query = "Select * from ak_products where productID=?;";

			PreparedStatement selectStmt = connection.prepareStatement(query);
			selectStmt.setInt(1, order.getProductID());
			ResultSet selectRS = selectStmt.executeQuery();

			if(selectRS.next()){
				quantity=selectRS.getInt("pInStore");
			}
			selectRS.close();
			connection.close();
			return quantity;
			/*
			 * table.setItems(data);
			 */ } catch (Exception e) {
				 e.printStackTrace();
				 System.out.println("Error on Building Data");
			 }
		return quantity;
	}

	
	private ObservableList<OrderTable> allorderList;
	/**
	 * this method returns all the orders for a particular user
	 * @param user
	 * @return
	 */
	public ObservableList<OrderTable> getAllOrders(User user) {
		allorderList = FXCollections.observableArrayList();
		try {
			connection = new ConnectionManager().getNewConnection();
			String query;
			ResultSet selectRS=null;
			
			if(user instanceof Customer)
			{
				query = "Select * from ak_orders where userID=?;";
				PreparedStatement selectStmt = connection.prepareStatement(query);
				selectStmt.setInt(1, user.getUserID());
				selectRS = selectStmt.executeQuery();
			}
			else if (user instanceof Employee)
			{
				query = "Select * from ak_orders where oStatus=?;";
				PreparedStatement selectStmt = connection.prepareStatement(query);
				selectStmt.setString(1, String.valueOf('p'));
				selectRS = selectStmt.executeQuery();
			}




			while (selectRS.next()) {

				OrderTable orderTable = new OrderTable();
				orderTable.orderID.set(selectRS.getString("orderID"));  
				orderTable.custID.set(selectRS.getString("userID"));
				orderTable.prodID.set(selectRS.getString("productID"));
				orderTable.prodName.set(selectRS.getString("pName"));
				orderTable.status.set(selectRS.getString("oStatus"));
				orderTable.price.set(selectRS.getString("tPrice"));
				orderTable.quantity.set(selectRS.getString("noOfItems"));
				orderTable.date.set(selectRS.getString("orderDate"));

				allorderList.add(orderTable);  


			}
			selectRS.close();

			/*
			 * table.setItems(data);
			 */ } catch (Exception e) {
				 e.printStackTrace();
				 System.out.println("Error on Building Data");
			 }
		return allorderList;
	}

	public void confirmOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			connection =new ConnectionManager().getNewConnection();
			// Query to insert a record to the bank table
			String query = "UPDATE ak_orders SET oStatus=? WHERE orderID = ?;";
			// Use prepared statements to avoid SQL injection attacks
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			// Set the parameters to the query
			statement.setString(1, String.valueOf('c'));
			statement.setInt(2, order.getOrderID());
			statement.executeUpdate();
			connection.close();

		}
		catch(Exception e){

		}
	}

	public boolean cancelOrder(Order order) {
		// TODO Auto-generated method stub
		try{
			int qtyStock=getProductStock(order);
			int qtyOrder=0;
			connection =new ConnectionManager().getNewConnection();
			String selectOrderQty="select * from ak_orders where orderID=?";
			PreparedStatement selectOrderPS = connection.prepareStatement(selectOrderQty);
			selectOrderPS.setInt(1,order.getOrderID());
			ResultSet qtyOrderRS=selectOrderPS.executeQuery();
			if(qtyOrderRS.next()){
				qtyOrder=qtyOrderRS.getInt("noOfItems");
			}

			String deleteQuery = "delete from ak_orders where orderID = ?;";
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
			deleteStatement.setInt(1,order.getOrderID());
			// Execute the insert
			deleteStatement.executeUpdate();
			int newQty=qtyOrder+qtyStock;
			updateProductQty(newQty, order.getProductID(), connection);
			connection.close();
			return true;
		}catch(Exception e){
			System.out.println("cancelOrder method "+e.getMessage());
		}
		return false;
	}
}
