/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.util;

import varastonhallinta.logic.exceptions.InputException;

/**
 *
 * @author tanel
 */
public class InvalidRangeException extends InputException{
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
