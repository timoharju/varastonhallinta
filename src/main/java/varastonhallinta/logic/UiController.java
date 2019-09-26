package varastonhallinta.logic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import varastonhallinta.logic.EntitiesController;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

public class UiController implements Initializable {

    public TextField usernameField;
    public PasswordField passwordField;
    public Label userLabel;	
    private EntitiesController userManager;
   
    
    public void button(ActionEvent actionEvent)  throws SQLException {
    	
    	
    	userManager.addUser(usernameField.getText(), passwordField.getText());
    	userLabel.setText(usernameField.getText() + " lisätty tietokantaan!");
    }
    
	public void initialize(URL location, ResourceBundle resources) {
		userManager = new EntitiesController();
	}

}
