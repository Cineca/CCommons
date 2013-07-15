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
package it.cilea.osd.common.controller;

import it.cilea.osd.common.constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;

public abstract class BaseAbstractController extends AbstractController {

	protected Log log = LogFactory.getLog(getClass());

	protected String method;

	private String detailsView;

	private String listView;

	private String errorView;

	public void setMethod(String method) {
		this.method = method;
	}

	public void saveMessage(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(
				Constants.MESSAGES_KEY);

		if (messages == null) {
			messages = new ArrayList();
		}

		messages.add(msg);
		request.getSession().setAttribute(Constants.MESSAGES_KEY, messages);
	}

	public void removeLastMessage(HttpServletRequest request) {
		List messages = (List) request.getSession().getAttribute(
				Constants.MESSAGES_KEY);

		if (messages == null) {
			messages = new ArrayList();
		}
		if (!messages.isEmpty()) {
			messages.remove(messages.size() - 1);
		}
		request.getSession().setAttribute(Constants.MESSAGES_KEY, messages);
	}

	/**
	 * Convenient method for getting a default i18n key's value.
	 * 
	 * @param msgKey
	 * 
	 * @return
	 */
	public String getText(String msgKey) {
		return getMessageSourceAccessor().getMessage(msgKey);
	}

	/**
	 * Convenience method for getting a i18n key's value. Calling
	 * getMessageSourceAccessor() is used because the RequestContext variable is
	 * not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, locale);
	}

	/**
	 * Convenient method for getting a i18n key's value with a single string
	 * argument.
	 * 
	 * @param msgKey
	 * @param arg
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, String arg, Locale locale) {
		return getText(msgKey, new Object[] { arg }, locale);
	}

	/**
	 * Convenience method for getting a i18n key's value with arguments.
	 * 
	 * @param msgKey
	 * @param args
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, Object[] args, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, args, locale);
	}

	public void setDetailsView(String detailsView) {
		this.detailsView = detailsView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public String getErrorView() {
		return errorView;
	}

	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

    public String getDetailsView()
    {
        return detailsView;
    }

    public String getListView()
    {
        return listView;
    }

}
