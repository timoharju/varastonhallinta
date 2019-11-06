/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.ui.exceptions.AddUserException;
import varastonhallinta.ui.exceptions.EntityException;

/**
 *
 * @author tanel
 */
public abstract class FXMLController4 implements Initializable, Searchable, Modifiable, Creatable, Deletable{
    
    protected Main application;
    protected static final int USERNAME_MIN_LENGTH = 3;
    protected static final int USERNAME_MAX_LENGTH = 20;
    protected static final int PASSWORD_MIN_LENGTH = 6;
    protected static final int PASSWORD_MAX_LENGTH = 30;
    protected static final int FIRST_NAME_MIN_LENGTH = 1;
    protected static final int FIRST_NAME_MAX_LENGTH = 30;
    protected static final int LAST_NAME_MIN_LENGTH = 1;
    protected static final int LAST_NAME_MAX_LENGTH = 30;
    
    private static Button create;
    private static Button modify;
    private static Button search;
    private static Button delete;
    
    private static EntityDialog<Item> addItemDialog;
    private static FXMLController4 activeController;
    
    /**
     *
     * @param application
     */
    public final void setApp(Main application){
        this.application = application;
    }
    
    public FXMLController4(){
        setApp(Main.getApp());
    }
    
    public void tryAddUser(String username, String password, String firstName, String lastName, String role){
        System.out.println("tryAddUser " + username + " " + password + " " +  firstName + " " +  lastName + " " +  role);
        
        try{
            validateUser(username, password, firstName, lastName, role);
            firstName = formatName(firstName);
            lastName = formatName(lastName);
            application.addEntity(new User(username, password, firstName, lastName, new Role(role)));
        }catch(AddEntityException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        } catch (AddUserException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", username + " lisätty!");
    }
    
    public void tryDeleteUser(User user){
        try{
            application.removeEntity(user);
        } catch (EntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", user.getFirstName() + " poistettu!");
    }
    
    public void tryModifyUser(User user){
        try{
            validateUser(user);
            user.setFirstName(formatName(user.getFirstName()));
            user.setLastName(formatName(user.getLastName()));
            application.update(user);
        }catch(AddUserException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        } catch (EntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }
        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", user.getUsername() + " lisätty!");
    }
    
    private String formatName(String name){
        return name.substring(0,1).toUpperCase().concat(name.substring(1).toLowerCase());
    }
    
    public void validateUser(User user) throws AddUserException{
        String username = user.getUsername();
        String password = user.getPassword(); 
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String role = user.getRole().getName();
        validateUser(username, password, firstName, lastName, role);
    }
    
    public void validateUser(String username, String password, String firstName, String lastName, String role) throws AddUserException{
        if(!validUsername(username)){
            throw new AddUserException("Viallinen käyttäjänimi!");
        }
        
        if(!validPassword(password)){
            throw new AddUserException("Viallinen salasana!");
        }
        
        if(!firstName.isEmpty()){
            firstName = firstName.toLowerCase();
            if(!validFirstName(firstName)){
                throw new AddUserException("Viallinen etunimi!");
            }
        }
        
        if(!lastName.isEmpty()){
            lastName = lastName.toLowerCase();
            if(!validLastName(lastName)){
                throw new AddUserException("Viallinen sukunimi!");
            }
        }
        
        if(role == null || "".equals(role)){
            throw new AddUserException("Valitse rooli!");
        }
    }
    
    public void tryAddUser(User user){
        String username = user.getUsername();
        String password = user.getPassword(); 
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String role = user.getRole().getName();
        tryAddUser(username, password, firstName, lastName, role);
    }
    
    private boolean validUsername(String username){
        String regex = "[A-Za-zåÅäÄöÖ0-9_\\-]{" + USERNAME_MIN_LENGTH + "," + USERNAME_MAX_LENGTH + "}";
        return username != null && username.matches(regex);
    }
    
    private boolean validPassword(String password){
        String regex = "[^\n]{" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "}";
        return password != null && password.matches(regex);
    }
    
    private boolean validFirstName(String firstName){
        String regex = "[a-zåäö]{" + FIRST_NAME_MIN_LENGTH + "," + FIRST_NAME_MAX_LENGTH + "}";
        return firstName == null || firstName.matches(regex);
    }
        
    private boolean validLastName(String lastName){
        String regex = "[a-zåäö]{" + LAST_NAME_MIN_LENGTH + "," + LAST_NAME_MAX_LENGTH + "}";
        return lastName == null || lastName.matches(regex);
    }
    
    public void tryAddItem(Item item){
        
    }
    
    public void tryAddItem(String name){

    }

    /**
     * @return the addItemDialog
     */
    public EntityDialog<Item> getAddItemDialog() {
        return addItemDialog;
    }

    /**
     * @param aAddItemDialog the addItemDialog to set
     */
    public void setAddItemDialog(EntityDialog<Item> aAddItemDialog) {
        addItemDialog = aAddItemDialog;
    }
    
    private void configureButtons() {
        if (getCreate() != null) {
            getCreate().setDisable(true);
        }
        if (getModify() != null) {
            getModify().setDisable(true);
        }
        if (getSearch() != null) {
            getSearch().setDisable(true);
        }
        if (getDelete() != null) {
            getDelete().setDisable(true);
        }
    } 
    
    public void updateSearchButtonState() {
        boolean disable = true;
        if (getSearch() != null) {
            final boolean canSearch = this.canSearch();
            disable = !canSearch;
        }
        if (getSearch() != null) {
            getSearch().setDisable(disable);
        }
    }
    
    public void updateModifyButtonState() {
        boolean disable = true;
        if (getModify() != null) {
            final boolean canModify = this.canModify();
            disable = !canModify;
        }
        if (getSearch() != null) {
            getModify().setDisable(disable);
        }
    }
    
    public void updateNewButtonState() {
        boolean disable = true;
        if (getCreate() != null) {
            final boolean canCreate = this.canCreate();
            disable = !canCreate;
        }
        if (getCreate() != null) {
            getCreate().setDisable(disable);
        }
    }
    
    public void updateDeleteButtonState() {
        boolean disable = true;
        if (getDelete() != null) {
            final boolean canDelete = this.canDelete();
            disable = !canDelete;
        }
        if (getDelete() != null) {
            getDelete().setDisable(disable);
        }
    }

    /**
     * @return the activeController
     */
    public static FXMLController4 getActiveController() {
        return activeController;
    }

    /**
     * @param activeController
     */
    public static void setActiveController(FXMLController4 activeController) {
        System.out.println("Set active controller " + activeController);
        FXMLController4.activeController = activeController;
    }
    
    @Override
    public boolean canSearch() {
        return false;
    }

    
    @Override
    public boolean canModify() {
        return false;
    }

    @Override
    public boolean canCreate() {
        return false;
    }

    @Override
    public boolean canDelete() {
        return false;
    }
    
    @Override
    public void modify() {
        
    }

    @Override
    public void create() {
        
    }

    @Override
    public void delete() {
        
    }

    @Override
    public void search() {
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * @return the create
     */
    public static Button getCreate() {
        return create;
    }

    /**
     * @param aCreate the create to set
     */
    public static void setCreate(Button aCreate) {
        create = aCreate;
    }

    /**
     * @return the modify
     */
    public static Button getModify() {
        return modify;
    }

    /**
     * @param aModify the modify to set
     */
    public static void setModify(Button aModify) {
        modify = aModify;
    }

    /**
     * @return the search
     */
    public static Button getSearch() {
        return search;
    }

    /**
     * @param aSearch the search to set
     */
    public static void setSearch(Button aSearch) {
        search = aSearch;
    }

    /**
     * @return the delete
     */
    public static Button getDelete() {
        return delete;
    }

    /**
     * @param aDelete the delete to set
     */
    public static void setDelete(Button aDelete) {
        delete = aDelete;
    }
}
