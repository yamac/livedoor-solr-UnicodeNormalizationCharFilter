package com.livedoor.lucene.analysis;

import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.UCharacterIterator;
import java.io.IOException;
import java.io.Reader;
import java.text.CharacterIterator;
import org.apache.lucene.analysis.charfilter.BaseCharFilter;

public class UnicodeNormalizationCharFilter extends BaseCharFilter
{
    private Normalizer.Mode mode;
    private boolean read;
    private StringBuffer sb;
    private Normalizer normalizer;
    private int outIndex;
    
    public UnicodeNormalizationCharFilter(Reader in, Normalizer.Mode mode)
    {
        super(in);
        this.mode = mode;
        read = false;
        outIndex = 0;
    }

    protected boolean readFully() throws IOException
    {
        if (read)
        {
            return false;
        }
        sb = new StringBuffer();
        for (;;)
        {
            int c = input.read();
            if (c == -1)
            {
                break;
            }
            sb.append((char)c);
        }
        normalizer = new Normalizer(UCharacterIterator.getInstance(sb), mode, 0);
        read = true;
        return true;
    }

    private int _read() throws IOException
    {
        readFully();
        int inIndex = normalizer.getIndex();
        int c = normalizer.next();
        if (c == Normalizer.DONE)
        {
            return -1;
        }
        addOffCorrectMap(outIndex, inIndex - outIndex);
        outIndex++;
        return c;
    }

    @Override
    public int read() throws IOException
    {
        return _read();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException
    {
        int numRead = 0;
        for (int i = off; i - off < len; i++)
        {
            int c = _read();
            if (c == -1)
            {
                break;
            }
            cbuf[i] = (char)c;
            numRead++;
        }
        return (numRead > 0 ? numRead : -1);
    }
}

