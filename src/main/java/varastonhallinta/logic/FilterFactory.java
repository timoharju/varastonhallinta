/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.scene.control.CheckBox;
import varastonhallinta.domain.Item;
import varastonhallinta.logic.exceptions.NonexistentEntityException;

/**
 *
 * @author tanel
 */
class FilterFactory <A>{
        private Map<CheckBox, Helper<A, ?>> filterMap = new HashMap<>();
        
        public Predicate<A> getFilter(CheckBox cb) throws InputException{
            Predicate<A> filter;
            try{
                filter = filterMap.get(cb).getFilter();
            }catch(NumberFormatException | InvalidRangeException e){
                InputException ie = new InputException(e.getMessage());
                ie.setVerb("Virheellinen");
                throw ie;
            }
            
            return filterMap.get(cb).getFilter();
        }
        
        public <T, T2> void addFilter(CheckBox cb, Input<T> i, Function<A, T2> f, BiFunction<T,T2,Boolean> bf){
            filterMap.put(cb, new Helper(i, f, bf));
        }
        
        private class Helper<T, T2>{
            private final Input<T> input;
            private final BiFunction<T,T2,Boolean> bf;
            private final Function<A, T2> f;
            
            public Helper(Input<T> i, Function<A, T2> f, BiFunction<T,T2,Boolean> bf){
                this.input = i;
                this.f = f;
                this.bf = bf;
            }
            
            public Predicate<A> getFilter() throws InputException{
                T in = input.getInput();
                return parameter -> bf.apply(in, f.apply(parameter));
            }
        }
    }