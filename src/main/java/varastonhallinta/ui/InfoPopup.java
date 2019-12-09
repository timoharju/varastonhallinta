/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import static javafx.stage.PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author tanel
 */
public class InfoPopup extends PopupControl {
    
    private ReadOnlyDoubleWrapper wrapper = new ReadOnlyDoubleWrapper(this.xProperty(), "xProperty", 0);
    
    ChangeListener<Number> windowXListener = new ChangeListener<Number>(){
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Node ownerNode = InfoPopup.this.ownerNodeProperty().get();
            Bounds screenBounds = ownerNode.localToScreen(ownerNode.getBoundsInLocal());
            InfoPopup.this.setX(screenBounds.getMinX());
        }
    };   
    
    ChangeListener<Number> windowYListener = new ChangeListener<Number>(){
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Node ownerNode = InfoPopup.this.ownerNodeProperty().get();
            Bounds screenBounds =ownerNode.localToScreen(ownerNode.getBoundsInLocal());
            InfoPopup.this.setY(screenBounds.getMinY() - InfoPopup.this.getHeight());
        }
    };
    
    EventHandler<WindowEvent> onShown = new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
               Node ownerNode = InfoPopup.this.getOwnerNode();
               if(ownerNode != null){
                    Window ownerWindow = ownerNode.getScene().getWindow();
                    ownerWindow.xProperty().addListener(windowXListener);
                    ownerWindow.yProperty().addListener(windowYListener);
                    ownerWindow.widthProperty().addListener(windowXListener);
                    ownerWindow.heightProperty().addListener(windowYListener);
               }
            }
    };
    
    EventHandler<WindowEvent> onClosed = new EventHandler<WindowEvent>(){
        @Override
        public void handle(WindowEvent event) {
           Node ownerNode = InfoPopup.this.getOwnerNode();
           if(ownerNode != null){
                Window ownerWindow = ownerNode.getScene().getWindow();
                ownerWindow.xProperty().addListener(windowXListener);
                ownerWindow.yProperty().addListener(windowYListener);
                ownerWindow.widthProperty().addListener(windowXListener);
                ownerWindow.heightProperty().addListener(windowYListener);
           }
        }
    };
    
    /**
     *
     * @return
     */
    public final StringProperty textProperty() { return text; }

    /**
     *
     * @param value
     */
    public final void setText(String value) {
        textProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public final String getText() { return text.getValue() == null ? "" : text.getValue(); }
    private final StringProperty text = new SimpleStringProperty(this, "text", "") {
        @Override protected void invalidated() {
            super.invalidated();
            final String value = get();
            if (isShowing() && value != null && !value.equals(getText())) {

            }
        }
    };
    
    /**
     *
     * @return
     */
    public final BooleanProperty wrapTextProperty() {
        return wrapText;
    }

    /**
     *
     * @param value
     */
    public final void setWrapText(boolean value) {
        wrapTextProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public final boolean isWrapText() {
        return wrapTextProperty().getValue();
    }
    private final BooleanProperty wrapText =
            new SimpleBooleanProperty(this, "wrapText", WRAP_TEXT_DEFAULT_VALUE);
    
    private static final boolean WRAP_TEXT_DEFAULT_VALUE = false;
    
    /**
     *
     * @param text
     * @param nodes
     */
    public InfoPopup(String text, Node... nodes){
        this(text, WRAP_TEXT_DEFAULT_VALUE, nodes);
    }
    
    /**
     *
     * @param text
     * @param wordWrap
     * @param nodes
     * 
     */
    public InfoPopup(String text, boolean wordWrap, Node... nodes){
        this.setOnShown(onShown);
        this.setOnCloseRequest(onClosed);
        setText(text);
        getStyleClass().setAll("tooltip");
        this.setAnchorLocation(WINDOW_BOTTOM_LEFT);
        for(Node node : nodes){
            node.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if(newValue == true){
                    Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());
                    show(node, screenBounds.getMinX(), screenBounds.getMinY());
                }else{
                    hide();
                }
            });
        }
        setWrapText(wordWrap);
    }
    
    @Override protected Skin<?> createDefaultSkin() {
        return new InfoPopupSkin(this);
    }
    
    private class InfoPopupSkin implements Skin<InfoPopup> {
    private Label tipLabel;
    private InfoPopup infoPopup;

    public InfoPopupSkin(InfoPopup t) {
        this.infoPopup = t;
        tipLabel = new Label();
//        tipLabel.contentDisplayProperty().bind(t.contentDisplayProperty());
//        tipLabel.fontProperty().bind(t.fontProperty());
//        tipLabel.graphicProperty().bind(t.graphicProperty());
//        tipLabel.graphicTextGapProperty().bind(t.graphicTextGapProperty());
//        tipLabel.textAlignmentProperty().bind(t.textAlignmentProperty());
//        tipLabel.textOverrunProperty().bind(t.textOverrunProperty());
        tipLabel.textProperty().bind(t.textProperty());
        tipLabel.wrapTextProperty().bind(t.wrapTextProperty());
        tipLabel.minWidthProperty().bind(t.minWidthProperty());
        tipLabel.prefWidthProperty().bind(t.prefWidthProperty());
        tipLabel.maxWidthProperty().bind(t.maxWidthProperty());
        tipLabel.minHeightProperty().bind(t.minHeightProperty());
        tipLabel.prefHeightProperty().bind(t.prefHeightProperty());
        tipLabel.maxHeightProperty().bind(t.maxHeightProperty());
//        tipLabel.translateXProperty().bind(t.translateXProperty());
//        tipLabel.translateYProperty().bind(t.translateYProperty());

        // RT-7512 - skin needs to have styleClass of the control
        // TODO - This needs to be bound together, not just set! Probably should
        // do the same for id and style as well.
        tipLabel.getStyleClass().setAll(t.getStyleClass());
        tipLabel.setStyle(t.getStyle());
        tipLabel.setId(t.getId());
//        System.out.println("tipLabel.wrapTextProperty().getValue() " + tipLabel.wrapTextProperty().getValue());
//        System.out.println("tipLabel.minWidthProperty() " + tipLabel.minWidthProperty().getValue());
//        System.out.println("tipLabel.prefWidthProperty() " + tipLabel.prefWidthProperty().getValue());
//        System.out.println("tipLabel.maxWidthProperty() " + tipLabel.maxWidthProperty().getValue());
    }

        @Override public InfoPopup getSkinnable() {
            return infoPopup;
        }

        @Override public Node getNode() {
            return tipLabel;
        }

        @Override public void dispose() {
            infoPopup = null;
            tipLabel = null;
        }
    }
}


