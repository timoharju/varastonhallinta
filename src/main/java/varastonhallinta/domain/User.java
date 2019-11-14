package varastonhallinta.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.*;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tanel
 */
@Entity(name = "User")
@Table(name = "user",
    uniqueConstraints=
        @UniqueConstraint(columnNames={"id", "username"}))
@NamedQuery(
    name="findUserWithName",
    query="SELECT u FROM User u WHERE u.username = :username"
)
public class User extends EntityClass implements Serializable {
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int PASSWORD_MIN_LENGTH = 5;
    private static final int PASSWORD_MAX_LENGTH = 30;
    private static final int FIRST_NAME_MIN_LENGTH = 1;
    private static final int FIRST_NAME_MAX_LENGTH = 30;
    private static final int LAST_NAME_MIN_LENGTH = 1;
    private static final int LAST_NAME_MAX_LENGTH = 30;
    
    @Id @GeneratedValue
   @Column(name = "id")
   private Integer id;

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
   
   private static final Map<Predicate<User>, String> map = new HashMap<>();
   
   static{
       map.put(user -> User.validUsername(user.getUsername()), "username");
       map.put(user -> User.validPassword(user.getPassword()), "password");
       map.put(user -> User.validLastName(user.getFirstName()), "firstname");
       map.put(user -> User.validFirstName(user.getLastName()), "lastname");
       map.put(user -> User.validLRole(user.getRole()), "role");
   }
    /**
     *
     */
    public User() {
    }
   
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
    
    public static boolean validUsername(String username){
        String regex = "[A-Za-zåÅäÄöÖ0-9_\\-]{" + USERNAME_MIN_LENGTH + "," + USERNAME_MAX_LENGTH + "}";
        return username != null && username.matches(regex);
    }
    
    public static boolean validPassword(String password){
        String regex = "[^\n]{" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "}";
        return password != null && password.matches(regex);
    }
    
    public static boolean validFirstName(String firstName){
        String regex = "[a-zåäö]{" + FIRST_NAME_MIN_LENGTH + "," + FIRST_NAME_MAX_LENGTH + "}";
        return "".equals(firstName) || firstName.matches(regex);
    }
        
    public static boolean validLastName(String lastName){
        String regex = "[a-zåäö]{" + LAST_NAME_MIN_LENGTH + "," + LAST_NAME_MAX_LENGTH + "}";
        return "".equals(lastName) || lastName.matches(regex);
    }
    
    public static boolean validLRole(Role role){
        try {
            role.validate();
        } catch (ValidationException ex) {
            return false;
        }
        return true;
    }
    
    @Override
    public void validate() throws ValidationException{
        this.testFields(map, this);
    }

    @Override
    public String toString(){
        return "id: " + id + " username: " + username + " password: " + password + 
                " firstName: " + firstName + " lastName: " + lastName + " role: " + role;
    }
                
}


