package ddep.morphology;


import ddep.hibernate.converters.Entity;
import ddep.hibernate.dao.ModelDAO;
import ddep.hibernate.entity.MorphModel;
import ddep.morphology.mystem.Gate;
import ddep.morphology.mystem.types.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * Created by Arol on 15.02.2016.
 */
public class Morphalyze {

    //путь к файлу, содержащему URL целевых источников
    String dirTargetSourcesList = "";
    String nameTargetSourcesList = "";

    //путь к директории, в которой будут размещены рабочие файлы морфализатора
    String morphalyzeWorkDir = "";
    String morphalyzeWorkDir_work = "";
    String morphalyzeWorkDir_result = "";

    //имя файла для сохранения текста интернет-страницы
    String namePageText = "";
    //имя файла для сохранение разбора текста
    String nameMorphRez = "";

    //префикс для навигационных файлов
    String navFilePrefix = "";
    //префик для информационных файлов
    String infFilePrefix = "";

    String resultNavFilePath = "";
    String aboveResultNavFilePath = "";
    String resultInfFilePath = "";
    String aboveResultInfFilePath = "";

    String configFilePath = "";

    public Morphalyze(String configFilePath) throws IOException {
        try {
            setValuesFromSettings(configFilePath);
            this.configFilePath = configFilePath;
        } catch (IOException ex) {
            throw new IOException("Ошибка при чтении файла конфигурации. " + ex.getMessage());
        }
    }

    public double[] runUno(String pageUrl) throws Exception {
        //определение имени ресурса
        System.out.println("Морфологический анализ страницы ["+pageUrl+"] начат.");
        double[] result;

        String resourceName = findResourceName(pageUrl);
        try{
             result = morph(pageUrl,morphalyzeWorkDir,morphalyzeWorkDir_work,morphalyzeWorkDir_result,namePageText,nameMorphRez,navFilePrefix+resourceName,"undef",resourceName);
        }catch (Exception ex){
            throw new Exception("Ошибка при проведении морфанализа текстового содержимого указанной страницы. " +ex.getMessage());
        }
        System.out.println("Морфологический анализ закончен. ");

        return result;
    }

    public void runCycle() throws IOException, InterruptedException {
        while (true)
        {
            List<String> pageURLs = new ArrayList<String>();
            try {
                pageURLs = getPathsFromFile(dirTargetSourcesList, nameTargetSourcesList);
            }catch (IOException ex){
                throw new IOException("Ошибка при чтении файла, содержащего список целевых источников. " + ex.getMessage());
            }
            String resourceName = "undef";
            for (String url : pageURLs)
            {
                //region получение имени источника для формирования имени файла результатов
                if (url.contains("mail.ru"))
                    resourceName = "mail";
                else if (url.contains("vesti"))
                    resourceName = "vesti";
                else if (url.contains("lenta.ru"))
                    resourceName = "lenta";
                else if (url.contains("slon.ru"))
                    resourceName = "slon";
                else if (url.contains("vedomosti.ru"))
                    resourceName = "vedomosti";
                else if (url.contains("gazeta.ru"))
                    resourceName = "gazeta";
                else if (url.contains("izvestia.ru"))
                    resourceName = "izvestia";
                else if (url.contains("rbc.ru"))
                    resourceName = "rbc";
                else
                    resourceName = findResourceName(url);
                //endregion
                try
                {
                    //морфологический разбор навигационной страницы
                    morph(url,morphalyzeWorkDir,morphalyzeWorkDir_work,morphalyzeWorkDir_result,namePageText,nameMorphRez,navFilePrefix+resourceName,"nav",resourceName);
                    //проход по всем новостям заданной страницы
                    if (true) {
                        //TODO print время начала обработки ссылок, соответствующих текущему источнику
                        System.out.println(new Date(System.currentTimeMillis()).toString());
                        //получение списка информационных страниц
                        List<String> infoURLs = getPageNewsLinks(url);
                        for(String infoUrl : infoURLs) {
                            //TODO print обрабатываемой строки
                            System.out.println(infoUrl);
                            try{
                                morph(infoUrl,morphalyzeWorkDir,morphalyzeWorkDir_work,morphalyzeWorkDir_result,namePageText,nameMorphRez,infFilePrefix+resourceName,"inf",resourceName);
                            }
                            catch(Exception ex){
                                Thread.sleep(1000*60);
                            }
                        }
                    }
                }
                catch (Exception ex){
                    System.out.println("Error in Morphalyze.java: method main(). " +ex.toString());
                    Thread.sleep(1000*60);
                }
            }
            //TODO print время завершения цикла обработки, уход в сон
            System.out.println(new Date(System.currentTimeMillis()).toString() + "GoToSleep");
            Thread.sleep(1000*60*60*2);
        }
    }

    private String findResourceName(String url){
        String resourceName = "undefined";

        String[] parts = url.split("\\.");
        //точка перед доменом страны
        if (parts.length == 1){
            resourceName = parts[0];
        }
        //первая точка после www
        else if (parts.length > 1){
            resourceName = parts[1];
        }
        return resourceName;
    }

    /**
     *
     * @param url адрес разбираемой страницы
     * @param dirWork директория, содержащая файлы, используемые в работе
     * @param dirWork_work папка, содержащая промежуточные файлы работы
     * @param dirWork_result папка, содержащая результаты работы
     * @param fileText имя файла для сохранения текста url-страницы (без расширения)
     * @param fileMorph имя файла для сохранения разбора fileText (без расширения)
     * @param fileRes базовое имя файла для сохранения результатов (в зависимости от типа к нему добавляется приставка result_ или above_result_) (без расширения) (например, inf или inf_mail
     * @throws IOException
     * @throws InterruptedException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private double[] morph(String url,
                              String dirWork, String dirWork_work, String dirWork_result,
                              String fileText, String fileMorph, String fileRes, String type, String resource) throws Exception {
        Gate mystem = new Gate(configFilePath);
        //адрес файла, хранящего текст страницы
        String pageTextFile = dirWork + "\\" + dirWork_work + "\\" + fileText + ".txt";
        //адрес файла, хранящего разбор текста страницы
        String morphFile = dirWork + "\\" + dirWork_work + "\\" + fileMorph + ".xml";
        //адрес файла, хранящего результат работы
        String resultFilePath = dirWork + "\\" + dirWork_result + "\\" + "result_"+fileRes+".txt";
        //адрес файла, хранящего приведённый результат работы
        String aboveResultFilePath = dirWork + "\\" + dirWork_result + "\\" + "above_result_" + fileRes + ".txt";

        try {
            reInitFile(pageTextFile);
            reInitFile(morphFile);
        } catch (IOException e) {
            throw  new IOException("Ошибка при ре-инициализации рабочего файла. " + e.getMessage());
        }

        try {
            initResultFile(resultFilePath, false);
            initResultFile(aboveResultFilePath, true);
        } catch (IOException e) {
            throw  new IOException("Ошибка при инициализации файла результатов. " + e.getMessage());
        }

        //получение текста страницы
        String morphedXML = null;
        try {
            morphedXML = getPageBodyText(url);
        } catch (IOException e) {
            throw  new IOException("Ошибка при получении текста интернет-страницы. " + e.getMessage());
        }

        //сохранение текста страницы в файле
        writeStrToFile(morphedXML,pageTextFile);

        //запуск морфанализатора
        try {
            mystem.execute(pageTextFile, morphFile);
        } catch (Exception ex) {
            throw  new Exception("Ошибка при выполнении морфанализа Mystem. " + ex.getMessage());
        }

        //запуск парсера
        Result parseResult = null;
        try {
            parseResult = mystem.parseMystemXML(morphFile, null);
        } catch (Exception ex) {
            throw  new Exception("Ошибка при разборе результатов морфанализа. " + ex.getMessage());
        }

        //запись результатов работы парсера в файл
        writeStrToFile(url+"\t"+parseResult.getResultAsRow(),resultFilePath);

        //запись результатов работы парсера в БД
        try {
            MorphModel morphModel = Entity.mResultToModel(parseResult, type, resource, url, morphedXML);
            ModelDAO modelDAO = new ModelDAO();
            modelDAO.createPageModel(morphModel);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        //запись приведённых результатов работы парсера в файл
        writeStrToFile(url+"\t"+parseResult.getResultArrAsRow(parseResult.getAboveResult()),aboveResultFilePath);

        return parseResult.getAboveResult();
    }

    private String getPageBodyText(String url) throws IOException {
        //получение документа по URL
        Document page = Jsoup.connect(url).get();
        //получение элемента body
        Element content = page.select("body").first();
        //получение текста страницы
        String contentText = content.text();
        return contentText;
    }

    /**
     *  Получение ссылок, расположенных на странице навигации. Предусмотрен примитивный отбор по тегам
     * @param url
     * @return
     * @throws IOException
     */
    private List<String> getPageNewsLinks(String url) throws IOException {
        String selectStr = "";
        List<String> linksList = new ArrayList<String>();
        Document page = Jsoup.connect(url).get();
        Elements links = null;
        if(url.contains("mail.ru")) {
            selectStr = "a[href].news__list__item__link";
            links = page.select(selectStr);
        }
        else if(url.contains("ria.ru"))
        {
            links = page.select("a[href]");
        }
        else
            links = page.select("a[href]");

        for (Element link : links)
            linksList.add(link.attr("abs:href"));

        //TODO print вывод всех ссылок соответствующего url
        //System.out.println(Arrays.toString(linksList.toArray()));
        return linksList;
    }

    /**
     * Получение списка URL из файла
     * @param filePath директория файла
     * @param fileName имя файла
     * @return
     * @throws IOException
     */
    private List<String> getPathsFromFile (String filePath, String fileName) throws IOException{
        List<String> filesPaths = new ArrayList<String>();
        File f = new File(filePath + File.separator + fileName);
        BufferedReader fin = new BufferedReader(new FileReader(f));
        try{
            //если файла не существует - FileNotFoundException
            if (!f.exists())
            {
                throw new FileNotFoundException("File ("+filePath+File.separator+fileName+") not found.");
            }
            String line;
            while ((line = fin.readLine()) != null)
            {
                filesPaths.add(line);
            }
        }
        catch(IOException ex)
        {
            throw new IOException("IOException in getPathsFromFile method"+ex.getMessage());
        }
        finally
        {
            fin.close();
        }
        return filesPaths;
    }

    /**
     * Проверяет существование файла результатов с путём path, если файл не существует - создаёт его и формирует заголовок
     * @param path путь к файлу
     * @param isAbove true - если файл приведённых результатов; false - если файл обычных результатов
     * @throws IOException
     */
    private void initResultFile(String path, boolean isAbove) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            createFile(file);
            //формирование заголовка таблицы
            if(isAbove)
                writeStrToFile(Result.getAboveHeadTableStr(),path);
            else
                writeStrToFile(Result.getHeadTableStr(),path);
        }
    }

    private void reInitFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()){
            file.delete();
        }
        try {
            createFile(file);
        }catch (Exception ex){
            throw new IOException("Ошибка при создании файла по адресу ["+path+"]. " + ex);
        };
    }

    /**
     *  Если файл не найден - создаёт файл и путь к нему
     * @param file
     */
    private void createFile(File file){
        File parent = file.getParentFile();
        if(!parent.exists() && !parent.mkdirs()){
            throw new IllegalStateException("Не удаётся создать директорию: " + parent);
        }
    }

    private String writeStrToFile (String inputStr, String outputFile)
    {
        Writer writer = null;
        try{
            writer = new FileWriter(outputFile,true);
            writer.write(inputStr);
            writer.write("\r\n");
            writer.flush();
        }
        catch(Exception ex){
            System.out.println("Ошибка в методе writeStrTOFile. "+ex.getMessage());
        }
        finally{
            if (writer != null)
                try{
                    writer.close();
                }
                catch(IOException ex){}
        }
        return outputFile;
    }

    /**
     * Получение настроек из файла
     * @param configPath путь к файлу настроек
     * @throws IOException
     */
    private void setValuesFromSettings(String configPath) throws IOException {
        FileInputStream fis;
        Properties properties = new Properties();
        try{
            //TODO доработать: выброc ошибок если какая-либо настройка не задана
            fis = new FileInputStream(configPath);
            properties.load(fis);
            dirTargetSourcesList = properties.getProperty("morphalyze.dirTargetSourcesList");
            nameTargetSourcesList = properties.getProperty("morphalyze.nameTargetSourcesList");
            morphalyzeWorkDir = properties.getProperty("morphalyze.morphalyzeWorkDir");
            morphalyzeWorkDir_work = properties.getProperty("morphalyze.morphalyzeWorkDir.work");
            morphalyzeWorkDir_result = properties.getProperty("morphalyze.morphalyzeWorkDir.result");
            namePageText = properties.getProperty("morphalyze.namePageText");
            nameMorphRez = properties.getProperty("morphalyze.nameMorphRez");
            navFilePrefix = properties.getProperty("morphalyze.navFilePrefix");
            infFilePrefix = properties.getProperty("morphalyze.infFilePrefix");
            resultNavFilePath = properties.getProperty("morphalyze.resultNavFilePath");
            aboveResultNavFilePath = properties.getProperty("morphalyze.aboveResultNavFilePath");
            resultInfFilePath = properties.getProperty("morphalyze.resultInfFilePath");
            aboveResultInfFilePath = properties.getProperty("morphalyze.aboveResultInfFilePath");
        }catch (IOException e){
            throw new IOException("Error. Файл настроек на найден. " + e.getMessage());
        }
    }

    /*private static String getFileBodyText(String filePath) throws IOException
    {
      File htmlFile = new File(filePath);
      //получение документа по URL
      Document page = Jsoup.parse(htmlFile,"UTF-8","http://example.com/");
      //получение элемента body
      Element content = page.select("body").first();
      //Element content = page.select("body").first();
      //получение текста страницы
      String contentText = content.text();
      return contentText;
    }*/
}
