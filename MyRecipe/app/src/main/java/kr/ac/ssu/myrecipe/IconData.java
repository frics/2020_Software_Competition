package kr.ac.ssu.myrecipe;

import java.util.HashMap;
import java.util.Map;

public class IconData {
    public static Map<String, Integer> textToicon = new HashMap<String, Integer>(){
        {
            put("과일", R.drawable.category_1);
            put("채소",R.drawable.category_2);
            put("쌀/잡곡",R.drawable.category_3);
            put("견과/건과",R.drawable.category_4);
            put("축산/계란", R.drawable.category_5);
            put("수산물/건어", R.drawable.category_6);
            put("생수/음료", R.drawable.category_7);
            put("커피/차", R.drawable.category_8);
            put("초콜릿/시럽", R.drawable.category_9);
            put("면/통조림", R.drawable.category_10);
            put("반찬/샐러드", R.drawable.category_11);
            put("냉동/간편요리", R.drawable.category_12);
            put("유제품/아이스크림", R.drawable.category_13);
            put("가루/오일", R.drawable.category_14);
            put("소스", R.drawable.category_15);
            put("카테고리 없음", R.drawable.ic_question_24dp);
        }
    };
    public static Map<Integer ,String> iconTotext = new HashMap<Integer,String>(){
        {
            put( R.drawable.category_1, "과일");
            put(R.drawable.category_2, "채소");
            put(R.drawable.category_3, "쌀/잡곡");
            put(R.drawable.category_4, "견과/건과");
            put(R.drawable.category_5, "축산/계란");
            put( R.drawable.category_6, "수산물/건어");
            put(R.drawable.category_7, "생수/음료");
            put(R.drawable.category_8, "커피/차");
            put(R.drawable.category_9, "초콜릿/시럽");
            put(R.drawable.category_10, "면/통조림");
            put(R.drawable.category_11, "반찬/샐러드");
            put(R.drawable.category_12, "냉동/간편요리");
            put(R.drawable.category_13, "유제품/아이스크림");
            put(R.drawable.category_14, "가루/오일");
            put(R.drawable.category_15, "소스");
            put(R.drawable.ic_question_24dp, "카테고리 없음");
        }
    };
    public static Integer[] iconList = {
            R.drawable.category_1, R.drawable.category_2,
            R.drawable.category_3, R.drawable.category_4,
            R.drawable.category_5, R.drawable.category_6,
            R.drawable.category_7, R.drawable.category_8,
            R.drawable.category_9, R.drawable.category_10,
            R.drawable.category_11, R.drawable.category_12,
            R.drawable.category_13, R.drawable.category_14,
            R.drawable.category_15, R.drawable.ic_question_24dp
    };
    public static String[] textList = {
            "과일", "채소","쌀/잡곡","견과/건과",
            "축산/계란", "수산물/건어","생수/음료","커피/차",
            "초콜릿/시럽", "면/통조림","반찬/샐러드","냉동/간편요리",
            "유제품/아이스크림", "가루/오일","소스","카테고리 없음"
    };
}
