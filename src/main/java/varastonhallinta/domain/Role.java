package varastonhallinta.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author tanel
 */
@Entity(name = "Role")
@Table(name = "role")
@NamedQuery(
    name="getRoleWithName",
    query="SELECT r FROM Role r WHERE r.name = :roleName"
)
public class Role extends EntityClass implements Serializable{
    private static final int ROLE_NAME_MIN_LENGTH = 3;
    private static final int ROLE_NAME_MAX_LENGTH = 20;

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name; 

    private static final Map<Predicate<Role>, String> map = new HashMap<>();

    static{
       map.put(role -> validRoleName(role.getName()), "name");
    }

    /**
     *
     */
    public Role() {}

    /**
     *
     * @param name
     */
    public Role(String name) {
       this.name = name;
    }
    
    public Role(Role other) {
       this.name = other.name;
    }
   
    /**
     *
     * @return
     */
   @Override
    public int getId() {
      return id;
   }
   
    /**
     *
     * @param id
     */
    @Override
    public void setId(Integer id ) {
      this.id = id;
   }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param username
     */
    public void setName(String username) {
        this.name = username;
    }
    
    public static boolean validRoleName(String roleName){
        String regex = "[a-zåäöA-ZÅÄÖ0-9]{" + ROLE_NAME_MIN_LENGTH + "," + ROLE_NAME_MAX_LENGTH + "}";
        return roleName != null && roleName.matches(regex);
    }

    @Override
    public void validate() throws ValidationException {
        this.testFields(map, this);
    }
    
        @Override
    public String toString(){
        return "id: " + id + " name: " + name;
    }
}

