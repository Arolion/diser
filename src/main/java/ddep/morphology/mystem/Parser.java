package ddep.morphology.mystem;



import ddep.morphology.mystem.types.MorphParams;
import ddep.morphology.mystem.types.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arol on 15.02.2016.
 */
public class Parser {
    private Result result;

    public Parser()
    {
        result = new Result();
    }

    /**
     *
     * @param morphStr свойства слова (морфологические атрибуты) в виде строки
     * @return результаты разбора в виде объекта Result
     */
    public Result parse(String morphStr)
    {
        //поиск неоднозначностей
        boolean isAmbiquity = findAmbiguity(morphStr);

        //выделение однозначной и неоднозначной части разбора омонимии
        String[] tmpStr = cutAmbiguityStr(morphStr);
        String uniqStr = tmpStr[0];
        //список точных определений из всех неоднозначностей
        List<String> ambList = new ArrayList<String>();
        if (isAmbiquity)
        {
            ambList = cutAmbiguity(tmpStr[1]);
        }
        //определение части речи
        //местоимение
        if (uniqStr.contains(MorphParams.APRO) || uniqStr.contains(MorphParams.SPRO)) {
            result.aspro++;
            //от прилагательного
            if (uniqStr.contains(MorphParams.APRO))
                result.apro++;
                //от существительного
            else if (uniqStr.contains(MorphParams.SPRO))
                result.spro++;
        }
        //числительные
        else if (uniqStr.contains(MorphParams.NUMERAL) || uniqStr.contains(MorphParams.ANUMERAL)) {
            result.numeral++;
        }
        //наречия
        else if (uniqStr.contains(MorphParams.ADVERB) || uniqStr.contains(MorphParams.ADVERBPRO)){
            result.adverb++;
            if (uniqStr.indexOf(MorphParams.ADVERBPRO)>=0)
                result.adverbpro++;
        }
        //предлог
        else if (uniqStr.contains(MorphParams.PRETEXT))
            result.pretext++;
            //союз
        else if (uniqStr.contains(MorphParams.CONJUCTION))
            result.conjuction++;
            //частица
        else if (uniqStr.contains(MorphParams.PART))
            result.part++;
            //междометие
        else if (uniqStr.contains(MorphParams.INTERJECTION))
            result.interjection++;
            //если существительное
        else if (uniqStr.contains(MorphParams.NOUN))
        {
            result.noun++;
        }
        //прилагательное
        else if (uniqStr.contains(MorphParams.ADJECTIVE)){
            result.adjective++;
            if (uniqStr.contains(MorphParams.ADJECTIVE_SUPR) || ambList.contains(MorphParams.ADJECTIVE_SUPR))
                result.adjectiveSupr++;
            else if (uniqStr.contains(MorphParams.ADJECTIVE_COMP) || ambList.contains(MorphParams.ADJECTIVE_COMP))
                result.adjectiveComp++;
        }
        //глагол
        else if (uniqStr.contains(MorphParams.VERB)) {
            result.verb++;

            //непрошедшее время
            if(uniqStr.contains(MorphParams.VERB_INPRAES1) ||ambList.contains(MorphParams.VERB_INPRAES1)
                    || uniqStr.contains(MorphParams.VERB_INPRAES2) || ambList.contains(MorphParams.VERB_INPRAES2)){
                result.verbInpraes++;
            }
            //прошедшее время
            else if(uniqStr.contains(MorphParams.VERB_PRAET) || ambList.contains(MorphParams.VERB_PRAET)){
                result.verbPraet++;
            }
            //3-е лицо
            if(uniqStr.contains(MorphParams.VERB_THIRD) || ambList.contains(MorphParams.VERB_THIRD)){
                result.verbThird++;
            }
            //изъявительное наклонение
            if(uniqStr.contains(MorphParams.VERB_INDICATIVE) || ambList.contains(MorphParams.VERB_INDICATIVE)){
                result.verbIndicative++;
            }
            //инфинитивная форма
            if(uniqStr.contains(MorphParams.VERB_INF) || ambList.contains(MorphParams.VERB_INF)){
                result.verbInf++;
            }
            //несовершенный вид
            if(uniqStr.contains(MorphParams.VERB_IMPERFECT) || ambList.contains(MorphParams.VERB_IMPERFECT)){
                result.verbImp++;
            }
            //совершенный вид
            else if(uniqStr.contains(MorphParams.VERB_COMMITED) || ambList.contains(MorphParams.VERB_COMMITED)){
                result.verbCom++;
            }
            //причастие
            if(uniqStr.contains(MorphParams.PARTICIPLE) || ambList.contains(MorphParams.PARTICIPLE)){
                result.participle++;
            }
            //деепричастие
            if(uniqStr.contains(MorphParams.PARTICIPLE_DE) || ambList.contains(MorphParams.PARTICIPLE_DE)){
                result.participleDe++;
            }
        }

        //предикатив (обычно наречие но может быть и другой частью речь)
        if (uniqStr.contains(MorphParams.PRAED) || ambList.contains(MorphParams.PRAED))
            result.praed++;

        return result;
    }


    public static boolean isRealWord(String morphStr){
        String uniqStr = getUniqMorphStr(morphStr);

        //местоимение
        if (uniqStr.contains(MorphParams.APRO) || uniqStr.contains(MorphParams.SPRO)) {
            return true;
        }
        //числительные
        else if (uniqStr.contains(MorphParams.NUMERAL) || uniqStr.contains(MorphParams.ANUMERAL)) {
            return  true;
        }
        //наречия
        else if (uniqStr.contains(MorphParams.ADVERB) || uniqStr.contains(MorphParams.ADVERBPRO)){
            return true;
        }
        //предлог
        else if (uniqStr.contains(MorphParams.PRETEXT))
            return false;
            //союз
        else if (uniqStr.contains(MorphParams.CONJUCTION))
            return false;
            //частица
        else if (uniqStr.contains(MorphParams.PART))
            return false;
            //междометие
        else if (uniqStr.contains(MorphParams.INTERJECTION))
            return false;
        else
            return true;


    }

    /**
     * Возвращает строку, содержащую однозначную часть морфологического описания заданного слова,
     * позволяет искать частицы, междометия, предлоги, союзы
     * @param morphStr
     * @return
     */
    public static String getUniqMorphStr(String morphStr){
        String[] tmpStr = cutAmbiguityStr(morphStr);
        String uniqStr = tmpStr[0];
        return uniqStr;
    }

    /**
     * Поиск вариантотивностей в строке морфологического разбора mystem
     * @param morphStr
     * @return true, если есть неоднозначность
     */
    private static boolean findAmbiguity(String morphStr)
    {
        Pattern regP = Pattern.compile(".*\\(.*|.*\\).*");
        Matcher regM = regP.matcher(morphStr);
        return regM.matches();
    }

    /**
     * Выделяет однозначную и неоднозначную часть морфологического разбора
     * @param morphStr - строка морфологического разбора
     * @return String[0] - однозначная часть разбора; String[1] - неоднозначная часть разбора
     */
    private static String[] cutAmbiguityStr(String morphStr){
        String[] result = new String[2];
        String uniqStr = "";
        String ambStr = "";
        if (!findAmbiguity(morphStr)) {
            uniqStr = morphStr.substring(4,morphStr.length());
        }
        else {
            int startAmb = morphStr.indexOf("(");
            int endAmb = morphStr.indexOf(")");
            //получение однозначной части подстроки, с 4-ого для отсева подстркои gr="
            uniqStr = morphStr.substring(4,startAmb);
            ambStr = morphStr.substring(startAmb+1, endAmb);
        }
        //поиск границ строки неоднозначностей
        result[0] = uniqStr;
        result[1] = ambStr;

        return result;
    }

    /**
     * Поиск совпадений в вариантах при снятии омнимии
     * @param ambStr подстрока неоднозначностей
     * @return массив строк, в котором значения - совпавшие для всех вариантов; null - не совпавший параметр
     */
    private static List<String> cutAmbiguity(String ambStr)
    {
        //получение вариантов в виде подстрок
        String[] ambSubs = ambStr.split("\\|");
        //получение параметров 0-ого варианта
        String[] sampleParams = ambSubs[0].split(",");
        //Массив списков параметров
        List<String>[] ambSubsList = new ArrayList[ambSubs.length];
        for (int i =0; i < ambSubsList.length; i++) {
            List<String> list = new ArrayList<String>();
            String[] params = ambSubs[i].split(",");
            //заполнение списка
            for (int j=0; j < params.length; j++) {
                list.add(params[j]);
            }
            ambSubsList[i] = list;
        }

        //поиск параметров, содержащихся в каждом варианте
        //список всех встречающихся вариантов параметров
        List<String> availParams = new ArrayList<String>();
        for (int i = 0; i < ambSubsList.length; i++) {
            for (String param : ambSubsList[i]) {
                if (!availParams.contains(param))
                    availParams.add(param);
            }
        }
        //список совпадающих параметров
        List<String> agreeParams = new ArrayList<String>();
        //по всем возможным параметрам
        for (String param : availParams) {
            boolean agree = true;
            //по всем вариантам параметров
            for(List params : ambSubsList) {
                if (params.contains(param) && agree)
                    agree = true;
                else
                    agree = false;
            }
            if (agree)
                agreeParams.add(param);
        }
        //System.out.println("1: "+Arrays.toString(agreeParams.toArray()));
        return agreeParams;


        //Двумерный массив: строки - строки вариантов; столбцы - параметры
    /*
    String[][] ambSubsArr = new String[ambSubs.length][ambSubs[0].length()];
    String[] rez = new String[sampleParams.length];
    for (int k=0; k < ambSubs.length; k++)
    {
      String[] tempStr = ambSubs[k].split(",");
      for (int l = 0; l < tempStr.length; l++)
        ambSubsArr[k][l] = tempStr[l];
    }

    //по всем элементам морфологии в строке отличий
    for (int j = 0; j < sampleParams.length; j++)
    {
      boolean isEqual = true;
      //по каждой строке отличий
      for (int i=0; i<ambSubs.length-1; i++)
      {
        if(!ambSubsArr[i][j].equals(ambSubsArr[i+1][j]))
        {
          isEqual = false;
        }
      }
      //если не было различий - задать значение элемента для результирующего массива
      if (isEqual)
        rez[j] = sampleParams[j];
      else
        rez[j] = null;
    }
    return rez;
*/
    }

    private static int findMaxParamsElem(String[] elements) {
        int maxParams = 0;
        int maxParamsIndex = 0;
        for (int i =0; i < elements.length-1; i++) {
            String str1 = elements[i];
            String str2 = elements[i+1];

            int params1 = str1.split(",").length;
            int params2 = str2.split(",").length;
            int tmpMax;
            int tmpElem;
            if (params2 > params1){
                tmpMax = params2;
                tmpElem = i+1;
            }
            else
            {
                tmpMax = params1;
                tmpElem = i;
            }
            if (tmpMax > maxParams)
            {
                maxParams = tmpMax;
                maxParamsIndex = tmpElem;
            }
        }
        return maxParamsIndex;
    }

    public Result getResult() {
        return this.result;
    }
}
