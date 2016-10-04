package ddep.utils.test;


import ddep.utils.files.FileHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Arol on 15.02.2016.
 */
public class MakeGoodFile {
    public static void main(String[] args)
            throws FileNotFoundException, IOException {
        String dir = "C:\\asper\\Mystem\\result\\";

        String[] badLinks = new String[]{"2.russia.tv/video","auto.vesti.ru","bestrussia.tv","culture.ru",
                "filmpro.ru","france.vesti","hockey.vesti","instagram","iroman.tv",
                "istoriya.tv","kanalsport","vgtrk","multkanal","narodnayakarta","radiovesti.ru/programs//direc",
                "rudetective.tv","rusroman","russia.tv","russia2.tv","russiahd.tv",
                "tvkultura.ru","twitter.com","vera.vesti","vesti7.ru","vk.com","bk-tv.ru",
                "cultradio.ru","culture","filmpro","facebook","m24.ru/videos","moya-planeta.ru","odnoklassniki",
                "radiomayak","radiorus","rambler.ru","rtr-planeta","russia.tv","tvkultura.ru",
                "vesti.ru/broadcasts","vesti.ru/section.html","plus.google.com","vesti.ru/videos",
                "vestifinance","vesti-moscow","youtube","vesti.rss","&cid","sportodin"};
        //mail "cars.mail.ru"
        String inFilePath = dir+"above_result_inf_vesti.txt";
        String outFilePath = dir+"good\\good_inf_vesti.txt";

        File outFile = new File(outFilePath);
        outFile.createNewFile();

        FileHandling.writeToGoodFile(inFilePath, outFilePath, badLinks);

    }
}
