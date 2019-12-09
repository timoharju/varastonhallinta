package varastonhallinta.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;

import varastonhallinta.domain.User;
import varastonhallinta.domain.ValidationException;
import varastonhallinta.logic.JPAController;
import varastonhallinta.logic.UserJpaController;
import varastonhallinta.security.Authenticator;
import varastonhallinta.ui.Main;
import varastonhallinta.ui.exceptions.AddEntityException;
 
/**
 *
 * @author tanel
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration()
            		.configure()
            		.addAnnotatedClass(User.class)
            		.buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    /**
     *
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    /**
     *
     */
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
 
        private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("varastonhallinta");
    
//    private UserJpaController userController = new UserJpaController(entityManagerFactory);
//    private ItemJpaController itemController = new ItemJpaController(entityManagerFactory);
//    private RoleJpaController roleController = new RoleJpaController(entityManagerFactory);
    
    public static void initDB(){
        System.out.println("InitDB");
        Main main = Main.getApp();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("varastonhallinta");
        main.setEntityManagerFactory(emf);
        EntityManager em = emf.createEntityManager();
        try {

            //em.createNativeQuery(sqlString);
            
            Role adR = new Role("Admin");
            Role vmR = new Role("Varastomies");
            Role tpR = new Role("TuotePäällikkö");
            Role asR = new Role("Asiakas");
            for(Role role : Arrays.asList(adR, vmR, tpR, asR)){
                em.getTransaction().begin();
                em.persist(role);
                em.getTransaction().commit();
            }
            User ad = new User("admin", "admin", "jorma", "jormala", adR);
            User vm = new User("varastomies", "varastomies", "seppo", "seppola", vmR);
            User tp = new User("tuotePäällikkö", "tuotePäällikkö", "kalle", "kallela", tpR);
            User as = new User("asiakas", "asiakas", "liisa", "liisala", asR);
            for(User user : Arrays.asList(ad, vm, tp, as)){
                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();
            }
            
            String description1 = "Nikonin harrastajarunko hyödyntää tiedonsiirrossa uutta SnapBridge-teknologiaa, "
                    + "jonka avulla yhteys mobiililaitteeseen säilyy jatkuvasti vaikka kamera sammutetaan. Kuvien siirto "
                    + "kamerasta hoituu tarvittaessa koko ajan taustalla ja halutessaan kuvakansion voi mobiililaitteella "
                    + "synkronoida pilveen, jolloin web-kokoiset kuvat saa jaettua täysin automaattisesti ja lähes viipeettä "
                    + "vaikka toiselle puolelle maailmaa.";
            
            String description2 = "\"Uusi, vähemmän makea ja kevyempi Monster Energy Ultra on nollakalorinen, \"\n" +
"                    + \"mutta täyttä Monster Energyä. Unleash the Ultra Beast!";
            
            String description3 = "Samsung UE55RU7172 / UE55RU7179 / UE55RU7170 on "
                    + "Ultra HD -resoluutioinen televisio, jossa on HDR10 ja HLG -tuki.\n" +
"55-tuumaisessa televisiossa on teräväpiirtokanavia tukevat digivirittimet DVB-T2, DVB-C HD ja DVB-S2. Siinä on myös CI+-liitäntä "
                    + "maksu-tv-kanavia varten ja voit kiinnittää sen seinälle VESA-standardin mukaisella telineellä.";
                    
            Item item1 = new Item("kamera", 0.4, 400, description1);
            Item item2 = new Item("Monster Energy Ultra", 0.5, 1.5, description2);
            Item item3 = new Item("Samsung UE55RU7172 55\" Smart 4K Ultra HD LED", 17.3, 450, description3);
        
            for(Item item : Arrays.asList(item1, item2, item3)){
                em.getTransaction().begin();
                em.persist(item);
                em.getTransaction().commit();
            }
        
        }catch (Exception ex){
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
//    
//    @Resource(lookup="java:comp/DefaultDataSource")	
//    private DataSource dataSource;
//
//    @Inject
//    private Pbkdf2PasswordHash passwordHash;
//    
//    @PostConstruct
//    public void init() {
//        
//        Map<String, String> parameters= new HashMap<>();
//        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
//        passwordHash.initialize(parameters);
//
//        executeUpdate(dataSource, "CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(255))");
//        executeUpdate(dataSource, "CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64))");
//        
//        executeUpdate(dataSource, "INSERT INTO caller VALUES('Joe', '" + passwordHash.generate("secret1".toCharArray()) + "')");
//        executeUpdate(dataSource, "INSERT INTO caller VALUES('Sam', '" + passwordHash.generate("secret2".toCharArray()) + "')");
//        executeUpdate(dataSource, "INSERT INTO caller VALUES('Tom', '" + passwordHash.generate("secret2".toCharArray()) + "')");
//        executeUpdate(dataSource, "INSERT INTO caller VALUES('Sue', '" + passwordHash.generate("secret2".toCharArray()) + "')");
//        
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Joe', 'foo')");
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Joe', 'bar')");
//        
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Sam', 'foo')");
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Sam', 'bar')");
//        
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Tom', 'foo')");
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Sue', 'foo')");
//    }
//
//    /**
//     * Drops the tables before instance is removed by the container.
//     */
//    @PreDestroy
//    public void destroy() {
//    	try {
//    		executeUpdate(dataSource, "DROP TABLE caller");
//    		executeUpdate(dataSource, "DROP TABLE caller_groups");
//    	} catch (Exception e) {
//    		// silently ignore, concerns in-memory database
//    	}
//    }
//
//    /*
//    Executes the SQL statement in this PreparedStatement object against the database it is pointing to.
//     */
//    private void executeUpdate(DataSource dataSource, String query) {
//        try (Connection connection = dataSource.getConnection()) {
//            try (PreparedStatement statement = connection.prepareStatement(query)) {
//                statement.executeUpdate();
//            }
//        } catch (SQLException e) {
//           throw new IllegalStateException(e);
//        }
//    }
}