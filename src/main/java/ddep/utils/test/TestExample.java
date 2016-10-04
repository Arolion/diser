package ddep.utils.test;



import ddep.utils.files.FileHandling;

import java.io.FileNotFoundException;

/**
 * Created by Arol on 15.02.2016.
 */
public class TestExample {
    public static void main(String[] args) throws FileNotFoundException {
        String dir = "C:\\asper\\Mystem\\result\\";
        String file = "above_result_inf_mail.ru.txt";

        float[][] array = FileHandling.getParamsFromFile(dir+file);

        for (int i = 0; i < array[0].length; i++){
            System.out.print(array[0][i] + "   ");
        }
    }
}

