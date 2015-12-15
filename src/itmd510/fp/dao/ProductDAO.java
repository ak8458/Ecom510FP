package itmd510.fp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.TableColumn;
import itmd510.fp.helper.ConnectionManager;
import itmd510.fp.model.Product;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * 
 * @author Akshay Patil 
 *This DAO class performs CRUD operations for the Product object
 */
public class ProductDAO {
	// Connection object
	private Connection connection;
	

	public class ProductTable{
		public SimpleStringProperty id = new SimpleStringProperty();
		public SimpleStringProperty name = new SimpleStringProperty();
		public SimpleStringProperty manf = new SimpleStringProperty();
		public SimpleStringProperty price = new SimpleStringProperty();
		public SimpleStringProperty free = new SimpleStringProperty();
		public SimpleStringProperty stock = new SimpleStringProperty();
		public String getPrice() {
			return price.get();
		}
		public String getId() {
			return id.get();
		}
		public String getName() {
			return name.get();
		}
		public String getManf() {
			return manf.get();
		}
		public String getFree() {
			return free.get();
		}
		public String getStock() {
			return stock.get();
		}
	}

	public ProductTable AddProduct(Product product) {
		// TODO Auto-generated method stub
		// Get a connection
		try {
			connection = new ConnectionManager().getNewConnection();
		
		// Query to insert a record to the bank table
		String query = "INSERT INTO ak_products (pName,pManufact, pPrice, freeShipping, pInStore) VALUES (?, ?, ?, ?, ?) ;";
		// Use prepared statements to avoid SQL injection attacks
		PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			// Set the parameters to the query
			statement.setString(1, product.getProductName());
			statement.setString(2, product.getProductManufacturer());
			statement.setFloat(3, product.getProductPrice());
			statement.setString(4, String.valueOf(product.getProductFreeShipp()));
			statement.setInt(5, product.getProductsInStore());

			// Execute the insert
			statement.executeUpdate();
			// To get the primary key (id) of the newly inserted record
			ResultSet resultSet = statement.getGeneratedKeys();

			ProductTable productTable=new ProductTable();
			if(resultSet.next())
				productTable.id.set(""+resultSet.getInt(1));  
			productTable.name.set(product.getProductName());
			productTable.manf.set(product.getProductManufacturer());
			productTable.price.set(""+product.getProductPrice());
			productTable.free.set(String.valueOf(product.getProductFreeShipp()));
			productTable.stock.set(""+product.getProductsInStore());
			return productTable;
		} catch (Exception e) {
			product = null;
			System.out.println("Error Creating Book: " + e.getMessage());
		}
		// Close the connection to the database - Very important!!!
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			System.out.println("Error closing connection: " + e);
		}
		// Return the bank object that was inserted with the id field set.
		return null;
	}


	private ObservableList<ProductTable> allProductsList;
	public ObservableList<ProductTable> getAllProducts() {
		allProductsList = FXCollections.observableArrayList();
		try {
				connection = new ConnectionManager().getNewConnection();
			

			String query = "Select * from ak_products;";
			ResultSet rs = connection.createStatement().executeQuery(query);



			while (rs.next()) {

				ProductTable productTable = new ProductTable();
				productTable.id.set(rs.getString("productID"));  
				productTable.name.set(rs.getString("pName"));
				productTable.manf.set(rs.getString("pManufact"));
				productTable.price.set(rs.getString("pPrice"));
				productTable.free.set(rs.getString("freeShipping"));
				productTable.stock.set(rs.getString("pInStore"));

				allProductsList.add(productTable);  


			}
			rs.close();

			/*
			 * table.setItems(data);
			 */ } catch (Exception e) {
				 e.printStackTrace();
				 System.out.println("Error on Building Data");
			 }
		return allProductsList;
	}

	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub
		try {
			connection = new ConnectionManager().getNewConnection();
		
		// Query to insert a record to the bank table
		String query = "delete from ak_products where productID=?;";

		// Use prepared statements to avoid SQL injection attacks
		PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1,product.getProductID());
			// Execute the insert
			statement.executeUpdate();
			// To get the primary key (id) of the newly inserted record
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.close();


			connection.close();
			connection = null;
		} catch (Exception e) {
			System.out.println("Error closing connection: " + e);
		}
		// Return the bank object that was inserted with the id field set.

	}

	public ProductTable updateProduct(Product product) {
		// TODO Auto-generated method stub
		try {
			connection =new ConnectionManager().getNewConnection();

			// Query to insert a record to the bank table
			String query = "UPDATE ak_products SET pName = ?,pManufact=?,pPrice=?,freeShipping=?,pInStore=? WHERE productID = ?;";
			// Use prepared statements to avoid SQL injection attacks
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			// Set the parameters to the query
			statement.setString(1, product.getProductName());
			statement.setString(2, product.getProductManufacturer());
			statement.setFloat(3, product.getProductPrice());
			statement.setString(4, String.valueOf(product.getProductFreeShipp()));
			statement.setInt(5, product.getProductsInStore());
			statement.setInt(6, product.getProductID());

			// Execute the insert
			statement.executeUpdate();
			// To get the primary key (id) of the newly inserted record
			ResultSet resultSet = statement.getGeneratedKeys();

			ProductTable productTable=new ProductTable();

			productTable.id.set(""+product.getProductID());  
			productTable.name.set(product.getProductName());
			productTable.manf.set(product.getProductManufacturer());
			productTable.price.set(""+product.getProductPrice());
			productTable.free.set(String.valueOf(product.getProductFreeShipp()));
			productTable.stock.set(""+product.getProductsInStore());
			
		
			connection.close();
			return productTable;
		} catch (SQLException e) {
			System.out.println("Error closing connection: " + e);
		}
		// Return the bank object that was inserted with the id field set.
		return null;
	}

}
