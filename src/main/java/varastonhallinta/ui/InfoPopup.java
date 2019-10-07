/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import static javafx.stage.PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT;
import static javafx.stage.PopupWindow.AnchorLocation.WINDOW_TOP_LEFT;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author tanel
 */
public class InfoPopup extends PopupControl {
    
    ReadOnlyDoubleWrapper wrapper = new ReadOnlyDoubleWrapper(this.xProperty(), "xProperty", 0);
    double xOffset;
    double yOffset;
    Window window;
    Region node;
    
    ChangeListener<Number> windowXListener = new ChangeListener<Number>(){
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            //InfoPopup.this.setX(xOffset + newValue.doubleValue());
            Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());
            InfoPopup.this.setX(screenBounds.getMinX());
        }
    };   
    
    ChangeListener<Number> windowYListener = new ChangeListener<Number>(){
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            //InfoPopup.this.setY(yOffset + newValue.doubleValue());
            Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());
            InfoPopup.this.setY(screenBounds.getMinY() - InfoPopup.this.getHeight());
        }
    };


    @Override
    public void show(Window ownerWindow, double anchorX, double anchorY) {
        window = ownerWindow;
        xOffset = anchorX - ownerWindow.getX();
        yOffset = (anchorY - this.getHeight()) - ownerWindow.getY();
        super.show(ownerWindow, anchorX, anchorY);
        ownerWindow.xProperty().addListener(windowXListener);
        ownerWindow.yProperty().addListener(windowYListener);
    }
    
    @Override
    public void hide(){
        node.getScene().getWindow().xProperty().removeListener(windowXListener);
        node.getScene().getWindow().xProperty().removeListener(windowYListener);
        super.hide();
    }
    
    public final StringProperty textProperty() { return text; }
    public final void setText(String value) {
        textProperty().setValue(value);
    }
    public final String getText() { return text.getValue() == null ? "" : text.getValue(); }
    private final StringProperty text = new SimpleStringProperty(this, "text", "") {
        @Override protected void invalidated() {
            super.invalidated();
            final String value = get();
            if (isShowing() && value != null && !value.equals(getText())) {

            }
        }
    };
    
    ReadOnlyDoubleProperty asd;
    
    public InfoPopup(String text, Region node){
        this.node = node;
        setText(text);
        getStyleClass().setAll("tooltip");
        this.setAnchorLocation(WINDOW_BOTTOM_LEFT);
        node.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue == true){
                    Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());
                    show(node.getScene().getWindow(), screenBounds.getMinX(), screenBounds.getMinY());
                }else{
                    hide();
                }
            }  
        });
    }
    
    @Override protected Skin<?> createDefaultSkin() {
        return new InfoPopupSkin(this);
    }
    
    private class InfoPopupSkin implements Skin<InfoPopup> {
    private Label tipLabel;

    private InfoPopup tooltip;

    public InfoPopupSkin(InfoPopup t) {
        this.tooltip = t;
        tipLabel = new Label();
//        tipLabel.contentDisplayProperty().bind(t.contentDisplayProperty());
//        tipLabel.fontProperty().bind(t.fontProperty());
//        tipLabel.graphicProperty().bind(t.graphicProperty());
//        tipLabel.graphicTextGapProperty().bind(t.graphicTextGapProperty());
//        tipLabel.textAlignmentProperty().bind(t.textAlignmentProperty());
//        tipLabel.textOverrunProperty().bind(t.textOverrunProperty());
        tipLabel.textProperty().bind(t.textProperty());
//        tipLabel.wrapTextProperty().bind(t.wrapTextProperty());
//        tipLabel.minWidthProperty().bind(node.minWidthProperty());
//        tipLabel.prefWidthProperty().bind(node.prefWidthProperty());
//        tipLabel.maxWidthProperty().bind(node.maxWidthProperty());
//        tipLabel.minHeightProperty().bind(t.minHeightProperty());
//        tipLabel.prefHeightProperty().bind(t.prefHeightProperty());
//        tipLabel.maxHeightProperty().bind(t.maxHeightProperty());
//        tipLabel.translateXProperty().bind(node.translateXProperty());
//        tipLabel.translateYProperty().bind(node.translateYProperty());

        // RT-7512 - skin needs to have styleClass of the control
        // TODO - This needs to be bound together, not just set! Probably should
        // do the same for id and style as well.
        tipLabel.getStyleClass().setAll(t.getStyleClass());
        tipLabel.setStyle(t.getStyle());
        tipLabel.setId(t.getId());
    }

        @Override public InfoPopup getSkinnable() {
            return tooltip;
        }

        @Override public Node getNode() {
            return tipLabel;
        }

        @Override public void dispose() {
            tooltip = null;
            tipLabel = null;
        }
    }
}


