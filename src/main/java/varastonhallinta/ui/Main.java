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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.logic.LoginController;
import varastonhallinta.logic.ProfileController;
import varastonhallinta.logic.RoleJpaController;
import varastonhallinta.logic.UiController;
import varastonhallinta.logic.UserJpaController;
import varastonhallinta.domain.User;
import varastonhallinta.logic.FXMLController;
import varastonhallinta.logic.ItemJpaController;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.security.Authenticator;
import varastonhallinta.ui.exceptions.ItemnameTakenException;
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
    
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("varastonhallinta");
    private UserJpaController userController = new UserJpaController(entityManagerFactory);
    private ItemJpaController itemController = new ItemJpaController(entityManagerFactory);
    private RoleJpaController roleController = new RoleJpaController(entityManagerFactory);
    private Authenticator authenticator = new Authenticator(userController);
    private Scene scene;
    private static Main application;
    
    {
        try {
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
            
            String description1 = "Nikonin harrastajarunko hyödyntää tiedonsiirrossa uutta SnapBridge-teknologiaa, "
                    + "jonka avulla yhteys mobiililaitteeseen säilyy jatkuvasti vaikka kamera sammutetaan. Kuvien siirto "
                    + "kamerasta hoituu tarvittaessa koko ajan taustalla ja halutessaan kuvakansion voi mobiililaitteella "
                    + "synkronoida pilveen, jolloin web-kokoiset kuvat saa jaettua täysin automaattisesti ja lähes viipeettä "
                    + "vaikka toiselle puolelle maailmaa.";
            
            String description2 = "\"Uusi, vähemmän makea ja kevyempi Monster Energy Ultra on nollakalorinen, \"\n" +
"                    + \"mutta täyttä Monster Energyä. Unleash the Ultra Beast!";
            
            String description3 = "Samsung UE55RU7172 / UE55RU7179 / UE55RU7170 on "
                    + "Ultra HD -resoluutioinen televisio, jossa on HDR10 ja HLG -tuki.\n" +
"55-tuumaisessa televisiossa on teräväpiirtokanavia tukevat digivirittimet DVB-T2, DVB-C HD ja DVB-S2. Siinä on myös CI+-liitäntä "
                    + "maksu-tv-kanavia varten ja voit kiinnittää sen seinälle VESA-standardin mukaisella telineellä.";
                    
            addItem("kamera", 0.4, 400, "asdd");
            addItem("Monster Energy Ultra", 0.5, 1.5, "asd");
            addItem("Samsung UE55RU7172 55\" Smart 4K Ultra HD LED", 17.3, 450, "asd");
        } catch (ItemnameTakenException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        application = this;
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

    public static Main getApp(){
        if(application == null){
            application = new Main();
        }
        
        return application;
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
            gotoUI(loggedUser.getRole().getName() + "UI2");
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
        System.out.println("replaceSceneContent " + fxml);
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
        scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    
    public Node loadContent(String fxml) throws Exception {
        System.out.println("loadContent " + fxml);
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Node node;
        try {
            node = (Node)loader.load(in);
        } finally {
            in.close();
        } 
        //((FXMLController) loader.getController()).setApp(this);
        return node;
    }
    
    public Initializable loadController(String fxml) throws Exception {
        System.out.println("loadController " + fxml);
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        Node node;
        try {
            node = (Node)loader.load(in);
        } finally {
            in.close();
        } 
        //((FXMLController) loader.getController()).setApp(this);
        return loader.getController();
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
    
    public void modifyUser(User user) throws Exception{
        userController.edit(user);
    }
    
    public void removeUser(User user) throws NonexistentEntityException {
        userController.destroy(user.getId());
    }
    
    public void addUser(User user) throws NoSuchRoleException, UsernameTakenException{
        addUser(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getRole().getName());
    }
    
    public void addItem(String itemname, double weight, double price, String description) throws ItemnameTakenException{
        try{
            if(itemController.findUserWithName(itemname) != null){
                throw new ItemnameTakenException("Item name: " + itemname + " is taken.");
            } 
        }catch(NoResultException ex){
            
        }
        itemController.create(new Item(itemname, weight, price, description));
    }
    
    
    public void addItem(Item item) throws ItemnameTakenException{
        addItem(item.getItemname(), item.getWeight(), item.getPrice(), item.getDescription());
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
    
    public User[] getUsers(Predicate<User> predicate){
        List<User> matchedUsers = new ArrayList<>();
        
        for(User user : this.userController.findUserEntities()){
            if(predicate.test(user)){
                matchedUsers.add(user);
            }
        }
        
        return matchedUsers.toArray(new User[matchedUsers.size()]);
    }
    
    public Item[] getItems(Predicate<Item> predicate){
        List<Item> matchedItems = new ArrayList<>();
        
        for(Item item : this.itemController.findItemEntities()){
            if(predicate.test(item)){
                matchedItems.add(item);
            }
        }
        
        return matchedItems.toArray(new Item[matchedItems.size()]);
    }
}
