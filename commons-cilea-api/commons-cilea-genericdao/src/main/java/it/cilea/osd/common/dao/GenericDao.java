package it.cilea.osd.common.dao;

import java.io.Serializable;

/**
 * <p>
 * Generic interface for the base "service" that a DAO MUST provide
 * </p>
 * <p>
 * This source are based on the <a
 * href="http://www.ibm.com/developerworks/java/library/j-genericdao.html">Per
 * Mellqvist article: Don't repeat the DAO!</a><br>
 * </p>
 * 
 * @author cilea
 * 
 * @param <T>
 *            the class of the managed Entity
 * @param <PK>
 *            the class of the primary key
 */
public interface GenericDao<T, PK extends Serializable> {

    /**
     * Persist the newInstance object into database
     * 
     * @param newInstance
     *            the instance to persist
     * 
     * @return the primary key of the persisted instance
     **/
	PK create(T newInstance);

    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     * 
     * @param id
     *            the primary key to lookup for
     * 
     * @result the retrieved instance
     */
	T read(PK id);

	/**
     * Save changes made to a persistent object
     * 
     * @param transientObject
     *            the instance to update
     */
	void update(T transientObject);

	/**
     * Merge a detached object against the persistent state
     * 
     * @param transientObject
     *            the detached object
     * @return a managed object
     */
	T merge(T transientObject);

	/**
     * Perform a save or update action on the object basis on the persistence
     * state
     * 
     * @param transientObject
     *            the object to persist or update
     */
	void saveOrUpdate(T transientObject);	
	
	/**
     * Remove an object from persistent storage in the database
     * 
     * @param persistentObject
     *            the object to remove
     */
	void delete(T persistentObject);
}
