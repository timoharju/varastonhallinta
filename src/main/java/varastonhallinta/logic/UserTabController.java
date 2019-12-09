    
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
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.ui.exceptions.EntityException;
/**
 * Login Controller.
 */
public class UserTabController extends TabController<User>{

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

    //private GridPane userGrid;
    private UserDialogController createUserController;
    private UserDialogController updateUserController;
    private static final String ADD_USER_GRID_LOCATION = "/fxml/UserGrid.fxml";

    
    public void configureDialogs(){
        try {
            createUserController = (UserDialogController) application.loadController(ADD_USER_GRID_LOCATION);
            updateUserController = (UserDialogController) application.loadController(ADD_USER_GRID_LOCATION);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private Map<CheckBox, String> inputNameMap = new HashMap<>();
    private Map<CheckBox, Input<String>> inputMap = new HashMap<>();
    private FilterFactory<User> filterFactory;
    
    private void configureFilters(){
        filterFactory = new FilterFactory<>();
        inputMap.put(idBox, Input.from(userIDField));
        inputMap.put(usernameBox, Input.from(usernameField));
        inputMap.put(firstNameBox, Input.from(firstNameField));
        inputMap.put(lastNameBox, Input.from(lastNameField));
        inputMap.put(roleBox, Input.from(roleComboBox));
        
        filterFactory.addFilter(idBox, Input.from(userIDField, Integer.class), (user) -> user.getId(),
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
    public void initialize(URL location, ResourceBundle resources) {
        configureDialogs();
        configureRolesBox();
        configureFilters();
        configureFindUserTable();
        super.configureDialogController(userTable, User.class, createUserController, updateUserController, filterFactory, inputNameMap, inputMap, "Lisäys onnistui", "Lisäys epäonnistui", "Muokkaus onnistui", "Muokkaus epäonnistui", "Poisto onnistui", "Poisto epäonnistui");
    }

}

