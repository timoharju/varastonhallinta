package varastonhallinta.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author tanel
 */
@Entity
@Table(name = "ITEM")
public class Item implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "itemiid")
	private int itemid;
	@Column(name = "itenmane")
	private String itemname;
	@Column(name = "weight")
	private double weight;

    /**
     *
     */
    public Item() {
	}

    /**
     *
     * @return
     */
    public int getItemid() {
		return itemid;
	}

    /**
     *
     * @param itemid
     */
    public void setItemid(int itemid) {
		this.itemid = itemid;
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

}
