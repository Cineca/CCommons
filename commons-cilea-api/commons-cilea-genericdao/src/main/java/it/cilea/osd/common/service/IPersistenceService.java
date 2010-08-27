package it.cilea.osd.common.service;

import it.cilea.osd.common.model.Identifiable;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

/**
 * Minimal interface that an applicationService should provide to provide access
 * to the persistente layer
 * 
 * @author cilea
 * 
 */
public interface IPersistenceService {

    /**
     * Retrive a managed object from its primary key
     * 
     * @param <T>
     *            the entity class
     * @param <PK>
     *            the primary key class
     * @param modelClass
     *            the entity class
     * @param pkey
     *            the primary key
     * @return the managed object
     */
	public abstract <T, PK extends Serializable> T get(Class<T> modelClass,
			PK pkey);

	/**
     * Persist or update an object basing on the object persistent state
     * 
     * @param <T>
     *            the entity class
     * @param modelClass
     *            the entity class
     * @param transientObject
     *            the object to persist or update
     */
	public abstract <T extends Identifiable> void saveOrUpdate(
			Class<T> modelClass, T transientObject);

	/**
     * Merge a detached object with his persistent state
     * 
     * @param <T>
     *            the entity class
     * @param oggetto
     *            the detached object
     * @param classe
     *            the entity class
     * @return a managed object
     */
	public abstract <T extends Identifiable> T merge(T oggetto, Class<T> classe);

	/**
     * Attach a detached object to the persistent context
     * 
     * @param <T>
     *            the entity class
     * @param oggetto
     *            the detached object
     * @param classe
     *            the entity class
     * @return a managed object
     */
	public abstract <T extends Identifiable> T refresh(T oggetto,
			Class<T> classe);

	/**
     * Remove a managed object from the persistence datasource
     * 
     * @param model
     *            the entity class
     * @param pkey
     *            the primary key
     **/
	public abstract <P, PK extends Serializable> void delete(Class<P> model,
			PK pkey);
	
	/**
     * Count the objects of a specific type
     * 
     * @param <T>
     *            the entity class
     * @param classe
     *            the entity class
     * @return the number of object of the requested type
     */
	public abstract <T> long count(Class<T> classe);

	 /**
     * Check for existence of an object with the supplied primary key and type
     * 
     * @param model
     *            the entity class
     * @param id
     *            the primary key
     * @return <code>true</code> if the object exists
     * 
     */
	public abstract <PK extends Serializable> boolean exist(Class model, PK id);
	
	/**
     * Return the list of objects of a specific type
     * 
     * @param <T>
     *            the entity class
     * @param model
     *            the entity class
     * @return the list of object of the requested class
     */
	public abstract <T> List<T> getList(Class<T> model);

    /**
     * Return a paginated list of managed object
     * 
     * @param <T>
     *            the entity class
     * @param model
     *            the entity class
     * @param sort
     *            the sort criteria
     * @param inverse
     *            asc or desc order
     * @param page
     *            the page number
     * @param maxResults
     *            the number of results per page
     * @return a paginated list of managed object
     */
	public abstract <T> List<T> getPaginateList(Class<T> model, String sort,
			boolean inverse, int page, int maxResults);
	
	/**
	 * Evict the object from the Persistente Context
	 * 
	 * @see ApplicationDao#evict(Identifiable)
	 * @see Session#evict(Object)
	 * @param identifiable
	 */
	public void evict(Identifiable identifiable);
}