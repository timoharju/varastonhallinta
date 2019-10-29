    
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import varastonhallinta.domain.Item;
import varastonhallinta.ui.AddObjectDialog;
import varastonhallinta.ui.Main;
/**
 * Login Controller.
 */
public class ItemTabController extends TabController{

    @FXML
    private TextField itemIDField;
    
    @FXML
    private TextField itemnameField;

    @FXML
    private TextField weightField;
    
    @FXML
    private TextField priceLowerField;
    
    @FXML
    private TextField priceHigherField;
    
    @FXML
    private TextField weightLowerField;
    
    @FXML
    private TextField weightHigherField;
    
    @FXML
    private TextField balanceLowerField;
    
    @FXML
    private TextField balanceHigherField;
    
    @FXML
    private TextField storageSpaceField;
    
    @FXML
    TableView<Item> itemTable;
    
    @FXML
    CheckBox idBox;
    
    @FXML
    CheckBox nameBox;
    
    @FXML
    CheckBox weightBox;
    
    @FXML
    CheckBox priceBox;
    
    @FXML
    CheckBox balanceBox;
    
    @FXML
    CheckBox storageSpaceBox;
    
    
    @FXML
    CheckBox roleBox;
    
    @FXML //  fx:id="colName"
    private TableColumn<Item, Integer> colID; // Value injected by FXMLLoader

    @FXML //  fx:id="colStatus"
    private TableColumn<Item, String> colItemname; // Value injected by FXMLLoader

    @FXML //  fx:id="colSynopsis"
    private TableColumn<Item, Double> colWeight; // Value injected by FXMLLoader
    
    @FXML //  fx:id="colStatus"
    private TableColumn<Item, Double> colPrice; // Value injected by FXMLLoader

    @FXML //  fx:id="colSynopsis"
    private TableColumn<Item, String> colStorageSpace; // Value injected by FXMLLoader
    
    @FXML //  fx:id="colSynopsis"
    private TableColumn<Item, Integer> colBalance; // Value injected by FXMLLoader
    
    @FXML //  fx:id="colSynopsis"
    private Object content; // Value injected by FXMLLoader

    
    final ObservableList<Item> tableContent = FXCollections.observableArrayList();
            // This listener listen to changes in the table widget selection and
    // update the DeleteIssue button state accordingly.
    private final ListChangeListener<Item> tableSelectionChanged =
            (ListChangeListener.Change<? extends Item> c) -> {
//                updateSearchButtonState();
//                updateDeleteButtonState();
//                updateModifyButtonState();
//                updateNewButtonState();
    };
    
    
    private AddObjectDialog<Item> addItemDialog;
    private AddObjectDialog<Item> modifyItemDialog;
    private GridPane itemGrid;
    private ItemDialogController dialogController;
    private static final String ADD_USER_GRID_LOCATION = "/fxml/ItemGrid.fxml";


    public void configureDialogs(){
        System.out.println(this + " configureAddItemDialog");
        try {
            dialogController = (ItemDialogController) application.loadController(ADD_USER_GRID_LOCATION);
            System.out.println("FUCK YOU gachiGASM");
            itemGrid = dialogController.getGrid();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Callback<ButtonType, Item> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            return dialogController.getItem();
        };
        
        addItemDialog = new AddObjectDialog<>(itemGrid, resultConverter);
        addItemDialog.resultProperty().addListener(
            (ObservableValue<? extends Item> observable, Item oldValue, Item newValue) -> {
                if(newValue != null){
                    tryAddItem(newValue);
                }
        });
        
//        modifyItemDialog = new AddObjectDialog<>(itemGrid, resultConverter);
//        addItemDialog.resultProperty().addListener(
//            (ObservableValue<? extends Item> observable, Item oldValue, Item newValue) -> {
//                if(newValue != null){
//                    tryModifyItem(newValue);
//                }
//        });
    }

    private void configureFindItemTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemname.setCellValueFactory(new PropertyValueFactory<>("itemname"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        itemTable.setItems(tableContent);
        final ObservableList<Item> tableSelection = itemTable.getSelectionModel().getSelectedItems();

        tableSelection.addListener(tableSelectionChanged);
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureDialogs();
        configureValueMap();
        configureFindItemTable();
        super.initialize(location, resources);
    }
    

    Map<CheckBox, Input> boxMap = new HashMap<>();
    Map<Object, Function<Item, String>> inputMap = new HashMap<>();
    
    private void configureValueMap(){
        
        boxMap.put(idBox, Input.from(itemIDField));
        boxMap.put(nameBox, Input.from(itemnameField));
        boxMap.put(balanceBox, Input.from(balanceLowerField, balanceHigherField));
        boxMap.put(priceBox, Input.from(priceLowerField, priceHigherField));
        boxMap.put(weightBox, Input.from(weightLowerField, weightHigherField));
        boxMap.put(storageSpaceBox, Input.from(storageSpaceField));
        
        inputMap.put(itemIDField, (item) -> "" + item.getItemid());
        inputMap.put(itemnameField, (item) -> item.getItemname());
        inputMap.put(balanceLowerField, (item) -> Double.toString(item.getWeight()));
        inputMap.put(balanceHigherField, (item) -> Double.toString(item.getPrice()));
        inputMap.put(priceLowerField, (item) -> Double.toString(item.getWeight()));
        inputMap.put(priceHigherField, (item) -> Double.toString(item.getPrice()));
        inputMap.put(weightLowerField, (item) -> Double.toString(item.getWeight()));
        inputMap.put(weightHigherField, (item) -> Double.toString(item.getPrice()));
        inputMap.put(storageSpaceField, (item) -> Double.toString(item.getPrice()));
    }

    @Override
    protected Object getContent() {
        return content;
    }
    
    private interface Input{
        public String getString();
        
        public static Input from(TextField textField){
            return () -> textField.getText();
        }
        
        public static Input from(ComboBox<String> textField){
            return () -> textField.getValue();
        }
        
        public static Input from(TextField textFieldLower, TextField textFieldHigher){
            return () -> textFieldLower.getText() + textFieldHigher.getText();
        }
        
    }

    @Override
    public boolean canSearch() {
        for(CheckBox box : boxMap.keySet()){
            if(box.isSelected() && !boxMap.get(box).getString().isEmpty()){
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
        return true;
    }

    @Override
    public boolean canDelete() {
        return !this.itemTable.getSelectionModel().getSelectedCells().isEmpty();
    }

    private Predicate<Item> simpleFilter(Input input){
        return item -> {
            String string = input.getString();
            return inputMap.get(input).apply(item).contains(string);
        };
    }
    
    private void filterItems(){
        Set<Item> items = new HashSet<>();
        for(CheckBox box : boxMap.keySet()){
            if(box.isArmed()){
                items.retainAll(Arrays.asList(this.application.getItems(simpleFilter(boxMap.get(box)))));
                if(items.isEmpty()){
                    return;
                }
            }
        }
        this.itemTable.getItems().addAll(items);
    }
    
    @Override
    public void search() {
        filterItems();
    }
    
    @Override
    public void modify() {
        dialogController.initFields(itemTable.getSelectionModel().getSelectedItem());
        this.modifyItemDialog.show();
    }

    @Override
    public void create() {
        dialogController.clearFields();
        this.addItemDialog.show();
    }

    @Override
    public void delete() {
        //itemTable.getSelectionModel().getSelectedItems().forEach(item -> tryDeleteItem(item));
    }
}

