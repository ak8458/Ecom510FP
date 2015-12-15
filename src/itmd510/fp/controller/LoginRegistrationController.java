package itmd510.fp.controller;

import java.io.IOException;

import itmd510.fp.dao.UserDAO;
import itmd510.fp.model.Customer;
import itmd510.fp.model.Employee;
import itmd510.fp.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * This controller is associated with the LOgin screen used to register a user and login the user.
 * @author Akshay Patil
 *
 */
public class LoginRegistrationController {
	//This is the parent stage
	private Stage dialogStage;

	//This is the Text box element in the view for name of bank

	//This is the Text box element in the view for address of bank
	@FXML
	private TextField firstName;
	
	@FXML
	private TextField lastName;
	@FXML
	private PasswordField password;
	@FXML
	private TextField address;
	@FXML
	private TextField userID;
	@FXML
	private PasswordField loginPassword;
	@FXML
	private Label errorLogin,regMessage,userIDError,loginPasswordError,
	errorAddress,errorUserPassword,errorLastName,errorFirstName;


	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * method to register a user
	 */
	public void registerUser() {
	
		boolean register=true;
		if(this.firstName.getText().equals("")||this.firstName.getText()==null){
			this.errorFirstName.setText("Please enter First Name");
			register=false;
		}
		if(this.lastName.getText().equals("")||this.lastName.getText()==null){
			this.errorLastName.setText("Please enter Last Name");
			register=false;
		}
		if(this.password.getText().equals("")||this.password.getText()==null){
			this.errorUserPassword.setText("Please enter Password");
			register=false;
		}
		if(this.address.getText().equals("")||this.address.getText()==null){
			this.errorAddress.setText("Please enter Address");
			register=false;
		}


		if(this.firstName.getText().length()>100){
			this.errorFirstName.setText("First Name cannot comprise more then 100 letters");
			register=false;
		}
		if(this.lastName.getText().length()>100){
			this.errorLastName.setText("Last Name cannot comprise more then 100 letters");
			register=false;
		}
		if(this.password.getText().length()>100){
			this.errorUserPassword.setText("Password cannot comprise more then 100 letters");
			register=false;
		}
		if(this.address.getText().length()>100){
			this.errorAddress.setText("Address cannot comprise more then 100 letters");
			register=false;
		}
		
		if(register){
			this.errorFirstName.setText("");
			this.errorLastName.setText("");
			this.errorUserPassword.setText("");
			this.errorAddress.setText("");
			String firstName = this.firstName.getText();
			String lastName = this.lastName.getText();
			String password = this.password.getText();
			String address = this.address.getText();



			//Create the model object
			Customer customer=new Customer();
			customer.setuFirstName(firstName);
			customer.setuLastName(lastName);
			customer.setuPassword(password);
			customer.setuAddress(address);
			customer.setuRole('c');



			//Create a DAO instance of the model
			UserDAO b = new UserDAO();
			int userid=b.AddUser(customer);
			if(userid!=-1){
				regMessage.setText("Please not your user ID to login: "+userid);
				this.firstName.clear();
				this.lastName.clear();
				this.password.clear();
				this.address.clear();
				this.userID.setText(""+userid);

			}
		}
	}


	/**
	 * method to login to application after credentials are verified
	 */
	public void loginToApplication() {
		boolean login=true;
		if(!this.userID.getText().matches("^\\d+$")){
			this.userIDError.setText("Your userID should be a positive Integer");
			login=false;
		}
		if(this.userID.getText()==null||this.userID.getText().equals("")){
			this.userIDError.setText("Please enter UserID");
			login=false;
		}
		if(this.userID.getText().length()>11){
			this.userIDError.setText("User ID Should be less then 11 digits");
			login=false;
		}
		if(this.loginPassword.getText()==null||this.loginPassword.getText().equals("")){
			this.loginPasswordError.setText("Please enter Password");
			login=false;
		}
		if(this.loginPassword.getText().length()>100){
			this.loginPasswordError.setText("The Password you have entered is too long");
			login=false;
		}
		if(login){
			this.loginPasswordError.setText("");
			this.userIDError.setText("");
			this.errorLogin.setText("");
			int uID=-1;

			uID = Integer.parseInt(this.userID.getText());
			String password = this.loginPassword.getText();
			UserDAO userAuthenticate=new UserDAO();
			User user=userAuthenticate.authenticateUser(uID,password);
			if(user!=null){
				this.errorLogin.setText("");
				if(user instanceof Customer)
				{
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/itmd510/fp/view/ProductViewCustomer.fxml")
								);
						/*scene.setRoot((Parent) loader.load());*/
						AnchorPane root = (AnchorPane) loader.load();
						dialogStage.setTitle("Shop");
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
				else if(user instanceof Employee){
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/itmd510/fp/view/ProductViewEmployee.fxml")
								);
						/*scene.setRoot((Parent) loader.load());*/
						AnchorPane root = (AnchorPane) loader.load();
						dialogStage.setTitle("Shop");
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
			}
			else{
				this.errorLogin.setText("Please enter Valid credentials");
			}
		}
	}

	/**
	 * Checks for a valid email format
	 * @param email
	 * @return true is in valid format
	 */
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }
	//This is required as stage.close() in the program will not trigger any events.
	//To have callback listeners on the close event, we trigger the external close event
	private void close() {
		dialogStage.fireEvent(new WindowEvent(dialogStage,WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}
