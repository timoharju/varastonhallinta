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
public class Item extends EntityClass implements Serializable{
    private static final int ITEM_NAME_MIN_LENGTH = 1;
    private static final int ITEM_NAME_MAX_LENGTH = 60;
    private static final double MIN_WEIGHT = 0;
    private static final double MAX_WEIGHT = Double.MAX_VALUE;
    private static final double MIN_PRICE = 0;
    private static final double MAX_PRICE = Double.MAX_VALUE;
    private static final int DESCRIPTION_MIN_LENGTH = 0;
    private static final int DESCRIPTION_MAX_LENGTH = 65000;

    
    @Column(name = "itenmane")
    private String itemname;
    @Column(name = "weight")
    private double weight;
    @Column(name = "price")
    private double price;
    @Column(name = "description", columnDefinition = "TEXT(" + DESCRIPTION_MAX_LENGTH + ")")
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
    
    public Item(Item other) {
        this.itemname = other.itemname;
        this.weight = other.weight;
        this.price = other.price;
        this.description = other.description;
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
    public Integer getId() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    public static boolean validItemname(String itemname){
        String regex = "[\\S]+([ ]?[\\S]+)+";
        return itemname != null && itemname.matches(regex) && itemname.length() >= ITEM_NAME_MIN_LENGTH && itemname.length() <= ITEM_NAME_MAX_LENGTH;
    }
    
    public static boolean validItemWeight(double weight){
        return weight >= MIN_WEIGHT && weight <= MAX_WEIGHT;
    }
    
    public static boolean validItemPrice(double price){
        return price >= MIN_PRICE && price <= MAX_PRICE;
    }
        
    public static boolean validItemDescription(String description){
        String regex = "[\\x{" + Integer.toHexString(Character.MIN_CODE_POINT) + "}-\\x{" + (Integer.toHexString(Character.MAX_CODE_POINT)) + "}]{" + DESCRIPTION_MIN_LENGTH + "," + DESCRIPTION_MAX_LENGTH + "}";
        return description != null && description.matches(regex);
    }

    @Override
    public void validate() throws ValidationException {
        this.testFields(map, this);
    }

    @Override
    public String toString(){
        return "id: " + id + " itemname: " + itemname + " weight: " + weight + 
                " price: " + price + " description: " + description;
    }
}
