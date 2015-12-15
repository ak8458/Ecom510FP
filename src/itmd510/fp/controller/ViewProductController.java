
package itmd510.fp.controller;
import itmd510.fp.dao.CartDAO;
import itmd510.fp.dao.ProductDAO;
import itmd510.fp.dao.UserDAO;
import itmd510.fp.dao.ProductDAO.ProductTable;
import itmd510.fp.model.CartItem;
import itmd510.fp.model.Product;
import itmd510.fp.model.User;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;



public class ViewProductController implements Initializable {
	ObservableList<ProductTable> data;
	private IntegerProperty index=new SimpleIntegerProperty();
	private User user;
	private Stage dialogStage;
	@FXML private TableView<ProductTable> tableView;
	@FXML private TableColumn<ProductTable, String> itemIdCol;
	@FXML private TableColumn<ProductTable, String> itemNameCol;
	@FXML private TableColumn<ProductTable, String> itemManfCol;
	@FXML private TableColumn<ProductTable, String> itemPriceCol;
	@FXML private TableColumn<ProductTable, String> itemFreeCol;
	@FXML private TableColumn<ProductTable, String> itemStockCol;
	@FXML
	private TextField productName;
	@FXML
	private TextField productManufacturer;
	@FXML
	private TextField productPrice;

	@FXML
	private TextField numberOfItems;
	@FXML
	private TextField quantity;
	@FXML
	private RadioButton freeShipYes;
	@FXML
	private RadioButton freeShipNo;
	@FXML private Label  sessionLabel,errorMessage,prodNameError,prodMnfError,prodPriceError,errorNoitems;
	@FXML ToggleGroup freeShip;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		itemIdCol.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("id"));
		itemIdCol.setMinWidth(tableView.getMaxWidth()/6);
		itemNameCol.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("name"));
		itemNameCol.setMinWidth(tableView.getMaxWidth()/6);
		itemManfCol.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("manf"));
		itemManfCol.setMinWidth(tableView.getMaxWidth()/6);
		itemPriceCol.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("price"));
		itemPriceCol.setMinWidth(tableView.getMaxWidth()/6);
		itemFreeCol.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("free"));
		itemFreeCol.setMinWidth(tableView.getMaxWidth()/6);
		itemStockCol.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("stock"));
		itemStockCol.setMinWidth(tableView.getMaxWidth()/6);
		data=new ProductDAO().getAllProducts();
		tableView.setItems(data);


	}
	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public void viewOrdersEmployee(){
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/itmd510/fp/view/OrderViewEmployee.fxml")
					);
			/*scene.setRoot((Parent) loader.load());*/
			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Orders");
			Scene scene=new Scene(root);
			OrderController controller = 
					loader.<OrderController>getController();

			controller.setOrdersTable(user,this,true);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewProductController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void addProduct() {
		prodNameError.setText("");
		prodMnfError.setText("");
		prodPriceError.setText("");
		errorNoitems.setText("");
		boolean addProduct=true;
		if(this.productName.getText().equals("")||this.productName.getText()==null){
			this.prodNameError.setText("Please enter Product Name");
			addProduct=false;
		}
		if(this.productName.getText().length()>100){
			this.prodNameError.setText("The Product Name is too long");
			addProduct=false;
		}
		if(this.productManufacturer.getText().equals("")||this.productManufacturer.getText()==null){
			this.prodMnfError.setText("Please enter Manufacturer");
			addProduct=false;
		}
		if(this.productManufacturer.getText().length()>100){
			this.prodMnfError.setText("The Manufacturer is too long");
			addProduct=false;
		}

		if(!this.productPrice.getText().matches("^[0-9]*\\.?[0-9]*$")){
			this.prodPriceError.setText("Please enter valid price in decimal");
			addProduct=false;
		}		
		if(this.productPrice.getText().equals("")||this.productPrice.getText()==null){
			this.prodPriceError.setText("Please enter Price");
			addProduct=false;
		}
		if(this.productPrice.getText().length()>13){
			this.prodPriceError.setText("The price of product is too high");
			addProduct=false;
		}
		if(this.numberOfItems.getText().length()>11){
			this.errorNoitems.setText("The number of Items you are trying to add is too much");
			addProduct=false;
		}
		if(!this.numberOfItems.getText().matches("^\\d+$")){
			this.errorNoitems.setText("Number of items should be an integer");
			addProduct=false;
		}
		
		if(this.numberOfItems.getText().equals("")||this.numberOfItems.getText()==null){
			this.errorNoitems.setText("Please enter number of items");
			addProduct=false;
		}
		
		
		if(addProduct){
			prodNameError.setText("");
			prodMnfError.setText("");
			prodPriceError.setText("");
			errorNoitems.setText("");
			String productName = this.productName.getText();
			String productManufacturer = this.productManufacturer.getText();
			Float productPrice = Float.parseFloat(this.productPrice.getText());
			Character freeShipping;
			if(freeShip.getSelectedToggle().equals(freeShipYes))
			{	freeShipping= '1';}
			else
			{freeShipping= '0';}

			Integer numberOfItems = Integer.parseInt(this.numberOfItems.getText());

			//Validate the data

			if(productName == null || productName.trim().equals("")) {
				return;
			}
			if(productManufacturer == null || productManufacturer.trim().equals("")) {
				return;
			}

			if(productPrice == null) {
				return;
			}
			if(freeShipping == null || freeShipping.equals("")) {
				return;
			}

			//Create the model object
			Product product=new Product();
			//Set the values from the input form
			/*book.setBookId(bookId);*/
			product.setProductManufacturer(productManufacturer);
			product.setProductName(productName);
			product.setProductFreeShipp(freeShipping);
			product.setProductPrice(productPrice);
			product.setProductsInStore(numberOfItems);



			//Create a DAO instance of the model
			ProductDAO b = new ProductDAO();
			ProductTable productTable=b.AddProduct(product);

			data.add(productTable);
		}
	}

	public void updateProduct(){
		if(tableView.getSelectionModel().getSelectedIndex()!=-1){
		boolean updateProduct=true;
		prodNameError.setText("");
		prodMnfError.setText("");
		prodPriceError.setText("");
		errorNoitems.setText("");
		Product product=new Product();
		
		product.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
		product.setProductName(data.get(tableView.getSelectionModel().getSelectedIndex()).getName());
		product.setProductFreeShipp(data.get(tableView.getSelectionModel().getSelectedIndex()).getFree().charAt(0));
		product.setProductManufacturer(data.get(tableView.getSelectionModel().getSelectedIndex()).getManf());
		product.setProductPrice(Float.parseFloat(data.get(tableView.getSelectionModel().getSelectedIndex()).getPrice()));
		product.setProductsInStore(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getStock()));

		if(!(this.productName.getText()==null||this.productName.getText().equals(""))){
			if(this.productName.getText().length()>100){
				this.prodNameError.setText("The Product Name is too long");
				updateProduct=false;
			}
			else{
			String productName = this.productName.getText();
			product.setProductName(productName);
			}
		}
		if(!(this.productManufacturer.getText()==null||this.productManufacturer.getText().equals(""))){
			if(this.productManufacturer.getText().length()>100){
				this.prodMnfError.setText("The Manufacturer is too long");
				updateProduct=false;
			}
			else{
			String productManufacturer = this.productManufacturer.getText();
			product.setProductManufacturer(productManufacturer);
			}
		}
		if(!(this.productPrice.getText()==null||this.productPrice.getText().equals(""))){
			if(!this.productPrice.getText().matches("^[0-9]*\\.?[0-9]*$")){
				this.prodPriceError.setText("Please enter valid price in decimal");
				updateProduct=false;
			}
			if(this.productPrice.getText().length()>13){
				this.prodPriceError.setText("The price of product is too high");
				updateProduct=false;
			}
			if(this.productPrice.getText().length()<13 && this.productPrice.getText().matches("^[0-9]*\\.?[0-9]*$")){
			Float productPrice = Float.parseFloat(this.productPrice.getText());
			product.setProductPrice(productPrice);
			}
		}
		Character freeShipping;
		if(freeShip.getSelectedToggle().equals(freeShipYes))
		{	freeShipping= '1';}
		else
		{freeShipping= '0';}
		product.setProductFreeShipp(freeShipping);
		
		if(!(this.numberOfItems.getText()==null||this.numberOfItems.getText().equals(""))){
			if(this.numberOfItems.getText().length()>11){
				this.errorNoitems.setText("The number of Items you are trying to add is too much");
				updateProduct=false;
			}
			if(!this.numberOfItems.getText().matches("^\\d+$")){
				this.errorNoitems.setText("Number of items should be an integer");
				updateProduct=false;
			}
			if(this.numberOfItems.getText().matches("^\\d+$")&&this.numberOfItems.getText().length()<11){
			Integer numberOfItems = Integer.parseInt(this.numberOfItems.getText());
			product.setProductsInStore(numberOfItems);
			}
		}
		if(updateProduct){
		ProductTable productTable=new ProductDAO().updateProduct(product);
		data.remove(tableView.getSelectionModel().getSelectedIndex());
		data.add(productTable);
		}
		}
		else{
			this.errorMessage.setText("Please select a product to update");
		}
	}
	
	
	public void addToCart(){
		if(tableView.getSelectionModel().getSelectedIndex()!=-1){
		boolean addtoCart=true;
		if(!this.quantity.getText().matches("^\\d+$")){
			this.errorMessage.setText("Please Enter a positive integer as quantity");
			addtoCart=false;
		}
		if(this.quantity.getText().equals("")||this.quantity.getText()==null){
			this.errorMessage.setText("Please Enter the quantity");
			addtoCart=false;
		}
		if(	addtoCart)
		{   this.errorMessage.setText("");
		if(Integer.parseInt(this.quantity.getText()) <= Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getStock()))
		{  	
			this.errorMessage.setText("");
			Product product=new Product();
			product.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
			product.setProductName(data.get(tableView.getSelectionModel().getSelectedIndex()).getName());
			product.setProductFreeShipp(data.get(tableView.getSelectionModel().getSelectedIndex()).getFree().charAt(0));
			product.setProductManufacturer(data.get(tableView.getSelectionModel().getSelectedIndex()).getManf());
			product.setProductPrice(Float.parseFloat(data.get(tableView.getSelectionModel().getSelectedIndex()).getPrice()));
			product.setProductsInStore(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getStock()));
			int quantity=Integer.parseInt(this.quantity.getText());

			CartItem cartItem=new CartItem();
			cartItem.setCartID(user.getUserID());
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			CartDAO cartDAO= new CartDAO();
			cartDAO.addCartItem(cartItem);
			this.quantity.clear();
		}
		else{
			this.errorMessage.setText("The quantity entered is greater then the stock available");
		}
		}
		}
		else{
			this.errorMessage.setText("Please select a product");
		}
	}
	public void viewCart(){

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/itmd510/fp/view/CartViewCustomer.fxml")
					);
			/*scene.setRoot((Parent) loader.load());*/
			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Products");
			Scene scene=new Scene(root);
			ViewCartController controller = 
					loader.<ViewCartController>getController();

			controller.setCartTable(user);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewProductController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void setUser(User user) {
		// TODO Auto-generated method stub
		this.sessionLabel.setText("Welcome "+user.getuFirstName());
		this.user=user;
	}

	public void viewOrders(){
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/itmd510/fp/view/OrderViewCustomer.fxml")
					);
			/*scene.setRoot((Parent) loader.load());*/
			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Orders");
			Scene scene=new Scene(root);
			OrderController controller = 
					loader.<OrderController>getController();

			controller.setOrdersTable(user,this,false);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewProductController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

public void logOut(){
	try {
		this.user=null;
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/itmd510/fp/view/UserLoginReg.fxml")
				);
		/*scene.setRoot((Parent) loader.load());*/
		AnchorPane root = (AnchorPane) loader.load();
		dialogStage.setTitle("Orders");
		Scene scene=new Scene(root);
		LoginRegistrationController controller = 
				loader.<LoginRegistrationController>getController();

		dialogStage.setScene(scene);
		controller.setDialogStage(dialogStage);
		dialogStage.show();
	} catch (IOException ex) {
		Logger.getLogger(ViewProductController.class.getName()).log(Level.SEVERE, null, ex);
	}
}
}