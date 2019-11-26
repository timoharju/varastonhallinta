/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varastonhallinta.logic;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import varastonhallinta.domain.ItemType;
import varastonhallinta.logic.exceptions.NonexistentEntityException;

/**
 *
 * @author tanel
 */
public class ItemJpaController extends JPAController<ItemType> {

    public ItemJpaController(EntityManagerFactory emf) {
        super(ItemType.class, emf);
    }

    /**
     *
     * @param emf

    
        /**
     *
     * @param name
     * @return
     */
    public ItemType findUserWithName(String name) {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("findItemWithName", ItemType.class).setParameter("itemname", name).getSingleResult();
        } finally {
            em.close();
        }
    }

    
}
