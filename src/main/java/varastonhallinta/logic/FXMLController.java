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
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.ItemType;
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
public abstract class FXMLController implements Initializable{
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
    
    private static ButtonHandler activeController;
    private String addSuccessfull;
    private String addFail;
    private String updateSuccessfull;
    private String updateFail;
    private String removeSuccessfull;
    private String removeFail;
    /**
     *
     * @param application
     */
    public final void setApp(Main application){
        this.application = application;
    }

    {
        setApp(Main.getApp());
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
    
    
    /**
     * @return the activeController
     */
    public static ButtonHandler getActiveController() {
        return activeController;
    }

    /**
     * @param activeController
     */
    public static void setActiveController(ButtonHandler bh) {
        System.out.println("Set active controller " + activeController);
        activeController = bh;
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
