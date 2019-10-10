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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import varastonhallinta.domain.Role;
import varastonhallinta.logic.LoginController;
import varastonhallinta.logic.ProfileController;
import varastonhallinta.logic.RoleJpaController;
import varastonhallinta.logic.UiController;
import varastonhallinta.logic.UserJpaController;
import varastonhallinta.domain.User;
import varastonhallinta.security.Authenticator;
import varastonhallinta.ui.exceptions.NoSuchRoleException;
import varastonhallinta.ui.exceptions.UsernameTakenException;

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
    
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("varastonhallinta");
    private static UserJpaController userController = new UserJpaController(entityManagerFactory);
    private static RoleJpaController roleController = new RoleJpaController(entityManagerFactory);
    private Authenticator authenticator = new Authenticator(userController);
    private Scene scene;
    
    static{
        Role admin = new Role("Admin");
        Role vm = new Role("Varastomies");
        Role tp = new Role("TuotePäällikkö");
        Role as = new Role("Asiakas");
        roleController.create(admin);
        roleController.create(vm);
        roleController.create(tp);
        roleController.create(as);
        userController.create(new varastonhallinta.domain.User("admin", "admin", admin));
        userController.create(new varastonhallinta.domain.User("varastomies", "varastomies", vm));
        userController.create(new varastonhallinta.domain.User("tuotePäällikkö", "tuotePäällikkö", tp));
        userController.create(new varastonhallinta.domain.User("asiakas", "asiakas", as));
    }

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
            stage.setTitle("Kirjautumissivu");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoLogin();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the currently logged in {@link User}. All user related operations
     * are performed on the user that is logged in.
     * @return the currently logged in user.
     */
    public User getLoggedUser() {
        return loggedUser;
    }
        
    /**
     *
     * @param username 
     * @param password
     * @return
     */
    public boolean userLogin(String username, String password){
        if (authenticator.validate(username, password)) {
            loggedUser = userController.findUserWithName(username);
            gotoUI(loggedUser.getRole().getName() + "UI");
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Logs out the current logged in user and loads the login page.
     */
    public void userLogout(){
        loggedUser = null;
        gotoLogin();
    }
    
    private void gotoProfile() {
        try {
            ProfileController profile = (ProfileController) replaceSceneContent(PROFILE_PAGE);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoUI(String uiName) {
        try {
            UiController ui = (UiController) replaceSceneContent("/fxml/" + uiName + ".fxml");
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
        scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
    //TODO: Create a Bindable Roles-property instead.
    /**
     * Returns a array of names of all of the possible {@link roles} that a user can have.
     * 
     * @return an array of strings containing the name of all user roles.
     */
    public String[] getRoleNames(){
        List<Role> roles = roleController.findRoleEntities();
        String[] roleNames = new String[roles.size()];
        for(int i=0; i<roles.size(); i++){
            roleNames[i] = roles.get(i).getName();
        }
        return roleNames;
    }
    
    /**
     * Creates and persists a new {@link User} entity. The given username must 
     * be unique for each created and existing user.
     * @param username The name that the user uses to login to the application.
     * @param password The character string that is used to identify the user during login. 
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param roleName The {@link Role} of the user.
     * @throws NoSuchRoleException If the specified role isn't found in the persistence unit.
     * @throws UsernameTakenException If the specified username already exists in the persistence unit.
     */
    public void addUser(String username, String password, String firstName, String lastName, String roleName) throws NoSuchRoleException, UsernameTakenException{
        Role role = roleController.findRoleWithName(roleName);
        if(role == null){
            throw new NoSuchRoleException("Role " + role + " doesn't exist.");
        }
        
        try{
            if(userController.findUserWithName(username) != null){
                throw new UsernameTakenException("Username: " + username + " is taken.");
            } 
        }catch(NoResultException ex){
            
        }
        
        userController.create(new User(username, password, firstName, lastName, role));
    }
    
    /**
     * Shows an popup window containing the given message and title. Used to 
     * convey important information to the user.
     * @param alertType The type of window to show to the user (refer to the 
     * {@link AlertType} documentation for clarification over which one is most 
     * appropriate). 
     * @param title The title header of the popup window.
     * @param message The message contained within the alert.
     */
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(scene.getWindow());
        alert.show();
    }
}
