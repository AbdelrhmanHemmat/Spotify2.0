package com.Spotify.oauth2.Utils;

import com.mifmif.common.regex.Generex;

public class FakerUtils {

    public static String generatePlayListName() {
        Generex generex = new Generex("[A-Za-z0-9 .,_-]{5,40}");
        return generex.random();
    }

    public static String generateDescription() {
        Generex generex = new Generex("[A-Za-z0-9 .,!?'_\\-]{20,120}");
        return generex.random();
    }
}
