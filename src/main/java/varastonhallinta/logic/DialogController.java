/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import static varastonhallinta.logic.FXMLController.USERNAME_MIN_LENGTH;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.InfoPopup;
import varastonhallinta.ui.Main;

/**
 *
 * @author tanel
 */
public abstract class DialogController<E extends EntityClass<E>> extends FXMLController{
    private GridPane createGrid;
    private GridPane modifyGrid;
    
    private EntityDialog<E> addUserDialog;
    private EntityDialog<E> modifyUserDialog;
    
    private ChangeListener<? super E> createChangeListener;
    private ChangeListener<? super E> modifyChangeListener;
    private Collection<Input<?>> inputs = new ArrayList<>();
    
    public abstract void initFields(E e);
    public abstract void clearFields();
    
    public void configureDialogs(){  
        Callback<ButtonType, E> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            return getEntity();
        };
        
        addUserDialog = new EntityDialog<E>(createGrid, resultConverter);
        modifyUserDialog = new EntityDialog<E>(modifyGrid, resultConverter);
    }
    
    public abstract E getEntity();
    
    public void configureDialogController(Collection<Input<?>> inputs, GridPane createGrid, GridPane modifyGrid, EntityDialog<E> addUserDialog, EntityDialog<E> modifyUserDialog){
        this.inputs = inputs;
        this.createGrid = createGrid;
        this.modifyGrid = modifyGrid;
        this.addUserDialog = addUserDialog;
        this.modifyUserDialog = modifyUserDialog;
        
        configureDialogs();
    }

    /**
     * @return the createChangeListener
     */
    public ChangeListener<? super E> getCreateChangeListener() {
        return createChangeListener;
    }

    /**
     * @param createChangeListener the createChangeListener to set
     */
    public void setCreateChangeListener(ChangeListener<? super E> createChangeListener) {
        this.createChangeListener = createChangeListener;
        addUserDialog.resultProperty().addListener(getCreateChangeListener());
    }

    /**
     * @return the modifyChangeListener
     */
    public ChangeListener<? super E> getModifyChangeListener() {
        return modifyChangeListener;
    }

    /**
     * @param modifyChangeListener the modifyChangeListener to set
     */
    public void setModifyChangeListener(ChangeListener<? super E> modifyChangeListener) {
        this.modifyChangeListener = modifyChangeListener;
        modifyUserDialog.resultProperty().addListener(getModifyChangeListener());
    }
}
