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
import varastonhallinta.domain.ValidationException;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
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
    private Map<CheckBox, String> inputNameMap;
    private Map<CheckBox, Input<String>> inputMap;
    private FilterFactory<E> filterFactory;
    private TableView<E> entityTable;
    private Class<? extends E> classObject;
    
    private EntityDialog<E> createDialog;
    private EntityDialog<E> updateDialog;
    //private GridPane userGrid;
    private DialogController<E> createDialogController;
    private DialogController<E> updateDialogController;
    
    
    protected abstract Object getContent();
    
    private String addSuccessfull;
    private String addFail;
    private String updateSuccessfull;
    private String updateFail;
    private String removeSuccessfull;
    private String removeFail;

    
    private void configureDialogs(){
//TODO: Make it so that we dont have to null the dialog result value.
//Ex. By making it handle some wrapper class of the entity class that overrides 
//the equals method to depend on all of the different fields of the entity class.
//Ex2. By making the handleCreate and handleUpdate -methods use the showAndWait-method instead
        createDialogController.setOnPositiveResult(() -> createDialogController.getEntity());
        updateDialogController.setOnPositiveResult(() -> updateDialogController.updateEntity(entityTable.getSelectionModel().getSelectedItem()));
        createDialog.resultProperty().addListener((
        (ObservableValue<? extends E> observable, E oldValue, E newValue) -> {
            if(newValue == null){
                return;
            }
            System.out.println("\ncreateDialog NEW RESULT RECEIVED " + newValue + "\n");
            this.create(newValue);
            updateDialog.setResult(null);
        }));
        updateDialog.resultProperty().addListener(
        (ObservableValue<? extends E> observable, E oldValue, E newValue) -> {
            if(newValue == null){
                return;
            }
            System.out.println("\nupdateDialog NEW RESULT RECEIVED " + newValue + "\n");
            this.update(newValue);
            updateTable();
            updateDialog.setResult(null);
        });
    }
    
    protected void configureDialogController(TableView<E> entityTable, Class<? extends E> classObject, DialogController<E> createDialogController, DialogController<E> updateDialogController, FilterFactory<E> filterFactory, Map<CheckBox, String> inputNameMap, Map<CheckBox, Input<String>> inputMap, String addSuccessfull, String addFail, String updateSuccessfull, String updateFail, String removeSuccessfull, String removeFail){
        this.entityTable = entityTable;
        this.classObject = classObject;
        this.createDialog = createDialogController.getDialog();
        this.updateDialog = updateDialogController.getDialog();
        this.createDialogController = createDialogController;
        this.updateDialogController = updateDialogController;
        this.addSuccessfull = addSuccessfull;
        this.addFail = addFail;
        this.updateSuccessfull = updateSuccessfull;
        this.updateFail = updateFail;
        this.removeSuccessfull = removeSuccessfull;
        this.removeFail = removeFail;
        this.filterFactory = filterFactory;
        this.inputNameMap = inputNameMap;
        this.inputMap = inputMap;
        this.configureDialogs();
        TabPaneController.onTabSet(getContent(), this::configureTab);
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
    public void handleUpdate() {
        updateDialogController.initFields(entityTable.getSelectionModel().getSelectedItem());
        this.updateDialog.show();
    }

    @Override
    public void handleCreate() {
        createDialogController.clearFields();
        this.createDialog.show();
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
            application.addEntity(e);
        }catch(AddEntityException ex){
            application.showAlert(Alert.AlertType.ERROR, "Virhe", addFail);
            return;
        } catch (ValidationException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", "Viallinen " + ex.getInvalidFieldName());
            return;
        }
        application.showAlert(Alert.AlertType.CONFIRMATION, "Käsky onnistunut", addSuccessfull);
    }
    
    @Override
    public final void delete(E e){
        try{
            application.removeEntity(e);
        } catch (NonexistentEntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", removeFail);
            return;
        }

        application.showAlert(Alert.AlertType.CONFIRMATION, "Käsky onnistunut", removeSuccessfull);
    }
    
    @Override
    public void update(E e){
        System.out.println("TAB CONTROLLER UPDATE " + e);
        try{
            application.update(e);
        } catch (NonexistentEntityException ex) {
            application.showAlert(Alert.AlertType.ERROR, "Virhe", updateFail);
            return;
        } catch (ValidationException ex) {
            Logger.getLogger(TabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        application.showAlert(Alert.AlertType.CONFIRMATION, "Käsky onnistunut", updateSuccessfull);
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
