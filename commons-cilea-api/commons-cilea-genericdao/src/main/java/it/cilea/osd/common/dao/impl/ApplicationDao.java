package it.cilea.osd.common.dao.impl;

import it.cilea.osd.common.dao.IApplicationDao;
import it.cilea.osd.common.model.Identifiable;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * {@link IApplicationDao} implementations based on Hibernate
 * 
 * @author cilea
 * 
 */
public class ApplicationDao extends HibernateDaoSupport implements
        IApplicationDao
{

    /**
     * {@inheritDoc}
     */
    public void evict(Identifiable identifiable)
    {
        getSession().evict(identifiable);
    }

}
