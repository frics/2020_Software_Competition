package kr.ac.ssu.myrecipe.ServerConnect;

public class URLs {
    private static final String ROOT_URL = "http://13.209.6.94/UserApi.php?apicall=";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "signin";
    public static final String URL_DB_BACKUP = "http://13.209.6.94/backUpUserRefrigerator.php";
    public static final String URL_GET_TAG ="http://13.209.6.94/getTag.php";
}