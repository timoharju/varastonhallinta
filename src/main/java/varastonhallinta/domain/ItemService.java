/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import javafx.collections.ObservableList;

/**
 *
 * @author tanel
 */
public interface ItemService {
    public ItemView getItem(String id);
    public ItemView createItem();
    public void deleteItem(String id);
    public void saveIssue(String balance, String price, String name, String storageSpace, String weight, String description);
}
