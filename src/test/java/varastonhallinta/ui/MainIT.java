/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import java.util.Collection;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import junit.framework.TestCase;
import varastonhallinta.domain.Item;
import varastonhallinta.domain.User;

/**
 *
 * @author tanel
 */
public class MainIT extends TestCase {
    
    public MainIT(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of main method, of class Main.
     */
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Main.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class Main.
     */
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
    public void testGetLoggedUser() {
        System.out.println("getLoggedUser");
        Main instance = new Main();
        User expResult = null;
        User result = instance.getLoggedUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of userLogin method, of class Main.
     */
    public void testUserLogin() {
        System.out.println("userLogin");
        String username = "";
        String password = "";
        Main instance = new Main();
        boolean expResult = false;
        boolean result = instance.userLogin(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBalance method, of class Main.
     */
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
    public void testUserLogout() {
        System.out.println("userLogout");
        Main instance = new Main();
        instance.userLogout();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadContent method, of class Main.
     */
    public void testLoadContent() throws Exception {
        System.out.println("loadContent");
        String fxml = "";
        Main instance = new Main();
        Node expResult = null;
        Node result = instance.loadContent(fxml);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadController method, of class Main.
     */
    public void testLoadController() throws Exception {
        System.out.println("loadController");
        String fxml = "";
        Main instance = new Main();
        Initializable expResult = null;
        Initializable result = instance.loadController(fxml);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showAlert method, of class Main.
     */
    public void testShowAlert() {
        System.out.println("showAlert");
        Alert.AlertType alertType = null;
        String title = "";
        String message = "";
        Main instance = new Main();
        instance.showAlert(alertType, title, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addEntity method, of class Main.
     */
    public void testAddEntity() throws Exception {
        System.out.println("addEntity");
        Main instance = new Main();
        instance.addEntity(null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeEntity method, of class Main.
     */
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
    public void testGetRoleNames() {
        System.out.println("getRoleNames");
        Main instance = new Main();
        String[] expResult = null;
        String[] result = instance.getRoleNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
