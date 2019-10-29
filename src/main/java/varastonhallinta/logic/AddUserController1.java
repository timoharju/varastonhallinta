    
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import static varastonhallinta.logic.FXMLController.USERNAME_MIN_LENGTH;
import varastonhallinta.ui.AddObjectDialog;
import varastonhallinta.ui.AddUserDialog;
import varastonhallinta.ui.InfoPopup;
import varastonhallinta.ui.Main;
/**
 * Login Controller.
 */
public class AddUserController1 extends TabController{

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
    TableView userTable;
    
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
    
    @FXML
    Tab userTab;
    
    
    private InfoPopup usernamePopup;
    private InfoPopup passwordPopup;
    private InfoPopup firstNamePopup;
    private InfoPopup lastNamePopup;


    public void configureAddUserDialog(){
        System.out.println(this + " configureTestArea()");
        
        Callback<ButtonType, User> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            
            String username = this.usernameField.getText();
            String password = this.passwordField.getText();
            String firstName = this.firstNameField.getText();
            String lastName = this.lastNameField.getText();
            String role = this.roleComboBox.getValue();
            
            return new User(username, password, firstName, lastName, new Role(role));
        };
        
        this.setAddUserDialog(new AddObjectDialog<>(addUserGrid, resultConverter));
        addUserDialog.resultProperty().addListener(
            (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
                if(newValue != null){
                    tryAddUser(newValue);
                }
        });
        
        String[] roles = application.getRoleNames();
        ObservableList<String> options = 
        FXCollections.observableArrayList(roles);
        roleComboBox.getItems().addAll(options);
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
    public void initialize(URL location, ResourceBundle resources) {
        configureAddUserDialog();
        configureTooltips();
        configureRolesBox();
        configureValueMap();
        super.initialize(location, resources);
    }
    
 
    List<CheckBox> checkBoxes = new ArrayList<>();
    Map<CheckBox, BooleanSupplier> valueMap;
    
    private void configureValueMap(){
        valueMap.put(idBox, this::validateIDField);
        valueMap.put(nameBox, this::validateUsernameField);
        valueMap.put(firstNameBox, this::validateFirstNameField);
        valueMap.put(lastNameBox, this::validateLastNameField);
        valueMap.put(roleBox, this::validateRoleField);
    }

    private boolean validateIDField(){
        return userIDField != null && !userIDField.getText().isEmpty();
    }
    
    private boolean validateUsernameField(){
        return usernameField != null && !usernameField.getText().isEmpty();
    }
    
    private boolean validateFirstNameField(){
        return firstNameField != null && !firstNameField.getText().isEmpty();
    }
   
    
    private boolean validateLastNameField(){
        return lastNameField != null && !lastNameField.getText().isEmpty();
    }
    
    private boolean validateRoleField(){
        return roleBox != null && roleComboBox.getValue().isEmpty();
    }

    @Override
    public boolean canSearch() {
        for(CheckBox box : valueMap.keySet()){
            if(box.isArmed() && valueMap.get(box).getAsBoolean()){
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

    @Override
    public Tab getTab() {
        return userTab;
    }

    @Override
    public void modify() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

