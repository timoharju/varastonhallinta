package varastonhallinta.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.*;

/**
 *
 * @author tanel
 */
@Entity
@Table(name = "ORDER")
public class Order extends EntityClass implements Serializable{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Temporal(TemporalType.DATE)
    private Date shippingDate;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private Collection<Item> itemCollection; 
  
    /**
     *
     */
    public Order() {
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
        return "id: " + id + " shippingDate: " + shippingDate + " itemCollection: " + itemCollection;
    }

    /**
     * @return the shippingDate
     */
    public Date getShippingDate() {
        return shippingDate;
    }

    /**
     * @param shippingDate the shippingDate to set
     */
    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    /**
     * @return the itemCollection
     */
    public Collection<Item> getItemCollection() {
        return itemCollection;
    }

    /**
     * @param itemCollection the itemCollection to set
     */
    public void setItemCollection(Collection<Item> itemCollection) {
        this.itemCollection = itemCollection;
    }
}
