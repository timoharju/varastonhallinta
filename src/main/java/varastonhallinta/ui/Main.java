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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
import javax.persistence.Persistence;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.logic.LoginController;
import varastonhallinta.logic.ProfileController;
import varastonhallinta.logic.UiController;
import varastonhallinta.logic.UserJpaController;
import varastonhallinta.domain.User;
import varastonhallinta.domain.ValidationException;
import varastonhallinta.logic.JPAController;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.security.Authenticator;
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.util.HibernateUtil;
import varastonhallinta.util.Range;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application{
    private Stage stage;
    private User loggedUser;
    private final double MINIMUM_WINDOW_WIDTH = 390.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;
    private final String UI_PAGE = "/fxml/admin.fxml";
    private final String LOGIN_PAGE = "/fxml/login.fxml";
    private final String PROFILE_PAGE = "/profile.fxml";
    
    private EntityManagerFactory entityManagerFactory;
    private UserJpaController userController;
    private Authenticator authenticator;
    private Scene scene;
    private static Main application;
    private Map<Class<?>, JPAController<?>> controllerMap = new HashMap<>();
    private Locale locale;
    private static final Map<String, Locale> LOCALE_MAP = new HashMap<>();
    {
        LOCALE_MAP.put("Finnish", new Locale("fi"));
        LOCALE_MAP.put("English", new Locale("en"));
        
        Locale defaultLocale = Locale.getDefault();
        LOCALE_MAP.values().forEach(myLocale -> {
            if(defaultLocale.getLanguage().equals(myLocale.getLanguage())){
                setLocale(myLocale);
            }
        });
        if(getLocale() == null){
            setLocale(LOCALE_MAP.values().iterator().next());
        }
                
    }
    private static final String BUNDLE_NAME = "properties.UiBundle";
    
    private void configureControllers(EntityManagerFactory em){
        userController = new UserJpaController(em);
        authenticator = new Authenticator(userController);
        JPAController userController = new JPAControllerImpl(User.class, em);
        JPAController itemController = new JPAControllerImpl(Item.class, em);
        JPAController roleController = new JPAControllerImpl(Role.class, em);
        controllerMap.put(Item.class, itemController);
        controllerMap.put(User.class, userController);
        controllerMap.put(Role.class, roleController);
    }
    
    private static class JPAControllerImpl<E extends EntityClass> extends JPAController<E> {
        public JPAControllerImpl(Class<? extends E> classObject, EntityManagerFactory emf) {
            super(classObject, emf);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }
    
    public Main(){
        application = this;
        HibernateUtil.initDB();
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
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        System.out.println("setLocale " + locale.getLanguage());
        this.locale = locale;
    }

    /**
     * @return the entityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * @param entityManagerFactory the entityManagerFactory to set
     */
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        System.out.println("setEntityManagerFactory " + entityManagerFactory);
        this.entityManagerFactory = entityManagerFactory;
        configureControllers(entityManagerFactory);
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
//        System.out.println("userLogin " + username + " " + password);
//        System.out.println("authenticator.validate(username, password) " + authenticator.validate(username, password));
        if (authenticator.validate(username, password)) {
            loggedUser = userController.findUserWithName(username);
            this.gotoUI("AdminUI");
            return true;
        } else {
            return false;
        }
    }
    
    public int getBalance(Item item){
        return 3;
    }
    
    public String getStorageSpace(Item item){
        return "A45";
    }
    
    /**
     * Logs out the current logged in user and loads the login page.
     */
    public void userLogout(){
        loggedUser = null;
        //gotoLogin();
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
            LoginController loginController = (LoginController) replaceSceneContent(LOGIN_PAGE);
            loginController.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        System.out.println("replaceSceneContent " + fxml);
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(getResourceBundle());
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

    
    public Initializable loadController(String fxml) throws Exception {
        System.out.println("loadController " + fxml);
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(getResourceBundle());
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        try {
            loader.load(in);
        } finally {
            in.close();
        } 
        //((FXMLController) loader.getController()).setApp(this);
        return loader.getController();
    }
    
    private ResourceBundle getResourceBundle(){
        ResourceBundle rb = ResourceBundle.getBundle(BUNDLE_NAME, getLocale());
        System.out.println("rb " + rb);
        System.out.println("rb.getKeys() " + rb.getKeys());
        return ResourceBundle.getBundle(BUNDLE_NAME, getLocale());
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

    public <T extends EntityClass> void addEntity(T t) throws ValidationException, AddEntityException {
        ((JPAController<T>)controllerMap.get(t.getClass())).create(t);
    }

    public <T extends EntityClass> void removeEntity(T t) throws NonexistentEntityException {
        controllerMap.get(t.getClass()).destroy(t.getId());
    }

    public <T extends EntityClass> void update(T t) throws NonexistentEntityException, ValidationException{
        ((JPAController<T>)controllerMap.get(t.getClass())).edit(t);
    }

    public <T extends EntityClass> Collection<T> getEntities(Class<? extends T> c){
        List<T> matchedEntities = ((JPAController<T>)controllerMap.get(c)).findEntities();
        return matchedEntities;
    }
    
    public <T extends EntityClass> Collection<T> getEntities(Class<? extends T> c, Predicate<T> predicate){
        List<T> matchedEntities = new ArrayList<>();
        for(T entity : ((JPAController<T>)controllerMap.get(c)).findEntities()){
            System.out.println("\ntest item " + entity);
            if(predicate.test(entity)){
                System.out.println("item matches");
                matchedEntities.add(entity);
            }
        }
        return matchedEntities;
    }
    
    public String[] getRoleNames(){
        List<Role> roles = ((JPAController<Role>)controllerMap.get(Role.class)).findEntities();
        String[] roleNames = new String[roles.size()];
        for(int i=0; i<roles.size(); i++){
            roleNames[i] = roles.get(i).getName();
        }
        return roleNames;
    }
    
    public Map<String, Locale> getLocaleMap(){
        return Main.LOCALE_MAP;
    }
}
