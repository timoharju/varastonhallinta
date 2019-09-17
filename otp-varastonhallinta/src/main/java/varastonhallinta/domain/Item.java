package varastonhallinta.domain;

import javax.persistence.*;

@Entity
@Table(name = "ITEM")
public class Item {
	@Id
	@GeneratedValue
	@Column(name = "itemiid")
	private int itemid;
	@Column(name = "itenmane")
	private String itemname;
	@Column(name = "weight")
	private double weight;

	public Item() {
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
