/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import varastonhallinta.domain.EntityClass;

/**
 *
 * @author tanel
 */
public interface ButtonController <E extends EntityClass>{
    public boolean canSearch();
    public boolean canUpdate();
    public boolean canCreate();
    public boolean canDelete();
    public void search();
    public void update(E e);
    public void create(E e);
    public void delete(E e);
}
