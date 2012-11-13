/**
 * Cilea Commons Framework
 * 
 * Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
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
