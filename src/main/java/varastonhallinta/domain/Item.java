package varastonhallinta.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author tanel
 */
@Entity
@Table(name = "ITEM")
@NamedQuery(
    name="findItemWithName",
    query="SELECT i FROM Item i WHERE i.itemname = :itemname"
)
public class Item implements Serializable{
	@Id
	@GeneratedValue
	@Column(name = "itemiid")
	private Integer itemId;
	@Column(name = "itenmane")
	private String itemname;
	@Column(name = "weight", columnDefinition = "TEXT(65000)")
	private double weight;
        @Column(name = "price")
	private double price;
        @Column(name = "description" )
	private String description;

    /**
     *
     */
    public Item() {
	}

    public Item(String itemname, double weight, double price, String description) {
        this.itemname = itemname;
        this.weight = weight;
        this.price = price;
        this.description = description;
    }
    
 

    /**
     *
     * @return
     */
    public int getItemid() {
		return itemId;
	}

    /**
     *
     * @param itemId
     */
    public void setItemid(int itemId) {
		this.itemId = itemId;
	}

    /**
     *
     * @return
     */
    public String getItemname() {
		return itemname;
	}

    /**
     *
     * @param itemname
     */
    public void setItemname(String itemname) {
		this.itemname = itemname;
	}

    /**
     *
     * @return
     */
    public double getWeight() {
		return weight;
	}

    /**
     *
     * @param weight
     */
    public void setWeight(double weight) {
		this.weight = weight;
	}

    /**
     * @return the price
     */
    
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the description
     */
    
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

        @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the itemId fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        return itemId + " " + itemname + " " + weight + " " + price + " " + description;
    }
}
