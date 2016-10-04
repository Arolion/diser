package iptd.web;

import iptd.exceptions.WebProcessingException;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arol on 18.09.2016.
 */
public class WebProcessing {

    /**
     * Метод получения всех ссылок с заданной страницы
     * @param urlPage
     * @return список ссылок, размещённых на заданной странице
     */
    public static List<String> getLinksFromPage(String urlPage) throws IOException, URISyntaxException, WebProcessingException {
        try{
            List<String> parsedLinks = new ArrayList<String>();
            Document doc = Jsoup.connect(urlPage).get();
            Elements links = doc.select("a[href]");

            for (Element element : links){
                String urlStr = element.attr("href");

                urlStr = convertToAbsoluteUrl(urlPage, urlStr);
                if (isUrlInnerBase(urlPage,urlStr)){
                    parsedLinks.add(urlStr);
                }
            }
            return parsedLinks;
        }catch(IOException ex){
            String errMsg = "Ошибка при получении ссылок, размещённых на заданной странице ["+urlPage+"].";
            throw new IOException(errMsg, ex);
        }
    }

    /**
     * Проверка, что указанный url является внутренним, относительно указанного базового url
     * @param rootUrl базовый url; должен быть представлен в виде: имя_ресурса.домен. Например: mail.ru , vesti.ru.
     *                <br>Если представление будет отлично от указанного будет осуществлена попытка преобразования
     *                введённого адреса к требуемому виду.
     * @param url проверяемый url
     * @return
     */
    private static boolean isUrlInnerBase(String rootUrl, String url) throws WebProcessingException {
        //  если url начинается с / -> указан относительный адрес
        if (isRelativeUrl(url))
            return true;
        // проверка на наличие в адресе нормализованного представления базового URL
        String normRootPath = getNormalizeRootPath(rootUrl);
        if (url.contains(normRootPath))
            return true;

        return false;
    }

    /**
     * Позволяет получить нормализованное представление базовой части исходного URL.
     * @param rootUrl базовый URL
     * @return нормализованное представление базовой части исходного URL.
     * <br>Формат: site_name.domen
     * @throws WebProcessingException - возникает при неправильном формате rootUrl
     */
    public static String getNormalizeRootPath(String rootUrl) throws WebProcessingException {
        String normRootPath;
        // удаление символа / на конце базового url
        if (rootUrl.endsWith("/"))
            rootUrl = rootUrl.substring(0,rootUrl.length() - 1);
        // разбиение базового url на части по точке
        String[] paths = rootUrl.split("\\.");
        int pathsNumb = paths.length;
        // выделение последней части как предположительного домена
        String domenPath = paths[pathsNumb-1];
        // проверка на соответствие правилам домена (используются ограниченные правила проверки)
        String domenRegexp = "[a-zA-Z]{2,3}/?";
        if (domenPath.matches(domenRegexp)){
            // выделение предпоследней части как предполодительного имени ресурса
            String namePath = paths[pathsNumb-2];
            // получение имени ресурса: разбор форматов http://mail.ru, http://www.mail.ru, www.mail.ru
            if (namePath.contains("/"))
                namePath = namePath.substring(namePath.lastIndexOf("/")+1);
            normRootPath = namePath + "." + domenPath;
            return normRootPath;
        } else{
            String errMsg = "Ошибка разбора url при получении базовой части. " +
                    "Неверный формат: не удалось определить домен.";
            throw new WebProcessingException(errMsg);
        }
    }

    /**
     * Провереряет, является ли URL абсолютным, если нет - преобразует относительные URL в абсолютный
     * @param baseUrl базовая часть URL (например: http://mail.ru)
     * @param url URL для проверки и преобразования
     * @return абсолютный URL
     * @throws WebProcessingException ошибка получения нормализованного представления базовой части URL
     */
    private static String convertToAbsoluteUrl(String baseUrl, String url) throws WebProcessingException {
        if (isRelativeUrl(url)){
            // в некоторых случаях url может представлять собой ссылку на саму базовую url: в этом случае вернуть базовую url;
            String normBaseUrl = getNormalizeRootPath(baseUrl);
            if (url.contains(normBaseUrl))
                return baseUrl;
            // формирование абсолютного пути
            String absoluteUrl = baseUrl + url.substring(1);
            return absoluteUrl;
        }else
            return url;

        //region old работало с ошибкой
       /* URI uri = new URI(url);
        if (uri.isAbsolute()){
            return url;
        }else{
            String absoluteUrl = baseUrl + url;
            uri = new URI(absoluteUrl);
            if (uri.isAbsolute())
                return  absoluteUrl;
            else{
                String errMsg = "Не удалось сформировать абсолютный адрес web-страницы на основе имеющегося ["+
                        url + "].";
                throw new WebProcessingException(errMsg);
            }
        }*/
        //endregion
    }

    /**
     * Проверяет, является ли URL относительным или абсолютным
     * @param url проверяемый URL
     * @return <b>true</b> - URL относительный
     * <br><b>false</b> - URL абсолютный
     */
    private static boolean isRelativeUrl(String url){
        if (url.startsWith("/"))
            return true;
        return false;
    }

}
