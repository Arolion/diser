package ddep.hibernate.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Arol on 11.05.2016.
 */
@Entity
@Table(name="test", schema="classification")
public class MorphModel {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @Column(name="type")
    private String type;
    @Column(name="resource")
    private String resource;
    @Column(name="url")
    private String url;
    @Column(name="add_time")
    private Date addTime;

    @Column(name = "page_text", columnDefinition="text")
    private String text;

    @Column(name="m_noun")
    private int noun;
    @Column(name="m_adjective")
    private int adjective;
    @Column(name="m_adjective_supr")
    private int adjectiveSupr;
    @Column(name="m_adjective_comp")
    private int adjectiveComp;

    @Column(name="m_verb")
    private int verb;
    @Column(name="m_verb_praet")
    private int verbPraet;

    @Column(name="m_verb_inpraes")
    private int verbInpraes;
    @Column(name="m_verb_third")
    private int verbThird;
    @Column(name="m_verb_inf")
    private int verbInf;
    @Column(name="m_verb_indicative")
    private int verbIndicat;
    @Column(name="m_verb_commited")
    private int verbCommit;
    @Column(name="m_verb_imperfect")
    private int verbImperf;

    @Column(name="m_aspro")
    private int aspro;
    @Column(name="m_spro")
    private int spro;
    @Column(name="m_apro")
    private int apro;

    @Column(name="m_numeral")
    private int numeral;

    @Column(name="m_adverb")
    private int adverb;
    @Column(name="m_adverb_pro")
    private int adverbPro;

    @Column(name="m_participle")
    private int participle;
    @Column(name="m_participle_de")
    private int participleDe;

    @Column(name="m_praed")
    private int praed;
    @Column(name="m_pretext")
    private int pretext;

    @Column(name="m_conjunction")
    private int conj;
    @Column(name="m_part")
    private int part;
    @Column(name="m_intejection")
    private int intej;

    @Column(name="words_numb")
    private int words;
    @Column(name="same_words_numb")
    private int sameWords;
    @Column(name="uniq_words_numb")
    private int uniqWords;




    //@GeneratedValue(generator = "increment")
    //@GenericGenerator(name ="increment", strategy = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }



    public int getNoun() {
        return noun;
    }

    public void setNoun(int noun) {
        this.noun = noun;
    }


    public int getAdjective() {
        return adjective;
    }

    public void setAdjective(int adjective) {
        this.adjective = adjective;
    }


    public int getAdjectiveSupr() {
        return adjectiveSupr;
    }

    public void setAdjectiveSupr(int adjectiveSupr) {
        this.adjectiveSupr = adjectiveSupr;
    }


    public int getAdjectiveComp() {
        return adjectiveComp;
    }

    public void setAdjectiveComp(int adjectiveComp) {
        this.adjectiveComp = adjectiveComp;
    }


    public int getVerb() {
        return verb;
    }

    public void setVerb(int verb) {
        this.verb = verb;
    }


    public int getVerbPraet() {
        return verbPraet;
    }

    public void setVerbPraet(int verbPraet) {
        this.verbPraet = verbPraet;
    }


    public int getVerbInpraes() {
        return verbInpraes;
    }

    public void setVerbInpraes(int verbInpraes) {
        this.verbInpraes = verbInpraes;
    }


    public int getVerbThird() {
        return verbThird;
    }

    public void setVerbThird(int verbThird) {
        this.verbThird = verbThird;
    }


    public int getVerbInf() {
        return verbInf;
    }

    public void setVerbInf(int verbInf) {
        this.verbInf = verbInf;
    }


    public int getVerbIndicat() {
        return verbIndicat;
    }

    public void setVerbIndicat(int verbIndicat) {
        this.verbIndicat = verbIndicat;
    }


    public int getVerbCommit() {
        return verbCommit;
    }

    public void setVerbCommit(int verbCommit) {
        this.verbCommit = verbCommit;
    }


    public int getVerbImperf() {
        return verbImperf;
    }

    public void setVerbImperf(int verbImperf) {
        this.verbImperf = verbImperf;
    }


    public int getSpro() {
        return spro;
    }

    public void setSpro(int spro) {
        this.spro = spro;
    }


    public int getApro() {
        return apro;
    }

    public void setApro(int apro) {
        this.apro = apro;
    }


    public int getNumeral() {
        return numeral;
    }

    public void setNumeral(int numeral) {
        this.numeral = numeral;
    }


    public int getAdverb() {
        return adverb;
    }

    public void setAdverb(int adverb) {
        this.adverb = adverb;
    }


    public int getAdverbPro() {
        return adverbPro;
    }

    public void setAdverbPro(int adverbPro) {
        this.adverbPro = adverbPro;
    }


    public int getParticiple() {
        return participle;
    }

    public void setParticiple(int participle) {
        this.participle = participle;
    }


    public int getParticipleDe() {
        return participleDe;
    }

    public void setParticipleDe(int participleDe) {
        this.participleDe = participleDe;
    }


    public int getPraed() {
        return praed;
    }

    public void setPraed(int praed) {
        this.praed = praed;
    }


    public int getPretext() {
        return pretext;
    }

    public void setPretext(int pretext) {
        this.pretext = pretext;
    }


    public int getConj() {
        return conj;
    }

    public void setConj(int conj) {
        this.conj = conj;
    }


    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }


    public int getIntej() {
        return intej;
    }

    public void setIntej(int intej) {
        this.intej = intej;
    }


    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }


    public int getSameWords() {
        return sameWords;
    }

    public void setSameWords(int sameWords) {
        this.sameWords = sameWords;
    }


    public int getUniqWords() {
        return uniqWords;
    }

    public void setUniqWords(int uniqWords) {
        this.uniqWords = uniqWords;
    }


    public int getAspro() {
        return aspro;
    }

    public void setAspro(int aspro) {
        this.aspro = aspro;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
