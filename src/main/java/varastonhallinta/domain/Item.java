package varastonhallinta.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.*;

/**
 *
 * @author tanel
 */
@Entity
@Table(name = "ITEM")
public class Item extends EntityClass implements Serializable{
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;
    
    @ManyToOne()
    @JoinColumn(name = "ITEM_TYPE_ID" , referencedColumnName = "ID", nullable = false)
    private ItemType itemType;
    
    @ManyToOne()
    @JoinColumn(name = "SHIPMENT_ID" , referencedColumnName = "ID", nullable = false)
    private Shipment shipment;
    
    @ManyToOne()
    @JoinColumn(name = "ORDER_ID" , referencedColumnName = "ID", nullable = true)
    private Order order;
    
        
    /**
     *
     */
    public Item() {
    }

    public Item(String itemname, double weight, double price, String description) {

    }
    
 
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void validate() throws ValidationException {
        
    }

    @Override
    public String toString(){
        return "id: " + id + " itemType: " + itemType + " shipment: " + shipment + 
                " order: " + order;
    }

    /**
     * @return the itemType
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the shipment
     */
    public Shipment getShipment() {
        return shipment;
    }

    /**
     * @param shipment the shipment to set
     */
    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Order order) {
        this.order = order;
    }
}
