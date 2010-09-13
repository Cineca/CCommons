package it.cilea.osd.common.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ClasspathEntityResolver implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException {

        InputSource inputSource = null;
      
        if (systemId.startsWith("classpath://"))
        {
            try {
                InputStream inputStream =    
                    ClassLoader.getSystemResourceAsStream(
                        systemId.replaceFirst("classpath://", ""));
                if (inputStream == null)
                {
                    throw new FileNotFoundException();
                }
                inputSource = new InputSource(inputStream);
                return inputSource;
            } catch (Exception e) {
                // No action; just let the null InputSource pass through
                throw new SAXException("The entity "+publicId + " " + systemId +" was not found in the classpath");
            }
        }
        else
        // null is returned, for normal processing
        return inputSource;
    }
}
