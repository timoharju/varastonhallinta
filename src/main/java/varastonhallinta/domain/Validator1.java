/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author tanel
 */
public class Validator1 {
    Map<Field, Predicate<Object>> validationMap;
    
    public Validator1(Map<Field, Predicate<Object>> validationMap){
        this.validationMap = validationMap;
    }
    
    public void validate() throws ValidationException{
        for(Field f : validationMap.keySet()){
            Object o = f.getValue();
            if(!validationMap.get(f).test(f.getValue())){
                throw new ValidationException(f.getFieldName());
            }
        }
    }
}

class Field{
    private Object value;
    private String fieldName;

    public Field(Object value, String fieldName) {
        this.value = value;
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

