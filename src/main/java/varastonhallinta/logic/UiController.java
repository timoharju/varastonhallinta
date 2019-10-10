package varastonhallinta.logic;

import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.InfoPopup;
import varastonhallinta.ui.exceptions.AddUserException;

/**
 *
 * @author tanel
 */
public class UiController implements Initializable {
    
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 30;
    private static final int FIRST_NAME_MIN_LENGTH = 1;
    private static final int FIRST_NAME_MAX_LENGTH = 30;
    private static final int LAST_NAME_MIN_LENGTH = 1;
    private static final int LAST_NAME_MAX_LENGTH = 30;
    
    @FXML
    private ComboBox<String> rolesBoxKL;
    
    @FXML
    private ComboBox<String> rolesBoxKM;
    
    @FXML
    private TextField userIDKL;
    
    @FXML
    private TextField usernameKL;
    
    @FXML
    private TextField passwordKL;

    @FXML
    private TextField firstNameKL;
    
    @FXML
    private TextField lastNameKL;
    
    @FXML
    private TextField userIDKM;
    
    @FXML
    private TextField usernameKM;
    
    @FXML
    private TextField passwordKM;

    @FXML
    private TextField firstNameKM;
    
    @FXML
    private TextField lastNameKM;
    
    private Main application;
    private InfoPopup usernamePopup;
    private InfoPopup passwordPopup;
    private InfoPopup firstNamePopup;
    private InfoPopup lastNamePopup;
    
    /**
     *
     * @param application
     */
    public void setApp(Main application){
        this.application = application;
        configureRolesBox();
        configureTooltips();
    }
    
    private void configureTooltips(){
        System.out.println("configureTooltips");
        String usernameFieldHelpText = "Käyttäjänimi:\n"
                + "Tulee olla " + USERNAME_MIN_LENGTH + " - " + USERNAME_MAX_LENGTH + " merkkiä pitkä,\n"
                + "Ei saa sisältää muita erikoismerkkejä kuin: - tai _\n"
                + "esim. Matti_Meikäläinen";
        usernamePopup = new InfoPopup(usernameFieldHelpText, usernameKL, usernameKM);

        String passwordFieldHelpText = "Salasanan tulee olla " + PASSWORD_MIN_LENGTH + " - " + PASSWORD_MAX_LENGTH + " merkkiä pitkä";
        passwordPopup = new InfoPopup(passwordFieldHelpText, passwordKL, passwordKM);
        
        String firstNameFieldHelpText = "Etunimi: \n"
                + "Ei ole pakollinen kenttä,\n"
                + "Tulee olla " + FIRST_NAME_MIN_LENGTH + " - " + FIRST_NAME_MAX_LENGTH + " merkkiä pitkä,\n"
                + "Saa sisältää vain kirjaimia\n"
                + "esim. Matti";
        firstNamePopup = new InfoPopup(firstNameFieldHelpText, firstNameKL, firstNameKM);
        
        String lastNameFieldHelpText = "Sukunimi: \n"
                + "Ei ole pakollinen kenttä,\n"
                + "Tulee olla " + LAST_NAME_MIN_LENGTH + " - " + LAST_NAME_MAX_LENGTH + " merkkiä pitkä,\n"
                + "Saa sisältää vain kirjaimia\n"
                + "esim. Meikäläinen";
        lastNamePopup = new InfoPopup(lastNameFieldHelpText, lastNameKL, lastNameKM);
    }
    
    private void configureRolesBox(){
        String[] roles = application.getRoleNames();
        ObservableList<String> options = 
        FXCollections.observableArrayList(roles);
        rolesBoxKL.getItems().addAll(options);
        rolesBoxKM.getItems().addAll(options);
    }
    
    /**
     *
     */
    @FXML
    public void toggleUsernameLabel(){

    }
    
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert rolesBoxKL != null : "fx:id=\"colName\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert colStatus != null : "fx:id=\"colStatus\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert colSynopsis != null : "fx:id=\"colSynopsis\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert deleteIssue != null : "fx:id=\"deleteIssue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert descriptionValue != null : "fx:id=\"descriptionValue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert details != null : "fx:id=\"details\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert displayedIssueLabel != null : "fx:id=\"displayedIssueLabel\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert newIssue != null : "fx:id=\"newIssue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert saveIssue != null : "fx:id=\"saveIssue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert synopsis != null : "fx:id=\"synopsis\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        
//        System.out.println(this.getClass().getSimpleName() + ".initialize");
//        configureButtons();
//        configureDetails();
//        configureTable();
//        connectToService();
//        if (list != null) {
//            list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//            list.getSelectionModel().selectedItemProperty().addListener(projectItemSelected);
//            displayedProjectNames.addListener(projectNamesListener);
//        }

        configureRolesBox();
        configureTooltips();
    }
        
    /**
     *
     */
    @FXML
    public void processAddUserForm(){
        String username = usernameKL.getText();
        String password = passwordKL.getText();
        String firstName = firstNameKL.getText();
        firstName = firstName == null ? "" : firstName.trim();
        String lastName = lastNameKL.getText();
        lastName = lastName == null ? "" : lastName.trim();
        String role = rolesBoxKL.getValue();
        
        if(!validUsername(username)){
            System.out.println("username " + username);
            application.showAlert(Alert.AlertType.ERROR, "Virhe", "Viallinen käyttäjänimi!");
            return;
        }
        
        if(!validPassword(password)){
            System.out.println("password " + password);
            application.showAlert(Alert.AlertType.ERROR, "Virhe", "Viallinen salasana!");
            return;
        }
        
        if("".equals(firstName)){
            System.out.println("firstName " + firstName);
            System.out.println("jorma".equals(firstName));
            firstName = firstName.toLowerCase();
            if(!validFirstName(firstName)){
                application.showAlert(Alert.AlertType.ERROR,  "Virhe", "Viallinen etunimi!");
                return;
            }
            firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
        }
        
        if("".equals(lastName)){
            System.out.println("lastName " + lastName);
            lastName = lastName.toLowerCase();
            if(!validLastName(lastName)){
                application.showAlert(Alert.AlertType.ERROR,  "Virhe", "Viallinen sukunimi!");
                return;
            }
            lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);
        }
        
        if(role == null || "".equals(role)){
            System.out.println("role " + role);
            application.showAlert(Alert.AlertType.ERROR,  "Virhe", "Valitse rooli!");
            return;
        }
        
        try{
            application.addUser(username, password, firstName, lastName, role);
        }catch(AddUserException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", username + " lisätty!");
    }
    
    private boolean validUsername(String username){
        String regex = "[A-Za-zåÅäÄöÖ0-9_\\-]{" + USERNAME_MIN_LENGTH + "," + USERNAME_MAX_LENGTH + "}";
        return username != null && username.matches(regex);
    }
    
    private boolean validPassword(String password){
        String regex = "[^\n]{" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "}";
        return password != null && password.matches(regex);
    }
    
    private boolean validFirstName(String firstName){
        String regex = "[a-zåäö]{" + FIRST_NAME_MIN_LENGTH + "," + FIRST_NAME_MAX_LENGTH + "}";
        return firstName == null || firstName.matches(regex);
    }
        
    private boolean validLastName(String lastName){
        String regex = "[a-zåäö]{" + LAST_NAME_MIN_LENGTH + "," + LAST_NAME_MAX_LENGTH + "}";
        return lastName == null || lastName.matches(regex);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
   
}
