/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.domain;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
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
    public abstract int getId();

    /**
     *
     * @param id
     *
     */
    public abstract void setId(Integer id);

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
//        if ((this.getID() == null && other.getID() != null) || (this.getID() != null && !this.getID().equals(other.getID()))) {
//            return false;
//        }
        if(this.getId() != other.getId()){
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
    
//    TODO: Is it necessary to validate EntityClass fields since they need to be
//    validated to be persisted in the first place???
    protected static <T extends EntityClass> boolean validEntity(T t){
        try {
            t.validate();
        } catch (ValidationException ex) {
            return false;
        }
        return true;
    }

    protected static <T extends EntityClass> boolean validEntity(Collection<T> tCollection){
        for(T t : tCollection){
            try {
                t.validate();
            } catch (ValidationException ex) {
                return false;
            }
        }
        return true;
    }
}
