/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 *
 * @author adm
 */
public class JPAUtil {

    private static EntityManagerFactory emf;
    private static ThreadLocal<EntityManager> managerEM;
    private static ThreadLocal<EntityTransaction> managerET;


    public JPAUtil(){}
    
    public static void init(String pu) {
        emf = Persistence.createEntityManagerFactory(pu);
        managerEM = new ThreadLocal<>();
        managerET = new ThreadLocal<>();
    }

    public static EntityManager getEntityManager() {
        if (managerEM.get() == null || !managerEM.get().isOpen()) {
            managerEM.set(emf.createEntityManager());
        }

        return managerEM.get();
    }

    public static EntityTransaction getTransaction() {
        if (managerET.get() == null || !managerET.get().isActive()) {
            managerET.set(getEntityManager().getTransaction());
        }

        return managerET.get();
    }

    public static void startTransaction() {
        getTransaction().begin();
    }

    public static void endTransaction(boolean commit) {
        if (commit) {
            EntityTransaction t = getTransaction();
            if(t.isActive()){
                t.commit();
            }
        } else {
            EntityTransaction t = getTransaction();
            if(t.isActive()){
                t.rollback();
            }
        }
    }
    

    public static void clear() {
        getEntityManager().clear();
    }
    

    public static void close() {
        try {
            getEntityManager().close();
        } catch (Exception e) { }
    }
    
    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }
    
    
}
