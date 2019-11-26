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
@Table(name = "SHIPMENT")
public class Shipment extends EntityClass implements Serializable{  
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;
    
    @Temporal(TemporalType.DATE)
    private Date arrivalDate;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id")
    private Collection<Item> itemCollection; 
    
        
    /**
     *
     */
    public Shipment() {
    }

    public Shipment(Date arrivalDate) {
        this.arrivalDate = arrivalDate;

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
        return "id: " + id + " arrivalDate: " + arrivalDate + " itemCollection: " + itemCollection;
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
