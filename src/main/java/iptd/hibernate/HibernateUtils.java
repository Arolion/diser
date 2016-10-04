package iptd.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Arol on 23.08.2016.
 */
public class HibernateUtils {
    private static final SessionFactory sessionFactory;

    static {
        try{
            sessionFactory = new Configuration().configure()
                    .buildSessionFactory();
        }catch (Throwable ex){
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}


