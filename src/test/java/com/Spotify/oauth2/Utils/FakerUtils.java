package com.Spotify.oauth2.Utils;

import com.mifmif.common.regex.Generex;

public class FakerUtils {

    public static String generatePlayListName() {
        Generex generex = new Generex("^[A-Za-z]{5,30}$");
        return generex.random();
    }

    public static String generateDescription() {
        Generex generex = new Generex("^[A-Za-z]{20,100}$");
        return generex.random();
    }

    public  static  String generatePlayListNameWithSpecialCharachters() {
        Generex generex = new Generex("^[A-Za-z!@#$%^&*()_+]{5,30}$");
        return generex.random();
    }

    public static String generateDescriptionWithSpecialCharachters() {
        Generex generex = new Generex("^[A-Za-z!@#$%^&*()_+]{20,100}$");
        return generex.random();
    }
    public static String createPlayListNameOfMaxLength(){
        Generex generex = new Generex("^[A-Za-z]{30}$");
        return generex.random();
    }
}
