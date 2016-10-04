package ddep.morphology.mystem.types;

/**
 * Класс, содержащий подсчёты количества морфологических параметров
 * Created by Arol on 15.02.2016.
 */
public class Result {

    //region количество частей речи
    //существительное
    public int noun = 0;
    //прилагательное
    public int adjective = 0;
    //в первосходной
    public int adjectiveSupr = 0;
    //в сравнительной
    public int adjectiveComp = 0;
    //глагол
    public int verb = 0;
    //в прошедшем времени
    public int verbPraet = 0;
    //в непрошедшем времени
    //настоящее
    //непрошедшее
    public int verbInpraes = 0;
    //3-е лицо
    public int verbThird = 0;
    //изъявительное наклонение
    public int verbIndicative = 0;
    //инфинитив
    public int verbInf = 0;
    //совершённого вида
    //совершённого вида
    public int verbCom = 0;
    //несовершённого вида
    public int verbImp = 0;
    //несовершенного вида
    //местоимения
    public int aspro = 0;
    //местоимение от существительного
    public int spro = 0;
    //местоимение от прилагательного
    public int apro = 0;
    //числительные
    public int numeral = 0;
    //наречия
    public int adverb = 0;
    //местоименное
    public int adverbpro = 0;
    //причастие
    public int participle = 0;
    //деепричастие
    public int participleDe = 0;
    //предикатив
    public int praed = 0;
    //предлог
    public int pretext = 0;
    //союз
    public int conjuction = 0;
    //частица
    public int part = 0;
    //междометие
    public int interjection = 0;

    public int wordsNumb = 0;

    public int uniqWordsNumb = 0;

    //одинаковые словоформы
    public int sameWords = 0;
    //endregion

    /**
     * Позволяет получить приведённые результаты подсчётов
     * @return массив приведённых результатов double[]
     */
    public double[] getAboveResult(){
        double[] aboveResult = new double[28];
        int[] preResult = getResultAsArr();
        //получение отношений частей речи к общему числу слов; preResult[25] - число слов
        for (int i=0; i < 25; i++){
            aboveResult[i] = (double)preResult[i] / preResult[25];
        }
        //число слов
        aboveResult[25] = preResult[25];
        //отношение уникальных слов к числу слов
        aboveResult[26] = (double)preResult[26] / preResult[25];
        //отношение глаголов к существительным
        aboveResult[27] = (double) preResult[4] / preResult[0];
        return aboveResult;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("");
        sb.append("Существительных (S): "+noun+"\n");
        sb.append("Прилагательных(A): " + adjective +" Превосходная степень (прев): "+adjectiveSupr + " Сравнительная степень (срав) "+ adjectiveComp + "\n");
        sb.append("Глаголов(V): " + verb + " Прошедшее время (прош): "+verbPraet+ " Непрошедшее время (наст, непрощ): "+verbInpraes +" В 3-ем лице (3-л): "+verbThird+" В изъявительном наклонении (изъяв): "+verbIndicative+" В инфинитивной форме (инф): "+verbInf + "\n");
        sb.append("Местоимений (SPRO, APRO): "+aspro + " От существительного (SPRO): "+spro+" От прилагательного (APRO): "+apro+"\n");
        sb.append("Числительных(NUM,ANUM): "+numeral+"\n");
        sb.append("Наречий (ADV,ADVPRO): "+adverb +" Местоимённых (ADVPRO)"+adverbpro+"\n");
        sb.append("Причастий(прич): "+participle+"\n");
        sb.append("Деепричастий(дееприч): "+participleDe+"\n");
        sb.append("Предикативов (прдк): " + praed+"\n");
        sb.append("Предлогов (PR): "+pretext+"\n");
        sb.append("Союзов (CONJ): "+conjuction+"\n");
        sb.append("Частиц (PART): "+part+"\n");
        sb.append("Междометий (INTJ)" + interjection + "\n");

        return sb.toString();
    }

    /**
     * Возвращает заголовок таблицы для файла с результатами; имена столбцов разделены знаком табуляции
     * @return заголовок таблицы результатов
     */
    public static String getHeadTableStr() {
        StringBuilder sb = new StringBuilder("Источник\t");
        sb.append("Сущ\t");
        sb.append("Прилаг\t");
        sb.append("Прилаг прев\t");
        sb.append("Прилаг сравн\t");
        sb.append("Глаг\t");
        sb.append("Глаг прош \t");
        sb.append("Глаг непрош \t");
        sb.append("Глаг 3-е л\t");
        sb.append("Глаг изъяв\t");
        sb.append("Глаг инф\t");
        sb.append("Глаг сов\t");
        sb.append("Глаг несов\t");
        sb.append("Местоим\t");
        sb.append("Местоим от сущ\t");
        sb.append("Местоим от прилаг\t");
        sb.append("Числит\t");
        sb.append("Наречий\t");
        sb.append("Нареч местоим\t");
        sb.append("Причастий\t");
        sb.append("Дееприч\t");
        sb.append("Предикатив\t");
        sb.append("Предлоов\t");
        sb.append("Союзов\t");
        sb.append("Частиц\t");
        sb.append("Междометий\t");
        sb.append("Число слов\t");
        sb.append("Уникальных слов\t");
        return sb.toString();
    }

    /**
     * Возвращает заголовок таблицы для файла с приведёнными результатами; имена столбцов разделены знаком табуляции
     * @return заголовок таблицы приведённых результатов
     */
    public static String getAboveHeadTableStr() {
        StringBuilder sb = new StringBuilder("Источник\t");
        sb.append("Сущ\t");
        sb.append("Прилаг\t");
        sb.append("Прилаг прев\t");
        sb.append("Прилаг сравн\t");
        sb.append("Глаг\t");
        sb.append("Глаг прош \t");
        sb.append("Глаг непрош \t");
        sb.append("Глаг 3-е л\t");
        sb.append("Глаг изъяв\t");
        sb.append("Глаг инф\t");
        sb.append("Глаг сов\t");
        sb.append("Глаг несов\t");
        sb.append("Местоим\t");
        sb.append("Местоим от сущ\t");
        sb.append("Местоим от прилаг\t");
        sb.append("Числит\t");
        sb.append("Наречий\t");
        sb.append("Нареч местоим\t");
        sb.append("Причастий\t");
        sb.append("Дееприч\t");
        sb.append("Предикатив\t");
        sb.append("Предлоов\t");
        sb.append("Союзов\t");
        sb.append("Частиц\t");
        sb.append("Междометий\t");
        sb.append("Число слов\t");
        sb.append("Отнош уник слов\t");
        sb.append("Глаг к сущ\t");
        return sb.toString();
    }

    /**
     *  Возвращает результаты подсчёта в виде строки, с разделитем в виде знака табуляции
     * @return строка результата
     */
    public String getResultAsRow() {
        StringBuilder sb = new StringBuilder("");
        sb.append(noun+"\t");
        sb.append(adjective+"\t");
        sb.append(adjectiveSupr+"\t");
        sb.append(adjectiveComp +"\t");
        sb.append(verb+"\t");
        sb.append(verbPraet+"\t");
        sb.append(verbInpraes+"\t");
        sb.append(verbThird+"\t");
        sb.append(verbIndicative+"\t");
        sb.append(verbInf+"\t");
        sb.append(verbCom+"\t");
        sb.append(verbImp+"\t");
        sb.append(aspro+"\t");
        sb.append(spro+"\t");
        sb.append(apro+"\t");
        sb.append(numeral+"\t");
        sb.append(adverb+"\t");
        sb.append(adverbpro+"\t");
        sb.append(participle+"\t");
        sb.append(participleDe+"\t");
        sb.append(praed+"\t");
        sb.append(pretext+"\t");
        sb.append(conjuction+"\t");
        sb.append(part+"\t");
        sb.append(interjection+"\t");
        sb.append(wordsNumb+"\t");
        sb.append(uniqWordsNumb+"\t");

        return sb.toString();
    }

    /**
     * Возвращает результаты подсчёта в виде массива int[]
     * @return результаты
     */
    public int[] getResultAsArr() {
        int[] resultArr = {
                noun, adjective, adjectiveSupr, adjectiveComp, verb, verbPraet, verbInpraes, verbThird, verbIndicative, verbInf,
                verbCom, verbImp, aspro, spro, apro, numeral, adverb, adverbpro, participle, participleDe,
                praed, pretext, conjuction, part, interjection, wordsNumb, uniqWordsNumb
        };
        return resultArr;
    }

    public String getResultArrAsRow(int[] resultArr) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < resultArr.length; i++) {
            sb.append(resultArr[i]);
            if (i != (resultArr.length - 1))
                sb.append("\t");
        }
        return sb.toString();
    }

    public String getResultArrAsRow(double[] resultArr) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < resultArr.length; i++) {
            sb.append(resultArr[i]);
            if (i != (resultArr.length - 1))
                sb.append("\t");
        }
        return sb.toString();
    }
}
