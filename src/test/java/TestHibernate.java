/**
 * Created by Arol on 11.05.2016.
 */

import ddep.hibernate.dao.ModelDAO;
import ddep.hibernate.entity.MorphModel;
import ddep.hibernate.entity.RatioMorphModel;
import iptd.hibernate.Factory;
import iptd.hibernate.entities.RatioModel;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestHibernate {

    //@Test
    public void getCount() throws SQLException{
        try {
            List<Criterion> criterions = new ArrayList<Criterion>();
            int count = Factory.getInstance().getDAO().countByCriterions(RatioModel.class, criterions);
            System.out.println("Count: " + count);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void deleteEntity() throws SQLException{
        try{
            RatioModel feature = Factory.getInstance().getDAO().getByKey(RatioModel.class, 12);
            Factory.getInstance().getDAO().delete(feature);
            System.out.println("Запись удалена");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //@Test
    public void readByCriterions() throws SQLException {
        List<RatioMorphModel> entities = null;
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("type","nav"));

        entities = Factory.getInstance().getDAO().getByCriterions(RatioModel.class,criterions,-1,100);

        System.out.println("Count by criterions: " + entities.size());
    }

    @Test
    public void addRatioModel() throws SQLException{
        RatioModel model = new RatioModel();
        model.setType("test");
        int id = (Integer)Factory.getInstance().getDAO().create(model);
        System.out.println("Запись создана. Created model id: " + id);
    }

    @Test
    public void testRandom() throws SQLException {
        try {
            List<RatioModel> entities = null;
            List<Criterion> criterions = new ArrayList<Criterion>();
            criterions.add(Restrictions.eq("type", "inf"));

            entities = Factory.getInstance().getDAO().getRandomByCriterions(RatioModel.class, criterions, -1, 100);
            System.out.println("Random entites size: " + entities.size() + "   id1: " + entities.get(0).getId() + "    id2: " + entities.get(1).getId());
        }catch (Exception ex){
            System.out.println("Error test processing. " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
