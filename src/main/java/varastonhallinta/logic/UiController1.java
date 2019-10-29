package varastonhallinta.logic;

import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import varastonhallinta.ui.AddUserDialog;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.InfoPopup;
import varastonhallinta.ui.exceptions.AddUserException;

/**
 *
 * @author tanel
 */
public class UiController1 implements Initializable {
    
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
    private ComboBox<String> rolesBoxKH;
    
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
    
    @FXML
    private TextField userIDKH;
    
    @FXML
    private TextField usernameKH;

    @FXML
    private TextField firstNameKH;
    
    @FXML
    private TextField lastNameKH;
    
    @FXML
    private TableView<User> findUserTable;
    
    @FXML //  fx:id="colName"
    private TableColumn<User, Integer> colID; // Value injected by FXMLLoader

    @FXML //  fx:id="colStatus"
    private TableColumn<User, String> colUsername; // Value injected by FXMLLoader

    @FXML //  fx:id="colSynopsis"
    private TableColumn<User, String> colFirstName; // Value injected by FXMLLoader
    
    @FXML //  fx:id="colStatus"
    private TableColumn<User, String> colLastName; // Value injected by FXMLLoader

    @FXML //  fx:id="colSynopsis"
    private TableColumn<User, String> colRole; // Value injected by FXMLLoader
    
    final ObservableList<User> tableContent = FXCollections.observableArrayList();
    
    private Main application;
    private InfoPopup usernamePopup;
    private InfoPopup passwordPopup;
    private InfoPopup firstNamePopup;
    private InfoPopup lastNamePopup;
    
        // This listener listen to changes in the table widget selection and
    // update the DeleteIssue button state accordingly.
    private final ListChangeListener<User> tableSelectionChanged =
            (ListChangeListener.Change<? extends User> c) -> {
                updateUserEditFields(c.getList().get(0));
    };
    
    public void updateUserEditFields(User user){
        String id = "";
        String username = "";
        String password = "";
        String firstName = "";
        String lastName = "";
        String role = "";
        if(user != null){
            id = Integer.toString(user.getId());
            username = user.getUsername();
            password = user.getPassword();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            role = user.getRole().getName();
        }
        
        userIDKM.setText(id);
        usernameKM.setText(username);
        passwordKM.setText(password);
        firstNameKM.setText(firstName);
        lastNameKM.setText(lastName);
        rolesBoxKM.valueProperty().set(role);
    }
    
    /**
     *
     * @param application
     */
    public void setApp(Main application){
        this.application = application;
        configureRolesBox();
        configureTooltips();
        configureFindUserTable();
        configureTestArea();
    }

    @FXML
    private VBox testArea;
    
    @FXML
    private GridPane addUserBox;
    
    @FXML
    private TextField userIDKM1;
    
    @FXML
    private TextField usernameKM1;
    
    @FXML
    private TextField passwordKM1;

    @FXML
    private TextField firstNameKM1;
    
    @FXML
    private TextField lastNameKM1;
    
    @FXML
    private ComboBox<String> rolesBoxKM1;
    
    @FXML
    private Button testButton;
    
    public void configureTestArea(){
//        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
//        ButtonType helpButtonType = new ButtonType("Help", ButtonData.HELP);
//        Dialog<String> dialog = new Dialog<>();
//        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
//        boolean disabled = false; // computed based on content of text fields, for example
//        dialog.getDialogPane().lookupButton(loginButtonType).setDisable(disabled);
//        
//        Optional<String> result = dialog.showAndWait();
//        if (result.isPresent()) {
//            System.out.println("result.get() " + result.get());
//        }

//        TextInputDialog dialog = new TextInputDialog();
//        
//        Optional<String> result = dialog.showAndWait();
//        if (result.isPresent()) {
//            System.out.println("result.get() " + result.get());
//        }
//        
//        Popup popup = new Popup();
//        popup.getContent().add(addUserBox);
//        popup.show(testArea.getScene().getWindow());
//
//        
        Callback<ButtonType, User> resultConverter = (dialogButton) -> {
            ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonData.OK_DONE){
                return null;
            }
            
            String username = usernameKM1.getText();
            String password = passwordKM1.getText();
            String firstName = firstNameKM1.getText();
            String lastName = lastNameKM1.getText();
            String role = rolesBoxKM1.getValue();
            
            return new User(username, password, firstName, lastName, new Role(role));
        };
        
        AddUserDialog addUserDialog = new AddUserDialog(addUserBox, resultConverter);
        addUserDialog.resultProperty().addListener(
            (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
                if(newValue != null){
                    tryAddUser(newValue.getUsername(), newValue.getPassword(), newValue.getFirstName(), newValue.getLastName(), newValue.getRole().getName());
                }
        });
        
        addUserDialog.
        
        testButton.setOnAction(
            (ActionEvent event) -> {
                addUserDialog.show();
        });
    }
    
    private void configureFindUserTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRole.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<User, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper(p.getValue().getRole().getName());
            }
        });
        
        findUserTable.setItems(tableContent);
        final ObservableList<User> tableSelection = findUserTable.getSelectionModel().getSelectedItems();

        tableSelection.addListener(tableSelectionChanged);
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
        rolesBoxKH.getItems().addAll(options);
    }
    
    private void findUser(Predicate<User> predicate){
        this.findUserTable.getItems().clear();
        User[] users = this.application.getUsers(predicate);
        if(users != null){
            findUserTable.getItems().addAll(users);
        }
    }
    
    @FXML
    public void findUsersWithId(){
        System.out.println("findUsersWithId");
        int id = Integer.parseInt(userIDKH.getText());
        findUser(user -> user.getId() == id);
    }
    
    @FXML
    public void findUsersWithUsername(){
        System.out.println("findUsersWithUsername");
        String username = usernameKH.getText();
        findUser(user -> user.getUsername().contains(username));
//        findUser(user -> {System.out.println("user.getUsername() " + user.getUsername()); return false;});
    }
    
    @FXML
    public void findUsersWithFirstName(){
        System.out.println("findUsersWithFirstName");
        String firstName = firstNameKH.getText();
        findUser(user -> user.getFirstName().contains(firstName));
    }
    
    @FXML
    public void findUsersWithLastName(){
        System.out.println("findUsersWithLastName");
        String lastName = lastNameKH.getText();
        findUser(user -> user.getLastName().contains(lastName));
    }
    
    @FXML
    public void findUsersWithRole(){
        System.out.println("findUsersWithRole");
        String role = rolesBoxKH.getValue();
        findUser(user -> user.getRole().getName().contains(role));
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
        
        tryAddUser(username, password, firstName, lastName, role);
    }
    
    private void tryAddUser(String username, String password, String firstName, String lastName, String role){
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
        
        if(!firstName.isEmpty()){
            System.out.println("firstName " + firstName);
            System.out.println("jorma".equals(firstName));
            firstName = firstName.toLowerCase();
            if(!validFirstName(firstName)){
                application.showAlert(Alert.AlertType.ERROR,  "Virhe", "Viallinen etunimi!");
                return;
            }
            firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
        }
        
        if(!lastName.isEmpty()){
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
