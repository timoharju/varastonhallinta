
package varastonhallinta.ui;


import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class AddObjectDialog<T> extends Dialog<T> {

    /**************************************************************************
     *
     * Fields
     *
     **************************************************************************/

    private final GridPane grid;
    private final Label label;
    private final TextField textField;
    private final String defaultValue;



    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    
    private Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360);
        return label;
    }
    
    /**
     * Creates a new TextInputDialog without a default value entered into the
     * dialog {@link TextField}.
     */
//    public AddUserDialogue() {
//        this("");
//    }

    /**
     * Creates a new TextInputDialog with the default value entered into the
     * dialog {@link TextField}.
     * @param grid
     * @param resultConverter
     */
//    public AddUserDialogue(@NamedArg("defaultValue") String defaultValue) {
//        final DialogPane dialogPane = getDialogPane();
//
//        // -- textfield
//        this.textField = new TextField(defaultValue);
//        this.textField.setMaxWidth(Double.MAX_VALUE);
//        GridPane.setHgrow(textField, Priority.ALWAYS);
//        GridPane.setFillWidth(textField, true);
//
//        // -- label
//        label = createContentLabel(dialogPane.getContentText());
//        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
//        label.textProperty().bind(dialogPane.contentTextProperty());
//
//        this.defaultValue = defaultValue;
//
//        this.grid = new GridPane();
//        this.grid.setHgap(10);
//        this.grid.setMaxWidth(Double.MAX_VALUE);
//        this.grid.setAlignment(Pos.CENTER_LEFT);
//
//        dialogPane.contentTextProperty().addListener(o -> updateGrid());
//
//        setTitle(ControlResources.getString("Dialog.confirm.title"));
//        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
//        dialogPane.getStyleClass().add("text-input-dialog");
//        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        updateGrid();
//
//        setResultConverter((dialogButton) -> {
//            ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
//            return data == ButtonData.OK_DONE ? textField.getText() : null;
//        });
//        
//        
//    }

    public AddObjectDialog(GridPane grid, Callback<ButtonType, T> resultConverter) {
        System.out.println("AddObjectDialog " + grid);
        final DialogPane dialogPane = getDialogPane();
        
        this.grid = grid;
        this.label = new Label("");
        this.textField = new TextField();
        this.defaultValue = "";
        
        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(grid);
        
        setResultConverter(resultConverter);
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    /**
     * Returns the {@link TextField} used within this dialog.
     */
    public final TextField getEditor() {
        return textField;
    }

    /**
     * Returns the default value that was specified in the constructor.
     */
    public final String getDefaultValue() {
        return defaultValue;
    }


    /**************************************************************************
     *
     * Private Implementation
     *
     **************************************************************************/

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(label, 0, 0);
        grid.add(textField, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(() -> textField.requestFocus());
    }
    
}

