package com.basgeekball.smarping.test.models;

import android.test.AndroidTestCase;

import com.basgeekball.smarping.models.Info;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
