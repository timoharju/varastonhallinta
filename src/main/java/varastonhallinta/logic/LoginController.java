    
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
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import varastonhallinta.domain.Role;
import varastonhallinta.ui.Main;
/**
 * Login Controller.
 */
public class LoginController extends AnchorPane implements Initializable {

    @FXML
    TextField userId;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Label errorMessage;
    @FXML
    ComboBox<Locale> languageBox;

    private Main application;
    
    /**
     *
     * @param application
     */
    public void setApp(Main application){
        this.application = application;
        configureLanguageBox();
    }
    
    private void configureLanguageBox(){
        Map<String, Locale> localeMap = application.getLocaleMap();
        languageBox.getItems().addAll(localeMap.values());

        languageBox.setConverter(new StringConverter<Locale>(){
            @Override
            public String toString(Locale object) {
                final StringBuilder displayName = new StringBuilder();
                localeMap.forEach((s, l) -> {
                    if(l == object){
                        displayName.append(s);
                    }
                });

                return displayName.toString();
            }

            @Override
            public Locale fromString(String string) {
                return localeMap.get(string);
            }
        });
        
        languageBox.valueProperty().set(application.getLocale());
        
        languageBox.valueProperty().addListener(
        (ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) -> {
            application.setLocale(newValue);
        });
    }
    
    public Locale getSelectedLocale(){
        return languageBox.getValue();
    }
    
    /**
     *
     * @param event
     */
    public void processLogin(ActionEvent event) {
        if (application == null){
            // We are running in isolated FXML, possibly in Scene Builder.
            // NO-OP.
            errorMessage.setText("Hello " + userId.getText());
        } else {
            if (!application.userLogin(userId.getText(), password.getText())){
                errorMessage.setText("Username/Password is incorrect");
            }
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        errorMessage.setText("");
        userId.setPromptText("käyttäjänimi");
        password.setPromptText("salasana");
    }
}
