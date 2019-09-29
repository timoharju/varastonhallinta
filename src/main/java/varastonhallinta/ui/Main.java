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
package varastonhallinta.ui;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import varastonhallinta.domain.Role;
import varastonhallinta.logic.LoginController;
import varastonhallinta.logic.ProfileController;
import varastonhallinta.logic.RoleJpaController;
import varastonhallinta.logic.UiController;
import varastonhallinta.logic.UserJpaController;
import varastonhallinta.model.User;
import varastonhallinta.security.Authenticator;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {

    private Stage stage;
    private User loggedUser;
    private final double MINIMUM_WINDOW_WIDTH = 390.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;
    private final String UI_PAGE = "/fxml/ui3.fxml";
    private final String LOGIN_PAGE = "/fxml/login.fxml";
    private final String PROFILE_PAGE = "/profile.fxml";
    private List<String> pages = new ArrayList<String>();
    private Iterator<String> pagesIterator = pages.iterator();
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("varastonhallinta");
    private static UserJpaController userController = new UserJpaController(entityManagerFactory);
    private static RoleJpaController roleController = new RoleJpaController(entityManagerFactory);
    private Authenticator authenticator = new Authenticator(userController);
    
    static{
        Role admin = new Role("Admin");
        Role user = new Role("User");
        Role editor = new Role("Editor");
        roleController.create(admin);
        roleController.create(user);
        roleController.create(editor);
        userController.create(new varastonhallinta.domain.User("admin", "admin", admin));
        userController.create(new varastonhallinta.domain.User("user", "user", user));
        userController.create(new varastonhallinta.domain.User("editor", "editor", editor));
    }
    //String[] pages = {LOGIN_PAGE, UI_PAGE};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("FXML Login Sample");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoLogin();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }
        
    public boolean userLogging(String userId, String password){
        if (authenticator.validate(userId, password)) {
            loggedUser = User.of(userId);
            gotoUI();
            return true;
        } else {
            return false;
        }
    }
    
    public void userLogout(){
        loggedUser = null;
        gotoLogin();
    }
    
    private void gotoProfile() {
        try {
            ProfileController profile = (ProfileController) replaceSceneContent(UI_PAGE);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoUI() {
        try {
            UiController ui = (UiController) replaceSceneContent(UI_PAGE);
            ui.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent(LOGIN_PAGE);
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Parent page;
        try {
            page = (Parent)loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
    public String[] getRoleNames(){
        List<Role> roles = roleController.findRoleEntities();
        String[] roleNames = new String[roles.size()];
        for(int i=0; i<roles.size(); i++){
            roleNames[i] = roles.get(i).getName();
        }
        return roleNames;
    }
}
