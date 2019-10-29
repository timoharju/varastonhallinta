/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

/**
 *
 * @author tanel
 */
public interface Modifiable {
    public boolean canModify();
    public void modify();
}
