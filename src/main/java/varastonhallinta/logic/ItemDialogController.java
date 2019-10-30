/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import varastonhallinta.domain.Item;

/**
 *
 * @author tanel
 */
public class ItemDialogController extends FXMLController{
    @FXML
    private TextField itemIDField;
    
    @FXML
    private TextField itemNameField;
    
    @FXML
    private TextField weightField;

    @FXML
    private TextField priceField;
    
    @FXML
    private TextArea descriptionField;
    
    @FXML
    private TextField storageSpaceField;
    
    @FXML
    private GridPane grid;

    /**
     * @return the grid
     */
    public GridPane getGrid() {
        return grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(GridPane grid) {
        this.grid = grid;
    }
    
    public Item getItem(){
        String itemname = this.itemNameField.getText();
        String weight = this.weightField.getText();
        String price = this.priceField.getText();
        String description = this.descriptionField.getText();
        String StorageSpace = this.storageSpaceField.getText();
            
        return new Item(itemname, 5, 5, StorageSpace);
    }
    
    public void initFields(Item item){
        itemNameField.setText(item.getItemname());
        weightField.setText(Double.toString(item.getWeight()));
        priceField.setText(Double.toString(item.getPrice()));
        descriptionField.setText(item.getDescription());
    }
    
    public void clearFields(){
        itemNameField.setText("");
        weightField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
    }
}
