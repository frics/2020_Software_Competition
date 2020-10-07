package kr.ac.ssu.billysrecipe.ServerConnect;

public class URLs {
    //이미지 전송 경로
    public static final String URL_IMG_TRANSFER = "http://billysrecipe.tk/ImageTransfer.php";

    //회원관리 경로
    private static final String URL_USER = "http://billysrecipe.tk/UserApi.php?apicall=";
    public static final String URL_REGISTER = URL_USER + "signup";
    public static final String URL_LOGIN= URL_USER + "signin";
    //DB 백업 경로
    public static final String URL_DB_BACKUP = "http://billysrecipe.tk/UserDBBackUp.php";
    //냉장고 재료 태그 획득 경로
    public static final String URL_GET_TAG ="http://billysrecipe.tk/getTag.php";

    //좋아요 버튼 관련 경로
    public static final String URL_SCRAP ="http://billysrecipe.tk/ScrapChange.php?apicall=";
    public static final String URL_SCRAP_CHANGE = URL_SCRAP + "scrapChange";
    public static final String URL_GET_SCRAP_COUNT = URL_SCRAP + "getScrapCount";
}