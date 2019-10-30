/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

/**
 *
 * @author tanel
 */
public abstract class TabController extends FXMLController{


    private static TabController activeTabController; 
    
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
//                TabController.getActiveController().updateSearchButtonState();
//                TabController.getActiveController().updateDeleteButtonState();
//                TabController.getActiveController().updateModifyButtonState();
//                TabController.getActiveController().updateNewButtonState();
            }
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TabPaneController.onTabSet(getContent(), this::configureTab);
    }
    
    protected abstract Object getContent();

    /**
     * @return the activeTabController
     */
    public static TabController getActiveTabController() {
        return activeTabController;
    }

    /**
     * @param aActiveTabController the activeTabController to set
     */
    public static void setActiveTabController(TabController aActiveTabController) {
        activeTabController = aActiveTabController;
    }
    
}
