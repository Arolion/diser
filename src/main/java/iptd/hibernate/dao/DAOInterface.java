package iptd.hibernate.dao;

import iptd.hibernate.entities.RatioModel;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Arol on 23.08.2016.
 */
public interface DAOInterface {

    public <T> Serializable create(T entity) throws SQLException;

    public <T> void update(T entity) throws SQLException;

    public <T> void delete(T entity) throws SQLException;

    //? передавать Class<T> ?
    public <T, S extends Serializable> T getByKey(Class<T> entityClass, S key) throws SQLException;

    public <T> List<T> getByCriterions(Class entityClass, List<Criterion> criterions, int offset, int limit) throws SQLException;

    public int countByCriterions(Class itemClass, List<Criterion> criterions) throws SQLException;

    public <T> List<T> getRandomByCriterions(Class entityClass, List<Criterion> criterions, int offset, int limit) throws SQLException;
}
