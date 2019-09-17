package varastonhallinta.domain;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {
   @Id @GeneratedValue
   @Column(name = "id")
   private int id;

   @Column(name = "username")
   private String username; 
   
   @Column(name = "password")
   private String password;  
   
   public User() {}
   
   public int getId() {
      return id;
   }
   
   public void setId( int id ) {
      this.id = id;
   }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
 
}


