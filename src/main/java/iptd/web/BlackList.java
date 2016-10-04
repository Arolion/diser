package iptd.web;

import iptd.exceptions.WebProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arol on 25.09.2016.
 */
public class BlackList {

    private String replaceExpression = "::replace::";
    private List<String> blackListRegexps = new ArrayList<String>();

    private List<Pattern> blackListPatterns = new ArrayList<Pattern>();

    public BlackList(){
        blackListRegexps.addAll(getMailBlackList());
        blackListPatterns.addAll(getVestiBlackListPatterns());
    }




    public boolean isUrlInBlackList(String url){
        /*for (String str : this.blackListRegexps){
            if (url.startsWith(str))123
                return true;
        }\
        return false;*/
        //123
        for (Pattern pattern : this.blackListPatterns){
            if (pattern.matcher(url).matches())
                return true;
        }
        return false;
    }

    // регулярное выражение, описывающее начало URL как http[s]://[www.]<URL>
    private static final String REGEXP_HTTP_WWW = "https?://(www\\.)?";
    // регулярное выражение, описывающее домен в URL состоящий из не 2 или 3 букв (домен страны)
    private static final String REGEXP_DOMEN = "[a-zA-Z]{2,3}";

    private List<Pattern> getVestiBlackListPatterns(){
        List<Pattern> blackList = new ArrayList<Pattern>();
        blackList.add(Pattern.compile(REGEXP_HTTP_WWW + ".*" + REGEXP_DOMEN + "/videos.*"));
        blackList.add(Pattern.compile(REGEXP_HTTP_WWW + "facebook" + REGEXP_DOMEN + ".*"));
        blackList.add(Pattern.compile(REGEXP_HTTP_WWW + "radiovesti" + REGEXP_DOMEN + ".*"));
        blackList.add(Pattern.compile(REGEXP_HTTP_WWW + ".*" + REGEXP_DOMEN + ".*/short/.*"));
        blackList.add(Pattern.compile(REGEXP_HTTP_WWW + ".*" + REGEXP_DOMEN + "/(un)?subscribe\\.html"));
        blackList.add(Pattern.compile(REGEXP_HTTP_WWW + ".*vera\\.vesti.*"));

        blackList.add(Pattern.compile(".*mailto:info@vesti.ru"));

        return blackList;
    }


    private List<String> getMailBlackList(){
        List<String> blackList = new ArrayList<String>();
        String prefix = "https://";
        blackList.add(prefix+"r.mail.ru");
        blackList.add("http://"+"r.mail.ru");
        blackList.add(prefix+"e.mail.ru");
        blackList.add(prefix+"cloud.mail.ru");
        blackList.add(prefix+"promo.calendar.mail.ru");
        blackList.add(prefix+"news.mail.ru/currency.html"); //курс
        blackList.add(prefix+"horo.mail.ru"); //гороскоп
        blackList.add(prefix+"dobro.mail.ru");
        blackList.add(prefix+"1l-go.mail.ru"); // online-игры mail.ru
        blackList.add("http://"+"1l-go.mail.ru"); // online-игры mail.ru

        return blackList;
    }

    /**
     * Формирование строки регулярного выражения через подставноку в заданное базовое выражение
     * заданного подстановочного значения
     * @param base базовое выражение, содержить часть для подстановки: ::replace::
     * @param replacement подстановочное значение, заменит часть ::replace:: базового выражения
     * @return строку регулярного выражения: базовое выражение с проведённой подстановкой
     * @throws WebProcessingException базовое выражение сожержит более одной позиций подстановки
     */
    private String getRegexpStr(String base, String replacement) throws WebProcessingException {

        int firstIndex = base.indexOf(this.replaceExpression);
        int lastIndex = base.lastIndexOf(this.replaceExpression);
        if (firstIndex != lastIndex){
            String errorStr = "Exception: BlackList.getRegexpStr() . Базовое выражение содержит более одной позиции для подстановки.";
            throw new WebProcessingException(errorStr);
        }

        return base.replace(this.replaceExpression,replacement);

    }
}
