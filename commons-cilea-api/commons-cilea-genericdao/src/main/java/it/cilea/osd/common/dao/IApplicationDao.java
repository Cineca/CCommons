package it.cilea.osd.common.dao;

import java.io.Serializable;
import java.util.List;

import it.cilea.osd.common.model.Identifiable;

/**
 * Common interface for interaction with the persistence engine
 * 
 * @author cilea
 * 
 */
public interface IApplicationDao  {

    /**
     * Evict a managed object from the persistence context
     * 
     * @param identifiable
     *            a managed object
     */
	public void evict(Identifiable identifiable);
	
	public <T, PK extends Serializable> List<T> getList(Class<T> clazz,
            List<PK> allIds);
		
}
