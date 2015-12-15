package itmd510.fp.launch;

import itmd510.fp.controller.LoginRegistrationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserLogin extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Create a loader for the UI components
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/itmd510/fp/view/UserLoginReg.fxml"));
			//Inflate the view using the loader
            AnchorPane root = (AnchorPane) loader.load();
            //Set window title
            primaryStage.setTitle("Login Registration");
            //Create a scene with the inflated view
            Scene scene = new Scene(root);
            
            
           
            //Set the scene to the stage
            primaryStage.setScene(scene);
            //Get the controller instance from the loader
            LoginRegistrationController controller = loader.getController();
            //Set the parent stage in the controller
            controller.setDialogStage(primaryStage);
            //Show the view
            primaryStage.show();
		} catch(Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
 
  
}