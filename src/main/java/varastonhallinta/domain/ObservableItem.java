/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import javafx.beans.value.ObservableValue;

/**
 *
 * @author tanel
 */
public interface ObservableItem extends IItem{
    public ObservableValue<String> idProperty();
    public ObservableValue<String> nameProperty();
    public ObservableValue<String> balanceProperty();
    public ObservableValue<String> priceProperty();
    public ObservableValue<String> storageSpaceProperty(); 
    public ObservableValue<String> weightProperty(); 
    public ObservableValue<String> descriptionProperty();
    
}
