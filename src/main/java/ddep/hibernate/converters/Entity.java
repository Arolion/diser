package ddep.hibernate.converters;

import ddep.hibernate.entity.MorphModel;
import ddep.morphology.mystem.types.Result;

import java.util.Date;

/**
 * Created by Arol on 12.05.2016.
 */
public class Entity {


    public static MorphModel mResultToModel(Result mResult, String type, String resource,
                                          String url, String text){
        MorphModel mModel = new MorphModel();

        mModel.setType(type);
        mModel.setResource(resource);
        mModel.setUrl(url);
        mModel.setAddTime(new Date());
        mModel.setText(text);

        mModel.setNoun(mResult.noun);
        mModel.setAdjective(mResult.adjective);
        mModel.setAdjectiveComp(mResult.adjectiveComp);
        mModel.setAdjectiveSupr(mResult.adjectiveSupr);

        mModel.setVerb(mResult.verb);
        mModel.setVerbPraet(mResult.verbPraet);
        mModel.setVerbInpraes(mResult.verbInpraes);
        mModel.setVerbThird(mResult.verbThird);
        mModel.setVerbInf(mResult.verbInf);
        mModel.setVerbIndicat(mResult.verbIndicative);
        mModel.setVerbCommit(mResult.verbCom);
        mModel.setVerbImperf(mResult.verbImp);

        mModel.setSpro(mResult.spro);
        mModel.setApro(mResult.apro);
        mModel.setAspro(mResult.aspro);

        mModel.setNumeral(mResult.numeral);

        mModel.setAdverb(mResult.adverb);
        mModel.setAdverbPro(mResult.adverbpro);

        mModel.setParticiple(mResult.participle);
        mModel.setParticipleDe(mResult.participleDe);

        mModel.setPraed(mResult.praed);
        mModel.setPretext(mResult.pretext);

        mModel.setConj(mResult.conjuction);

        mModel.setPart(mResult.part);

        mModel.setIntej(mResult.interjection);

        mModel.setWords(mResult.wordsNumb);
        mModel.setSameWords(mResult.sameWords);
        mModel.setUniqWords(mResult.uniqWordsNumb);

        return mModel;

    }
}
