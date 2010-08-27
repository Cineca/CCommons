package it.cilea.osd.common.dao;


import java.io.Serializable;
import java.util.List;

/**
 * Query methods that a DAO that want support pagination need to provide
 * 
 * @author cilea
 * 
 * @param <T>
 *            the class of the managed Entity
 * @param <PK>
 *            the class of the primary key
 */
public interface PaginableObjectDao<T, PK extends Serializable> extends GenericDao<T, PK> {
	/**
     * Return the full list of the objects in the database
     * 
     * @return the full list of the objects in the database
     */
    public List<T> findAll();
    
    /**
     * Count the number of objects in the database
     * 
     * @return the number of objects in the database
     */
	public long count();
	
	/**
     * Run the named query: paginate.[sort].asc or desc if the inverse parameter
     * is <code>true</code>
     * 
     * @param sort
     *            the attribute to use for sorting (i.e "id")
     * @param inverse
     *            true for desc order
     * @param firstResult
     *            the offset to use in query
     * @param maxResults
     *            the max number of results to return
     * @return the "page" of objects in the requested order and position
     */
	public List<T> paginate(String sort, boolean inverse, int firstResult,
			int maxResults);	
}
