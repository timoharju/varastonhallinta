/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.User;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.ui.AddObjectDialog;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.exceptions.AddUserException;

/**
 *
 * @author tanel
 */
public abstract class FXMLController implements Initializable, Searchable, Modifiable, Creatable, Deletable{
    
    protected Main application;
    protected static final int USERNAME_MIN_LENGTH = 3;
    protected static final int USERNAME_MAX_LENGTH = 20;
    protected static final int PASSWORD_MIN_LENGTH = 6;
    protected static final int PASSWORD_MAX_LENGTH = 30;
    protected static final int FIRST_NAME_MIN_LENGTH = 1;
    protected static final int FIRST_NAME_MAX_LENGTH = 30;
    protected static final int LAST_NAME_MIN_LENGTH = 1;
    protected static final int LAST_NAME_MAX_LENGTH = 30;
    
    @FXML
    protected Button create;
    @FXML
    protected Button modify;
    @FXML
    protected Button search;
    @FXML
    protected Button delete;
    
    private static AddObjectDialog<Item> addItemDialog;
    private static FXMLController activeController;
    
    /**
     *
     * @param application
     */
    public final void setApp(Main application){
        this.application = application;
    }
    
    public FXMLController(){
        setApp(Main.getApp());
    }
    
    public void tryAddUser(String username, String password, String firstName, String lastName, String role){
        System.out.println("tryAddUser " + username + " " + password + " " +  firstName + " " +  lastName + " " +  role);
        
        try{
            validateUser(username, password, firstName, lastName, role);
            firstName = formatName(firstName);
            lastName = formatName(lastName);
            application.addUser(username, password, firstName, lastName, role);
        }catch(AddUserException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", username + " lisätty!");
    }
    
    public void tryDeleteUser(User user){
        try{
            application.removeUser(user);
        }catch(NonexistentEntityException ex){
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
            application.modifyUser(user);
        }catch(AddUserException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        } catch (NonexistentEntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
        } catch (Exception ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
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
    public AddObjectDialog<Item> getAddItemDialog() {
        return addItemDialog;
    }

    /**
     * @param aAddItemDialog the addItemDialog to set
     */
    public void setAddItemDialog(AddObjectDialog<Item> aAddItemDialog) {
        addItemDialog = aAddItemDialog;
    }
    
    private void configureButtons() {
        if (create != null) {
            create.setDisable(true);
        }
        if (modify != null) {
            modify.setDisable(true);
        }
        if (search != null) {
            search.setDisable(true);
        }
        if (delete != null) {
            delete.setDisable(true);
        }
    } 
    
    public void updateSearchButtonState() {
        boolean disable = true;
        System.out.println("updateSearchButtonState " + search);
        if (search != null) {
            System.out.println("\nupdateSearchButtonState1 " + disable);
            final boolean canSearch = this.canSearch();
            disable = canSearch;
            System.out.println("updateSearchButtonState2 " + disable);
        }
        if (search != null) {
            search.setDisable(disable);
        }
    }
    
    public void updateModifyButtonState() {
        boolean disable = true;
        if (modify != null) {
            final boolean canModify = this.canModify();
            disable = canModify;
        }
        if (search != null) {
            modify.setDisable(disable);
        }
    }
    
    public void updateNewButtonState() {
        boolean disable = true;
        if (create != null) {
            final boolean canModify = this.canCreate();
            disable = canModify;
        }
        if (create != null) {
            create.setDisable(disable);
        }
    }
    
    public void updateDeleteButtonState() {
        boolean disable = true;
        if (delete != null) {
            final boolean canModify = this.canDelete();
            disable = canModify;
        }
        if (delete != null) {
            delete.setDisable(disable);
        }
    }

    /**
     * @return the activeController
     */
    public static FXMLController getActiveController() {
        return activeController;
    }

    /**
     * @param activeController
     */
    public static void setActiveController(FXMLController activeController) {
        System.out.println("Set active controller " + activeController);
        FXMLController.activeController = activeController;
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
}
