/*package itmd510.fp.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import itmd510.fp.dao.BankDAO;
import itmd510.fp.model.Product;

public class ProductMenuController {

	private Scene scene;
	Stage dialogStage;
	  public ProductMenuController(Scene scene) {
	    this.scene = scene;
	  }

	//Method to set the parent stage of the current view
		public void setDialogStage(Stage dialogStage) {
			this.dialogStage = dialogStage;
		}

	public void addProduct() {
		 try {
		      FXMLLoader loader = new FXMLLoader(
		        getClass().getResource("AddProduct.fxml")
		      );
		      scene.setRoot((Parent) loader.load());
		      AddProductController controller = 
		        loader.<AddProductController>getController();

		    } catch (IOException ex) {
		      Logger.getLogger(ProductMenuController.class.getName()).log(Level.SEVERE, null, ex);
		    }
	}

	public void viewProduct() {
		 try {
		      FXMLLoader loader = new FXMLLoader(
		        getClass().getResource("ProductView.fxml")
		      );
		      scene.setRoot((Parent) loader.load());
		      ViewProductController controller = 
		        loader.<ViewProductController>getController();

		    } catch (IOException ex) {
		      Logger.getLogger(ProductMenuController.class.getName()).log(Level.SEVERE, null, ex);
		    }
	}
	//This is required as stage.close() in the program will not trigger any events.
		//To have callback listeners on the close event, we trigger the external close event
		private void close() {
			dialogStage.fireEvent(new WindowEvent(dialogStage,WindowEvent.WINDOW_CLOSE_REQUEST));
		}
}
 */

package itmd510.fp.controller;

import java.io.IOException;



import java.util.logging.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;





public class ProductMenuController {
	/*private Scene scene;*/

	public ProductMenuController(/*Scene scene*/) {
		/*this.scene = scene;*/
	}
	//This is the parent stage
	private Stage dialogStage;

	//This is the Text box element in the view for name of bank

	//This is the Text box element in the view for address of bank
	@FXML
	private Button addProduct;
	@FXML
	private Button viewProduct;


	
	public void viewProduct() {
	    try {
	      FXMLLoader loader = new FXMLLoader(
	        getClass().getResource("/itmd510/fp/view/ProductView.fxml")
	      );
	     /* scene.setRoot((Parent) loader.load());*/
	      AnchorPane root = (AnchorPane) loader.load();
	      dialogStage.setTitle("Products");
	      Scene scene=new Scene(root);
	      ViewProductController controller = 
	        loader.<ViewProductController>getController();
	      dialogStage.setScene(scene);
	      controller.setDialogStage(dialogStage);
	      dialogStage.show();
	    } catch (IOException ex) {
	      Logger.getLogger(ProductMenuController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    catch (Exception ex) {
		      Logger.getLogger(ProductMenuController.class.getName()).log(Level.SEVERE, null, ex);
		    }
	  }
	//Method to set the parent stage of the current view
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	

	public void initialize() {
		
	}

	

	
	
	//This is required as stage.close() in the program will not trigger any events.
	//To have callback listeners on the close event, we trigger the external close event
	private void close() {
		dialogStage.fireEvent(new WindowEvent(dialogStage,WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}




