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
package it.cilea.osd.common.webflow;

import it.cilea.osd.common.constants.Constants;
import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.common.service.IPersistenceService;


import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.MessageSource;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class BaseFormAction<IA extends IPersistenceService> extends FormAction {
	
	protected IA applicationService;

	protected Log log = LogFactory.getLog(BaseFormAction.class);
	
	private String formObjectIDName;
 
	private Map<Class, PropertyEditor> customPropertyEditors;

	protected MessageSource messageSource = null;
	
	@Override
	/**
	 * Register the custom property editors as defined in the  
	 * customPropertyEditors map
	 */
	protected void registerPropertyEditors(PropertyEditorRegistry registry) {
		super.registerPropertyEditors(registry);
		if (customPropertyEditors != null) {
			for (Class propertyClass : customPropertyEditors.keySet()) {
				log.debug("Register customEditor "+ customPropertyEditors.get(propertyClass).getClass() + " for the "+propertyClass);
				registry.registerCustomEditor(propertyClass, customPropertyEditors
						.get(propertyClass));
			}
		}
	}

	@Override
	/**
	 * Restituisce l'oggetto prelevandolo dallo Scope definito dal FormObjectScope se esistente.
	 * Altrimenti cerca di ottenerlo dal db utilizzando, nello stesso Scope, il valore contenuto 
	 * nella variabile il cui nome e' definito dall'attributo FormObjectIDName (valorizzato di default
	 * a FormObjectName_id).
	 * In caso di insuccesso si comporta di come la FormAction standard di webflow.
	 * 
	 */
	protected Object createFormObject(RequestContext context) throws Exception {
		if (getFormObjectClass() == null)
		{
			log.debug("FormObjectClass Undefined! call the super createFormObject");
			return super.createFormObject(context);
		}
		MutableAttributeMap formScope = getFormObjectScope().getScope(context);
		Object formObject = formScope.get(getFormObjectName());
		//FIXME: verificare la correttezza di questo check! il createFormObject non dovrebbe sempre restituire una nuova istanza
		if (formObject != null)
		{
			log.debug("get the object from the scope " + getFormObjectScope().getLabel() );
			return formObject;
		}
		else 
		{
			log.debug("form object null");
			if (formObjectIDName == null) formObjectIDName = getFormObjectName()+"_id";
			log.debug("Object "+getFormObjectName()+" not found in the scope "+getFormObjectScope().getLabel());
			Integer formObjectID = formScope.getInteger(formObjectIDName, null);
			if (formObjectID != null)
			{
				log.debug("Retrieving the object "+ getFormObjectClass() + " from the DB - ID: "+formObjectID);
				return applicationService.get(getFormObjectClass(), formObjectID);
			}
			else
			{
				log.debug("Creating a new instance of "+ getFormObjectClass());
				return super.createFormObject(context);
			}
		}		
	}

	public void setApplicationService(IA applicationService) {
		this.applicationService = applicationService;
	}

	public void setFormObjectIDName(String formObjectIDName) {
		this.formObjectIDName = formObjectIDName;
	}

	public void setCustomPropertyEditors(
			Map<Class, PropertyEditor> customPropertyEditors) {
		this.customPropertyEditors = customPropertyEditors;
	}
	
	public Event refresh(RequestContext context) throws Exception
	{
		log.debug("Refreshing the object in the flow");
		Object objectRefresh = applicationService.refresh((Identifiable) getFormObject(context), getFormObjectClass());		
		getFormObjectScope().getScope(context).put(getFormObjectName(), objectRefresh);
		return success();
	}

	public void saveMessage(RequestContext context, String msg) {
		ServletExternalContext servletContext = (ServletExternalContext) context
				.getExternalContext();
		HttpServletRequest request = servletContext.getRequest();
		List messages = (List) request.getSession().getAttribute(
				Constants.MESSAGES_KEY);

		if (messages == null) {
			messages = new ArrayList();
		}

		messages.add(msg);
		request.getSession().setAttribute(Constants.MESSAGES_KEY, messages);
	}
	
	public String getText(String msgCode, Object[] args) {
		return msgCode;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
