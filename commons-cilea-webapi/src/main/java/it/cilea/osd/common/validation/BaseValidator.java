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
package it.cilea.osd.common.validation;

import it.cilea.osd.common.constants.Constants;
import it.cilea.osd.common.model.BaseObject;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

public abstract class BaseValidator implements Validator
{
    private static Log log = LogFactory.getLog(BaseValidator.class);

    private Class clazz;

    public boolean supports(Class clazz)
    {
        return BaseObject.class.isAssignableFrom(clazz);
    }

    /**
     * Get the FieldError validation message from the underlying MessageSource
     * for the given fieldName.
     * 
     * @param errors
     *            The validation errors.
     * @param fieldName
     *            The fieldName to retrieve the error message from.
     * @return The validation message or an empty String.
     */
    protected String getValidationMessage(Errors errors, String fieldName)
    {
        String message = "";
        FieldError fieldError = errors.getFieldError(fieldName);
        if (fieldError != null)
        {
            String errore = fieldError.getCode();
            // FIXME localize it
            ResourceBundle msgs = ResourceBundle
                    .getBundle(Constants.BUNDLE_KEY);
            message = msgs.getString(errore);
        }
        return message;
    }

    protected String getValidationMessage(String errors)
    {
        String message = "";
        if (errors != null && errors.length() != 0)
        {
            // FIXME localize it
            ResourceBundle msgs = ResourceBundle
                    .getBundle(Constants.BUNDLE_KEY);
            message = msgs.getString(errors);
        }
        return message;
    }

    public Class getClazz()
    {
        return clazz;
    }

    public void setClazz(Class clazz)
    {
        this.clazz = clazz;
    }
}
