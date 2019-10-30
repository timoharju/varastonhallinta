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
import javafx.scene.control.TextField;
import javax.persistence.Tuple;

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
        System.out.println("textField " + textField);
        return () -> converterFactory.getConverter(String.class, c).convert(textField.getText());
    }
    
    public static Input<String> from(ComboBox<String> comboBox){
        return () -> comboBox.getValue();
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

class Range{
    double min;
    double max;
    
    public Range(double min, double max) throws InvalidRangeException{
        this.min = min;
        this.max = max;
        
        if(min > max){
            InvalidRangeException ex = new InvalidRangeException("min greater than max");
            throw ex;
        }
    }
    
    public Range(String minString, String maxString) throws InvalidRangeException{
        this(Double.parseDouble(minString), Double.parseDouble(maxString));
    }
    
    public boolean isInRange(double n){
        return n >= min && n <= max;
    }
}

class InvalidRangeException extends InputException{
        /**
     *
     * @param message
     * @param cause
     */
    public InvalidRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public InvalidRangeException(String message) {
        super(message);
    }
}

class InputException extends Exception{
    private String verb = "";
    private String object = "";
    /**
     *
     * @param message
     * @param cause
     */
    public InputException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public InputException(String message) {
        super(message);
    }
    
    public String getHRMessage(){
        return verb + " " + object;
    }

    /**
     * @return the verb
     */
    public String getVerb() {
        return verb;
    }

    /**
     * @param verb the verb to set
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }

    /**
     * @return the object
     */
    public String getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(String object) {
        this.object = object;
    }
}