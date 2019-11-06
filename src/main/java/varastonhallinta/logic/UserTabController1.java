    
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.User;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.Main;
/**
 * Login Controller.
 */
public class UserTabController1 extends TabController<User>{

    @FXML
    private TextField userIDField;
    
    @FXML
    private TextField usernameField;

    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private ComboBox<String> roleComboBox;
    
    @FXML
    TableView<User> userTable;
    
    @FXML
    CheckBox idBox;
    
    @FXML
    CheckBox usernameBox;
    
    @FXML
    CheckBox firstNameBox;
    
    @FXML
    CheckBox lastNameBox;
    
    @FXML
    CheckBox roleBox;
    
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
    
    @FXML //  fx:id="colSynopsis"
    private Object content; // Value injected by FXMLLoader

    
    final ObservableList<User> tableContent = FXCollections.observableArrayList();
            // This listener listen to changes in the table widget selection and
    // update the DeleteIssue button state accordingly.
    private final ListChangeListener<User> tableSelectionChanged =
            (ListChangeListener.Change<? extends User> c) -> {
//                updateSearchButtonState();
//                updateDeleteButtonState();
//                updateModifyButtonState();
//                updateNewButtonState();
    };
    
    
    private EntityDialog<User> addUserDialog;
    private EntityDialog<User> modifyUserDialog;
    //private GridPane userGrid;
    private UserDialogController addUserController;
    private UserDialogController modifyUserController;
    private static final String ADD_USER_GRID_LOCATION = "/fxml/UserGrid.fxml";


    @FXML //  fx:id="colSynopsis"
    private VBox roleVBox; // Value injected by FXMLLoader

    public UserTabController1(String addSuccessfull, String addFail, String updateSuccessfull, String updateFail, String removeSuccessfull, String removeFail) {
        super(addSuccessfull, addFail, updateSuccessfull, updateFail, removeSuccessfull, removeFail);
    }
    
    public void configureDialogs(){
        System.out.println(this + " configureAddUserDialog");
        System.out.println("content " + content);
        try {
            addUserController = (UserDialogController) application.loadController(ADD_USER_GRID_LOCATION);
            modifyUserController = (UserDialogController) application.loadController(ADD_USER_GRID_LOCATION);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Callback<ButtonType, User> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            return addUserController.getUser();
        };
        
        addUserDialog = new EntityDialog<User>(addUserController.getGrid(), resultConverter);
        addUserDialog.resultProperty().addListener(
            (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
                if(newValue != null){
                    tryAddUser(newValue);
                }
        });
        
        modifyUserDialog = new EntityDialog<User>(modifyUserController.getGrid(), resultConverter);
        addUserDialog.resultProperty().addListener(
            (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
                if(newValue != null){
                    tryModifyUser(newValue);
                }
        });
    }

    private void configureFindUserTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colRole.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper(p.getValue().getRole().getName());
            }
        });
        
        userTable.setItems(tableContent);
        final ObservableList<User> tableSelection = userTable.getSelectionModel().getSelectedItems();

        tableSelection.addListener(tableSelectionChanged);
    }
    
    private void configureRolesBox(){
        String[] roles = application.getRoleNames();
        ObservableList<String> options = 
        FXCollections.observableArrayList(roles);
        roleComboBox.getItems().addAll(options);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureDialogs();
        configureRolesBox();
        configureValueMap();
        configureFindUserTable();
        super.initialize(location, resources);
    }
    
    private Map<CheckBox, String> inputNameMap = new HashMap<>();
    Map<CheckBox, Input<String>> inputMap = new HashMap<>();
    private FilterFactory<User> filterFactory;
    
    private void configureValueMap(){
        filterFactory = new FilterFactory<User>();
        inputMap.put(idBox, Input.from(userIDField));
        inputMap.put(usernameBox, Input.from(usernameField));
        inputMap.put(firstNameBox, Input.from(firstNameField));
        inputMap.put(lastNameBox, Input.from(lastNameField));
        inputMap.put(roleBox, Input.from(roleComboBox));
        
        filterFactory.addFilter(idBox, Input.from(userIDField, Integer.class), (user) -> user.getID(),
                (input, id) -> id.equals(input));
        filterFactory.addFilter(usernameBox, Input.from(usernameField), (user) -> user.getUsername(),
                (input, username) -> username.contains(input) || input.contains(username));
        filterFactory.addFilter(firstNameBox, Input.from(firstNameField), (user) -> user.getFirstName(),
                (input, firstName) -> firstName.contains(input) || input.contains(firstName));
        filterFactory.addFilter(lastNameBox, Input.from(lastNameField), (user) -> user.getLastName(),
                (input, lastName) -> lastName.contains(input) || input.contains(lastName));
        filterFactory.addFilter(roleBox, Input.from(roleComboBox), (user) -> user.getRole().getName(),
                (input, role) -> role.equals(input));
        
        inputNameMap.put(idBox, "ID");
        inputNameMap.put(usernameBox, "käyttäjänimi");
        inputNameMap.put(firstNameBox, "etunimi");
        inputNameMap.put(lastNameBox, "sukunimi");
        inputNameMap.put(roleBox, "rooli");

    }

    @Override
    protected Object getContent() {
        return content;
    }
    
    @Override
    public boolean canSearch() {
        for(CheckBox box : inputMap.keySet()){
            try {
                if(box.isSelected() && !inputMap.get(box).getInput().isEmpty()){
                    return true;
                }
            } catch (InputException ex) {
                Logger.getLogger(UserTabController1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    @Override
    public boolean canModify() {
        return !this.userTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    @Override
    public boolean canCreate() {
        return true;
    }

    @Override
    public boolean canDelete() {
        return !this.userTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    private void filterUsers(){
        Set<User> items = new HashSet<>();
        for(CheckBox box : inputMap.keySet()){
            if(box.isSelected()){
                Collection<?> temp;
                try {
                   temp = Arrays.asList(application.getEntities(User.class, filterFactory.getFilter(box)));
                } catch (InputException ex) {
                    ex.setObject(getInputName(box));
                    application.showAlert(Alert.AlertType.ERROR, "Error", ex.getHRMessage());
                    return;
                }
                if(items.isEmpty()){
                    items.addAll(temp);
                }else{
                    items.retainAll(temp);
                }
                if(items.isEmpty()){
                    return;
                }
            }
        }
        this.userTable.getItems().addAll(items);
    }
    
    private String getInputName(CheckBox checkBox){
        return inputNameMap.get(checkBox);
    }
    
    @Override
    public void search() {
        userTable.getItems().clear();
        filterUsers();
    }
    
    @Override
    public void modify() {
        modifyUserController.initFields(userTable.getSelectionModel().getSelectedItem());
        this.modifyUserDialog.show();
    }

    @Override
    public void create() {
        addUserController.clearFields();
        this.addUserDialog.show();
        updateTable();
    }

    @Override
    public void delete() {
        userTable.getSelectionModel().getSelectedItems().forEach(user -> tryDeleteUser(user));
        updateTable();
    } 
    
    private void updateTable(){
        userTable.getItems().setAll(application.getUsers(user -> userTable.getItems().contains(user)));
    }

    @Override
    public void handleSearch() {
        userTable.getItems().clear();
        filterUsers();
    }

    @Override
    public void handleModify() {
        modifyUserController.initFields(userTable.getSelectionModel().getSelectedItem());
        this.modifyUserDialog.show();
    }

    @Override
    public void handleCreate() {
        addUserController.clearFields();
        this.addUserDialog.show();
        updateTable();
    }

    @Override
    public void handleDelete() {
        userTable.getSelectionModel().getSelectedItems().forEach(user -> remove(user));
        updateTable();
    }

    @Override
    public boolean validate(User e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }  
}

