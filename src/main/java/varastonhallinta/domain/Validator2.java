/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author tanel
 */
public abstract class Validator2 {
    public void validate(Object... objects) throws ValidationException{
        Map<Predicate<Object>, String> validationMap = getValidationMap();
        List<Predicate<Object>> tests = new ArrayList(validationMap.keySet());
            for(int i=0; i<tests.size(); i++){
                Predicate<Object> test = tests.get(i);
                try{
                    if(!test.test(objects[i])){
                        throw new ValidationException(validationMap.get(test));
                    }
                }catch(ClassCastException ex){
                    throw new ValidationException(validationMap.get(test));
                }
            }
    }
    
    protected abstract Map<Predicate<Object>, String> getValidationMap();
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

