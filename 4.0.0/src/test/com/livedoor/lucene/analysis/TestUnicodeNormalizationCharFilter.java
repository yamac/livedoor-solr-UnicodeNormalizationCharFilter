package com.livedoor.lucene.analysis;

import com.ibm.icu.text.Normalizer;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;

/**
 * Tests for {@link UnicodeNormalizationCharFilter}
 */
public class TestUnicodeNormalizationCharFilter extends BaseTokenStreamTestCase
{
    public void testBasics() throws IOException
    {
        String t, s;

        // Alphabetic and number should not be changed.
        t = "Alphabetic/Number-";
        s = "this is a test for UnicodeNormalizationCharFilter. 012-345-6789.";
        assertTrue("NFD  " + t + s, doNormalize(s, Normalizer.NFD, s));
        assertTrue("NFKD " + t + s, doNormalize(s, Normalizer.NFKD, s));
        assertTrue("NFC  " + t + s, doNormalize(s, Normalizer.NFC, s));
        assertTrue("NFKC " + t + s, doNormalize(s, Normalizer.NFKC, s));

        // Japanese.
        t = "Japanese Full-width-";
        s = "ｔｈｉｓ　ｉｓ　ａ　ｔｅｓｔ．これはテスト。０１２ー３４５ー６７８９とＡＢＣからＸＹＺ";
        assertTrue("NFD  " + t + s, doNormalize(s, Normalizer.NFD, s));
        assertTrue("NFKD " + t + s, doNormalize(s, Normalizer.NFKD,
            "this is a test.これはテスト。012ー345ー6789とABCからXYZ"));
        assertTrue("NFC  " + t + s, doNormalize(s, Normalizer.NFC, s));
        assertTrue("NFKC " + t + s, doNormalize(s, Normalizer.NFKC,
            "this is a test.これはテスト。012ー345ー6789とABCからXYZ"));

        t = "Japanese Half-width-";
        s = "this is a test.ｺﾚﾊﾃｽﾄ｡012-345-6789ﾄABCｶﾗXYZ";
        assertTrue("NFD  " + t + s, doNormalize(s, Normalizer.NFD, s));
        assertTrue("NFKD " + t + s, doNormalize(s, Normalizer.NFKD,
            "this is a test.コレハテスト。012-345-6789トABCカラXYZ"));
        assertTrue("NFC  " + t + s, doNormalize(s, Normalizer.NFC, s));
        assertTrue("NFKC " + t + s, doNormalize(s, Normalizer.NFKC,
            "this is a test.コレハテスト。012-345-6789トABCカラXYZ"));
    }

    public boolean doNormalize(String in, Normalizer.Mode mode, String answer) throws IOException
    {
        UnicodeNormalizationCharFilter reader =
            new UnicodeNormalizationCharFilter(new StringReader(in), mode);
        StringBuilder sb = new StringBuilder();
        int ch = -1;
        while ((ch = reader.read()) != -1)
        {
            sb.append((char)ch);
        }
        String fs = sb.toString();
        return fs.equals(answer);
    }
}

