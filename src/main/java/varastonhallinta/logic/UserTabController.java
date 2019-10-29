    
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

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import varastonhallinta.domain.User;
import varastonhallinta.ui.AddObjectDialog;
import varastonhallinta.ui.Main;
/**
 * Login Controller.
 */
public class UserTabController extends TabController{

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
    CheckBox nameBox;
    
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
                updateSearchButtonState();
                updateDeleteButtonState();
                updateModifyButtonState();
                updateNewButtonState();
    };
    
    
    private AddObjectDialog<User> addUserDialog;
    private AddObjectDialog<User> modifyUserDialog;
    //private GridPane userGrid;
    private UserDialogController addUserController;
    private UserDialogController modifyUserController;
    private static final String ADD_USER_GRID_LOCATION = "/fxml/UserGrid.fxml";


    @FXML //  fx:id="colSynopsis"
    private VBox roleVBox; // Value injected by FXMLLoader
    
    public void configureDialogs(){
        System.out.println(this + " configureAddUserDialog");
        System.out.println("content " + content);
        GridPane userGrid = null;
        //GridPane testGrid;
        try {
            addUserController = (UserDialogController) application.loadController(ADD_USER_GRID_LOCATION);
            modifyUserController = (UserDialogController) application.loadController(ADD_USER_GRID_LOCATION);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //userGrid = createRegistrationFormPane();
        //userGrid = new GridPane();
        //GridPane testGrid;
//        Color color;
//        Rectangle rectangle = new Rectangle(200,  200, javafx.scene.paint.Color.RED);
//        Button button = new Button("This is a button");
//        TextField textField = new TextField("This is a text field");
//        userGrid.getChildren().addAll(button, textField, rectangle);
//        GridPane.setConstraints(textField, 0, 0);
//        GridPane.setConstraints(textField, 1, 0);
//        GridPane.setConstraints(rectangle, 1, 1);
//        GridPane testGrid  = createRegistrationFormPane();
//        userGrid = dialogController.getGrid();
//        Rectangle rectangle = new Rectangle(200,  200, javafx.scene.paint.Color.RED);
//        System.out.println("testGrid " + userGrid);
//        roleVBox.getChildren().add(userGrid);
//        roleVBox.getChildren().add(dialogController.getRectangle());
        
        Callback<ButtonType, User> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            return addUserController.getUser();
        };
        
        addUserDialog = new AddObjectDialog<User>(addUserController.getGrid(), resultConverter);
        addUserDialog.resultProperty().addListener(
            (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
                if(newValue != null){
                    tryAddUser(newValue);
                }
        });
        
        modifyUserDialog = new AddObjectDialog<User>(modifyUserController.getGrid(), resultConverter);
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
    
 
    List<CheckBox> checkBoxes = new ArrayList<>();
    Map<CheckBox, Input> boxMap = new HashMap<>();
    Map<Object, Function<User, String>> inputMap = new HashMap<>();
    
    private void configureValueMap(){
        boxMap.put(idBox, Input.from(userIDField));
        boxMap.put(nameBox, Input.from(usernameField));
        boxMap.put(firstNameBox, Input.from(firstNameField));
        boxMap.put(lastNameBox, Input.from(lastNameField));
        boxMap.put(roleBox, Input.from(roleComboBox));
        
        inputMap.put(boxMap.get(idBox), (user) -> "" + user.getId());
        inputMap.put(boxMap.get(nameBox), (user) -> user.getUsername());
        inputMap.put(boxMap.get(firstNameBox), (user) -> user.getFirstName());
        inputMap.put(boxMap.get(lastNameBox), (user) -> user.getLastName());
        inputMap.put(boxMap.get(roleBox), (user) -> user.getRole().getName());
    }

    @Override
    protected Object getContent() {
        return content;
    }
    
    private interface Input{
        public String getString();
        
        public static Input from(TextField textField){
            return () -> textField.getText();
        }
        
        public static Input from(ComboBox<String> textField){
            return () -> textField.getValue();
        }
    }

    @Override
    public boolean canSearch() {
        System.out.println("can search " + this);
        for(CheckBox box : boxMap.keySet()){
            System.out.println("box.isSelected() " + box.isSelected());
            System.out.println("!boxMap.get(box).getString().isEmpty() " + !boxMap.get(box).getString().isEmpty());
            System.out.println("boxMap.get(box).getString() " + boxMap.get(box).getString());
            if(box.isSelected() && !boxMap.get(box).getString().isEmpty()){
                return true;
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
        return create != null;
    }

    @Override
    public boolean canDelete() {
        return !this.userTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    private Predicate<User> simpleFilter(Input input){
        return user -> {
            String string = input.getString();
            System.out.println("string " + string);
            String inputString = inputMap.get(input).apply(user);
            System.out.println(inputString);
            System.out.println(string + "contains " + inputString + " = " + inputString.contains(string));
            return inputMap.get(input).apply(user).contains(string);
        };
    }
    
    private void filterUsers(){
        System.out.println("filterUsers");
        Set<User> users = new HashSet<>();
        for(CheckBox box : boxMap.keySet()){
            if(box.isSelected()){
                System.out.println("users.isEmpty");
                if(users.isEmpty()){
                    System.out.println("users.isEmpty");
                    users.addAll(Arrays.asList(this.application.getUsers(simpleFilter(boxMap.get(box)))));
                }else{
                    users.retainAll(Arrays.asList(this.application.getUsers(simpleFilter(boxMap.get(box)))));
                }
                System.out.println("users " + users);
                if(users.isEmpty()){
                    return;
                }
            }
        }
        this.userTable.getItems().addAll(users);
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
    }

    @Override
    public void delete() {
        userTable.getSelectionModel().getSelectedItems().forEach(user -> tryDeleteUser(user));
    } 
}

