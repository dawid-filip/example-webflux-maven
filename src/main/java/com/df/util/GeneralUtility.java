package com.df.util;

import java.util.concurrent.ThreadLocalRandom;

public class GeneralUtility {

    public static Long getRandomValue(int countMax) {
        return (long) ThreadLocalRandom.current().nextInt(1, countMax);
    }

}
