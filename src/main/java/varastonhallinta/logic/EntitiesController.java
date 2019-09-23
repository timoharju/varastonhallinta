package varastonhallinta.logic;

import java.util.List; 
import java.util.Iterator; 
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;

import varastonhallinta.domain.User;
import varastonhallinta.util.HibernateUtil;

public class EntitiesController { 
   
   public EntitiesController(){}
   
   /* Method to CREATE an user in the database */
   public Integer addUser(String username, String password){
      Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = null;
      Integer userID = null;
      
      try {
         tx = session.beginTransaction();
         User user = new User();
         user.setUsername(username);
         user.setPassword(password);
         userID = (Integer) session.save(user); 
         tx.commit();
      } catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      } finally {
         session.close(); 
      }
      return userID;
   }
   
   /* Method to  READ all the users */
   public void listUsers( ){
      Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = null;
      
      try {
         tx = session.beginTransaction();
         List<?> users = session.createQuery("FROM Employee").list(); 
         for (Iterator<?> iterator = users.iterator(); iterator.hasNext();){
            User user = (User) iterator.next(); 
            System.out.print("username: " + user.getUsername()); 
            System.out.print("  password:  "  + user.getPassword()); 
         }
         tx.commit();
      } catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      } finally {
         session.close(); 
      }
   }
//   
//   /* Method to UPDATE salary for an employee */
//   public void updateEmployee(Integer EmployeeID, int salary ){
//      Session session = factory.openSession();
//      Transaction tx = null;
//      
//      try {
//         tx = session.beginTransaction();
//         Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
//         employee.setSalary( salary );
//		 session.update(employee); 
//         tx.commit();
//      } catch (HibernateException e) {
//         if (tx!=null) tx.rollback();
//         e.printStackTrace(); 
//      } finally {
//         session.close(); 
//      }
//   }
//   
//   /* Method to DELETE an employee from the records */
//   public void deleteEmployee(Integer EmployeeID){
//      Session session = factory.openSession();
//      Transaction tx = null;
//      
//      try {
//         tx = session.beginTransaction();
//         Employee employee = (Employee)session.get(Employee.class, EmployeeID); 
//         session.delete(employee); 
//         tx.commit();
//      } catch (HibernateException e) {
//         if (tx!=null) tx.rollback();
//         e.printStackTrace(); 
//      } finally {
//         session.close(); 
//      }
//   }
}

