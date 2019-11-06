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
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.exceptions.AddEntityException;

/**
 *
 * @author tanel
 */
public abstract class FXMLController1<T> implements Initializable, Searchable, Modifiable, Creatable, Deletable{
    
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
    
    private EntityDialog<T> addEntityDialog;
    private static FXMLController1 activeController;
    
    /**
     *
     * @param application
     */
    public final void setApp(Main application){
        this.application = application;
    }
    
    public FXMLController1(){
        setApp(Main.getApp());
    }
    
    public void tryAddEntity(String username, String password, String firstName, String lastName, String role){
        System.out.println("tryAddEntity " + username + " " + password + " " +  firstName + " " +  lastName + " " +  role);
        
        try{
            validateEntity(username, password, firstName, lastName, role);
            firstName = formatName(firstName);
            lastName = formatName(lastName);
            application.addEntity(username, password, firstName, lastName, role);
        }catch(AddEntityException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", username + " lisätty!");
    }
    
    public void tryDeleteEntity(Entity user){
        try{
            application.removeEntity(user);
        }catch(NonexistentEntityException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", user.getFirstName() + " poistettu!");
    }
    
    public void tryModifyEntity(Entity user){
        try{
            validateEntity(user);
            user.setFirstName(formatName(user.getFirstName()));
            user.setLastName(formatName(user.getLastName()));
            application.modifyEntity(user);
        }catch(AddEntityException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
            return;
        } catch (NonexistentEntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
        } catch (Exception ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", ex.getMessage());
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käyttäjä ", user.getEntityname() + " lisätty!");
    }
    
    private String formatName(String name){
        return name.substring(0,1).toUpperCase().concat(name.substring(1).toLowerCase());
    }
    
    public void validateEntity(Entity user) throws AddEntityException{
        String username = user.getEntityname();
        String password = user.getPassword(); 
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String role = user.getRole().getName();
        validateEntity(username, password, firstName, lastName, role);
    }
    
    public void validateEntity(String username, String password, String firstName, String lastName, String role) throws AddEntityException{
        if(!validEntityname(username)){
            throw new AddEntityException("Viallinen käyttäjänimi!");
        }
        
        if(!validPassword(password)){
            throw new AddEntityException("Viallinen salasana!");
        }
        
        if(!firstName.isEmpty()){
            firstName = firstName.toLowerCase();
            if(!validFirstName(firstName)){
                throw new AddEntityException("Viallinen etunimi!");
            }
        }
        
        if(!lastName.isEmpty()){
            lastName = lastName.toLowerCase();
            if(!validLastName(lastName)){
                throw new AddEntityException("Viallinen sukunimi!");
            }
        }
        
        if(role == null || "".equals(role)){
            throw new AddEntityException("Valitse rooli!");
        }
    }
    
    public void tryAddEntity(Entity user){
        String username = user.getEntityname();
        String password = user.getPassword(); 
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String role = user.getRole().getName();
        tryAddEntity(username, password, firstName, lastName, role);
    }
    
    private boolean validEntityname(String username){
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
    
    public void tryAddEntity(Entity item){
        
    }
    
    public void tryAddEntity(String name){

    }

    /**
     * @return the addEntityDialog
     */
    public EntityDialog<Entity> getAddEntityDialog() {
        return addEntityDialog;
    }

    /**
     * @param aAddEntityDialog the addEntityDialog to set
     */
    public void setAddEntityDialog(EntityDialog<Entity> aAddEntityDialog) {
        addEntityDialog = aAddEntityDialog;
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
    public static FXMLController1 getActiveController() {
        return activeController;
    }

    /**
     * @param activeController
     */
    public static void setActiveController(FXMLController1 activeController) {
        System.out.println("Set active controller " + activeController);
        FXMLController1.activeController = activeController;
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
