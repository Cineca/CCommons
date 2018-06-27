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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import it.cilea.osd.common.core.HasTimeStampInfo;
import it.cilea.osd.common.core.ITimeStampInfo;
import it.cilea.osd.common.core.SingleTimeStampInfo;
import it.cilea.osd.common.core.TimeStampInfo;
import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.common.listener.NativeLoadEventListener;
import it.cilea.osd.common.listener.NativePostDeleteEventListener;
import it.cilea.osd.common.listener.NativePostUpdateEventListener;
import it.cilea.osd.common.listener.NativePreInsertEventListener;
import it.cilea.osd.common.listener.NativePreUpdateEventListener;
import it.cilea.osd.common.model.Identifiable;

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
    
    private List<NativePreInsertEventListener> listenerOnPreInsert;
    
    private List<NativePreUpdateEventListener> listenerOnPreUpdate;

    private List<NativeLoadEventListener> listenerOnLoad;
    
    public List<NativeLoadEventListener> getListenerOnLoad()
    {
        if (this.listenerOnLoad == null)
        {
            this.listenerOnLoad = new ArrayList<NativeLoadEventListener>();
        }
        return listenerOnLoad;
    }

    public void setListenerOnLoad(List<NativeLoadEventListener> listenerOnLoad)
    {
        this.listenerOnLoad = listenerOnLoad;
    }

    public List<NativePostDeleteEventListener> getListenerOnPostDelete()
    {
        if (this.listenerOnPostDelete == null)
        {
            this.listenerOnPostDelete = new ArrayList<NativePostDeleteEventListener>();
        }
        return listenerOnPostDelete;
    }

    public void setListenerOnPostDelete(
            List<NativePostDeleteEventListener> listenerOnPostDelete)
    {
        this.listenerOnPostDelete = listenerOnPostDelete;
    }

    public List<NativePostUpdateEventListener> getListenerOnPostUpdate()
    {
        if (this.listenerOnPostUpdate == null)
        {
            this.listenerOnPostUpdate = new ArrayList<NativePostUpdateEventListener>();
        }
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
    protected <T extends Identifiable> Boolean recordTimeStampInfo(
            Class<T> modelClass, T transientObject)
    {
        if (transientObject instanceof HasTimeStampInfo)
        {
            log.debug("Added record TimeStampInfo: modelClass: "
                    + modelClass.getName() + " id: " + transientObject.getId());
            HasTimeStampInfo hrt = (HasTimeStampInfo) transientObject;
            ITimeStampInfo rt = hrt.getTimeStampInfo();
            if (rt == null)
            {
                rt = new TimeStampInfo();
            }
            if (rt.getTimestampCreated() == null)
            {
                rt.setInfoCreated(getCurrentTimeStampInfo());
                return true;
            }
            else
            {
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
    protected SingleTimeStampInfo getCurrentTimeStampInfo()
    {
        return new SingleTimeStampInfo(new Date());
    }

    /**
     * {@inheritDoc}
     */
    public <T, PK extends Serializable> T get(Class<T> modelClass, PK pkey)
    {
        GenericDao<T, PK> modelDao = modelDaos.get(modelClass.getName());
        log.debug("modelClass:" + modelClass);
        log.debug("dao:" + modelDao);
        T result = modelDao.read(pkey);
        if(result != null) {
            for (NativeLoadEventListener update : getListenerOnLoad()) {
                update.onLoad(result);
            }
        }
        return result;
    }

    
    /**
     * {@inheritDoc}
     */
    public <T extends Identifiable> void saveOrUpdate(Class<T> modelClass,
            T transientObject)
    {
    	saveOrUpdate(modelClass, transientObject, true);
    }

    
    public <T extends Identifiable> void saveOrUpdate(Class<T> modelClass,
            T transientObject, boolean listenerEnable)
    {
        final String modelClassName = modelClass.getName();

        log.debug("saveOrUpdate: " + modelClassName + " id: "
                + transientObject.getId());
        GenericDao<T, ?> modelDao = modelDaos.get(modelClassName);
		if (listenerEnable) {
			if (transientObject.getId() == null) {
				for (NativePreInsertEventListener insert : getListenerOnPreInsert()) {
					insert.onPreInsert(transientObject);
				}
			} else {
				for (NativePreUpdateEventListener update : getListenerOnPreUpdate()) {
					update.onPreUpdate(transientObject);
				}
			}
		}
        Boolean creation = recordTimeStampInfo(modelClass, transientObject);
        modelDao.saveOrUpdate(transientObject);
		if (listenerEnable) {
			for (NativePostUpdateEventListener update : getListenerOnPostUpdate()) {
				update.onPostUpdate(transientObject);
			}
		}
        log.debug("after saveOrUpdate id: " + transientObject.getId());
    }
    
    /**
     * {@inheritDoc}
     */
    public <T extends Identifiable> T merge(T oggetto, Class<T> classe)
    {
        log.debug("merge: " + classe.getName() + " -id: " + oggetto.getId());
        recordTimeStampInfo(classe, oggetto);
        T result = getDaoByModel(classe).merge(oggetto);
        log.debug("after merger id: " + oggetto.getId());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public <T extends Identifiable> T refresh(T oggetto, Class<T> classe)
    {
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
        for (NativePostDeleteEventListener delete : getListenerOnPostDelete())
        {
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
        log.debug("CHIAMATO getDaoByModel " + nome);
        return modelDaos.get(nome);
    }

    /**
     * {@inheritDoc}
     */
    public <PK extends Serializable> boolean exist(Class model, PK id)
    {
        GenericDao<?, PK> modelDao = getDaoByModel(model);
        if (modelDao.read(id) != null)
        {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> getList(Class<T> model)
    {
        PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
        List<T> modelList = modelDao.findAll();
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> getPaginateList(Class<T> model, String sort,
            boolean inverse, int page, int maxResults)
    {
        PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
        List<T> modelList = modelDao.paginate(sort, inverse, (page - 1)
                * maxResults, maxResults);
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    public <T> long count(Class<T> classe)
    {
        return ((PaginableObjectDao<T, ?>) getDaoByModel(classe)).count();
    }

    public List<NativePreInsertEventListener> getListenerOnPreInsert()
    {
        if (this.listenerOnPreInsert == null)
        {
            this.listenerOnPreInsert = new ArrayList<NativePreInsertEventListener>();
        }        
        return listenerOnPreInsert;
    }

    public void setListenerOnPreInsert(
            List<NativePreInsertEventListener> listenerOnPreInsert)
    {
        this.listenerOnPreInsert = listenerOnPreInsert;
    }

    public List<NativePreUpdateEventListener> getListenerOnPreUpdate()
    {
        if (this.listenerOnPreUpdate == null)
        {
            this.listenerOnPreUpdate = new ArrayList<NativePreUpdateEventListener>();
        }        
        return listenerOnPreUpdate;
    }

    public void setListenerOnPreUpdate(
            List<NativePreUpdateEventListener> listenerOnPreUpdate)
    {
        this.listenerOnPreUpdate = listenerOnPreUpdate;
    }
}
