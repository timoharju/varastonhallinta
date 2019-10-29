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
public interface IItem {

    /**
     * @return the description
     */
    String getDescription();

    /**
     *
     * @return
     */
    String getItemid();

    /**
     *
     * @return
     */
    String getItemname();

    /**
     * @return the price
     */
    String getPrice();

    /**
     *
     * @return
     */
    String getWeight();
    
}
