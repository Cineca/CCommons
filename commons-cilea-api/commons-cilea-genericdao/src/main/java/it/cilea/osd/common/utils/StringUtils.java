package it.cilea.osd.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils
{
    public static String convertStreamToString(InputStream is, String encoding)
            throws IOException
    {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        if (is != null)
        {
            StringBuilder sb = new StringBuilder();
            String line;

            try
            {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, encoding));
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
            }
            finally
            {
                is.close();
            }
            return sb.toString();
        }
        else
        {
            return "";
        }
    }
}
