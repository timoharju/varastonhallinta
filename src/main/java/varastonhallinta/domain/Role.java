package varastonhallinta.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name = "Role")
@Table(name = "role")
@NamedQuery(
    name="getRoleWithName",
    query="SELECT r FROM Role r WHERE r.name = :roleName"
)
public class Role implements Serializable{
   @Id @GeneratedValue
   @Column(name = "id")
   private int id;

   @Column(name = "name")
   private String name; 

   public Role() {}
   public Role(String name) {
       this.name = name;
   }
   
   public int getId() {
      return id;
   }
   
   public void setId( int id ) {
      this.id = id;
   }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }
}
