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
package it.cilea.osd.common.dao.impl;

import it.cilea.osd.common.dao.IApplicationDao;
import it.cilea.osd.common.model.Identifiable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

/**
 * {@link IApplicationDao} implementations based on Hibernate
 * 
 * @author cilea
 * 
 */
public class ApplicationDao implements IApplicationDao {
	/**
	 * The Log4J logger
	 */
	private static final Log log = LogFactory.getLog(ApplicationDao.class);

	private static final int MAX_IN_CLAUSE = 500;

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public void evict(Identifiable identifiable) {
		getSessionFactory().getCurrentSession().evict(identifiable);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T, PK extends Serializable> List<T> getList(Class<T> clazz,
			List<PK> allIds) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(clazz);

		final int maxResults = allIds.size();
		if (maxResults == 0)
			return null;

		int loop = maxResults / MAX_IN_CLAUSE;
		boolean exact = maxResults % MAX_IN_CLAUSE == 0;

		if (!exact)
			loop++;

		Disjunction disjunction = Restrictions.disjunction();

		for (int index = 0; index < loop; index++) {
			int max = index * MAX_IN_CLAUSE + MAX_IN_CLAUSE <= maxResults ? index
					* MAX_IN_CLAUSE + MAX_IN_CLAUSE
					: maxResults;
			List<PK> ids = new ArrayList<PK>(max - index * MAX_IN_CLAUSE);
			for (int entityInfoIndex = index * MAX_IN_CLAUSE; entityInfoIndex < max; entityInfoIndex++) {
				ids.add(allIds.get(entityInfoIndex));
			}
			disjunction.add(Restrictions.in("id", ids));
		}
		criteria.add(disjunction);
		criteria.list(); // load all objects

		// mandatory to keep the same ordering
		List<T> result = new ArrayList<T>(allIds.size());
		for (PK id : allIds) {
			T element = (T) session.load(clazz, id);
			if (Hibernate.isInitialized(element)) {
				// all existing elements should have been loaded by the query,
				// the other ones are missing ones
				result.add(element);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Object with id: " + id + " not in database: "
							+ clazz);
				}
			}
		}
		return result;
	}

}
