package web;

import iptd.exceptions.WebProcessingException;
import iptd.web.BlackList;
import iptd.web.WebProcessing;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arol on 18.09.2016.
 */
public class WebProcessingTest {

    //@Test
    public void getLinks() throws IOException, URISyntaxException, WebProcessingException {
        //String url = "https://mail.ru/";
        String url = "http://www.vesti.ru/";
        List<String> links = WebProcessing.getLinksFromPage(url);
        System.out.print("Size links list: " + links.size());
        for (String str : links)
            System.out.println(str);
    }

    @Test
    public void testRegexp(){
        String url = "http://vesti.ru/videos?cid=4141";

        Pattern pattern = Pattern.compile("https?://w*\\.?.*.[a-zA-Z]{2,3}/videos.*");
        Matcher matcher = pattern.matcher(url);
        System.out.println(matcher.matches());

        BlackList bl = new BlackList();
        System.out.println(bl.isUrlInBlackList(url));

    }

    @Test
    public void getLinksWithBlackListPrint() throws IOException, URISyntaxException, WebProcessingException {
        //String url = "https://mail.ru/";
        String url = "http://www.vesti.ru/";
        List<String> links = WebProcessing.getLinksFromPage(url);
        System.out.print("Size links list: " + links.size());
        BlackList blackListMail = new BlackList();
        for (String str : links)
            System.out.println(!blackListMail.isUrlInBlackList(str) + "      " + str);

    }

    //@Test
    public void getLinksWithBlackList() throws IOException, URISyntaxException, WebProcessingException {
        String url = "https://mail.ru/";
        //String url = "http://www.vesti.ru/";
        List<String> links = WebProcessing.getLinksFromPage(url);
        System.out.print("Size links list: " + links.size());
        BlackList blackListMail = new BlackList();
        for (String str : links)
            if (!blackListMail.isUrlInBlackList(str))
             System.out.println(str);
    }




}
