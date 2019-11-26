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
import varastonhallinta.util.Range;

/**
 *
 * @author tanel
 */
public abstract class EntityClass{

    public abstract void validate() throws ValidationException;
    
    protected final <T extends EntityClass> void testFields(Map<Predicate<T>, String> validationMap, T entity) throws ValidationException{
        for(Predicate<T> test : validationMap.keySet()){
            if(!test.test(entity)){
                throw new ValidationException(validationMap.get(test));
            }
        }
    }

    /**
     *
     * @return
     */
    public abstract Integer getId();

    /**
     *
     * @param id
     *
     */
    public abstract void setID(Integer id);

    @Override
    public int hashCode() {

        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!this.getClass().isInstance(object)) {
            return false;
        }
        EntityClass other = this.getClass().cast(object);
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }
    
    
    private static class Validator{
        private Range range;
        private String allowedChars;
        
        public Validator(){
            
        }
        
    }

}
