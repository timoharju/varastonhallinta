package varastonhallinta.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author tanel
 */
@Entity(name = "User")
@Table(name = "user")
@NamedQuery(
    name="findUserWithName",
    query="SELECT u FROM User u WHERE u.username = :username"
)
public class User implements Serializable {
   @Id @GeneratedValue
   @Column(name = "id")
   private int id;

   @Column(name = "username")
   private String username; 
   
   @Column(name = "password")
   private String password; 
   
   @Column(name = "firstname")
   private String firstName;
   
   @Column(name = "lastname")
   private String lastName;
   
   @ManyToOne()
   @JoinColumn(name = "role_id" , referencedColumnName = "id")
   private Role role;
   
    /**
     *
     */
    public User() {}
   
    /**
     *
     * @param username
     * @param password
     * @param role
     */
    public User(String username, String password, Role role) {
       this(username, password, "", "", role);
   }
    
    /**
     *
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param role
     */
    public User(String username, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
   }
   
    /**
     *
     * @return
     */
    public int getId() {
      return id;
   }
   
    /**
     *
     * @param id
     */
    public void setId( int id ) {
      this.id = id;
   }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password){
        this.password = password;
    }
    
    /**
     *
     * @return
     */
    public Role getRole(){
        return role;
    }

    /**
     *
     * @param role
     */
    public void setRole(Role role){
        this.role = role;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


