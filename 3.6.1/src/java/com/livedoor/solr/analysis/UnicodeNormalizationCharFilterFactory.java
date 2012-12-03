package com.livedoor.solr.analysis;

import com.ibm.icu.text.Normalizer;
import com.livedoor.lucene.analysis.UnicodeNormalizationCharFilter;
import java.util.Map;
import org.apache.lucene.analysis.CharStream;
import org.apache.solr.analysis.BaseCharFilterFactory;

/**
 * Creates new instances of UnicodeNormalizationCharFilter.
 */
public class UnicodeNormalizationCharFilterFactory extends BaseCharFilterFactory
{
    public static final String ARG_MODE = "mode";
    public static final String MODE_NONE = "none";
    public static final String MODE_NFD = "nfd";
    public static final String MODE_NFKD = "nfkd";
    public static final String MODE_NFC = "nfc";
    public static final String MODE_NFKC = "nfkc";
    public static final String MODE_FCD = "fcd";

    private Normalizer.Mode mode = null;

    @Override
    public void init(Map<String, String> args)
    {
        super.init(args);
        String formOpt = args.get(ARG_MODE);
        if (MODE_NONE.equals(formOpt))
        {
            mode = Normalizer.NONE;
        }
        else if (MODE_NFD.equals(formOpt))
        {
            mode = Normalizer.NFD;
        }
        else if (MODE_NFKD.equals(formOpt))
        {
            mode = Normalizer.NFKD;
        }
        else if (MODE_NFC.equals(formOpt))
        {
            mode = Normalizer.NFC;
        }
        else if (MODE_NFKC.equals(formOpt))
        {
            mode = Normalizer.NFKC;
        }
        else if (MODE_FCD.equals(formOpt))
        {
            mode = Normalizer.FCD;
        }
        else
        {
            mode = Normalizer.NFC;
        }
    }

    public CharStream create(CharStream input)
    {
        return new UnicodeNormalizationCharFilter(input, mode);
    }
}

