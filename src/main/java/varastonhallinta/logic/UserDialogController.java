/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import static varastonhallinta.logic.FXMLController.USERNAME_MIN_LENGTH;
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
        //String[] rolesNames = application.getRoleNames();
        Collection<Role> roles = application.getEntities(Role.class);
        //ObservableList<Role> options = FXCollections.observableArrayList(roles);
        roleComboBox.getItems().addAll(roles);
        
//        roleComboBox.setCellFactory(
//        (ListView<Role> p) -> new ListCell<Role>() {
//            @Override protected void updateItem(Role item, boolean empty) {
//                super.updateItem(item, empty);
//                
//                if (item == null || empty) {
//                    this.setText("");
//                } else {
//                    this.setText(item.getName());
//                }
//            }
//        });

//        roleComboBox.selectionModelProperty().addListener(
//        (ObservableValue<? extends SingleSelectionModel<Role>> observable, SingleSelectionModel<Role> oldValue, SingleSelectionModel<Role> newValue) -> {
//            System.out.println("Selection changed");
//        });
        
        roleComboBox.setConverter(new StringConverter<Role>(){
            @Override
            public String toString(Role object) {
                return object.getName();
            }

            @Override
            public Role fromString(String string) {
                Role role = null;
                for(Role r : roles){
                    if(r.getName().equals(string)){
                        role = r;
                    }
                }
                return role;
            }
        });
    }

    @Override
    public void initFields(User user){
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        roleComboBox.setValue(user.getRole());
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
        configureDialogController(grid);
        configureRolesBox();
        configureTooltips();
    }

    @Override
    public User getEntity() {
        String username = ""; 
        String password = ""; 
        String firstName = ""; 
        String lastName = ""; 
        Role role = null;
        try{
            username = Input.from(usernameField, String.class).getInput(); 
            password = Input.from(passwordField, String.class).getInput(); 
            firstName = Input.from(firstNameField, String.class).getInput(); 
            lastName = Input.from(lastNameField, String.class).getInput(); 
            role = roleComboBox.getValue();
        }catch(Exception e){
            
        }
        return new User(username, password, firstName, lastName, role);
    }

    @Override
    public User updateEntity(User user) {
        System.out.println("updateEntity " + user);
        String username = ""; 
        String password = ""; 
        String firstName = ""; 
        String lastName = ""; 
        Role role = null;
        try{
            username = Input.from(usernameField, String.class).getInput(); 
            password = Input.from(passwordField, String.class).getInput(); 
            firstName = Input.from(firstNameField, String.class).getInput(); 
            lastName = Input.from(lastNameField, String.class).getInput(); 
            role = roleComboBox.getValue();
        }catch(Exception e){
            Logger.getLogger(UserDialogController.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        return user;
    }
}
