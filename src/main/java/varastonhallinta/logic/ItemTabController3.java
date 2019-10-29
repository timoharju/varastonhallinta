    
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.User;
import varastonhallinta.ui.AddObjectDialog;
import varastonhallinta.ui.Main;
/**
 * Login Controller.
 */
public class ItemTabController3 extends TabController{

    @FXML
    private TextField itemIDField;
    
    @FXML
    private TextField balanceLowerField;
    
    @FXML
    private TextField balanceHigherField;
    
    @FXML
    private TextField priceHigherField;
    
    @FXML
    private TextField priceLowerField;
    
    @FXML
    private TextField weightHigherField;
    
    @FXML
    private TextField weightLowerField;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField storageSpaceField;
    
    @FXML
    private TextField weightField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private TableView itemTable;
    
    @FXML
    private CheckBox idBox;
    
    @FXML
    private CheckBox nameBox;
    
    @FXML
    private CheckBox balanceBox;
    
    @FXML
    private CheckBox priceBox;
    
    @FXML
    private CheckBox weightBox;
    
    @FXML
    private CheckBox storageSpaceBox;

    private static final String ITEM_GRID_LOCATION = "/fxml/ItemGrid.fxml";
    private GridPane itemGrid;
    private ItemDialogController dialogController;
    
    public void configureAddItemDialog(){
        System.out.println(this + " configureTestArea()");
        try {
            dialogController = (ItemDialogController) application.loadController(ITEM_GRID_LOCATION);
            itemGrid = dialogController.getGrid();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Callback<ButtonType, Item> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }

            String balance = this.balanceLowerField.getText();
            double price = Double.parseDouble(this.priceLowerField.getText());
            String name = this.nameField.getText();
            String space = this.storageSpaceField.getText();
            double weight = Double.parseDouble(this.weightField.getText());
            String description = this.descriptionField.getText();
            
            return new Item(name, weight, price, description);
        };
        
        this.setAddItemDialog(new AddObjectDialog<>(itemGrid, resultConverter));
        this.getAddItemDialog().resultProperty().addListener(
            (ObservableValue<? extends Item> observable, Item oldValue, Item newValue) -> {
                if(newValue != null){
                    tryAddItem(newValue);
                }
        });
    }
    
    List<CheckBox> checkBoxes = new ArrayList<>();
    Map<CheckBox, BooleanSupplier> valueMap = new HashMap<>();
    
    private void configureValueMap(){
        valueMap.put(idBox, this::validateIDField);
        valueMap.put(nameBox, this::validateNameField);
        valueMap.put(balanceBox, this::validateBalanceField);
        valueMap.put(priceBox, this::validatePriceField);
        valueMap.put(weightBox, this::validateWeightField);
        valueMap.put(storageSpaceBox, this::validateStorageSpaceField);
    }
    
    private boolean validateIDField(){
        return this.itemIDField != null && !itemIDField.getText().isEmpty();
    }
    
    private boolean validateNameField(){
        return nameBox != null && !nameBox.getText().isEmpty();
    }
    
    private boolean validateBalanceField(){
        if(balanceLowerField == null || balanceHigherField == null){
            return false;
        }
        double balanceLower = Double.NaN;
        double balanceHigher = Double.NaN;
        try{
            balanceLower = Double.parseDouble(balanceLowerField.getText());
            balanceHigher = Double.parseDouble(balanceHigherField.getText());
        }catch(NumberFormatException e){
            return false;
        }
        
        if(balanceLower > balanceHigher){
            return false;
        }
        
        return true;
    }
   
    
    private boolean validatePriceField(){
        if(priceLowerField == null || priceHigherField == null){
            return false;
        }
        double priceLower = Double.NaN;
        double priceHigher = Double.NaN;
        try{
            priceLower = Double.parseDouble(priceLowerField.getText());
            priceHigher = Double.parseDouble(priceHigherField.getText());
        }catch(NumberFormatException e){
            return false;
        }
        
        if(priceLower > priceHigher){
            return false;
        }
        
        return true;
    }
    
    private boolean validateWeightField(){
        if(weightLowerField == null || weightHigherField == null){
            return false;
        }
        double weightLower = Double.NaN;
        double weightHigher = Double.NaN;
        try{
            weightLower = Double.parseDouble(weightLowerField.getText());
            weightHigher = Double.parseDouble(weightHigherField.getText());
        }catch(NumberFormatException e){
            return false;
        }
        
        if(weightLower > weightHigher){
            return false;
        }
        
        return true;
    }
    
    
    private boolean validateStorageSpaceField(){
        return storageSpaceField != null && !idBox.getText().isEmpty();
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureAddItemDialog();
        configureValueMap();
        super.initialize(location, resources);
    }

    @Override
    public boolean canSearch() {
        for(CheckBox box : valueMap.keySet()){
            if(box.isArmed() && valueMap.get(box).getAsBoolean()){
                return true;
            }
        }
        
        return false;
    }

    
    @Override
    public boolean canModify() {
        return !this.itemTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    @Override
    public boolean canCreate() {
        return create != null;
    }

    @Override
    public boolean canDelete() {
        return !this.itemTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    @Override
    public void modify() {
        
    }

    @Override
    public void create() {
        
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    protected Object getContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
