package iptd.implementation.classification.classificator;

import ddep.classifier.naiveBayes.BayesClassifier;
import ddep.classifier.naiveBayes.Classification;
import ddep.classifier.naiveBayes.Classifier;
import iptd.interfaces.classification.TypedSample;

import java.util.List;

/**
 * Created by Arol on 11.08.2016.
 */
public class Bayes {

    final Classifier<Double, String> bayes = new BayesClassifier<Double, String>();

    /**
     * Обучение классификатора байеса
     * @param samples список объектов типа TypedSample обучающей выборки
     */
    public /*Classifier<Float, String>*/void learn(List<TypedSample> samples){
        this.bayes.reset();

        Classification classification;
        for (TypedSample sample : samples){
            classification = new Classification(sample.getFeatures(), sample.getCategory());
            this.bayes.learn(classification);
        }
        //return this.bayes;
    }

    /**
     * Определяет класс (категорию), соответствующий заданнму набору параметров (наблюдению)
     * @param samples наблюдение
     * @return имя класса (категории)
     */
    public String classify(List<Double> samples){
        Classification classification = this.bayes.classify(samples);
        return classification.getCategory().toString();
    }

    /**
     * Получение вероятности ошибки при классификации
     * @param samples список объектов типа TypedSample для получения вероятности ошибки обученного классификатора
     * @return вероятность ошибки текущего обученного классификатора (число ошибок классификации / число наблюдений)
     */
    public double checkAccuracy(List<TypedSample> samples){
        int errorsNumb = 0;
        int samplesNumb = samples.size();

        for (TypedSample sample : samples){
            String resultCategory = classify(sample.getFeatures());
            if (!sample.getCategory().equals(resultCategory))
                errorsNumb++;
        }

        double probabilityError =  (double)errorsNumb / (double)samplesNumb;
        return  probabilityError;

    }




}
