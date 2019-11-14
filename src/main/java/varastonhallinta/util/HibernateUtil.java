package varastonhallinta.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    
    public static void initDB(Main main){
        main.setEntityManagerFactory(Persistence.createEntityManagerFactory("varastonhallinta"));
        try {
            Role adR = new Role("Admin");
            Role vmR = new Role("Varastomies");
            Role tpR = new Role("TuotePäällikkö");
            Role asR = new Role("Asiakas");
            for(Role role : Arrays.asList(adR, vmR, tpR, asR)){
                main.addEntity(role);
            }
            User ad = new User("admin", "admin", adR);
            User vm = new User("varastomies", "varastomies", vmR);
            User tp = new User("tuotePäällikkö", "tuotePäällikkö", tpR);
            User as = new User("asiakas", "asiakas", asR);
            for(User user : Arrays.asList(ad, vm, tp, as)){
                main.addEntity(user);
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
                    
            main.addEntity(new Item("kamera", 0.4, 400, "description"));
            main.addEntity(new Item("Monster Energy Ultra", 0.5, 1.5, "description"));
            main.addEntity(new Item("Samsung UE55RU7172 55\" Smart 4K Ultra HD LED", 17.3, 450, "description"));
        }catch (ValidationException ex) {
            System.out.println("ex.getInvalidFieldName() " + ex.getInvalidFieldName());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getInvalidFieldName(), ex);
            
        } catch (AddEntityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}