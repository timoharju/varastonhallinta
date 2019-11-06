/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author tanel
 */
public abstract class EntityClass<T>{

    public abstract void validate() throws ValidationException;
    
    protected final void testFields(Map<Predicate<T>, String> validationMap, T t) throws ValidationException{
        for(Predicate<T> test : validationMap.keySet()){
            if(!test.test(t)){
                throw new ValidationException(validationMap.get(test));
            }
        }
    }
    /**
     *
     * @return
     */
    public abstract Integer getID();

    /**
     *
     * @param id
     *
     */
    public abstract void setID(Integer id);

    @Override
    public int hashCode() {
        return Objects.hashCode(getID());
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this.getClass().isInstance(object)) {
            return false;
        }
        EntityClass<T> other = this.getClass().cast(object);
        this.getClass().cast(object);
        if ((this.getID() == null && other.getID() != null) || (this.getID() != null && !this.getID().equals(other.getID()))) {
            return false;
        }
        return true;
    }
    
}
