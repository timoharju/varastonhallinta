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
@NamedQuery(
    name="findItemWithName",
    query="SELECT i FROM Item i WHERE i.itemname = :itemname"
)
public class Item extends EntityClass<Item> implements Serializable{
    private static final int ITEM_NAME_MIN_LENGTH = 3;
    private static final int ITEM_NAME_MAX_LENGTH = 20;
    private static final double MIN_WEIGHT = 0;
    private static final double MAX_WEIGHT = Double.MAX_VALUE;
    private static final double MIN_PRICE = 0;
    private static final double MAX_PRICE = Double.MAX_VALUE;
    private static final int DESCRIPTION_MIN_LENGTH = 1;
    private static final int DESCRIPTION_MAX_LENGTH = 30;
    
    @Column(name = "itenmane")
    private String itemname;
    @Column(name = "weight", columnDefinition = "TEXT(65000)")
    private double weight;
    @Column(name = "price")
    private double price;
    @Column(name = "description" )
    private String description;
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer id;

    private static final Map<Predicate<Item>, String> map = new HashMap<>();
   
   static{
       map.put(item -> validItemname(item.getItemname()), "itenmane");
       map.put(item -> validItemWeight(item.getWeight()), "weight");
       map.put(item -> validItemPrice(item.getPrice()), "price");
       map.put(item -> validItemDescription(item.getDescription()), "description");
   }
        
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
    public String toString(){
        return id + " " + itemname + " " + weight + " " + price + " " + description;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    public static boolean validItemname(String itemname){
        String regex = "[A-Za-zåÅäÄöÖ0-9_\\-]{" + ITEM_NAME_MIN_LENGTH + "," + ITEM_NAME_MAX_LENGTH + "}";
        return itemname != null && itemname.matches(regex);
    }
    
    public static boolean validItemWeight(double weight){
        return weight >= MIN_WEIGHT && weight >= MAX_WEIGHT;
    }
    
    public static boolean validItemPrice(double price){
        return price >= MIN_PRICE && price >= MAX_PRICE;
    }
        
    public static boolean validItemDescription(String description){
        String regex = "[.]{" + DESCRIPTION_MIN_LENGTH + "," + DESCRIPTION_MAX_LENGTH + "}";
        return description != null && description.matches(regex);
    }

    @Override
    public void validate() throws ValidationException {
        this.testFields(map, this);
    }

}
