/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.User;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.ui.exceptions.EntityException;

/**
 *
 * @author tanel
 * @param <E>
 */
public abstract class TabController <E extends EntityClass<E>> extends FXMLController implements ButtonController<E>, ButtonHandler{
    private static TabController activeTabController; 
    private Map<CheckBox, String> inputNameMap = new HashMap<>();
    private Map<CheckBox, Input<String>> inputMap = new HashMap<>();
    private FilterFactory<E> filterFactory;
    private TableView<E> entityTable;
    private Class<? extends E> classObject;
    
    private EntityDialog<E> addUserDialog;
    private EntityDialog<E> modifyUserDialog;
    //private GridPane userGrid;
    private UserDialogController addUserController;
    private UserDialogController modifyUserController;
    
    
    protected abstract Object getContent();
    
    private String addSuccessfull;
    private String addFail;
    private String updateSuccessfull;
    private String updateFail;
    private String removeSuccessfull;
    private String removeFail;
    
    public TabController(String addSuccessfull, String addFail, String updateSuccessfull, String updateFail, String removeSuccessfull, String removeFail) {
        this.addSuccessfull = addSuccessfull;
        this.addFail = addFail;
        this.updateSuccessfull = updateSuccessfull;
        this.updateFail = updateFail;
        this.removeSuccessfull = removeSuccessfull;
        this.removeFail = removeFail;
    }

    private void configureTab(Tab tab){
        System.out.println("configureTab " + tab);
        if(tab.isSelected()){
            TabController.setActiveController(this);
        }
        tab.selectedProperty().addListener(
        (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            System.out.println("SELECTION CHANGED");
            if(newValue == true){
                System.out.println("TAB SELECTED");
                TabController.setActiveController(this);
//                updateSearchButtonState();
//                updateDeleteButtonState();
//                updateModifyButtonState();
//                updateNewButtonState();
            }
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TabPaneController.onTabSet(getContent(), this::configureTab);
    }
    
    public abstract boolean validate(E e) throws AddEntityException;
    
    @Override
    public void search() {
        this.handleSearch();
    }
    
        
    @Override
    public boolean canSearch() {
        for(CheckBox box : inputMap.keySet()){
            try {
                if(box.isSelected() && !inputMap.get(box).getInput().isEmpty()){
                    return true;
                }
            } catch (InputException ex) {
                Logger.getLogger(UserTabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    @Override
    public boolean canUpdate() {
        return !this.entityTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    @Override
    public boolean canCreate() {
        return true;
    }

    @Override
    public boolean canDelete() {
        return !this.entityTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    private void filterEntities(){
        Set<E> items = new HashSet<>();
        for(CheckBox box : inputMap.keySet()){
            if(box.isSelected()){
                Collection<E> temp;
                try {
                   temp = application.getEntities(classObject, filterFactory.getFilter(box));
                } catch (InputException ex) {
                    ex.setObject(getInputName(box));
                    application.showAlert(Alert.AlertType.ERROR, "Error", ex.getHRMessage());
                    return;
                } catch (EntityException ex) {
                    application.showAlert(Alert.AlertType.ERROR, "Error", "");
                    return;
                }
                if(items.isEmpty()){
                    items.addAll(temp);
                }else{
                    items.retainAll(temp);
                }
                if(items.isEmpty()){
                    return;
                }
            }
        }
        this.entityTable.getItems().addAll(items);
    }
    
    private String getInputName(CheckBox checkBox){
        return inputNameMap.get(checkBox);
    }
    
    private void updateTable(){
        try {
            entityTable.getItems().setAll(application.getEntities(classObject, user -> entityTable.getItems().contains(user)));
        } catch (EntityException ex) {
            Logger.getLogger(UserTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void handleSearch() {
        entityTable.getItems().clear();
        filterEntities();
    }

    @Override
    public void handleModify() {
        modifyUserController.initFields(entityTable.getSelectionModel().getSelectedItem());
        this.modifyUserDialog.show();
    }

    @Override
    public void handleCreate() {
        addUserController.clearFields();
        this.addUserDialog.show();
        updateTable();
    }

    @Override
    public void handleDelete() {
        entityTable.getSelectionModel().getSelectedItems().forEach(user -> {
            delete(user);
        });
        updateTable();
    }
    
    @Override
    public final void create(E e){
        try{
            validate(e);
            application.addEntity(e);
        }catch(AddEntityException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", addFail);
            return;
        }
        application.showAlert(Alert.AlertType.CONFIRMATION, "Käsky onnistunut", addSuccessfull);
    }
    
    @Override
    public final void delete(E e){
        try{
            application.removeEntity(e);
        } catch (EntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", removeFail);
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käsky onnistunut ", removeSuccessfull);
    }
    
    @Override
    public void update(E e){
        try{
            validate(e);
            application.update(e);
        }catch (EntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", updateFail);
            return;
        } catch (AddEntityException ex) {
                Logger.getLogger(TabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        application.showAlert(Alert.AlertType.CONFIRMATION, "Käsky onnistunut ", updateFail);
    }
  
    
    @Override
    public boolean canSearch() {
        return false;
    }

    
    @Override
    public boolean canUpdate() {
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
            final boolean canModify = this.canUpdate();
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
}
