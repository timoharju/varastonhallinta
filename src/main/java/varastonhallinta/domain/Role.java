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
public class Role extends EntityClass<Role> implements Serializable{
    private static final int ROLE_NAME_MIN_LENGTH = 3;
    private static final int ROLE_NAME_MAX_LENGTH = 20;
    
    @Id @GeneratedValue
   @Column(name = "id")
   private Integer id;

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
   
    /**
     *
     * @return
     */
   @Override
    public Integer getID() {
      return id;
   }
   
    /**
     *
     * @param id
     */
    @Override
    public void setID(Integer id ) {
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public static boolean validRoleName(String roleName){
        String regex = "[a-zåäö]{" + ROLE_NAME_MIN_LENGTH + "," + ROLE_NAME_MAX_LENGTH + "}";
        return roleName != null && roleName.matches(regex);
    }

    @Override
    public void validate() throws ValidationException {
        this.testFields(map, this);
    }
}

