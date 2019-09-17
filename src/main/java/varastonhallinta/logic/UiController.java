package varastonhallinta.logic;

import java.sql.SQLException;
import varastonhallinta.logic.EntitiesController;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UiController {

    public TextField usernameField;
    public PasswordField passwordField;
    public Label userLabel;	
   
    
    public void button(ActionEvent actionEvent) throws SQLException {
    	
    	EntitiesController userManager = new EntitiesController();
    	
    	userManager.addUser(usernameField.getText(), passwordField.getText());
    	userLabel.setText(usernameField.getText() + " lisätty tietokantaan!");
    }

}
