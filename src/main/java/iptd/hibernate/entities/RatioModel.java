package iptd.hibernate.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Arol on 25.08.2016.
 */
@Entity
@Table(schema = "pages", name = "tbl_vesti_ratio")
public class RatioModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "type")
    private String type;
    @Column(name = "resource")
    private String resource;
    @Column(name = "url")
    private String url;
    @Column(name = "add_time")
    private Date addTime;

    @Column(name = "page_text", columnDefinition = "text")
    private String text;

    @Column(name = "m_noun")
    private double noun;
    @Column(name = "m_adjective")
    private double adjective;
    @Column(name = "m_adjective_supr")
    private double adjectiveSupr;
    @Column(name = "m_adjective_comp")
    private double adjectiveComp;

    @Column(name = "m_verb")
    private double verb;
    @Column(name = "m_verb_praet")
    private double verbPraet;

    @Column(name = "m_verb_inpraes")
    private double verbInpraes;
    @Column(name = "m_verb_third")
    private double verbThird;
    @Column(name = "m_verb_inf")
    private double verbInf;
    @Column(name = "m_verb_indicative")
    private double verbIndicat;
    @Column(name = "m_verb_commited")
    private double verbCommit;
    @Column(name = "m_verb_imperfect")
    private double verbImperf;

    @Column(name = "m_aspro")
    private double aspro;
    @Column(name = "m_spro")
    private double spro;
    @Column(name = "m_apro")
    private double apro;

    @Column(name = "m_numeral")
    private double numeral;

    @Column(name = "m_adverb")
    private double adverb;
    @Column(name = "m_adverb_pro")
    private double adverbPro;

    @Column(name = "m_participle")
    private double participle;
    @Column(name = "m_participle_de")
    private double participleDe;

    @Column(name = "m_praed")
    private double praed;
    @Column(name = "m_pretext")
    private double pretext;

    @Column(name = "m_conjunction")
    private double conj;
    @Column(name = "m_part")
    private double part;
    @Column(name = "m_intejection")
    private double intej;

    @Column(name = "words_numb")
    private int words;
    @Column(name = "same_words_numb")
    private int sameWords;
    @Column(name = "uniq_words_numb")
    private int uniqWords;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getNoun() {
        return noun;
    }

    public void setNoun(double noun) {
        this.noun = noun;
    }

    public double getAdjective() {
        return adjective;
    }

    public void setAdjective(double adjective) {
        this.adjective = adjective;
    }

    public double getAdjectiveSupr() {
        return adjectiveSupr;
    }

    public void setAdjectiveSupr(double adjectiveSupr) {
        this.adjectiveSupr = adjectiveSupr;
    }

    public double getAdjectiveComp() {
        return adjectiveComp;
    }

    public void setAdjectiveComp(double adjectiveComp) {
        this.adjectiveComp = adjectiveComp;
    }

    public double getVerb() {
        return verb;
    }

    public void setVerb(double verb) {
        this.verb = verb;
    }

    public double getVerbPraet() {
        return verbPraet;
    }

    public void setVerbPraet(double verbPraet) {
        this.verbPraet = verbPraet;
    }

    public double getVerbInpraes() {
        return verbInpraes;
    }

    public void setVerbInpraes(double verbInpraes) {
        this.verbInpraes = verbInpraes;
    }

    public double getVerbThird() {
        return verbThird;
    }

    public void setVerbThird(double verbThird) {
        this.verbThird = verbThird;
    }

    public double getVerbInf() {
        return verbInf;
    }

    public void setVerbInf(double verbInf) {
        this.verbInf = verbInf;
    }

    public double getVerbIndicat() {
        return verbIndicat;
    }

    public void setVerbIndicat(double verbIndicat) {
        this.verbIndicat = verbIndicat;
    }

    public double getVerbCommit() {
        return verbCommit;
    }

    public void setVerbCommit(double verbCommit) {
        this.verbCommit = verbCommit;
    }

    public double getVerbImperf() {
        return verbImperf;
    }

    public void setVerbImperf(double verbImperf) {
        this.verbImperf = verbImperf;
    }

    public double getAspro() {
        return aspro;
    }

    public void setAspro(double aspro) {
        this.aspro = aspro;
    }

    public double getSpro() {
        return spro;
    }

    public void setSpro(double spro) {
        this.spro = spro;
    }

    public double getApro() {
        return apro;
    }

    public void setApro(double apro) {
        this.apro = apro;
    }

    public double getNumeral() {
        return numeral;
    }

    public void setNumeral(double numeral) {
        this.numeral = numeral;
    }

    public double getAdverb() {
        return adverb;
    }

    public void setAdverb(double adverb) {
        this.adverb = adverb;
    }

    public double getAdverbPro() {
        return adverbPro;
    }

    public void setAdverbPro(double adverbPro) {
        this.adverbPro = adverbPro;
    }

    public double getParticiple() {
        return participle;
    }

    public void setParticiple(double participle) {
        this.participle = participle;
    }

    public double getParticipleDe() {
        return participleDe;
    }

    public void setParticipleDe(double participleDe) {
        this.participleDe = participleDe;
    }

    public double getPraed() {
        return praed;
    }

    public void setPraed(double praed) {
        this.praed = praed;
    }

    public double getPretext() {
        return pretext;
    }

    public void setPretext(double pretext) {
        this.pretext = pretext;
    }

    public double getConj() {
        return conj;
    }

    public void setConj(double conj) {
        this.conj = conj;
    }

    public double getPart() {
        return part;
    }

    public void setPart(double part) {
        this.part = part;
    }

    public double getIntej() {
        return intej;
    }

    public void setIntej(double intej) {
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

}
