package com.dreiri.smarping.test.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.test.AndroidTestCase;

import com.dreiri.smarping.models.Info;

public class InfoTest extends AndroidTestCase {

    Info info;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        info = new Info();
    }

    public void testFormatDate() {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s[+-]\\d{4}");
        Matcher matcher = pattern.matcher(info.formatDate());
        assertTrue(matcher.matches());
    }

}
