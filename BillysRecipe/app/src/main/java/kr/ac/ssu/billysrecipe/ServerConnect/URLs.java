package kr.ac.ssu.billysrecipe.ServerConnect;

public class URLs {
    private static final String URL_USER = "http://13.209.6.94/UserApi.php?apicall=";
    public static final String URL_REGISTER = URL_USER + "signup";
    public static final String URL_LOGIN= URL_USER + "signin";

    public static final String URL_DB_BACKUP = "http://13.209.6.94/UserDBBackUp.php";
    public static final String URL_GET_TAG ="http://13.209.6.94/getTag.php";

    public static final String URL_SCRAP ="http://13.209.6.94/ScrapChange.php?apicall=";
    public static final String URL_SCRAP_CHANGE = URL_SCRAP + "scrapChange";
    public static final String URL_GET_SCRAP_COUNT = URL_SCRAP + "getScrapCount";
}