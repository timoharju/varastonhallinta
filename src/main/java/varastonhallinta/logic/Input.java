/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.persistence.Tuple;
import varastonhallinta.logic.exceptions.InputException;
import varastonhallinta.util.Range;

/**
 *
 * @author tanel
 */
//public interface Input{
//    public String getInput();
//
//    public static Input from(TextField textField){
//        return () -> textField.getText();
//    }
//
//    public static Input from(ComboBox<String> comboBox){
//        return () -> comboBox.getValue();
//    }
//    
//    public static Input from(TextField textFieldLower, TextField textFieldHigher){
//        return () -> textFieldLower.getText() + textFieldHigher.getText();
//    }
//}

public interface Input<T>{
    public T getInput() throws InputException;

    public static <T> Input<T> from(TextField textField, Class<? extends T> c){
        return () -> converterFactory.getConverter(String.class, c).convert(textField.getText());
    }
    
    public static Input<String> from(ComboBox<String> comboBox){
        return () -> comboBox.getValue();
    }

    public static <T> Input<T> from(TextArea textArea, Class<? extends T> c){
        return () -> converterFactory.getConverter(String.class, c).convert(textArea.getText());
    }
    
    public static  Input<Range> from(TextField textFieldMin, TextField textFieldMax){
        return () -> new Range(textFieldMin.getText(), textFieldMax.getText());
    }
    
    public static Input<String> from(TextField... textFields){
        return () -> concatFields(textFields);
    }
   
    static String concatFields(TextField[] textFields){
        String result = "";
        for(TextField tf : textFields){
            result += tf.getText();
        }
        return result;
    }
    
}

interface Converter<I, O>{
    public O convert(I input);
}

class converterFactory{
    private static Map<Helper, Converter<?, ?>> converterMap = new HashMap<>();
    
    static{
        addConverter(String.class, Integer.class, string -> Integer.parseInt(string));
        addConverter(String.class, Double.class, string -> Double.parseDouble(string));
    }
    
    private static <I,O> void addConverter(Class<? extends I> input, Class<? extends O> output, Converter<I,O> converter){
        converterMap.put(new Helper(input, output), converter);
    }
    
    public static <I,O> Converter<I,O> getConverter(Class<? extends I> input, Class<? extends O> output){
        Converter<I, O> converter = (Converter<I, O>) converterMap.get(new Helper(input, output));
        return converter != null ? converter : in -> output.cast(in);
    }
    
    private static class Helper{
        
        private Class<?> input;
        private  Class<?> output;
        
        public Helper(Class<?> input, Class<?> output){
            this.input = input;
            this.output = output;
        }
        
        @Override
        public int hashCode(){
            return Objects.hash(input, output);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Helper other = (Helper) obj;
            if (!Objects.equals(this.input, other.input)) {
                return false;
            }
            if (!Objects.equals(this.output, other.output)) {
                return false;
            }
            return true;
        }


    }
}


