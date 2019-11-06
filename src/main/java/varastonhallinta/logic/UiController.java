package varastonhallinta.logic;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import varastonhallinta.ui.Main;

/**
 *
 * @author tanel
 */
public class UiController extends FXMLController {

    
    @FXML
    Initializable userTabContent;
    
    @FXML
    Initializable itemTabContent;
    
    @FXML
    private Button create;
    
    @FXML
    private Button modify;
    
    @FXML
    private Button search;
    
    @FXML
    private Button delete;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
//        assert rolesBox != null : "fx:id=\"colName\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert colStatus != null : "fx:id=\"colStatus\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert colSynopsis != null : "fx:id=\"colSynopsis\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert deleteIssue != null : "fx:id=\"deleteIssue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert descriptionValue != null : "fx:id=\"descriptionValue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert details != null : "fx:id=\"details\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert displayedIssueLabel != null : "fx:id=\"displayedIssueLabel\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert newIssue != null : "fx:id=\"newIssue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert saveIssue != null : "fx:id=\"saveIssue\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert synopsis != null : "fx:id=\"synopsis\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
//        
//        System.out.println(this.getClass().getSimpleName() + ".initialize");
//        configureButtons();
//        configureDetails();
//        configureTable();
//        connectToService();
//        if (list != null) {
//            list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//            list.getSelectionModel().selectedItemProperty().addListener(projectItemSelected);
//            displayedProjectNames.addListener(projectNamesListener);
//        }

//        configureRolesBox();
//        configureTooltips();
//        configureFindUserTable();
//        configureTestArea();
    }
    
    /**
     * Called when the new button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void newFired(ActionEvent event) {
        FXMLController.getActiveController().handleCreate();
    }

    /**
     * Called when the delete button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void deleteFired(ActionEvent event) {
        FXMLController.getActiveController().handleDelete();
    }
    
        /**
     * Called when the search button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void searchFired(ActionEvent event) {
        System.out.println("searchFired");
        FXMLController.getActiveController().handleSearch();
    }
    
        /**
     * Called when the modify button is fired.
     *
     * 
     * @param event the action event.
     */
    @FXML
    void modifyFired(ActionEvent event) {
        FXMLController.getActiveController().handleModify();
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLController.setCreate(create);
        FXMLController.setModify(modify);
        FXMLController.setSearch(search);
        FXMLController.setDelete(delete);
    }
   
}
