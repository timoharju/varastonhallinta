/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

/**
 *
 * @author tanel
 */
public class TabPaneController extends FXMLController{
    @FXML
    private Tab userTab;
    @FXML
    private Tab itemTab;
    
    private static Map<Object, Tab> tabMap = new HashMap<>();
    private static Map<Object, Consumer<Tab>> tabConsumerMap = new HashMap<>();
    
    
    public static void onTabSet(Object content, Consumer<Tab> tabConsumer){
        tabConsumerMap.put(content, tabConsumer);
    }
    
    private void configureTabs(){
        setTab(userTab.getContent(), userTab);
        setTab(itemTab.getContent(), itemTab);
    }
    
    private static void setTab(Object content, Tab tab){
        tabMap.put(content, tab);
        tabConsumerMap.get(content).accept(tab);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("userTab " + userTab);
//        System.out.println("itemTab " + itemTab);
//        System.out.println("TabPane content " + userTab.getContent());
//        System.out.println("TabPane content " + itemTab.getContent());
        configureTabs();
    }
    

}
