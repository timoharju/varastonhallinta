/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.util;
/**
 *
 * @author tanel
 */
public class Range{
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
