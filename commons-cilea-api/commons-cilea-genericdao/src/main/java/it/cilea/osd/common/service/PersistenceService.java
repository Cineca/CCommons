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
package it.cilea.osd.common.service;

import it.cilea.osd.common.core.HasTimeStampInfo;
import it.cilea.osd.common.core.ITimeStampInfo;
import it.cilea.osd.common.core.SingleTimeStampInfo;
import it.cilea.osd.common.core.TimeStampInfo;
import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.common.listener.NativePostDeleteEventListener;
import it.cilea.osd.common.listener.NativePostUpdateEventListener;
import it.cilea.osd.common.model.Identifiable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link IPersistenceService} implementations based on the {@link GenericDao}
 * framework
 * 
 * @author cilea
 * 
 */
public abstract class PersistenceService implements IPersistenceService 
{
    /**
     * A map of generic dao. The key is the canonical name of the model class
     * that the dao manage
     */
    private Map<String, GenericDao> modelDaos;
    
    /**
     * log4j category
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    private List<NativePostDeleteEventListener> listenerOnPostDelete;
    
    private List<NativePostUpdateEventListener> listenerOnPostUpdate;
    
    public List<NativePostDeleteEventListener> getListenerOnPostDelete()
    {
        return listenerOnPostDelete;
    }

    public void setListenerOnPostDelete(
            List<NativePostDeleteEventListener> listenerOnPostDelete)
    {
        this.listenerOnPostDelete = listenerOnPostDelete;
    }

    public List<NativePostUpdateEventListener> getListenerOnPostUpdate()
    {
        return listenerOnPostUpdate;
    }

    public void setListenerOnPostUpdate(
            List<NativePostUpdateEventListener> listenerOnPostUpdate)
    {
        this.listenerOnPostUpdate = listenerOnPostUpdate;
    }

    /**
     * Add timestamp information (creation, last update) to the object if it
     * supports them
     * 
     * @param <T>
     *            the Entity class
     * @param modelClass
     *            the Entity class
     * @param transientObject
     *            the transient object
     * @return true for creation operation, false for update operation
     */
	protected <T extends Identifiable> Boolean recordTimeStampInfo(Class<T> modelClass,
			T transientObject) {
		if (transientObject instanceof HasTimeStampInfo)
        {
			log.debug("Added record TimeStampInfo: modelClass: "+modelClass.getName()+" id: "+ transientObject.getId());
        	HasTimeStampInfo hrt = (HasTimeStampInfo) transientObject;
        	ITimeStampInfo rt = hrt.getTimeStampInfo();
        	if (rt == null) {
				rt = new TimeStampInfo();
			}
        	if (rt.getTimestampCreated() == null) {        		
        		rt.setInfoCreated(getCurrentTimeStampInfo());
        		return true;
        	}
        	else {
        		rt.setInfoLastModified(getCurrentTimeStampInfo());
        		return false;
        	}        	
        }
		return null;
	}
	
	/**
     * Build a new {@link SingleTimeStampInfo} using the current date
     * 
     * @return a new {@link SingleTimeStampInfo} using the current date
     */
	protected SingleTimeStampInfo getCurrentTimeStampInfo() {
		return new SingleTimeStampInfo(new Date());		
	}

	/**
	 * {@inheritDoc}
	 */
	public <T, PK extends Serializable> T get(Class<T> modelClass, PK pkey)
    {
    	GenericDao<T, PK> modelDao = modelDaos.get(modelClass.getName());   
    	log.debug("modelClass:"+modelClass);
    	log.debug("dao:"+modelDao);
        return modelDao.read(pkey);
    }

    
    /**
     * {@inheritDoc}
     */
    public <T extends Identifiable> void saveOrUpdate(Class<T> modelClass, T transientObject)
    {
    	final String modelClassName = modelClass.getName();
    	
    	log.debug("saveOrUpdate: "+modelClassName+ " id: "+transientObject.getId());
        GenericDao<T, ?> modelDao = modelDaos.get(modelClassName);    
        Boolean creation = recordTimeStampInfo(modelClass, transientObject);
        modelDao.saveOrUpdate(transientObject);        
        for(NativePostUpdateEventListener update : listenerOnPostUpdate) {
            update.onPostUpdate(transientObject);
        }
        log.debug("after saveOrUpdate id: "+transientObject.getId());
     }

    
    /**
     * {@inheritDoc}
     */
    public <T extends Identifiable> T merge(T oggetto,Class<T> classe) {    	
    	log.debug("merge: "+classe.getName()+" -id: "+oggetto.getId());
    	recordTimeStampInfo(classe, oggetto);
    	T result = getDaoByModel(classe).merge(oggetto);
    	log.debug("after merger id: "+oggetto.getId());
    	return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T extends Identifiable> T refresh(T oggetto,Class<T> classe) {
    	return getDaoByModel(classe).merge(oggetto);
    }    
    
    /**
     * {@inheritDoc}
     */
    public <P, PK extends Serializable> void delete(Class<P> model, PK pkey)
    {
        GenericDao<P, PK> modelDao = modelDaos.get(model.getName());
        P toDeleteObject = modelDao.read(pkey);
        modelDao.delete(toDeleteObject);
        for(NativePostDeleteEventListener delete : listenerOnPostDelete) {
            delete.onPostDelete(toDeleteObject);
        }
    }

    /**
     * Setter method.
     * 
     * @param modelDaos
     *            A map of generic dao. The key is the canonical name of the
     *            model class that the dao manage
     */
    public void setModelDaos(Map<String, GenericDao> modelDaos)
    {
    	log.debug("injection on PersistenceService");
        this.modelDaos = modelDaos;
    }

    /**
     * Return the {@link GenericDao} instance suitable for the request model
     * class
     * 
     * @param <T>
     *            the Entity class
     * @param model
     *            the Entity class
     * @return the {@link GenericDao} instance suitable for the request model
     *         class
     */
    protected <T> GenericDao<T, ?> getDaoByModel(Class<T> model)
    {    	
       	String nome = model.getName();
       	log.debug("CHIAMATO getDaoByModel "+nome);       
       	return modelDaos.get(nome);
    }

    
   /**
     * {@inheritDoc}
     */
    public <PK extends Serializable> boolean exist(Class model, PK id) {
        GenericDao<?, PK> modelDao = getDaoByModel(model);
        if (modelDao.read(id) != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> getList(Class<T> model) {
        PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
        List<T> modelList = modelDao.findAll();
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> getPaginateList(Class<T> model, String sort,
            boolean inverse, int page, int maxResults) {
        PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
        List<T> modelList = modelDao.paginate(sort, inverse, (page - 1)
                * maxResults, maxResults);
        return modelList;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> long count(Class<T> classe) {
        return ((PaginableObjectDao<T, ?>) getDaoByModel(classe)).count();
    }
}
