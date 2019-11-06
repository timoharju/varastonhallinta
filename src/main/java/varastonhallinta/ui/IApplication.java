/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.ui;

import java.util.Collection;
import java.util.function.Predicate;
import varastonhallinta.domain.EntityClass;
import varastonhallinta.ui.exceptions.*;

/**
 *
 * @author tanel
 */
public interface IApplication {
    public <T extends EntityClass<T>> void addEntity(T t) throws AddEntityException;
    public <T extends EntityClass<T>> void removeEntity(T t) throws EntityException;
    public <T extends EntityClass<T>> void update(T t) throws EntityException;
    public <T extends EntityClass<T>> Collection<T> getEntities(Class<? extends T> c, Predicate<T> predicate) throws EntityException;
}
