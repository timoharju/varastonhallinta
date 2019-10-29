/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author tanel
 */
public class ItemView implements ObservableItem{
    private final SimpleStringProperty id;
    private SimpleStringProperty balance;
    private SimpleStringProperty price;
    private SimpleStringProperty name;
    private SimpleStringProperty storageSpace;
    private SimpleStringProperty weight;
    private SimpleStringProperty description;
    
    
    ItemView(String name, String id) {
        this(id, name, null, null);
    }
    ItemView(String id, String name, String weight, String price) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.weight = new SimpleStringProperty(weight);
        this.price = new SimpleStringProperty(price);
        this.storageSpace = new SimpleStringProperty("");
        this.balance = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
    }

    @Override
    public ObservableValue<String> idProperty() {
        return id;
    }

    @Override
    public ObservableValue<String> nameProperty() {
        return name;
    }

    @Override
    public ObservableValue<String> balanceProperty() {
        return balance;
    }

    @Override
    public ObservableValue<String> priceProperty() {
        return price;
    }

    @Override
    public ObservableValue<String> storageSpaceProperty() {
        return storageSpace;
    }

    @Override
    public ObservableValue<String> weightProperty() {
        return weight;
    }
    
    @Override
    public ObservableValue<String> descriptionProperty() {
        return description;
    }

    @Override
    public String getDescription() {
        return this.description.get();
    }

    @Override
    public String getItemid() {
        return this.id.get();
    }

    @Override
    public String getItemname() {
        return this.name.get();
    }

    @Override
    public String getPrice() {
        return this.price.get();
    }

    @Override
    public String getWeight() {
        return this.weight.get();
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(String balance) {
        this.balance.set(balance);
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price.set(price);
    }

    /**
     * @param storageSpace the storageSpace to set
     */
    public void setStorageSpace(String storageSpace) {
        this.storageSpace.set(storageSpace);
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(String weight) {
        this.weight.set(weight);
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description.set(description);
    }
}
