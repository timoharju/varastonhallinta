/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInfo;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import varastonhallinta.domain.ValidationException;
import varastonhallinta.logic.exceptions.NonexistentEntityException;
import varastonhallinta.ui.exceptions.AddEntityException;

/**
 *
 * @author tanel
 * 
 * Tests for the class varastonhallinta.ui.Main
 */

//TODO: Create proper unit tests and mock all of the objects that the main class
//uses.
//TODO: Make tests for FXML-dependent classes.
//TODO: Make tests more independent so one test uses only one method of the main class
public class MainTest {
    
    /**
     *
     */
    public MainTest() {
    }

    static final Logger logger = Logger.getLogger(MainTest.class.getName());
    private static final String SEPARATOR = "========================================================";
    
    //Array of example names used for the test entities that will be added to the db
    final static String[] firstNames = new String[] {"jorma", "seppo", "kalle"};
    final static String[] lastNames = new String[] {"jormala", "seppola", "kallela"};
    final static int ENTITY_AMOUNT = Math.min(firstNames.length, lastNames.length);
    private static final User[] users = new User[ENTITY_AMOUNT];
    private static final Item[] items = new Item[ENTITY_AMOUNT];
    private static final Role[] roles = new Role[ENTITY_AMOUNT];
    
    //Returns a array of test roles
    private Role[] getTestRoles(){
        Role[] copy = new Role[ENTITY_AMOUNT];
        for(int i=0; i<ENTITY_AMOUNT; i++){
            copy[i] = new Role(roles[i]);
        }
        return copy;
    }
    
    //Returns a array of test users
    private User[] getTestUsers(){
        User[] copy = new User[ENTITY_AMOUNT];
        for(int i=0; i<ENTITY_AMOUNT; i++){
            copy[i] = new User(users[i]);
        }
        return copy;
    }
    
    //Returns a array of test items
    private Item[] getTestItems(){
        Item[] copy = new Item[ENTITY_AMOUNT];
        for(int i=0; i<ENTITY_AMOUNT; i++){
            copy[i] = new Item(items[i]);
        }
        return copy;
    }
    
    //Returns a list of roles, users and items
    private List<EntityClass> getTestEntities(){
        final List<EntityClass> entities = new ArrayList<>();
        entities.addAll(Arrays.asList(getTestRoles()));
        entities.addAll(Arrays.asList(getTestUsers()));
        entities.addAll(Arrays.asList(getTestItems()));
        return entities;
    }
    
    /**
     * Creates valid users, items and roles to use for tests
     */
    @BeforeAll
    public static void setUpClass() {
        for(int i=0; i<ENTITY_AMOUNT; i++){
            roles[i] = new Role("role" + i);
            items[i] = new Item("item" + i, i, i, "description" + i);
            users[i] = new User("user" + i, "password" + i, firstNames[i], lastNames[i], roles[i]);
        }
    }
    
    /**
     *
     */
    @AfterAll
    public static void tearDownClass() {
    }
    
    /** 
     * Clears the DB and prints the name of the next test before all tests.
     * Drops all tables in the DB and creates 4 example Entities of all of the
     * existing Entity Classes in varastonhallinta.domain
     * @param testInfo
     */
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        logger.info(() -> String.format("About to execute [%s] \n" + SEPARATOR,
            testInfo.getDisplayName()));
        varastonhallinta.util.HibernateUtil.initDB();
    }
    
    /**
     * Prints the name of the test that finished executing
     * @param testInfo
     */
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        logger.info(() -> String.format("Finished executing [%s] \n" + SEPARATOR,
            testInfo.getDisplayName()));
    }


    /**
     * Test of start method, of class Main. Will have to mock all javaFX-objects
     * to test this method.
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
     * Test of getApp method, of class Main. The main class should be a singleton
     * so the getApp()-method should always return the same result.
     */
    @Test
    public void testGetApp() {
        System.out.println("getApp");
        Main result = Main.getApp();
        Main result2 = Main.getApp();
        //Assert that the same object is returned on both calls to getApp()
        assertEquals(result, result2);
    }

    /**
     * Test of getLoggedUser method, of class Main.
     */
    @Test
    public void testGetLoggedUser() {
        System.out.println("getLoggedUser");
        Main instance = Main.getApp();
        

        //Get the users that are in the DB
        Collection<User> users = instance.getEntities(User.class);
        //There should be some users in the DB by default
        assertFalse(users.isEmpty(), "no users found");
        
        instance.userLogout();
        //getLoggedUser should return null before a successfull login
        Assertions.assertNull(instance.getLoggedUser());
        
        for(User user : users){
            //login to the system with a username and password in the DB
            instance.userLogin(user.getUsername(), user.getPassword());
            
            //Get the currently logged in user
            User result = instance.getLoggedUser();
            User expResult = user;
            
            //Assert that the returned user is the same as the one we used to login.
            assertEquals(expResult, result);
        }

    }
    

   /**
     * Test of userLogin method, of class Main.
     */
    @Test
    public void testUserLogin() {
        System.out.println("userLogin");
        Main instance = Main.getApp();

        //Get the users that are in the DB
        Collection<User> users = instance.getEntities(User.class);
        //There should be some users in the DB by default
        assertFalse(users.isEmpty(), "no users found");
        
        users.forEach(user -> {
            //login to the system with a username and password in the DB
            tryLogin(user.getUsername(), user.getPassword(), true, instance);
        });
        
        users.forEach(user -> {
            //Try to login with a username that is not in the DB
            tryLogin(user.getUsername().substring(1), user.getPassword(), false, instance);
        });
        
        users.forEach(user -> {
            //Try to login with a password that is not in the DB
            tryLogin(user.getUsername(), user.getPassword().substring(1), false, instance);
        });
    }
    
    private void tryLogin(String username, String password, boolean expResult, Main instance){
            boolean result = instance.userLogin(username, password);

            assertEquals(expResult, result);
    }
    
    
    
    
    /**
     * Test of getBalance method, of class Main.
     */
    @Test
    @Disabled("Method not implemented yet")
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
    @Disabled("Method not implemented yet")
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

        //Get the users that are in the DB
        Collection<User> users = instance.getEntities(User.class);
        //There should be some users in the DB by default
        assertFalse(users.isEmpty(), "no users found");
        
        for(User user : users){
            //login to the system with a username and password in the DB
            boolean loginSuccessfull = instance.userLogin(user.getUsername(), user.getPassword());
            assertTrue(loginSuccessfull);
            
            instance.userLogout();
            
            User result = instance.getLoggedUser();
            User expResult = null;
            
            //Assert that no user is returned after logout
            assertEquals(expResult, result);
        }
    }
    
    private Collection<String> getFxmlFileNames() throws IOException{
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "fxml");
        List<String> names = new ArrayList<>();
        Files.newDirectoryStream(path.toAbsolutePath()).iterator().forEachRemaining(p -> names.add("/fxml/" + p.getFileName().toString()));
        return names;
    }

    /**
     * Looks for all of the FXML-files in the /fxml/ folder and tries to load
     * them. Fails if there is an exception during loading.
     */
    @Test
    @Disabled("JavaFX method")
    public void testLoadController(){
        System.out.println("loadController");
        Main instance = Main.getApp();
        Initializable result;
        try {
            for(String name : getFxmlFileNames()){
                result = instance.loadController(name);
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
        
        //Role with white-space in the name
        Role invalidRole = new Role("te     eserg st");
        //Item with negative price
        Item invalidItem = new Item("test", 1, -500, "description");
         //User with a username that is too short
        User invalidUser = new User("a", "test", invalidRole);
        
        //Get the users that are in the DB
        Collection<User> usersInDB = instance.getEntities(User.class);
        //There should be some users in the DB by default
        Assertions.assertFalse(usersInDB.isEmpty());
        
        //Duplicate users shouldn't be allowed in the DB
        //When a duplicate user is added, an AddEntityException should be thrown
        for(User user : usersInDB){
            Assertions.assertThrows(AddEntityException.class, () -> {
                try {
                    instance.addEntity(user);
                } catch (ValidationException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                    //Users fields should be valid
                    fail(ex);
                }
            });
        }
    
        //A list of valid entities (Role, User, Item)
        List<EntityClass> validEntities = new ArrayList<>();
        List<User> validUsers = Arrays.asList(getTestUsers());
        //Get the Role-Entities that the User-entities are referencing
        List<Role> validRoles = Arrays.asList(validUsers.stream().map(user -> user.getRole()).toArray(Role[]::new));
        List<Item> validItems = Arrays.asList(getTestItems());
        validEntities.addAll(validRoles);
        validEntities.addAll(validUsers);
        validEntities.addAll(validItems);
 
        for(EntityClass entity : validEntities){
            try {
                //Get all entities
                Collection<EntityClass> result = instance.getEntities(entity.getClass());
                //Assert that the entity is not in the DB
                Assertions.assertFalse(result.contains(entity));
                
                //Add the valid entity into the DB
                instance.addEntity(entity);
                //Get all entities
                result = instance.getEntities(entity.getClass());
                //Assert that the valid entity is in the DB
                Assertions.assertTrue(result.contains(entity));
            } catch (ValidationException | AddEntityException ex) {
                //No exceptions should be thrown
                fail(ex);
            }
        }

        //A list of invalid entites (Role, User, Item)
        List<EntityClass> invalidEntities = Arrays.asList(invalidRole, invalidItem, invalidUser);
        
        for(EntityClass entity : invalidEntities){
            //When an entity with invalid field values is added, a ValidationException
            //should be thrown
            Assertions.assertThrows(ValidationException.class, () -> {
                instance.addEntity(entity);
            });
            //Get all entities
            Collection<EntityClass> result = instance.getEntities(entity.getClass());
            //Assert that the valid entity is not in the DB
            Assertions.assertFalse(result.contains(entity));
        }

    }

    /**
     * Test of removeEntity method, of class Main.
     */
    @Test
    public void testRemoveEntity(){
        System.out.println("removeEntity");
        Main instance = Main.getApp();
        //Create a list of all the different Entity-classes
        List<Class<? extends EntityClass>> entityClasses = Arrays.asList(User.class, Item.class, Role.class);
        
        for(Class<? extends EntityClass> entityClass : entityClasses){
            //Get all entities of that Entity-class
            Collection<EntityClass> entities = instance.getEntities(entityClass);
            //There should be some entities of each Entity-class
            assertFalse(entities.isEmpty());

            //Remove all of the entities of that Entity-class
            entities.forEach(e -> {
                try {
                    //Try to remove the entity
                    instance.removeEntity(e);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                    fail();
                }
            });
            
            //No entities of that Entity-class should be left in the DB
            assertTrue(instance.getEntities(entityClass).isEmpty());
        }

        //Get a list of valid entities that are not in the DB
        List<EntityClass> entitiesNotInDB = this.getTestEntities();
        
        for(EntityClass entity : entitiesNotInDB){
            Collection<EntityClass> entities = instance.getEntities(entity.getClass());
            //Assert that the entity is not in the DB
            assertFalse(entities.contains(entity));
            
            //When a entity that is not in the DB is removed, a NonexistentEntityException
            //should be thrown
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
        
        //A list of valid entities (Role, User, Item)
        List<User> newUsers = Arrays.asList(getTestUsers());
        //Get the Role-Entities that the User-entities are referencing
        List<Role> newRoles = Arrays.asList(newUsers.stream().map(user -> user.getRole()).toArray(Role[]::new));
        List<Item> newItems = Arrays.asList(getTestItems());
        //The roles need to be added to the DB
        newRoles.forEach(role -> {
            try {
                instance.addEntity(role);
            } catch (ValidationException | AddEntityException ex) {
                Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                fail();
            }
        });
        
        //Create collections of the entity-classes that are in the DB
        Collection<User> oldUsers = instance.getEntities(User.class);
        Collection<Role> oldRoles = instance.getEntities(Role.class);
        Collection<Item> oldItems = instance.getEntities(Item.class);
 
        //Create a mapping of the old and new -Entities
        Map<Iterator<? extends EntityClass>, Iterator<? extends EntityClass>> map = new HashMap<>();
        map.put(newUsers.iterator(), oldUsers.iterator());
        map.put(newRoles.iterator(), oldRoles.iterator());
        map.put(newItems.iterator(), oldItems.iterator());
        
        map.forEach((newEntities, oldEntities) -> {
            while(newEntities.hasNext() && oldEntities.hasNext()){
                EntityClass newEntity = newEntities.next();
                EntityClass oldEntity = oldEntities.next();
                
                //Ensure that the field values are not the same
                assertFalse(isEqual(oldEntity, newEntity));
                
                //Update the field values of the old entity with those of the new one
                updateEntity(oldEntity, newEntity);
                
                try{
                    //Update the entity in the DB
                    instance.update(oldEntity);
                }catch(ValidationException | NonexistentEntityException ex){
                    fail(ex);
                }
                
                //Get the updated entity from the DB
                Collection<EntityClass> c = instance.getEntities(oldEntity.getClass(), e -> oldEntity.equals(e));
                assertEquals(c.size(), 1);
                EntityClass updatedEntity = c.iterator().next();
                
                //Ensure that the field values are the same
                assertTrue(isEqual(updatedEntity, newEntity));
            }
        });
    }
    
    //A method that checks that all of the fields of the entity-classes are 
    //the same
    private boolean isEqual(EntityClass oldEntity, EntityClass newEntity){
        boolean equal = true;
        if(oldEntity instanceof User && newEntity instanceof User){
            User oldUser = (User)oldEntity;
            User newUser = (User)newEntity;
            
            equal &= oldUser.getUsername().equals(newUser.getUsername());
            equal &= oldUser.getPassword().equals(newUser.getPassword());
            equal &= oldUser.getFirstName().equals(newUser.getFirstName());
            equal &= oldUser.getLastName().equals(newUser.getLastName());
            equal &= oldUser.getRole().equals(newUser.getRole());
        }else if(oldEntity instanceof Item && newEntity instanceof Item){
            Item oldItem = (Item)oldEntity;
            Item newItem = (Item)newEntity;
            
            equal &= (oldItem.getItemname().equals(newItem.getItemname()));
            equal &= Double.compare(oldItem.getWeight(), newItem.getWeight()) == 0;
            equal &= Double.compare(oldItem.getPrice(), newItem.getPrice()) == 0;
            equal &= (oldItem.getDescription().equals(newItem.getDescription()));
            equal &= (oldItem.getItemname().equals(newItem.getItemname()));
        }else if(oldEntity instanceof Role && newEntity instanceof Role){
            Role oldRole = (Role)oldEntity;
            Role newRole = (Role)newEntity;
            
            equal &= (oldRole.getName().equals(newRole.getName()));
        }
        return equal;
    }
    
    //A method that changes the values of the old entity with the new one
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
     * Test of getEntities method, of class Main. Tests that the getEntities method
     * returns all of the entities of a certain entity-class
     */
    @Test
    public void testGetEntities_Class() {
        System.out.println("getEntities");
        Main instance = Main.getApp();

        //Create a mapping of each of the entity-classes and a list of instances
        //of that class
        Map<Class<? extends EntityClass>, List<? extends EntityClass>> entityMap = new HashMap<>();
        List<User> validUsers = Arrays.asList(getTestUsers());
        //Get the Role-Entities that the User-entities are referencing
        List<Role> validRoles = Arrays.asList(validUsers.stream().map(user -> user.getRole()).toArray(Role[]::new));
        List<Item> validItems = Arrays.asList(getTestItems());
        entityMap.put(Role.class, validRoles);
        entityMap.put(User.class, validUsers);
        entityMap.put(Item.class, validItems);
        
        //Go through all of the entity-classess and lists containing the instances
        //of that class to be added to the DB
        entityMap.forEach((entityClass, entityList) -> {
            entityList.forEach(e -> {
                try {
                    //Add all of the entities in the list to the DB
                    instance.addEntity(e);
                } catch (ValidationException | AddEntityException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            //Get a list of all of the entities of a certain entity-class
            //that are in the DB
            Collection<EntityClass> entitiesInDB = instance.getEntities(entityClass);
            
            //Ensure that the entities were added
            entityList.forEach(e -> {
                System.out.println("entitiesInDB " + entitiesInDB);
                System.out.println("e " + e);
                assertTrue(entitiesInDB.contains(e));
            });
        });
    }

    
    /**
     * Test of getEntities method, of class Main.
     */
//    @Test
//    public void testGetEntities_Class_Predicate(){
//        System.out.println("getEntities");
//        Main instance = Main.getApp();
//        
//        //A helper class that receives a set of entities of a certain entity-class
//        //and runs a test on them
//        class TestHelper<T extends EntityClass>{
//            private List<Function<T, Object>> functions;
//            private List<T> entities;
//            
//            public TestHelper(List<Function<T, Object>> functions, List<T> entities){
//                this.functions = functions;
//                this.entities = entities;
//            }
//
//            public void runTests(){
//                entities.forEach(entity -> {
//                    try {
//                        instance.addEntity(entity);
//                    } catch (ValidationException | AddEntityException ex) {
//                        Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
//                        fail(ex);
//                    }
//                });
//                
//                entities.forEach(entity -> {
//                    functions.forEach(function -> {
//                        Collection<EntityClass> result = instance.getEntities(entity.getClass(), e -> function.apply(entity).equals(function.apply((T)e)));
//                        assertEquals(1, result.size());
//                        assertEquals(entity, result.iterator().next());
//                    });
//                });
//            }
//        }
//        
//        //A list of valid entities (Role, User, Item)
//        List<User> newUsers = Arrays.asList(getTestUsers());
//        //Get the Role-Entities that the User-entities are referencing
//        List<Role> newRoles = Arrays.asList(newUsers.stream().map(user -> user.getRole()).toArray(Role[]::new));
//        List<Item> newItems = Arrays.asList(getTestItems());
//        //The roles need to be added to the DB
//        newRoles.forEach(role -> {
//            try {
//                instance.addEntity(role);
//            } catch (ValidationException | AddEntityException ex) {
//                Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
//                fail();
//            }
//        });
//
//        List<Function<User, Object>> userFunctions = Arrays.asList(
//                (user) -> user.getId(),
//                (user) -> user.getUsername(),
//                (user) -> user.getPassword(),
//                (user) -> user.getFirstName(),
//                (user) -> user.getLastName(),
//                (user) -> user.getRole()
//        );
//        
//        List<Function<Item, Object>> itemFunctions = Arrays.asList(
//                (item) -> item.getId(),
//                (item) -> item.getItemname(),
//                (item) -> item.getPrice(),
//                (item) -> item.getWeight(),
//                (item) -> item.getDescription()
//        );
//        
//        List<Function<Role, Object>> roleFunctions = Arrays.asList(
//                (role) -> role.getId(),
//                (role) -> role.getName()
//        );
//
//        List<TestHelper<?>> helpers = Arrays.asList(new TestHelper<>(roleFunctions, newRoles),
//                new TestHelper<>(userFunctions, newUsers),
//                new TestHelper<>(itemFunctions, newItems));
//
//        helpers.forEach(helper -> helper.runTests());
//    }
//    
    /**
     * Test of getEntities method, of class Main. The getEntities-method 
     * returns a collection of entities using a predicate to filter the entities
     * in the DB. This test gets all of entities of different entity classes from
     * the DB, makes a predicate that should receive exactly one result, and then
     * calls the getEntities-method using that predicate and assert that the returned
     * entity is the same that the predicate was formed from.
     */
    @Test
    public void testGetEntities_Class_Predicate(){
        Main instance = Main.getApp();
        
        //Create collections of the entity-classes that are in the DB
        Collection<User> usersInDB = instance.getEntities(User.class);
        Collection<Role> rolesInDB = instance.getEntities(Role.class);
        Collection<Item> itemsInDB = instance.getEntities(Item.class);

        usersInDB.forEach(user -> {
            this.<User>testGetEntities(User.class, (entity) -> entity.getId(), user, instance);
            this.<User>testGetEntities(User.class, (entity) -> entity.getUsername(), user, instance);
            this.<User>testGetEntities(User.class, (entity) -> entity.getPassword(), user, instance);
            this.<User>testGetEntities(User.class, (entity) -> entity.getFirstName(), user, instance);
            this.<User>testGetEntities(User.class, (entity) -> entity.getLastName(), user, instance);
            this.<User>testGetEntities(User.class, (entity) -> entity.getRole(), user, instance);
        });
        
        itemsInDB.forEach(item -> {
            this.<Item>testGetEntities(Item.class, (entity) -> entity.getId(), item, instance);
            this.<Item>testGetEntities(Item.class, (entity) -> entity.getItemname(), item, instance);
            this.<Item>testGetEntities(Item.class, (entity) -> entity.getPrice(), item, instance);
            this.<Item>testGetEntities(Item.class, (entity) -> entity.getWeight(), item, instance);
            this.<Item>testGetEntities(Item.class, (entity) -> entity.getDescription(), item, instance);
        });
        
        rolesInDB.forEach(role -> {
            this.<Role>testGetEntities(Role.class, (entity) -> entity.getId(), role, instance);
            this.<Role>testGetEntities(Role.class, (entity) -> entity.getName(), role, instance);
        });
    }
    
    //Tests the getEntities method by making a predicate that applies a function
    //on the predicate-parameter and the entity given to this function.
    //Then calls the getEntities-method using that predicate and asserts that 
    //the returned entity is equal to the given entity.
    private <T extends EntityClass> void testGetEntities(Class<? extends T> entityClass, Function<T, Object> function, T entity, Main instance){
        System.out.println("testGetEntities " + entity);
        System.out.println("");
        Predicate<T> predicate = e -> {
            System.out.println("function.apply(e) " + function.apply(e));
            System.out.println("function.apply(entity) " + function.apply(entity));
            System.out.println("unction.apply(e).equals(function.apply(entity)) " + function.apply(e).equals(function.apply(entity)));
            return function.apply(e).equals(function.apply(entity));
        };
        Collection<T> entityCollection = instance.getEntities(entityClass, predicate);
        assertEquals(1, entityCollection.size());
        T result = entityCollection.iterator().next();
        assertEquals(entity, result);
    }

    /**
     * Test of getRoleNames method, of class Main.
     */
    @Test
    @Disabled("Temp method that will be removed in the future")
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
