package itmd510.fp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import java.util.logging.Level;
import java.util.logging.Logger;

import itmd510.fp.dao.OrderDAO;
import itmd510.fp.dao.OrderDAO.OrderTable;
import itmd510.fp.model.Order;
import itmd510.fp.model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OrderController implements Initializable {
	ObservableList<OrderTable> data;
	Initializable controller;
	Boolean employee;
	private User user;
	private Stage dialogStage;
	@FXML private TableView<OrderTable> tableView;
	@FXML private TableColumn<OrderTable, String> orderID;
	@FXML private TableColumn<OrderTable, String> custID;
	@FXML private TableColumn<OrderTable, String> prodID;
	@FXML private TableColumn<OrderTable, String> prodName; 
	@FXML private TableColumn<OrderTable, String> orderDate;
	@FXML private TableColumn<OrderTable, String> orderStatus;
	@FXML private TableColumn<OrderTable, String> quantity;
	@FXML private TableColumn<OrderTable, String> price;
	@FXML private Label  sessionLabel,errorMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	public void setOrdersTable(User user,Initializable controller,Boolean employee) {
		// TODO Auto-generated method stub
		this.sessionLabel.setText("Welcome "+user.getuFirstName());
		orderID.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("orderID"));
		orderID.setMinWidth(tableView.getMaxWidth()/6);
		custID.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("custID"));
		custID.setMinWidth(tableView.getMaxWidth()/6);

		prodID.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("prodID"));
		prodID.setMinWidth(tableView.getMaxWidth()/6);

		prodName.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("prodName"));
		prodName.setMinWidth(tableView.getMaxWidth()/6);

		orderStatus.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("status"));
		orderStatus.setMinWidth(tableView.getMaxWidth()/6);

		orderDate.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("date"));
		orderDate.setMinWidth(tableView.getMaxWidth()/6);

		quantity.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("quantity"));
		quantity.setMinWidth(tableView.getMaxWidth()/6);

		price.setCellValueFactory(new PropertyValueFactory<OrderTable, String>("price"));
		price.setMinWidth(tableView.getMaxWidth()/6);
		data=new OrderDAO().getAllOrders(user);
		tableView.setItems(data);
		this.user=user;
		this.controller=controller;
		this.employee=employee;
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
	public void backActivity(){
		if(employee){
			try {
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/itmd510/fp/view/ProductViewEmployee.fxml")
						);

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

			}
		}
		else{
			if(controller instanceof ViewProductController)
			{
				try {
					FXMLLoader loader = new FXMLLoader(
							getClass().getResource("/itmd510/fp/view/ProductViewCustomer.fxml")
							);

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

				}
			}
			else if(controller instanceof ViewCartController)
			{
				System.out.println("got in here too");
				try {
					FXMLLoader loader = new FXMLLoader(
							getClass().getResource("/itmd510/fp/view/CartViewCustomer.fxml")
							);
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
					Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void confirmOrder(){
		if(data.isEmpty()){
			this.errorMessage.setText("Sorry, There are no orders to confirm!!");
		}else{
		Order order=new Order();
		order.setOrderID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getOrderID()));
		OrderDAO orderDAO=new OrderDAO();
		orderDAO.confirmOrder(order);
		data.remove(tableView.getSelectionModel().getSelectedIndex());
		}
	}
	public void cancelOrder(){
		if(data.isEmpty()){
			this.errorMessage.setText("Sorry, It Seems you have No order to cancel!!");
		}
		else{
			if(data.get(tableView.getSelectionModel().getSelectedIndex()).getStatus().equals("c")){
				this.errorMessage.setText("We are sorry this order is already confirmed and cannot be cancelled");
			}
			else
			{
				Order order=new Order();
				order.setOrderID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getOrderID()));
				order.setProductID(Integer.parseInt(data.get(tableView.getSelectionModel().getSelectedIndex()).getProdID()));

				OrderDAO orderDAO=new OrderDAO();
				if(orderDAO.cancelOrder(order))
				{
					this.errorMessage.setText("Order cancelled successfully!!");
					data.remove(tableView.getSelectionModel().getSelectedIndex());
				}
				else{
					this.errorMessage.setText("Sorry we couldn't cancel your order, please try again later");
				}
			}
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
