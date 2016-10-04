package iptd.interfaces.classification;

import java.util.List;

/**
 * Created by Arol on 11.08.2016.
 */
public interface Classificator {

    /**
     * Обучение классификатора
     * @param category категория, соответствующая заданной обучающей выборке
     * @param features наблюдения, используемые для обучения
     * @param <K>
     * @param <T>
     */
    public <K,T> void learn(K category, List<T> features);




}
