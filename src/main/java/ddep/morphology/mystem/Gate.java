package ddep.morphology.mystem;


import ddep.morphology.mystem.types.Result;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * Created by Arol on 15.02.2016.
 */
public class Gate {
    private String mystemDir = "";
    private String mystemExe = "";
    private String mystemEncoding = "";

    public Gate(String configFilePath) throws IOException{
        try {
            setValuesFromSettings(configFilePath);
        } catch (IOException ex) {
            throw new IOException("Ошибка при чтении файла конфигурации. " + ex.getMessage());
        }
    }

    /**
     * Запуск mystem
     * @param fileInput путь к входному файлу для разбора
     * @param fileOutput путь к выходному файлу разбора
     * @return путь к XML-файлу с морфологическим разбором
     * @throws IOException
     */
    public String execute(String fileInput, String fileOutput)
            throws IOException, InterruptedException
    {
        Runtime r = Runtime.getRuntime();

        //получение команды запуска с параметрами (строка, т.е. иначе не воспринимается --format xml)
        String comandStr = getComandsStr(fileInput, fileOutput);
        //запуск mystem
        Process proc =r.exec(comandStr);

        //вывод сообщений и ошибок
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null)
            System.out.println(s);
        while ((s = stdError.readLine()) != null)
            System.out.println(s);

        proc.waitFor();
        return fileOutput;
    }

    /**
     * Разбор XML-файла, полученного от mystem
     * @param fileInput  xml-файл, результат работы mystem
     * @param fileOutput выходной файл разбора (не нужен)
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public Result parseMystemXML(String fileInput, String fileOutput)
            throws ParserConfigurationException, SAXException, IOException
    {
        File xmlFile = new File(fileInput);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);
        //получение всех node, содержащих слова
        NodeList words = doc.getElementsByTagName("ana");
        Parser xmlParser = new Parser();
        xmlParser.getResult().wordsNumb = words.getLength();
        xmlParser.getResult().uniqWordsNumb = getUniqWordsNumb(doc);

        int numbSameWords = 0;
        Set<String> sameWords = new HashSet<String>();

        //задание числа слов
        //по всем словам, содержащимся в файле разбора
        for (int i =0; i < words.getLength(); i++)
        {
            Node word = words.item(i);
            //получение атрибутов слова в виде строки
            NamedNodeMap attributes =  word.getAttributes();
            //подсчёт числа уникальных слов
            //если объект не был добавлен в hashset -  значит он там есть -> повторение использования слова

            if(isWordExist(attributes,sameWords)){
                numbSameWords++;
            }
            String morphStr = attributes.getNamedItem("gr").toString();
            xmlParser.parse(morphStr);
        }
        //System.out.println(" Повторений слов: " + numbSameWords + "   из  " + words.getLength());

        xmlParser.getResult().sameWords = numbSameWords;
        return xmlParser.getResult();
    }

    private boolean isWordExist(NamedNodeMap attributes, Set<String> words){
        //проверка, что слово - не предлог, союз, частица или междометие
        String morphStr = attributes.getNamedItem("gr").toString();
        //если слово - не частица, предлог, союз или междометие
        if (Parser.isRealWord(morphStr)){
            //если слово не может быть добавлено -> оно уже существует в тексте
            if (words.add(attributes.getNamedItem("lex").toString()) == false){
                return true;
            }
        }
        return false;
    }

    public int getUniqWordsNumb(Document doc) {
        NodeList words = doc.getElementsByTagName("w");
        List<String> uniqWords = new ArrayList<String>();
        for (int i=0; i < words.getLength(); i++) {
            String word = words.item(i).getTextContent();
            if (!uniqWords.contains(word))
                uniqWords.add(word);
        }
        return uniqWords.size();
    }

    private String[] getComandsArr(String fileInput, String fileOutput)
    {
        List<String> comandParams = new ArrayList<String>();
        //параметр запуска mystem
        comandParams.add(mystemDir + "\\" +mystemExe);
        //копировать весь ввод на вывод (включая промежутки)
        comandParams.add("-c");
        //печатать грамматическую информацию
        comandParams.add("-i");
        //склеивать информацию словоформ при одной лемме (c -i)
        comandParams.add("-g");
        //кодировка ввода-вывода
        //TODO кодировка
        comandParams.add("-e " + mystemEncoding);
        //comandParams.add("-e utf8");
        //контекстное снятие омонимии
        comandParams.add("-d");
        //формат вывода
        comandParams.add("--format xml");
        //входной файл
        comandParams.add(fileInput);
        //выходной файл
        comandParams.add(fileOutput);
        //вернуть список параметров, преобразованный в массив
        return comandParams.toArray(new String[comandParams.size()]);
    }

    private String getComandsStr(String fileInput, String fileOutput)
    {
        String[] comands= getComandsArr(fileInput, fileOutput);
        StringBuilder s = new StringBuilder("");
        for (int i=0; i < comands.length; i++)
        {
            s.append(comands[i]);
            if (i != comands.length - 1)
                s.append(" ");
        }

        return s.toString();
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
            mystemDir = properties.getProperty("mystem.dir");
            mystemExe = properties.getProperty("mystem.exe");
            mystemEncoding = properties.getProperty("mystem.encoding");
        }catch (IOException e){
            throw new IOException("Error. Файл настроек на найден. " + e.getMessage());
        }
    }


    /**
     * Устанавливает путь к каталогу, содержащему mystem.exe. По-умолчанию: "C:\\asper\\mystem".
     * @param dir путь к каталогу с mystem.exe
     */
    public void setMystemDir(String dir)
    {
        mystemDir = dir;
    }
}
