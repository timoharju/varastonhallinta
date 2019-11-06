/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javax.persistence.Column;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import static varastonhallinta.logic.FXMLController.USERNAME_MIN_LENGTH;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.InfoPopup;

/**
 *
 * @author tanel
 */
public class UserDialogController extends DialogController<User>{
    @FXML
    private TextField userIDField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField passwordField;

    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private ComboBox<Role> roleComboBox;
    
    @FXML
    private GridPane createGrid;
    
    @FXML
    private GridPane modifyGrid;
    
    private InfoPopup usernamePopup;
    private InfoPopup passwordPopup;
    private InfoPopup firstNamePopup;
    private InfoPopup lastNamePopup;
    
    private Collection<Input<String>> inputs;
    private EntityDialog<User> addUserDialog;
    private EntityDialog<User> modifyUserDialog;
    
    
    private class Validator{
        Predicate<String> p;
        Input<String> i;
        Runnable r;
        String failMessage;
        
        public void validate(){
            try {
                if(p.test(i.getInput())){
                    r.run();
                }else{
                    application.showAlert(Alert.AlertType.ERROR, "Error", failMessage);
                }
            } catch (InputException ex) {
                application.showAlert(Alert.AlertType.ERROR, "Error", failMessage);
            }
        }
    }
    
    private void configureTooltips(){
        System.out.println("configureTooltips");
        String usernameFieldHelpText = "Käyttäjänimi:\n"
                + "Tulee olla " + USERNAME_MIN_LENGTH + " - " + USERNAME_MAX_LENGTH + " merkkiä pitkä,\n"
                + "Ei saa sisältää muita erikoismerkkejä kuin: - tai _\n"
                + "esim. Matti_Meikäläinen";
        usernamePopup = new InfoPopup(usernameFieldHelpText, usernameField, usernameField);

        String passwordFieldHelpText = "Salasanan tulee olla " + PASSWORD_MIN_LENGTH + " - " + PASSWORD_MAX_LENGTH + " merkkiä pitkä";
        passwordPopup = new InfoPopup(passwordFieldHelpText, passwordField, passwordField);
        
        String firstNameFieldHelpText = "Etunimi: \n"
                + "Ei ole pakollinen kenttä,\n"
                + "Tulee olla " + FIRST_NAME_MIN_LENGTH + " - " + FIRST_NAME_MAX_LENGTH + " merkkiä pitkä,\n"
                + "Saa sisältää vain kirjaimia\n"
                + "esim. Matti";
        firstNamePopup = new InfoPopup(firstNameFieldHelpText, firstNameField, firstNameField);
        
        String lastNameFieldHelpText = "Sukunimi: \n"
                + "Ei ole pakollinen kenttä,\n"
                + "Tulee olla " + LAST_NAME_MIN_LENGTH + " - " + LAST_NAME_MAX_LENGTH + " merkkiä pitkä,\n"
                + "Saa sisältää vain kirjaimia\n"
                + "esim. Meikäläinen";
        lastNamePopup = new InfoPopup(lastNameFieldHelpText, lastNameField, lastNameField);
    }
    
    private void configureRolesBox(){
        String[] roles = application.getRoleNames();
        ObservableList<String> options = 
        FXCollections.observableArrayList(roles);
        roleComboBox.getItems().addAll(options);
    }

    
    @Override
    public void initFields(User user){
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        roleComboBox.setValue(user.getRole().getName());
    }
    
    @Override
    public void clearFields(){
        usernameField.setText("");
        passwordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureRolesBox();
        configureTooltips();
        configureDialogController(createGrid, modifyGrid, addUserDialog, modifyUserDialog);
    }

    @Override
    public User getEntity() {
        String username = ""; 
        String password = ""; 
        String firstName = ""; 
        String lastName = ""; 
        Role role;
        try{
            username = Input.from(usernameField, String.class).getInput(); 
            password = Input.from(passwordField, String.class).getInput(); 
            firstName = Input.from(firstNameField, String.class).getInput(); 
            lastName = Input.from(lastNameField, String.class).getInput(); 
            role = Input.from(roleComboBox).getInput();
        }catch(Exception e){
            
        }
        return new User(username, password, firstName, lastName);
    }
}
