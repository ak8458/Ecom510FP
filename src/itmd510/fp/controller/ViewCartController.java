package itmd510.fp.controller;

import itmd510.fp.dao.CartDAO;
import itmd510.fp.dao.CartDAO.CartTable;
import itmd510.fp.dao.OrderDAO;
import itmd510.fp.dao.ProductDAO;
import itmd510.fp.dao.ProductDAO.ProductTable;
import itmd510.fp.helper.ConfirmAlert;
import itmd510.fp.model.CartItem;
import itmd510.fp.model.Order;
import itmd510.fp.model.Product;
import itmd510.fp.model.User;

import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewCartController implements Initializable{

	ObservableList<CartTable> data;
	private User user;
	private Stage dialogStage;
	@FXML private TableView<CartTable> tableView;
	@FXML private TableColumn<CartTable, String> productID;
	@FXML private TableColumn<CartTable, String> prodName;
	@FXML private TableColumn<CartTable, String> noItems;
	@FXML private TableColumn<CartTable, String> priceTax;
	@FXML private Label  sessionLabel,errorMessage,successMessage;
	@FXML
	private TextField quantity;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.errorMessage.setTextFill(Color.web("#ff1a1a"));
	}

	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public void setCartTable(User user) {
		// TODO Auto-generated method stub
		this.sessionLabel.setText("Welcome "+user.getuFirstName());
		productID.setCellValueFactory(new PropertyValueFactory<CartTable, String>("id"));
		productID.setMinWidth(tableView.getMaxWidth()/6);
		prodName.setCellValueFactory(new PropertyValueFactory<CartTable, String>("name"));
		prodName.setMinWidth(tableView.getMaxWidth()/6);

		priceTax.setCellValueFactory(new PropertyValueFactory<CartTable, String>("price"));
		priceTax.setMinWidth(tableView.getMaxWidth()/6);

		noItems.setCellValueFactory(new PropertyValueFactory<CartTable, String>("noOfItems"));
		noItems.setMinWidth(tableView.getMaxWidth()/6);
		data=new CartDAO().getAllCartItems(user);
		tableView.setItems(data);
		this.user=user;
	}
	public void backToProductView(){
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/itmd510/fp/view/ProductViewCustomer.fxml")
					);
			/*scene.setRoot((Parent) loader.load());*/
			AnchorPane root = (AnchorPane) loader.load();
			dialogStage.setTitle("Products");
			Scene scene=new Scene(root);
			ViewProductController controller = 
					loader.<ViewProductController>getController();
			controller.setUser(user);
			dialogStage.setScene(scene);
			controller.setDialogStage(dialogStage);
			dialogStage.show();
		} catch (IOException ex) {
			Logger.getLogger(ViewCartController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void chechoutItems(){
		
		if(data.isEmpty()){
			this.errorMessage.setText("Sorry, It Seems you have Nothing to checkout!!");
		}
		else{
			if(tableView.getSelectionModel().getSelectedIndex()!=-1){
			boolean result=new ConfirmAlert().displayAlert("Order Confirmation", "Are you sure you wanna order this item?");
			if(result){
				Order order=new Order();
				order.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
				order.setCustomerID(user.getUserID());
				order.setPrice(Float.parseFloat(data.get(tableView.getSelectionModel().getSelectedIndex()).getPrice()));
				order.setQuantity(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getNoOfItems()));
				order.setStatus('p');
				order.setProductName(data.get(tableView.getSelectionModel().getSelectedIndex()).getName());
				int orderID=new OrderDAO().AddOrder(order);
				if(orderID>0){
					this.successMessage.setText("Order Confirmed, Please Note Order ID"+orderID);
					Product product=new Product();
					CartItem cartItem =new CartItem();
					product.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
					cartItem.setCartID(user.getUserID());
					cartItem.setProduct(product);
					CartDAO cartDAO=new CartDAO();
					if(cartDAO.removeFromCart(cartItem)){

						data.remove(data.get(tableView.getSelectionModel().getSelectedIndex()));
					}
					else{
						this.errorMessage.setText("Sorry the product couldn't be removed");
					}
				}
				else
				{
					this.errorMessage.setText("Sorry This product is out of stock");
				}
			}
		}else
		{
			this.errorMessage.setText("Please select a product to checkout");
		}
		}
		
	}
	public void removeItem(){
		if(data.isEmpty()){
			this.errorMessage.setText("Sorry, It Seems you have No item in your cart!!");
		}
		else{
			if(tableView.getSelectionModel().getSelectedIndex()!=-1){
			Product product=new Product();
			CartItem cartItem =new CartItem();
			product.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getId()));
			cartItem.setCartID(user.getUserID());
			cartItem.setProduct(product);
			CartDAO cartDAO=new CartDAO();
			if(cartDAO.removeFromCart(cartItem)){
				this.errorMessage.setText(data.get(tableView.getSelectionModel().getSelectedIndex()).getName()+" removed");
				data.remove(data.get(tableView.getSelectionModel().getSelectedIndex()));
			}
			else{
				this.errorMessage.setText("Sorry the product couldn't be removed");
			}
		}
			else{
				this.errorMessage.setText("Please select an item to be removed");
			}
		}
		
	}

	public void viewOrders(){
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/itmd510/fp/view/OrderViewCustomer.fxml")
					);

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
			Logger.getLogger(ViewCartController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void logOut(){
		this.user=null;
		try {
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
