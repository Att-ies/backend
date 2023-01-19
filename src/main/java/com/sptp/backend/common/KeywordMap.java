package com.sptp.backend.common;

import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;

import java.util.*;

public class KeywordMap {

    public static void checkExistsKeyword(String key) {
        if (!KeywordMap.map.containsKey(key)) {
            throw new CustomException(ErrorCode.NOT_FOUND_KEYWORD);
        }
    }

    public static String getKeywordName(Integer keywordId) {

        String keywordName = "";
        Set<Map.Entry<String, Integer>> entrySet = KeywordMap.map.entrySet();

        // KeywordName(key) 구하기
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue().equals(keywordId)) {
                keywordName = entry.getKey();
                break;
            }
        }

        return keywordName;
    }

    public static final HashMap<String, Integer> map = new HashMap<>();
    static {
        map.put("유화", 1);
        map.put("심플한", 2);
        map.put("세련된", 3);
        map.put("모던한", 4);
        map.put("화려한", 5);
        map.put("변화의", 6);
        map.put("비판적인", 7);
        map.put("젊은", 8);
        map.put("자유로운", 9);
        map.put("다양한", 10);
        map.put("개성적인", 11);
        map.put("소박한", 12);
        map.put("율동적인", 13);
        map.put("편안한", 14);
        map.put("포근한", 15);
        map.put("자연적인", 16);
        map.put("강한", 17);
        map.put("다이나믹한", 18);
        map.put("차가운", 19);
        map.put("새로운", 20);
        map.put("소박한", 21);
        map.put("안정된", 22);
        map.put("고급수러운", 23);
        map.put("수수한", 24);
        map.put("탁한", 25);
        map.put("우울한", 26);
        map.put("여유있는", 27);
        map.put("우아한", 28);
        map.put("단순한", 29);
        map.put("선명한", 30);
        map.put("투명한", 31);
        map.put("단순한", 32);
    }
}
