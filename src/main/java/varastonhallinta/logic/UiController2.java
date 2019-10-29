package varastonhallinta.logic;

import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
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
public class UiController2 extends FXMLController {
     
    
    @FXML
    private TextField userID;
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;

    @FXML
    private TextField firstName;
    
    @FXML
    private TextField lastName;
    
    @FXML
    private ComboBox<String> rolesBox;
    
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
    
    @FXML
    private Button newButton;
    
    final ObservableList<User> tableContent = FXCollections.observableArrayList();
    
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
        
        this.userID.setText(id);
        this.username.setText(username);
        this.password.setText(password);
        this.firstName.setText(firstName);
        this.lastName.setText(lastName);
        this.rolesBox.valueProperty().set(role);
    }
    
    public void configureTestArea(){
        System.out.println(this + " configureTestArea()");
        String uiName = "UserGrid";
        String fxml = "/fxml/" + uiName + ".fxml";
        try {
            application.loadContent(fxml);
        } catch (Exception ex) {
            Logger.getLogger(UiController2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        newButton.setOnAction(
            (ActionEvent event) -> {
                getAddUserDialog().show();
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
 
    private void configureRolesBox(){
        String[] roles = application.getRoleNames();
        ObservableList<String> options = 
        FXCollections.observableArrayList(roles);
        rolesBox.getItems().addAll(options);
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
        int id = Integer.parseInt(this.userID.getText());
        findUser(user -> user.getId() == id);
    }
    
    @FXML
    public void findUsersWithUsername(){
        System.out.println("findUsersWithUsername");
        String username = this.username.getText();
        findUser(user -> user.getUsername().contains(username));
//        findUser(user -> {System.out.println("user.getUsername() " + user.getUsername()); return false;});
    }
    
    @FXML
    public void findUsersWithFirstName(){
        System.out.println("findUsersWithFirstName");
        String firstName = this.firstName.getText();
        findUser(user -> user.getFirstName().contains(firstName));
    }
    
    @FXML
    public void findUsersWithLastName(){
        System.out.println("findUsersWithLastName");
        String lastName = this.lastName.getText();
        findUser(user -> user.getLastName().contains(lastName));
    }
    
    @FXML
    public void findUsersWithRole(){
        System.out.println("findUsersWithRole");
        String role = this.rolesBox.getValue();
        findUser(user -> user.getRole().getName().contains(role));
    }
    
    
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert rolesBox != null : "fx:id=\"colName\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
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

//        configureRolesBox();
//        configureTooltips();
//        configureFindUserTable();
//        configureTestArea();
    }
    
    /**
     * Called when the new button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void newFired(ActionEvent event) {
        FXMLController.getActiveController().create();
    }

    /**
     * Called when the delete button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void deleteFired(ActionEvent event) {
        FXMLController.getActiveController().delete();
    }
    
        /**
     * Called when the search button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void searchFired(ActionEvent event) {
        FXMLController.getActiveController().search();
    }
    
        /**
     * Called when the modify button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void modifyFired(ActionEvent event) {
        FXMLController.getActiveController().modify();
    }
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");
        configureTestArea();
        configureRolesBox();
        configureFindUserTable();
        super.initialize(location, resources);
    }
   
}
