package kr.ac.ssu.billysrecipe.ServerConnect;

public class URLs {
    //이미지 전송 경로
    public static final String URL_IMG_TRANSFER = "http://13.209.6.94/ImageTransfer.php";

    //회원관리 경로
    private static final String URL_USER = "http://13.209.6.94/UserApi.php?apicall=";
    public static final String URL_REGISTER = URL_USER + "signup";
    public static final String URL_LOGIN= URL_USER + "signin";
    //DB 백업 경로
    public static final String URL_DB_BACKUP = "http://13.209.6.94/UserDBBackUp.php";
    //냉장고 재료 태그 획득 경로
    public static final String URL_GET_TAG ="http://13.209.6.94/getTag.php";

    //좋아요 버튼 관련 경로
    public static final String URL_SCRAP ="http://13.209.6.94/ScrapChange.php?apicall=";
    public static final String URL_SCRAP_CHANGE = URL_SCRAP + "scrapChange";
    public static final String URL_GET_SCRAP_COUNT = URL_SCRAP + "getScrapCount";
}