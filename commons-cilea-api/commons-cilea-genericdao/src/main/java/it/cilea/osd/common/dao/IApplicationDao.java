package it.cilea.osd.common.dao;

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
}
