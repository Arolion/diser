package ddep.hibernate.dao;

import ddep.hibernate.entity.MorphModel;
import ddep.hibernate.entity.RatioMorphModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arol on 11.05.2016.
 */
public class ModelDAO extends DAO {

    public MorphModel createPageModel(MorphModel pageModel){
        begin();
        getSession().save(pageModel);
        commit();
        return pageModel;

    }

    public List<RatioMorphModel> getRandomRatioFeatures(List<Criterion> criterions, int size){
        List<RatioMorphModel> randomFeatures = new ArrayList<RatioMorphModel>();


        Criteria criteria = getSession().createCriteria(MorphModel.class);
        // установка критериев, полученных от пользователя
        for (Criterion criterion : criterions){
            System.out.println(criterion);
            criteria.add(criterion);
        }
        // установка критерия, отвечающего за случайный выбор
       /* criteria.add(Restrictions.sqlRestriction("1=1 order by RANDOM()"));*/

       // criteria.setMaxResults(size);

        begin();
        System.out.println("dao size " + criteria.list().size());
        return criteria.list();


    }
}
