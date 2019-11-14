/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic.exceptions;

/**
 *
 * @author tanel
 */
public class InputException extends Exception{
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
