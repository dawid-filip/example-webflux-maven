package com.df.util;

import java.util.List;
import java.util.stream.Collectors;

public class RequestUtility {

    public static Long convertTextToNumber(String valueText) {
        boolean isNumber = valueText
                .chars()
                .allMatch(Character::isDigit);

        return isNumber ?
                Long.parseLong(valueText) : null;
    }

    public static List<Long> covertTextsToNumbers(List<String> valueTexts) {
        List<Long> ids = valueTexts.stream()
                .filter(textId -> convertTextToNumber(textId) != null)
                .map(id -> Long.parseLong(id))
                .collect(Collectors.toList());

        return ids == null || ids.isEmpty() ?
                List.of() : ids;
    }

}
