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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import varastonhallinta.domain.Item;
import varastonhallinta.logic.exceptions.InputException;

/**
 *
 * @author tanel
 */
public class ItemDialogController extends DialogController<Item>{
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
    
    @Override
    public void initFields(Item item){
        itemIDField.setText(Integer.toString(item.getId()));
        itemNameField.setText(item.getItemname());
        weightField.setText(Double.toString(item.getWeight()));
        priceField.setText(Double.toString(item.getPrice()));
        descriptionField.setText(item.getDescription());
    }
    
    @Override
    public void clearFields(){
        itemNameField.setText("");
        weightField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureDialogController(grid);
    }

    @Override
    public Item getEntity() {
        String name = ""; 
        double weight = 0; 
        double price = 0; 
        String description = ""; 
        try{
            name = Input.from(this.itemNameField, String.class).getInput(); 
            weight = Input.from(this.weightField, Double.class).getInput(); 
            price = Input.from(this.priceField, Double.class).getInput(); 
            description = Input.from(this.descriptionField, String.class).getInput(); 
        }catch(InputException e){
            
        }
        return new Item(name, weight, price, description);
    }

    @Override
    public Item updateEntity(Item item) {
        System.out.println("updateEntity " + item);
        String name = ""; 
        double weight = 0; 
        double price = 0; 
        String description = ""; 
        try{
            name = Input.from(this.itemNameField, String.class).getInput(); 
            weight = Input.from(this.weightField, Double.class).getInput(); 
            price = Input.from(this.priceField, Double.class).getInput(); 
            description = Input.from(this.descriptionField, String.class).getInput(); 
        }catch(InputException e){
            Logger.getLogger(ItemDialogController.class.getName()).log(Level.SEVERE, null, e.getMessage());
        }
        item.setItemname(name);
        item.setWeight(weight);
        item.setPrice(price);
        item.setDescription(description);
        return item;
    }
}
