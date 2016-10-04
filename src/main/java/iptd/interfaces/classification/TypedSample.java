package iptd.interfaces.classification;

import java.util.List;

/**
 * Класс, описывающий набор наблюдений, описывающий конкретный тип
 * Created by Arol on 16.08.2016.
 */
public class TypedSample {

    private List<Double> features;
    private String category;

    public TypedSample(String category, List<Double> features){
        this.category = category;
        this.features = features;
    }

    public List<Double> getFeatures() {
        return features;
    }

    public String getCategory() {
        return category;
    }
}
