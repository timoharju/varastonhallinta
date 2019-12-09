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
import java.util.function.Supplier;
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
public abstract class DialogController<E extends EntityClass> extends FXMLController{

    /**
     * @return the dialog
     */
    public EntityDialog<E> getDialog() {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(EntityDialog<E> dialog) {
        this.dialog = dialog;
    }

    private EntityDialog<E> dialog;
    private Supplier<E> onPositiveResult;
    
    public abstract void initFields(E e);
    public abstract void clearFields();
    public abstract E getEntity();
    public abstract E updateEntity(E e);
    
    public void setOnPositiveResult(Supplier<E> r){
        this.onPositiveResult = r;
    }
    
    public Supplier<E> getOnPositiveResult(){
        return onPositiveResult;
    }
    
    protected void configureDialogController(GridPane grid){  
        Callback<ButtonType, E> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            System.out.println("onPositiveResult.get()");
            return onPositiveResult.get();
        };
        
        setDialog(new EntityDialog<>(grid, resultConverter));
    }
}
