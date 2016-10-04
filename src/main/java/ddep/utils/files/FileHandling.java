package ddep.utils.files;

import java.io.*;

/**
 * Created by Arol on 15.02.2016.
 */
public class FileHandling {
    /**
     * Получение параметров из файла
     * @param filePath
     * @return float[i][j]; i - строки; j - столбцы
     * @throws FileNotFoundException
     */
    public static float[][] getParamsFromFile(String filePath)
            throws FileNotFoundException
    {
        float[][] result;
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException("Файл ("+filePath+") не найден. ");

        result = new float[getFileRowsCount(filePath) - 1][getFileParamsCount(filePath) - 1];

        //reader для работы с файлом
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try
        {
            String line;
            int curRowN = 0;
            String paramsStr = reader.readLine();
            //прочёл строку
            while ((line = reader.readLine()) != null)
            {
                String[] params = line.split("\t");
                //по всем параметрам строки не считая первого (имя источника)
                for (int i = 1; i < params.length; i++) {
                    result[curRowN][i-1] = Float.parseFloat(params[i]);
                }
                curRowN++;
            }
        } catch (IOException e) {
        }
        return result;
    }

    /**
     * Возвращает число строк в файле
     * @param filePath путь к файлу
     * @return 0 - если строк нет или произошла ошибка при чтении файла (кроме "не найден")
     * @throws FileNotFoundException
     */
    private static int getFileRowsCount(String filePath)
            throws FileNotFoundException
    {
        int count = 0;
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException("Файл ("+filePath+") не найден. ");
        //reader для работы с файлом
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                count++;
            }
        }
        catch (IOException e)
        {
            count = 0;
        }
        finally
        {
            try{reader.close();}catch(IOException ex){}
        }
        return count;
    }

    /**
     * Возвращает число параметров в файле (по первой строке, разделитель параметров - \t)
     * @param filePath путь к файлу
     * @return 0 - если строк нет или произошла ошибка при работе с файлом
     * @throws FileNotFoundException
     */
    private static int getFileParamsCount(String filePath)
            throws FileNotFoundException
    {
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException("Файл ("+filePath+") не найден. ");
        //reader для работы с файлом
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        try{
            line = reader.readLine();
        }
        catch (IOException e){line = "";}
        finally
        {
            try{reader.close();}catch(IOException ex){}
        }
        String[] params = line.split("\t");
        return params.length;
    }

    /**
     * Запись в файл
     * @param filePath путь к файлу
     * @param val строка для записи
     */
    public static void writeToFile(String filePath, String val)
    {
        Writer writer = null;
        try{
            writer = new FileWriter(filePath,true);
            writer.write(val);
            writer.flush();
        }
        catch(Exception ex){
            System.out.println("Exception in writeToFile method. "+ex.getMessage());
        }
        finally{
            if (writer != null)
                try{
                    writer.close();
                }
                catch(IOException ex){}
        }
    }

    /**
     * Перезапись исходного файла приведённых значений в нужный вид
     * @param inFilePath путь к исходному файлу приведённых значений
     * @param outFilePath путь к результирующему файлу
     * @param badLinks массив ссылок для исключения
     */
    public static void writeToGoodFile(String inFilePath, String outFilePath, String[] badLinks)
            throws FileNotFoundException
    {
        //номер столбца (от 0), содержащего данные по общему числу слов
        int countWordsIndex = 26;
        //получить параметры из файла
        File inFile= new File(inFilePath);
        if (!inFile.exists())
            throw new FileNotFoundException();

        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        try
        {
            String line;
            //получение строки параметров
            String paramsStr = reader.readLine();
            String[] paramsArr = paramsStr.split("\t");
            //запись в файл имён столбцов
            for (int i = 0; i < paramsArr.length; i++)
            {
                if (i != countWordsIndex)
                {
                    if (i != paramsArr.length - 1)
                        writeToFile(outFilePath,paramsArr[i]+"\t");
                    else
                        writeToFile(outFilePath,paramsArr[i]+"\r\n");
                }
            }
            //проход по всем строкам
            while ((line = reader.readLine()) != null)
            {
                String[] params = line.split("\t");
                //если ссылка не в списке плохих
                if (!isBadLink(params[0], badLinks))
                {
                    for (int i = 0; i < params.length; i++)
                    {
                        if (i != countWordsIndex)
                        {
                            if (i != params.length - 1)
                                writeToFile(outFilePath,params[i]+"\t");
                            else
                                writeToFile(outFilePath,params[i]+"\r\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    private static boolean isBadLink(String link, String[] badLinks)
    {
        for (String badLink : badLinks)
        {
            if (link.contains(badLink))
                return true;
        }
        return false;
    }
}
