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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtils
{

    public static List<Element> getElementList(Element dataRoot, String name)
    {
        NodeList list = dataRoot.getElementsByTagName(name);
        List<Element> listElements = new ArrayList<Element>();
        for (int i = 0; i < list.getLength(); i++)
        {
            listElements.add((Element) list.item(i));
        }
        return listElements;
    }

    public static String getElementAttribute(Element dataRoot, String name,
            String attr)
    {
        NodeList nodeList = dataRoot.getElementsByTagName(name);
        Element element = null;
        if (nodeList != null && nodeList.getLength() > 0)
        {
            element = (Element) nodeList.item(0);
        }
        
        String attrValue = null;
        if (element != null)
        {
            attrValue = element.getAttribute(attr);
            if (StringUtils.isNotBlank(attrValue))
            {
                attrValue = attrValue.trim();
            }
            else
                attrValue = null;
        }
        return attrValue;
    }

    public static String getElementValue(Element dataRoot, String name)
    {
        NodeList nodeList = dataRoot.getElementsByTagName(name);
        Element element = null;
        if (nodeList != null && nodeList.getLength() > 0)
        {
            element = (Element) nodeList.item(0);
        }
        String elementValue = null;
        if (element != null)
        {
            elementValue = element.getTextContent();
            if (StringUtils.isNotBlank(elementValue))
            {
                elementValue = elementValue.trim();
            }
            else
                elementValue = null;
        }
        return elementValue;
    }

    /**
     * Restituisce il primo sottoelemeto di xmlRoot trovato con il nome
     * specificato
     * 
     * @param xmlRoot
     *            l'elemento in cui cercare (NOT null)
     * @param name
     *            il nome del sottoelemento da restituire
     * @return il primo sottoelemento trovato o null se non presente
     */
    public static Element getSingleElement(Element xmlRoot, String name)
    {
        NodeList nodeList = xmlRoot.getElementsByTagName(name);
        Element element = null;
        if (nodeList != null && nodeList.getLength() > 0)
        {
            element = (Element) nodeList.item(0);
        }
        return element;
    }

    /**
     * 
     * @param rootElement
     *            l'elemento in cui cercare
     * @param subElementName
     *            il nome del sottoelemento di cui estrarre il valore
     * @return una lista di stringhe contenente tutti i valori del sottoelemento
     *         cercato. Null se non sono presenti sottoelementi o l'elemento
     *         radice se null
     */
    public static List<String> getElementValueList(
            Element rootElement, String subElementName)
    {
        if (rootElement == null)
            return null;
        
        List<Element> subElements = getElementList(rootElement, subElementName);
        if (subElements == null)
            return null;
        
        List<String> result = new LinkedList<String>();
        for (Element el : subElements)
        {
            if (StringUtils.isNotBlank(el.getTextContent()))
            {
                result.add(el.getTextContent().trim());
            }
        }
        return result;
    }

    /**
     * root/subElement[]/field1, field2, fieldN
     * 
     * @param rootElement
     *            l'elemnto radice
     * @param subElementName
     *            il nome del sottoelemento di cui si vuole processare il
     *            contenuto
     * @param fieldsName
     *            uno o piu' sotto-sotto-elementi xml di cui si vuole estrarre il
     *            valore testuale
     * @return una lista di array di stringhe, la dimensione dell'array viene
     *         determinata dal numero di fields richiesti. Per ogni field viene
     *         inserita nella corrispondente posizione dell'array il valore
     *         testuale del primo soto-sotto-elemento xml trovato, null se non e'
     *         presente
     */
    public static List<String[]> getElementValueArrayList(
            Element rootElement, String subElementName, String... fieldsName)
    {
        if (rootElement == null)
            return null;
        
        List<Element> subElements = getElementList(rootElement, subElementName);
        if (subElements == null)
            return null;
        
        List<String[]> result = new LinkedList<String[]>();
        for (Element el : subElements)
        {
            String[] tmp = new String[fieldsName.length];
            for (int idx = 0; idx < fieldsName.length; idx++)
            {
                tmp[idx] = XMLUtils.getElementValue(el, fieldsName[idx]);
            }
            result.add(tmp);
        }
        return result;
    }
}
