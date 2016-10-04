package ddep.program;


import ddep.morphology.Morphalyze;
import ddep.pageTypeClassifier.PageClassifier;

/**
 * Created by Arol on 16.02.2016.
 */
public class Program {

    /**
     *
                "-u",
               //"http://www.vesti.ru/",
               //"http://www.vesti.ru/doc.html?id=2745368",
               //"http://www.vesti.ru/doc.html?id=2745335",
     * @param args
     *         args[0] - путь к settings
     *         args[0] - вызываемая функция: m или morph  - морфоразбор; c или classif - классификатор
     *
     */
        public static void main(String[] args) throws Exception {
       /*args = new String[] {"-m",
                //"http://mail.ru/",
               //"https://news.mail.ru/politics/25532270/?frommail=1",
                "resources\\settings.properties"};*/

        args = new String[] {"-m",
                "-c",
                "D://Programming//Projects//diser//dep//src//main//resources//settings.properties"};
       /* args = new String[] {"-c",
                "-t",
                "resources//settings.properties"};*/
        /*http://radiovesti.ru/episode/show/episode_id/33484*/
       /* args = new String[] {"-c",
                "-u",
                "https://sport.mail.ru/news/football-eurocups/24865375/?frommail=1",
                "resources//settings.properties"};*/


        String resourceFilePath = "settings.properties";

        //args = new String[]{"C:\\asper\\sites.txt"};
        if ("-m".equalsIgnoreCase(args[0]) || "-morph".equals(args[0])){
            //region модуль морфологии
            Morphalyze morphalyze;
            if ("-u".equalsIgnoreCase(args[1])){
                String pageUrl = args[2];
                if (args.length == 4)
                    resourceFilePath = getConfigPath(args[3]);
                try{
                    morphalyze = new Morphalyze(resourceFilePath);
                    morphalyze.runUno(pageUrl);
                }catch (Exception ex){
                    throw new Exception("Ошибка при работе морфанализатора. " +ex.getClass() +" : " + ex.getMessage());
                }
            }else if ("-c".equalsIgnoreCase(args[1])){
                if (args.length == 3)
                    resourceFilePath = getConfigPath(args[2]);
                try{
                    morphalyze = new Morphalyze(resourceFilePath);
                    morphalyze.runCycle();
                }catch (Exception ex){
                    throw new Exception("Ошибка при работе морфанализатора в режиме цикла. " +ex.getClass() +" : " + ex.getMessage());
                }
            }
            //endregion
        }else if("-c".equalsIgnoreCase(args[0]) || "-classif".equals(args[0])){
            PageClassifier pageClassifier;
            //если классифицировать один сайт
            if ("-u".equalsIgnoreCase(args[1])){
                String pageUrl = args[2];
                if (args.length == 4)
                    resourceFilePath = getConfigPath(args[3]);
                try{
                    pageClassifier = new PageClassifier(resourceFilePath);
                    pageClassifier.classifyPage(pageUrl);
                }catch (Exception ex){
                    throw new Exception("Ошибка при работе классификатора. " +ex.getClass() +" : " + ex.getMessage());
                }
            }
            //если запустить классификатор в тестовом режиме
            else if ("-t".equalsIgnoreCase(args[1])){
                if (args.length == 3)
                    resourceFilePath = getConfigPath(args[2]);
                try{
                    pageClassifier = new PageClassifier(resourceFilePath);
                    pageClassifier.runClassifier();
                }catch(Exception ex){
                    throw new Exception("Ошибка при работе классификатора в режиме теста. " +ex.getClass() +" : " + ex.getMessage());
                }
            }


        }else if("-h".equalsIgnoreCase(args[0]) || "-help".equals(args[0]))
        {
            System.out.println("Available commands: ");
            System.out.println("  -m (-morph)");
            System.out.println("      -u URI Path_to_settings");
            System.out.println("      -c Path_to_settings");
            System.out.println("  -c (-classif)");
            System.out.println("      -u URI Path_to_settings");
            System.out.println("      -t Path_to_settings");
        }

    }

    private static String getConfigPath(String path){
        String configFilePath = "settings.properties";
        if (path.contains("settings.properties"))
            configFilePath = path;
        else{
            if (path.endsWith("\\") || path.endsWith("\\\\"))
                configFilePath = path + "settings.properties";
            else
                configFilePath = path + "\\settings.properties";
        }
        return configFilePath;
    }

}


