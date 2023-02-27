package com.sptp.backend.common;

import com.sptp.backend.common.exception.CustomException;
import com.sptp.backend.common.exception.ErrorCode;

import java.util.*;

public class KeywordMap {

    public static final HashMap<String, Integer> map = new HashMap<>();

    private static final String[] keywordNames = {null,
            "유화", "심플한", "세련된", "모던한", "화려한",
            "변화의", "비판적인", "젊은", "자유로운", "다양한",
            "개성적인","소박한", "율동적인", "편안한", "포근한",
            "자연적인","강한", "다이나믹한", "차가운", "새로운",
            "안정된", "고급스러운", "수수한", "탁한", "우울한",
            "여유있는", "우아한", "단순한", "선명한", "투명한"};

    static {
        for (int keywordId = 1; keywordId < keywordNames.length; keywordId++) {
            map.put(keywordNames[keywordId], keywordId);
        }
    }
    public static String getKeywordName(Integer keywordId) {
        if (keywordId <= 0 || keywordId >= keywordNames.length) {
            return "";
        }

        return keywordNames[keywordId];
    }

    public static void checkExistsKeyword(String key) {
        if (!KeywordMap.map.containsKey(key)) {
            throw new CustomException(ErrorCode.NOT_FOUND_KEYWORD);
        }
    }
}
