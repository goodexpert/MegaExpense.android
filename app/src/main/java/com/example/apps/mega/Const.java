package com.example.apps.mega;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Const {

    public static final String BASE_URL = "http://api.currencylayer.com/";
    public static final String ACCESS_KEY = "36847008d0790b2fd2f1e8c51c8d2176";

    public static final String PREF_DEFAULT_CURRENCY_CODE = "pref_default_currency_code";
    public static final String PREF_CURRENCY_CODES = "pref_currency_codes";

    public static final Set<String> DEFAULT_CURRENCY_CODES = new HashSet<>(Arrays.asList("USD", "NZD"));
}
