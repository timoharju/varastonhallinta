/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import static varastonhallinta.logic.FXMLController.USERNAME_MIN_LENGTH;
import varastonhallinta.ui.InfoPopup;

/**
 *
 * @author tanel
 */
public class UserDialogController1 extends FXMLController{
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
    private ComboBox<String> roleComboBox;
    
    @FXML
    private GridPane grid;
    
    private InfoPopup usernamePopup;
    private InfoPopup passwordPopup;
    private InfoPopup firstNamePopup;
    private InfoPopup lastNamePopup;
    
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

    /**
     * @return the grid
     */
    public GridPane getGrid() {
        System.out.println("getGrid " + grid);
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    
    public User getUser(){
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String firstName = this.firstNameField.getText();
        String lastName = this.lastNameField.getText();
        String role = this.roleComboBox.getValue();
            
        return new User(username, password, firstName, lastName, new Role(role));
    }
    
    public void initFields(User user){
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        roleComboBox.setValue(user.getRole().getName());
    }
    
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
    }
    
}
