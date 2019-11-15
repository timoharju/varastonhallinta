/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.CsvSource;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import varastonhallinta.domain.ValidationException;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.ui.exceptions.AddUserException;
import varastonhallinta.ui.exceptions.EntityException;
import varastonhallinta.util.Range;

/**
 *
 * @author tanel
 */
public class MainTest {
    
    public MainTest() {
    }
    
//    private static final List<User> users = new ArrayList<>();
//    private static final List<Item> items = new ArrayList<>();
//    private static final List<Role> roles = new ArrayList<>();

    final static String[] firstNames = new String[] {"jorma", "seppo", "kalle"};
    final static String[] lastNames = new String[] {"jormala", "seppola", "kallela"};
    final static int ENTITY_AMOUNT = Math.min(firstNames.length, lastNames.length);
    private static final User[] users = new User[ENTITY_AMOUNT];
    private static final Item[] items = new Item[ENTITY_AMOUNT];
    private static final Role[] roles = new Role[ENTITY_AMOUNT];
    //private static final List<EntityClass> entities = new ArrayList<>();
    
    private Role[] getTestRoles(){
        Role[] copy = new Role[ENTITY_AMOUNT];
        for(int i=0; i<ENTITY_AMOUNT; i++){
            copy[i] = new Role(roles[i]);
        }
        return copy;
    }
    
    private User[] getTestUsers(){
        User[] copy = new User[ENTITY_AMOUNT];
        for(int i=0; i<ENTITY_AMOUNT; i++){
            copy[i] = new User(users[i]);
        }
        return copy;
    }
        
    private Item[] getTestItems(){
        Item[] copy = new Item[ENTITY_AMOUNT];
        for(int i=0; i<ENTITY_AMOUNT; i++){
            copy[i] = new Item(items[i]);
        }
        return copy;
    }
    
    private List<EntityClass> getTestEntities(){
        final List<EntityClass> entities = new ArrayList<>();
        entities.addAll(Arrays.asList(getTestRoles()));
        entities.addAll(Arrays.asList(getTestUsers()));
        entities.addAll(Arrays.asList(getTestItems()));
        return entities;
    }
    
    @BeforeAll
    public static void setUpClass() {
        for(int i=0; i<ENTITY_AMOUNT; i++){
            roles[i] = new Role("role" + i);
            items[i] = new Item("item" + i, i, i, "description" + i);
            users[i] = new User("user" + i, "password" + i, firstNames[i], lastNames[i], roles[i]);
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    private static int testCounter = 1;
    
    @BeforeEach
    public void setUp() {
        varastonhallinta.util.HibernateUtil.initDB(Main.getApp());
        System.out.println("Test number: " + testCounter++ + " Start");
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("______________________________________________________________");
    }


    /**
     * Test of start method, of class Main.
     */
    @Test
    @Disabled("JavaFX method")
    public void testStart() {
        System.out.println("start");
        Stage primaryStage = null;
        Main instance = Main.getApp();
        instance.start(primaryStage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApp method, of class Main.
     */
    @Test
    public void testGetApp() {
        System.out.println("getApp");
        Main result = Main.getApp();
        Main result2 = Main.getApp();
        assertEquals(result, result2);
    }

    /**
     * Test of getLoggedUser method, of class Main.
     */
    @Test
    public void testGetLoggedUser() {
        System.out.println("getLoggedUser");
        Main instance = Main.getApp();

        Collection<User> users = instance.getEntities(User.class);
        assertFalse(users.isEmpty(), "no users found");
        
        for(User user : users){
            instance.userLogin(user.getUsername(), user.getPassword());
            
            User result = instance.getLoggedUser();
            User expResult = user;
            
            assertEquals(expResult, result);
        }

    }
    

    /**
     * Test of userLogin method, of class Main.
     */
    @ParameterizedTest
    @CsvSource({
        "admin,admin",
        "varastomies,varastomies",
        "tuotePäällikkö,tuotePäällikkö",
        "asiakas,asiakas"
    })
    public void testUserLoginTrue(String username, String password) {
        System.out.println("userLoginTrue");
        Main instance = Main.getApp();
        boolean expResult = true;
        boolean result = instance.userLogin(username, password);
        assertEquals(expResult, result);
    }

        /**
     * Test of userLogin method, of class Main.
     */
    @ParameterizedTest
    @CsvSource({
        "amin,admin",
        "varastomies,varasomies",
        "tuotePäällikk,tuoktePäällikkö",
        "asiiakas,asiakas"
    })
    public void testUserLoginFalse(String username, String password) {
        System.out.println("userLoginFalse");
        Main instance = Main.getApp();
        boolean expResult = false;
        boolean result = instance.userLogin(username, password);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBalance method, of class Main.
     */
    @Test
    @Disabled("Method not implemented")
    public void testGetBalance() {
        System.out.println("getBalance");
        Item item = null;
        Main instance = Main.getApp();
        int expResult = 0;
        int result = instance.getBalance(item);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStorageSpace method, of class Main.
     */
    @Test
    @Disabled("Method not implemented")
    public void testGetStorageSpace() {
        System.out.println("getStorageSpace");
        Item item = null;
        Main instance = Main.getApp();
        String expResult = "";
        String result = instance.getStorageSpace(item);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of userLogout method, of class Main.
     */
    @Test
    public void testUserLogout() {
        System.out.println("testUserLogout");
        Main instance = Main.getApp();

        Collection<User> users = instance.getEntities(User.class);
        assertFalse(users.isEmpty(), "no users found");
        
        for(User user : users){
            boolean successLogin = instance.userLogin(user.getUsername(), user.getPassword());
            assertTrue(successLogin);
            
            instance.userLogout();
            
            User result = instance.getLoggedUser();
            User expResult = null;
            
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of loadContent method, of class Main.
     */
    @Test
    @Disabled("JavaFX dependent method")
    public void testLoadContent(){
        System.out.println("loadContent");
        Main instance = Main.getApp();
        Node result;
        
        try {
            for(String name : getFxmlFileNames()){
                System.out.println("getFxmlFileNames " + name);
                result = instance.loadContent(name);
                Assertions.assertNotNull(result);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }
    
    private Collection<String> getFxmlFileNames() throws IOException{
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "fxml");
        List<String> names = new ArrayList<>();
        Files.newDirectoryStream(path.toAbsolutePath()).iterator().forEachRemaining(p -> names.add("/fxml/" + p.getFileName().toString()));
        return names;
    }

    /**
     * Test of loadController method, of class Main.
     */
    @Test
    @Disabled("JavaFX dependent method")
    public void testLoadController() throws Exception {
        System.out.println("loadController");
        Main instance = Main.getApp();
        Node result;
        try {
            for(String name : getFxmlFileNames()){
                result = instance.loadContent(name);
                Assertions.assertNotNull(result);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }

    /**
     * Test of showAlert method, of class Main.
     */
    @Test
    @Disabled("JavaFX method")
    public void testShowAlert() {
        System.out.println("showAlert");
        try{
            Alert.AlertType alertType = null;
            String title = "";
            String message = "";
            Main instance = Main.getApp();
            instance.showAlert(alertType, title, message);
        }catch(Exception ex){
            fail(ex.getMessage());
        }
        
    }
        /**
     * Test of addEntity method, of class Main.
     */
    @Test
    public void testAddEntity(){
        System.out.println("addEntity");
        Main instance = Main.getApp();
        
        Role invalidRole = new Role("te     eserg st");
        Item invalidItem = new Item("test", 1, -500, "description");
        User invalidUser = new User("a", "test", invalidRole);
        
        Collection<User> usersInDB = instance.getEntities(User.class);
        Assertions.assertFalse(usersInDB.isEmpty());
        
        for(User user : usersInDB){
            Assertions.assertThrows(AddEntityException.class, () -> {
                try {
                    instance.addEntity(user);
                } catch (ValidationException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                    fail(ex);
                }
            });
        }
    
        List<EntityClass> validEntities = new ArrayList<>();
        List<User> validUsers = Arrays.asList(getTestUsers());
        List<Role> validRoles = Arrays.asList(validUsers.stream().map(user -> user.getRole()).toArray(Role[]::new));
        List<Item> validItems = Arrays.asList(getTestItems());
        validEntities.addAll(validRoles);
        validEntities.addAll(validUsers);
        validEntities.addAll(validItems);
        List<EntityClass> invalidEntities = Arrays.asList(invalidRole, invalidItem, invalidUser);
 
        for(EntityClass entity : validEntities){
            try {
                instance.addEntity(entity);
                Collection<EntityClass> result = instance.getEntities(entity.getClass(), e -> e.equals(entity));
                Assertions.assertEquals(1, result.size());
                Assertions.assertEquals(result.iterator().next(), entity);
            } catch (ValidationException | AddEntityException ex) {
                fail(ex);
            }
        }

        
        for(EntityClass entity : invalidEntities){
            Assertions.assertThrows(ValidationException.class, () -> {
                instance.addEntity(entity);
            });
            Collection<EntityClass> result = instance.getEntities(entity.getClass(), e -> e.equals(entity));
            Assertions.assertEquals(0, result.size());
        }

    }

    /**
     * Test of removeEntity method, of class Main.
     */
    @Test
    public void testRemoveEntity(){
        System.out.println("removeEntity");
        Main instance = Main.getApp();
        List<Class<? extends EntityClass>> entityClasses = Arrays.asList(User.class, Item.class, Role.class);
        
        for(Class<? extends EntityClass> entityClass : entityClasses){
            Collection<EntityClass> entities = instance.getEntities(entityClass);
            assertFalse(entities.isEmpty());

            entities.forEach(e -> {
                try {
                    instance.removeEntity(e);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                    fail();
                }
            });
            
            assertTrue(instance.getEntities(entityClass).isEmpty());
        }
        
        Role role = new Role("test");
        Item item = new Item("test", 1, 100, "description");
        User user = new User("test", "test", role);
        
        List<EntityClass> entitiesNotInDB = Arrays.asList(role, item, user);
        
        for(EntityClass entity : entitiesNotInDB){
            System.out.println("Entity " + entity);
            Collection<EntityClass> entities = instance.getEntities(entity.getClass(), e -> e.equals(entity));
            assertTrue(entities.isEmpty());
            
            Assertions.assertThrows(NonexistentEntityException.class, () -> instance.removeEntity(entity));
        }
    }

    /**
     * Test of update method, of class Main.
     */
    @Test
    public void testUpdate(){
        System.out.println("updateEntity");
        Main instance = Main.getApp();
        
        List<User> users = Arrays.asList(getTestUsers());
        System.out.println("users " + users);
        List<Role> roles = Arrays.asList(users.stream().map(user -> user.getRole()).toArray(Role[]::new));
        List<Item> items = Arrays.asList(getTestItems());
        roles.forEach(role -> {
            try {
                instance.addEntity(role);
            } catch (ValidationException | AddEntityException ex) {
                Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex);
            }
        });

        Map<Iterator<? extends EntityClass>, Iterator<? extends EntityClass>> map = new HashMap<>();
        map.put(users.iterator(), instance.getEntities(User.class).iterator());
        map.put(roles.iterator(), instance.getEntities(Role.class).iterator());
        map.put(items.iterator(), instance.getEntities(Item.class).iterator());

        
        map.forEach((newEntities, oldEntities) -> {
            while(newEntities.hasNext() && oldEntities.hasNext()){
                EntityClass newEntity = newEntities.next();
                EntityClass oldEntity = oldEntities.next();
                
                updateEntity(oldEntity, newEntity);
                
                try{
                    instance.update(oldEntity);
                }catch(ValidationException | NonexistentEntityException ex){
                    fail(ex);
                }
                
                checkEntityEquality(oldEntity, newEntity);
            }
        });
    }
    
    private void checkEntityEquality(EntityClass oldEntity, EntityClass newEntity){
        if(oldEntity instanceof User && newEntity instanceof User){
            User oldUser = (User)oldEntity;
            User newUser = (User)newEntity;
            
            assertEquals(oldUser.getUsername(), newUser.getUsername());
            assertEquals(oldUser.getPassword(), newUser.getPassword());
            assertEquals(oldUser.getFirstName(), newUser.getFirstName());
            assertEquals(oldUser.getLastName(), newUser.getLastName());
            assertEquals(oldUser.getRole(), newUser.getRole());
        }else if(oldEntity instanceof Item && newEntity instanceof Item){
            Item oldItem = (Item)oldEntity;
            Item newItem = (Item)newEntity;
            
            assertEquals(oldItem.getItemname(), newItem.getItemname());
            assertEquals(oldItem.getWeight(), newItem.getWeight());
            assertEquals(oldItem.getPrice(), newItem.getPrice());
            assertEquals(oldItem.getDescription(), newItem.getDescription());
        }else if(oldEntity instanceof Role && newEntity instanceof Role){
            Role oldRole = (Role)oldEntity;
            Role newRole = (Role)newEntity;
            
            assertEquals(oldRole.getName(), newRole.getName());
        }
    }
    
    private void updateEntity(EntityClass oldEntity, EntityClass newEntity){
        if(oldEntity instanceof User && newEntity instanceof User){
            User oldUser = (User)oldEntity;
            User newUser = (User)newEntity;
            
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());
            oldUser.setFirstName(newUser.getFirstName());
            oldUser.setLastName(newUser.getLastName());
            oldUser.setRole(newUser.getRole());
        }else if(oldEntity instanceof Item && newEntity instanceof Item){
            Item oldItem = (Item)oldEntity;
            Item newItem = (Item)newEntity;
            
            oldItem.setItemname(newItem.getItemname());
            oldItem.setWeight(newItem.getWeight());
            oldItem.setPrice(newItem.getPrice());
            oldItem.setDescription(newItem.getDescription());
        }else if(oldEntity instanceof Role && newEntity instanceof Role){
            Role oldRole = (Role)oldEntity;
            Role newRole = (Role)newEntity;
            
            oldRole.setName(newRole.getName());
        }
    }

    /**
     * Test of getEntities method, of class Main.
     */
    @Test
    public void testGetEntities_Class() {
        System.out.println("getEntities");
        Main instance = Main.getApp();

//        List<List<EntityClass>> entityTypes = Arrays.asList(Arrays.asList(roles), Arrays.asList(items), Arrays.asList(users));
        Map<Class<? extends EntityClass>, List<EntityClass>> entityMap = new HashMap<>();
        //entityTypes.forEach(list -> list.forEach(entity -> map.get(entity.getClass()).add(entity)));
        entityMap.put(User.class, Arrays.asList(getTestUsers()));
        entityMap.put(Item.class, Arrays.asList(getTestItems()));
        entityMap.put(Role.class, Arrays.asList(getTestRoles()));
        
        entityMap.forEach((entityClass, entityList) -> {
            Collection<EntityClass> previousEntities = instance.getEntities(entityClass);
            
            List<EntityClass> allEntities = new ArrayList<>();
            allEntities.addAll(entityList);
            allEntities.addAll(previousEntities);
            
            entityList.forEach(e -> {
                try {
                    instance.addEntity(e);
                } catch (ValidationException | AddEntityException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            instance.getEntities(entityClass).forEach(e -> {
                if(allEntities.contains(e)){
                    allEntities.remove(e);
                }else{
                    fail("Not all entities were added");
                }
            });
        });
    }

    
    /**
     * Test of getEntities method, of class Main.
     */
    @Test
    public void testGetEntities_Class_Predicate(){
        System.out.println("getEntities");
        Main instance = Main.getApp();
        
        class Helper<T extends EntityClass>{
            private List<Function<T, Object>> functions;
            private List<T> entities;
            
            public Helper(List<Function<T, Object>> functions, List<T> entities){
                this.functions = functions;
                this.entities = entities;
            }

            public void runTests(){
                entities.forEach(t -> {
                    try {
                        instance.addEntity(t);
                    } catch (ValidationException | AddEntityException ex) {
                        Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                        fail(ex);
                    }
                });
                
                entities.forEach(t -> {
                    functions.forEach(function -> {
                        System.out.println("function.apply(t) " + function.apply(t));
                        Collection<EntityClass> result = instance.getEntities(t.getClass(), e -> function.apply(t).equals(function.apply((T)e)));
                        assertEquals(1, result.size());
                        assertEquals(t, result.iterator().next());
                    });
                });
            }
        }
        
        final int ENTITY_AMOUNT = 3;
        
        List<User> users = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        
        String[] firstNames = new String[] {"jorma", "seppo", "kalle"};
        String[] lastNames = new String[] {"jormala", "seppola", "kallela"};
   
        for(int i=0; i<ENTITY_AMOUNT; i++){
            Role validRole = new Role("role" + i);
            Item validItem = new Item("item" + i, i, i, "description" + i);
            User validUser = new User("user" + i, "password" + i, firstNames[i], lastNames[i], validRole);
            
            users.add(validUser);
            items.add(validItem);
            roles.add(validRole);
        }
        
        List<Function<User, Object>> userFunctions = Arrays.asList(
                (user) -> user.getID(),
                (user) -> user.getUsername(),
                (user) -> user.getPassword(),
                (user) -> user.getFirstName(),
                (user) -> user.getLastName(),
                (user) -> user.getRole()
        );
        
        List<Function<Item, Object>> itemFunctions = Arrays.asList(
                (item) -> item.getID(),
                (item) -> item.getItemname(),
                (item) -> item.getPrice(),
                (item) -> item.getWeight(),
                (item) -> item.getDescription()
        );
        
        List<Function<Role, Object>> roleFunctions = Arrays.asList(
                (role) -> role.getID(),
                (role) -> role.getName()
        );

        List<Helper<?>> helpers = Arrays.asList(
                new Helper<>(roleFunctions, roles),
                new Helper<>(userFunctions, users),
                new Helper<>(itemFunctions, items));

        helpers.forEach(helper -> helper.runTests());
    }

    /**
     * Test of getRoleNames method, of class Main.
     */
    @Test
    @Disabled
    public void testGetRoleNames() {
        System.out.println("getRoleNames");
        Main instance = Main.getApp();
        String[] expResult = null;
        String[] result = instance.getRoleNames();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
