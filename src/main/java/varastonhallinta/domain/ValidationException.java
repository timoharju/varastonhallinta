/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

/**
 *
 * @author tanel
 */
public class ValidationException extends Exception{
    private String fieldName;
    
    public ValidationException(String fieldName){
        this.fieldName = fieldName;
    }
    
    public String getInvalidFieldName(){
        return fieldName;
    }
}
