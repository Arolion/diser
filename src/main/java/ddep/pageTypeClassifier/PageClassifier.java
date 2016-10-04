package ddep.pageTypeClassifier;


import ddep.classifier.naiveBayes.BayesClassifier;
import ddep.classifier.naiveBayes.Classification;
import ddep.classifier.naiveBayes.Classifier;
import ddep.morphology.Morphalyze;
import ddep.utils.files.FileHandling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Arol on 15.02.2016.
 */
public class PageClassifier {
    //классификатор
    final Classifier<Float,String> bayes = new BayesClassifier<Float,String>();;

    String resourceFilePath = "";

    //TODO упростить параметры до папки и имён
    String learnFilePath_inf = "";
    String learnFilePath_nav = "";
    String testFilePath_inf = "";
    String testFilePath_nav = "";
    String resultFilePath = "";

    int signifParamsNubm = 10;

    //последовательность параметров, расположенных по степени их значимости
    //значимость определена на основе анализа (Алина)
    final static int[] paramsPriorities = new int[]{12,14,16,0,13,11,22,23,10,19,18,8,6,20,5,17,9,1,5,21,2,3,7,25,24,15};

    public PageClassifier(String configFilePath) throws IOException {
        resourceFilePath = configFilePath;
        try {
            setValuesFromSettings(configFilePath);
        } catch (IOException ex) {
            throw new IOException("Ошибка при чтении файла конфигурации. " + ex.getMessage());
        }
    }

    /**
     *  Запуск прогона классификатора для разного количества значимых параметров последовательно
     */
    public void runClassifier() throws FileNotFoundException {
        writeResFileHead(resultFilePath, paramsPriorities.length);

        float error;
        for (int i = 1; i <= paramsPriorities.length; i++)
        {
            //учитываемые в данной итерации параметры
            int[] curParams = Arrays.copyOfRange(paramsPriorities, 0, i);
            // печать номера итерации
            System.out.print(i + " ");
            error = defenitionTypeFromParameters(learnFilePath_inf, learnFilePath_nav, testFilePath_inf, testFilePath_nav, curParams);
            //запись результатов в файл
            String str = error + "\t" ;
            FileHandling.writeToFile(resultFilePath, str);
        }
        FileHandling.writeToFile(resultFilePath,"\r\t");
    }

    private float roundFloat(double number, int scale){
        float newFloat = new BigDecimal(number).setScale(scale,BigDecimal.ROUND_HALF_EVEN).floatValue();
        return newFloat;
    }

    public void classifyPage(String pageUrl) throws FileNotFoundException {
        //получить морфологический разбор
        List<Float> pageMorphParams = new ArrayList<Float>();
        double[] tmp_pageMorphParams;
        try {
            Morphalyze morphalyze = new Morphalyze(resourceFilePath);
            tmp_pageMorphParams = morphalyze.runUno(pageUrl);
        }catch(Exception ex){
            throw new FileNotFoundException("Ошибка при выполнении морфанализа. " + ex.getMessage());
        }
        //перевод double во float, заполненеие массива с результатами морфанализа
        for (int i = 0; i < tmp_pageMorphParams.length; i++){
            //окргуление, т.к. примеры округлены
            pageMorphParams.add(roundFloat(tmp_pageMorphParams[i],3));
        }
        //обучение сети Байеса
        int[] signifParams = Arrays.copyOfRange(paramsPriorities,0,signifParamsNubm);
        try {
            learnBayes(new String[]{learnFilePath_inf, learnFilePath_nav}, new String[]{"informational","navigational"},signifParams);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Ошибка при обучении классификатора Байеса. " + ex.getMessage());
        }

        Classification classif = bayes.classify(pageMorphParams);
        System.out.println("Результаты классификации: ");
        System.out.println("  тип страницы: " + classif.getCategory());
        System.out.println("  свойства: " + classif.getFeatureset());
        bayes.reset();
    }

    private void writeResFileHead(String resultFile, int paramsCount)
    {
        String str = "";
        for (int i = 1; i <= paramsCount; i++)
            str  += i + "\t\t";
        FileHandling.writeToFile(resultFile, str + "\r\n");
    }
    /**
     *
     * @param neededParams  индексы параметров, используемых классификатором Байеса
     * @throws FileNotFoundException
     */
    public float defenitionTypeFromParameters(String learnInfFile, String learnNavFile,String testInfFile, String testNavFile, int[] neededParams) throws FileNotFoundException {

        learnBayes(new String[]{learnInfFile,learnNavFile},new String[]{"informational","navigational"},neededParams);

        float error = runBayes(new String[]{testInfFile,testNavFile},new String[]{"informational","navigational"},neededParams);
        System.out.println("Вероятнось ошибки:     " + error);
        System.out.println();
        return error;

      /*
      Classification classiff = bayes.classify(x);
      System.out.println(classiff.getCategory());
      System.out.println(classiff.getProbability());
      System.out.println(classiff.getFeatureset());
      System.out.println(((BayesClassifier<Float,String>) bayes).classifyDetailed(x).toString());
      */
    }

    /**
     * Осуществляет обучение классификатора, основываясь на данных, содержащихся в файлах.
     * @param learnFilesPath массив путей к файлам, содержащим обучающие выборки
     * @param learnFilesCategory массив категорий, соответствующих обучающим файлам. learnFilesCategory[i] - категория выборки, содержащейся в learnFilesPath[i] - файле
     * @param neededParams массив индексов параметров, используемых для обучения, в порядке уменьшения значимости
     */
    private void learnBayes(String[] learnFilesPath, String[] learnFilesCategory, int[] neededParams)
            throws FileNotFoundException
    {
        bayes.reset();
        List<List<Float>> learnParams;
        Classification learnClassif;
        for(int i = 0; i < learnFilesPath.length; i++)
        {
            //System.out.println(learnFilesPath[i] + "   " + learnFilesCategory[i]);
            //получение обучающей выборки
            learnParams = getParamsList(FileHandling.getParamsFromFile(learnFilesPath[i]),neededParams);

            for (int j = 0; j < learnParams.size(); j++)
            {
                learnClassif = new Classification(learnParams.get(j),learnFilesCategory[i]);
                bayes.learn(learnClassif);
            }
        }
    }

    private float runBayes(String[] testFilesPath, String[] testFilesCategory, int[] neededParams)
            throws FileNotFoundException
    {
        List<List<Float>> testParams;
        int error = 0;
        int size = 0;

        for (int i =0; i < testFilesPath.length; i++)
        {
            testParams = getParamsList(FileHandling.getParamsFromFile(testFilesPath[i]),neededParams);
            int tmpError = 0;
            size += testParams.size();
            for (int j = 0; j < testParams.size(); j++) {
                //классификация
                Classification classif = bayes.classify(testParams.get(j));
                //проверка результатов классификации
                if (!testFilesCategory[i].equals(classif.getCategory()))
                    tmpError++;
            }
            error  += tmpError;
            System.out.println("Ошибок категории "+ testFilesCategory[i] +" :"+ tmpError + " из " + testParams.size());
        }
        return ((float)error / (float)size);
    /*
    String testParamsInfFile = "C:\\asper\\morphalyze\\Files\\v_inf_test.txt";
    String testParamsNavFile = "C:\\asper\\morphalyze\\Files\\v_nav_test.txt";

    int infErr = 0;
    int navErr = 0;

    //Classification classif;
    //по всем тестам в тестовой выборке

    for (int i = 0; i < testParamsInf.size(); i++) {
        Classification classif = bayes.classify(testParamsInf.get(i));
        if (!"informational".equals(classif.getCategory()))
           infErr++;
    }
    //testParamsInf.clear();
    //по всем тестам в тестовой выборке
    for (int i = 0; i < testParamsNav.size(); i++) {
        Classification classif = bayes.classify(testParamsNav.get(i));
        if (!"navigational".equals(classif.getCategory()))
          navErr++;
    }
     */

    }

    private List<List<Float>> getParamsList(float[][] array, int[] neededParams) {
        //listList.get(0) - параметры первого примера; listLists.get(0).get(0) - значение первого параметра
        List<List<Float>> listLists = new ArrayList<List<Float>>();

        //по всем строкам (примерам)
        for (int i = 0; i < array.length; i++)
        {
            List<Float> params = new ArrayList<Float>();
            //по всем интересующим параметрам
            for (int paramsNum = 0; paramsNum < neededParams.length; paramsNum++){
                params.add(array[i][neededParams[paramsNum]]);
            }
            listLists.add(params);
        }
      /*System.out.println("---");
      for (int paramsNum = 0; paramsNum < neededParams.length; paramsNum++)
        System.out.println(array[4][neededParams[paramsNum]]);
        System.out.println(listLists.get(4));
      System.out.println("---");*/

        return listLists;
    }

    private List<List<Float>> getLearnSamples(String[] files) {
        return null;
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
            learnFilePath_inf = properties.getProperty("classifier.learnFilePath_inf");
            learnFilePath_nav = properties.getProperty("classifier.learnFilePath_nav");
            testFilePath_inf = properties.getProperty("classifier.testFilePath_inf");
            testFilePath_nav = properties.getProperty("classifier.testFilePath_nav");
            resultFilePath  = properties.getProperty("classifier.resultFilePath");
            signifParamsNubm = Integer.parseInt(properties.getProperty("classifier.signifParamsNumb"));
        }catch (IOException e){
            throw new IOException("Error. Файл настроек на найден. " + e.getMessage());
        }
    }
}
