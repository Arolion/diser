package iptd.hibernate;

import iptd.hibernate.dao.DAOImpl;
import iptd.hibernate.dao.DAOInterface;

/**
 * Created by Arol on 23.08.2016.
 */
public class Factory {
    private static DAOInterface dao = null;
    private static Factory instance = null;

    public static synchronized  Factory getInstance(){
        if (instance == null)
            instance = new Factory();

        return instance;
    }

    public DAOInterface getDAO(){
        if (dao == null)
            dao = new DAOImpl();

        return dao;
    }


}
