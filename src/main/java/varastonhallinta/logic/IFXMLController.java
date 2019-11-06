/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.util.Collection;
import java.util.function.Predicate;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.ui.exceptions.AddEntityException;
import varastonhallinta.ui.exceptions.EntityException;

/**
 *
 * @author tanel
 */
public interface IFXMLController<T>{
    public void add(T t);
    public void remove(T t);
    public void update(T t);
}

