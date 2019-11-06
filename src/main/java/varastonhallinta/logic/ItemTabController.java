    
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BiFunction;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.User;
import varastonhallinta.ui.EntityDialog;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.ui.exceptions.EntityException;
/**
 * Login Controller.
 */
public class ItemTabController extends TabController<Item>{

    @FXML
    private TextField itemIDField;
    
    @FXML
    private TextField itemnameField;
    
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
    
    
    private EntityDialog<Item> addItemDialog;
    private EntityDialog<Item> modifyItemDialog;
    private GridPane itemGrid;
    private ItemDialogController addItemController;
    private ItemDialogController modifyItemController;
    private static final String ADD_USER_GRID_LOCATION = "/fxml/ItemGrid.fxml";


    public ItemTabController() {
        super("Lisäys onnistui", "Lisäys epäonnistui", "Muokkaus onnistui", "Muokkaus epäonnistui", "Poisto onnistui", "Poisto epäonnistui");
    }
    

    public void configureDialogs(){
        System.out.println(this + " configureAddItemDialog");
        try {
            addItemController = (ItemDialogController) application.loadController(ADD_USER_GRID_LOCATION);
            modifyItemController = (ItemDialogController) application.loadController(ADD_USER_GRID_LOCATION);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Callback<ButtonType, Item> resultConverter = (dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if(data == null || data != ButtonBar.ButtonData.OK_DONE){
                return null;
            }
            return addItemController.getItem();
        };
        
        addItemDialog = new EntityDialog<>(addItemController.getGrid(), resultConverter);
        addItemDialog.resultProperty().addListener(
            (ObservableValue<? extends Item> observable, Item oldValue, Item newValue) -> {
                if(newValue != null){
                    super.create(newValue);
                }
        });
        
        modifyItemDialog = new EntityDialog<>(modifyItemController.getGrid(), resultConverter);
        modifyItemDialog.resultProperty().addListener(
            (ObservableValue<? extends Item> observable, Item oldValue, Item newValue) -> {
                if(newValue != null){
                    super.update(newValue);
                }
        });

    }
    
    private void configureFindItemTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemname.setCellValueFactory(new PropertyValueFactory<>("itemname"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        itemTable.setItems(tableContent);
        final ObservableList<Item> tableSelection = itemTable.getSelectionModel().getSelectedItems();

        tableSelection.addListener(tableSelectionChanged);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureDialogs();
        configureFindItemTable();
        configureFilters();
        super.initialize(location, resources);
    }
    
    private Map<CheckBox, Input<String>> inputMap = new HashMap<>();
    private Map<CheckBox, String> inputNameMap = new HashMap<>();
    private FilterFactory<Item> filterFactory;
    
    private void configureFilters(){
        filterFactory = new FilterFactory<>();
        
        filterFactory.addFilter(idBox, Input.from(itemIDField, Integer.class), (item) -> item.getID(),
                (inputID, id) -> id.equals(inputID));
        
        filterFactory.addFilter(nameBox, Input.from(itemnameField, String.class), (item) -> item.getItemname(),
               basicStringFilter);
        
        filterFactory.addFilter(balanceBox, Input.from(balanceLowerField, balanceHigherField), (item) -> application.getBalance(item),
                basicIntRangeFilter);
        
        filterFactory.addFilter(priceBox, Input.from(priceLowerField, priceHigherField), (item) -> item.getPrice(),
                basicDoubleRangeFilter);
        
        filterFactory.addFilter(weightBox, Input.from(weightLowerField, weightHigherField), (item) -> item.getWeight(),
                basicDoubleRangeFilter);
        
        filterFactory.addFilter(storageSpaceBox, Input.from(storageSpaceField, String.class), (item) -> application.getStorageSpace(item),
                basicStringFilter);
        
        inputMap.put(idBox, Input.from(itemIDField));
        inputMap.put(nameBox, Input.from(itemnameField));
        inputMap.put(balanceBox, Input.from(new TextField[]{balanceLowerField, balanceHigherField}));
        inputMap.put(priceBox, Input.from(new TextField[]{priceLowerField, priceHigherField}));
        inputMap.put(weightBox, Input.from(new TextField[]{weightLowerField, weightHigherField}));
        inputMap.put(storageSpaceBox, Input.from(storageSpaceField, String.class));
        
        inputNameMap.put(idBox, "tuote ID");
        inputNameMap.put(nameBox, "tuotenimi");
        inputNameMap.put(balanceBox, "saldo");
        inputNameMap.put(priceBox, "hinta");
        inputNameMap.put(weightBox, "paino");
        inputNameMap.put(storageSpaceBox, "varastopaikka");
    }
    
    private BiFunction<String, String, Boolean> basicStringFilter = (input, attribute) -> {
            input = input.toLowerCase();
            attribute = attribute.toLowerCase();
            return  input.contains(attribute) || attribute.contains(input);
    };
    
    private final BiFunction<Range, Double, Boolean> basicDoubleRangeFilter = (range, attribute) -> {
        return  range.isInRange(attribute);
    };
    
    private final BiFunction<Range, Integer, Boolean> basicIntRangeFilter = (range, attribute) -> {
        return  range.isInRange(attribute);
    };

    @Override
    protected Object getContent() {
        return content;
    }
    
    @Override
    public boolean canSearch() {
        for(CheckBox box : inputMap.keySet()){
            try {
                if(box.isSelected() && !inputMap.get(box).getInput().isEmpty()){
                    return true;
                }
            } catch (InputException ex) {
                Logger.getLogger(ItemTabController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    @Override
    public boolean canUpdate() {
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

    private void filterUsers(){
        Set<Item> items = new HashSet<>();
        for(CheckBox box : inputMap.keySet()){
            if(box.isSelected()){
                Collection<Item> temp;
                try {
                   temp = application.getEntities(Item.class, filterFactory.getFilter(box));
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
        this.itemTable.getItems().addAll(items);
    }
    
    private String getInputName(CheckBox checkBox){
        return inputNameMap.get(checkBox);
    }
    
    private void updateTable(){
        try {
            itemTable.getItems().setAll(application.<Item>getEntities(Item.class, user -> itemTable.getItems().contains(user)));
        } catch (EntityException ex) {
            Logger.getLogger(ItemTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void handleSearch() {
        itemTable.getItems().clear();
        filterUsers();
    }

    @Override
    public void handleModify() {
        modifyItemController.initFields(itemTable.getSelectionModel().getSelectedItem());
        this.modifyItemDialog.show();
    }

    @Override
    public void handleCreate() {
        addItemController.clearFields();
        this.addItemDialog.show();
        updateTable();
    }

    @Override
    public void handleDelete() {
        itemTable.getSelectionModel().getSelectedItems().forEach(item -> {
            delete(item);
        });
        updateTable();
    }

    @Override
    public boolean validate(Item item) throws AddEntityException{
        String name = item.getItemname();
        
        if(!validUsername(name)){
            throw new AddEntityException("Viallinen käyttäjänimi!");
        }

        return true;
    }  
    
    private boolean validUsername(String username){
        return true;
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

}

