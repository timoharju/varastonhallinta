/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.CsvSource;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import varastonhallinta.util.Range;

/**
 *
 * @author tanel
 */
public class MainTest {
    
    public MainTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }


    /**
     * Test of start method, of class Main.
     */
    @Test
    @Disabled("JavaFX method")
    public void testStart() {
        System.out.println("start");
        Stage primaryStage = null;
        Main instance = new Main();
        instance.start(primaryStage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApp method, of class Main.
     */
    @Test
    @Disabled("Temp utility method")
    public void testGetApp() {
        System.out.println("getApp");
        Main expResult = null;
        Main result = Main.getApp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLoggedUser method, of class Main.
     */
    @Test
    public void testGetLoggedUser() {
        System.out.println("getLoggedUser");
        Main instance = new Main();

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
     * Test of getLoggedUser method, of class Main.
     */
    @Test
    public void testGetLoggedUser2() {
        System.out.println("getLoggedUser2");
        Main instance = new Main();

        Collection<User> users = instance.getEntities(User.class);
        assertFalse(users.isEmpty(), "no users found");
        
        for(User user : users){
            boolean successLogin = instance.userLogin(user.getUsername(), user.getPassword());
            assertTrue(successLogin);
            
            boolean failLogin = instance.userLogin(failUsernameProvider().findAny().get(), successPasswordProvider().findAny().get());
            assertFalse(failLogin);
            
            User result = instance.getLoggedUser();
            User expResult = user;
            
            assertEquals(expResult, result);
        }

    }

    static Stream<Arguments> successLoginProvider() {
        return Stream.of(
            arguments("admin","admin"),
            arguments("varastomies","varastomies"),
            arguments("tuotePäällikkö","tuotePäällikkö"),
            arguments("asiakas","asiakas")
        );
    }
    
    static Stream<Arguments> failLoginProvider() {
        return Stream.of(
            arguments("amin","admin"),
            arguments("varastomies","varasomies"),
            arguments("tuotePäällikk","tuoktePäällikkö"),
            arguments("asiiakas","asiakas")
        );
    }   
    
    static Stream<String> failUsernameProvider() {
        return Stream.of(
            "amin",
            "varahstomies",
            "tuotePäällikk",
            "asiiakas"
        );
    }
    
    static Stream<String> successUsernameProvider() {
        return Stream.of(
            "admin",
            "varastomies",
            "tuotePäällikkö",
            "asiakas"
        );
    }   
    
    static Stream<String> failPasswordProvider() {
        return Stream.of(
            "amin",
            "varahstomies",
            "tuotePäällikk",
            "asiiakas"
        );
    }
    
    static Stream<String> successPasswordProvider() {
        return Stream.of(
           "admin",
            "varastomies",
            "tuotePäällikkö",
            "asiakas"
        );
    }
    
    static Stream<String> failFirstNameProvider() {
        return Stream.of(
            "amin",
            "varahstomies",
            "tuotePäällikk",
            "asiiakas"
        );
    }
    
    static Stream<String> successFirstNameProvider() {
        return Stream.of(
           "admin",
            "varastomies",
            "tuotePäällikkö",
            "asiakas"
        );
    }
        
    static Stream<String> failLastNameProvider() {
        return Stream.of(
            "amin",
            "varahstomies",
            "tuotePäällikk",
            "asiiakas"
        );
    }
    
    static Stream<String> successLastNameProvider() {
        return Stream.of(
           "admin",
            "varastomies",
            "tuotePäällikkö",
            "asiakas"
        );
    }   
    
    static Stream<String> successRoleProvider() {
        return Stream.of(
           "admin",
            "varastomies",
            "tuotePäällikkö",
            "asiakas"
        );
    }   
    
        static Stream<String> failRoleProvider() {
        return Stream.of(
           "admin",
            "varastomies",
            "tuotePäällikkö",
            "asiakas"
        );
    }   
    
    private static Role testRole = new Role("test");
    private static Item testItem = new Item("test", 1, 100, "description");
    private static User testUser = new User("test", "test", testRole);
        
    static Stream<Arguments> elementProvider() {
        return Stream.of(
            arguments(),
            arguments(new User(successUsernameProvider().findAny().get(), "test", testRole), "varasomies"),
            arguments("tuotePäällikk","tuoktePäällikkö"),
            arguments("asiiakas","asiakas")
        );
    }  

    static Stream<User> successUserProvider() {
        String[] username = successUsernameProvider().toArray(String[]::new);
        String[] password = successPasswordProvider().toArray(String[]::new);
        String[] firstName = successFirstNameProvider().toArray(String[]::new);
        String[] lastName = successLastNameProvider().toArray(String[]::new);
        Role[] role = successRoleProvider().toArray(Role[]::new);
        
        List<User> users = new ArrayList<>();
        for(int i=0; i<username.length; i++){
            users.add(new User(username[i], password[i], firstName[i], lastName[i], role[i]));
        }
        return users.stream();
    }   
    
    static Stream<User> failUserProvider() {
        String[] username = failUsernameProvider().toArray(String[]::new);
        String[] password = failPasswordProvider().toArray(String[]::new);
        String[] firstName = failFirstNameProvider().toArray(String[]::new);
        String[] lastName = failLastNameProvider().toArray(String[]::new);
        Role[] role = failRoleProvider().toArray(Role[]::new);
        
        List<User> users = new ArrayList<>();
        for(int i=0; i<username.length; i++){
            users.add(new User(username[i], password[i], firstName[i], lastName[i], role[i]));
        }
        return users.stream();
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
        Main instance = new Main();
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
        Main instance = new Main();
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
        Main instance = new Main();
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
        Main instance = new Main();
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
        Main instance = new Main();

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
    public void testLoadContent(){
        System.out.println("loadContent");
        Main instance = new Main();
        Node result;
        try {
            for(String name : getFxmlFileNames()){
                result = instance.loadContent(name);
                Assertions.assertNotNull(result);
            }
        } catch (Exception ex) {
            fail();
        }
    }
    
    private Collection<String> getFxmlFileNames() throws IOException{
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "fxml");
        System.out.println("path.toAbsolutePath() " + path.toAbsolutePath());
        List<String> names = new ArrayList<>();
        Files.newDirectoryStream(path.toAbsolutePath()).iterator().forEachRemaining(p -> names.add(p.getFileName().toUri().getPath()));
        return names;
    }

    /**
     * Test of loadController method, of class Main.
     */
    @Test
    public void testLoadController() throws Exception {
        System.out.println("loadController");
        Main instance = new Main();
        Node result;
        try {
            for(String name : getFxmlFileNames()){
                result = instance.loadContent(name);
                Assertions.assertNotNull(result);
            }
        } catch (Exception ex) {
            fail();
        }
    }

    /**
     * Test of showAlert method, of class Main.
     */
    @Test
    public void testShowAlert() {
        System.out.println("showAlert");
        try{
            Alert.AlertType alertType = null;
            String title = "";
            String message = "";
            Main instance = new Main();
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
        Main instance = new Main();
        instance.addEntity(null);
                System.out.println("testUserLogout");
                
        Role testRole = new Role("test");
        Item testItem = new Item("test", 1, 100, "description");
        User testUser = new User("test", "test", testRole);

        Collection<User> users = instance.getEntities(User.class);
        assertFalse(users.isEmpty(), "no users found");
        
        Item item = new Item();
        
        
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
     * Test of addEntity method, of class Main.
     */
    @Test
    public void testAddEntity2(){
        System.out.println("addEntity");
        Main instance = new Main();
                
        Role validRole = new Role("test");
        Item validItem = new Item("test", 1, 100, "description");
        User validUser = new User("test", "test", testRole);
        
        Role invalidRole = new Role("te     eserg st");
        Item invalidItem = new Item("test", 1, -500, "description");
        User invalidUser = new User("a", "test", testRole);

        List<EntityClass<? extends EntityClass<?>>> validEntities = Arrays.asList(validRole, validItem, validUser);
        List<EntityClass<?>> invalidEntities = Arrays.asList(invalidRole, invalidItem, invalidUser);
        
        try{
            for(EntityClass<? extends EntityClass<?>> entity : validEntities){
                instance.addEntity(entity);
            }
        }catch(){
            
        }
    }
    
    private <T extends EntityClass<T>> void helper( List<EntityClass<T>> entities){
        Main instance = new Main();
        try{
            for(EntityClass<T> entity : entities){
                instance.addEntity(entity);
            }
        }catch(){
            
        }
    }

    /**
     * Test of removeEntity method, of class Main.
     */
    @Test
    public void testRemoveEntity() throws Exception {
        System.out.println("removeEntity");
        Main instance = new Main();
        instance.removeEntity(null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class Main.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Main instance = new Main();
        instance.update(null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntities method, of class Main.
     */
    @Test
    public void testGetEntities_Class() {
        System.out.println("getEntities");
        Class c = null;
        Main instance = new Main();
        Collection expResult = null;
        Collection result = instance.getEntities(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntities method, of class Main.
     */
    @Test
    public void testGetEntities_Class_Predicate() throws Exception {
        System.out.println("getEntities");
        Main instance = new Main();
        Collection expResult = null;
        Collection result = instance.getEntities(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoleNames method, of class Main.
     */
    @Test
    public void testGetRoleNames() {
        System.out.println("getRoleNames");
        Main instance = new Main();
        String[] expResult = null;
        String[] result = instance.getRoleNames();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    private static abstract class EntityTester<T extends EntityClass<T>>  implements EntityFieldVisitor{
        private List<EntityField<?>> fields;
                
        public EntityTester(List<EntityField<?>> fields){
            this.fields = fields;
        }
        
        public T getInvalidEntity(){
            
        }
        
        public T getValidEntity(){
            
        }
    }
    
    private static class UserTester extends EntityTester<User>{

        @Override
        public void visit(String value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void visit(Integer value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void visit(Double value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    private abstract static class EntityField<T> implements EntityFieldVisitor{
        private Range range;
        private char[] invalidChars;
        private char[] validChars;
        
        public EntityField(){
            
        }
        
        public T getInvalidValue(){
            
        }
        
        public T getValidValue(){
            
        }

        public abstract void accept(EntityFieldVisitor visitor);
        
    }
    
    private static class UserEntityField{
        
    }
    
    private static class UsernameField extends EntityField<String>{

        @Override
        public void accept(UserVisitor userVisitor) {
            userVisitor.visit(this);
        }
        
    }
    
    private static class PasswordField extends EntityField<String>{
        
    }
        
    private static class FirstNameField extends EntityField<String>{
        
    }
            
    private static class LastNameField extends EntityField<String>{
        
    }
    
    private static class RoleField extends EntityField<String>{
        
    }
    
    private interface EntityFieldVisitor{
        public String visit(String value);
        public String visit(String value);
        public String visit(String value);
        
    }
}
