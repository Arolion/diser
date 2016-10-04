package iptd.hibernate.dao;

import iptd.hibernate.Factory;
import iptd.hibernate.HibernateUtils;
import iptd.hibernate.entities.RatioModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arol on 23.08.2016.
 */
public class DAOImpl implements DAOInterface {

    /**
     * Метод закрытия указанной сессии: осуществляет проверку, и если сессия != Null и открыта - закрывает её
     * @param session объект сессии
     */
    private void closeCurSession(Session session){
        if (session != null && session.isOpen())
            session.close();
    }

    public <T> Serializable create(T entity) throws SQLException {
        Session session = null;
        Serializable id = null;
        try{
            session = HibernateUtils.getSessionFactory().openSession();
            id = session.save(entity);
            session.flush();
            return id;
        }catch (Exception ex){
            throw new SQLException("Ошибка при создании записи в БД. Тип entity: "
                + entity.getClass().getSimpleName() + ".", ex);
        }finally{
            closeCurSession(session);
        }

    }

    public <T> void update(T entity) throws SQLException {
        Session session = null;
        try{
            session = HibernateUtils.getSessionFactory().openSession();
            session.update(entity);
            session.flush();
        }catch(Exception ex){
            String exMsg = "Ошибка при обновлении записи.";
            throw new SQLException(exMsg, ex);
        }finally {
            closeCurSession(session);
        }

    }

    public <T> void delete(T entity) throws SQLException {
        Session session = null;
        try{
            session = HibernateUtils.getSessionFactory().openSession();

            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }catch(Exception ex){
            System.out.println("Ошибка при удалении указанной записи. ");
            throw new SQLException("Ошибка при удалении указанной записи.",ex);
        }finally {
            closeCurSession(session);
        }
    }

    public int countByCriterions(Class itemClass, List<Criterion> criterions) throws SQLException {
        int count = -1;
        Session session = null;
        try{
            session = HibernateUtils.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(itemClass);
            for (Criterion criterion : criterions){
                criteria.add(criterion);
            }
            criteria.setMaxResults(1);
            count = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            return count;
        }catch(Exception ex){
            throw new SQLException("Ошибка при получении из БД числа записей, соответствующих ограничениям."
            +" Type: " + itemClass.getSimpleName() + ".", ex);
        }finally {
            closeCurSession(session);
        }
    }

    public <T, S extends Serializable> T getByKey(Class<T> entityClass, S key) throws SQLException {
        Session session = null;
        T entity = null;
        try{
            session = HibernateUtils.getSessionFactory().openSession();
            entity = (T)session.get(entityClass,key);
            return entity;
        }catch (Exception ex){
           String exMsg = "Ошибка при получении записи по ключу. ";
            System.out.print(exMsg);
            throw new SQLException(exMsg, ex);
        }finally {
            closeCurSession(session);
        }
    }

    public <T> List<T> getRandomByCriterions(Class entityClass, List<Criterion> criterions, int offset, int limit) throws SQLException{
        List<T> randomEntities = new ArrayList<T>();
        Session session = null;
        try{
            session = HibernateUtils.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(entityClass);
            for (Criterion criterion : criterions) {
                criteria.add(criterion);
            }

            if (offset > 0)
                criteria.setFirstResult(offset);

            if (limit > 0)
                criteria.setMaxResults(limit);

            criteria.add(Restrictions.sqlRestriction("1=1 order by RANDOM()"));
            randomEntities = criteria.list();
        }catch(Exception ex){
            System.out.println("Ошибка при получении случайных записей, соответствующих списку критериев.");
            throw new SQLException("Ошибка при получении записей, соответствующих списку критериев.",ex);
        } finally {
            closeCurSession(session);
        }

        return randomEntities;
    }

    public <T> List<T> getByCriterions(Class entityClass, List<Criterion> criterions, int offset, int limit) throws SQLException {
        List<T> entities = new ArrayList<T>();
        Session session = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(entityClass);
            for (Criterion criterion : criterions) {
                criteria.add(criterion);
            }

            if (offset > 0)
                criteria.setFirstResult(offset);

            if (limit > 0)
                criteria.setMaxResults(limit);

            entities = criteria.list();
        }catch (Exception ex){
            System.out.println("Ошибка при получении записей, соответствующих списку критериев.");
            throw new SQLException("Ошибка при получении записей, соответствующих списку критериев.",ex);
        } finally {
            closeCurSession(session);
        }
        return entities;
    }
}
