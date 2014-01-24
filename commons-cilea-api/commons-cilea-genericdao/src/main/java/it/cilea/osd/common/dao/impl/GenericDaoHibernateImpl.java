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

import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.dao.NamedQueryExecutor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

/**
 * <p>
 * {@link GenericDao}, {@link NamedQueryExecutor} implementations based on
 * Hibernate
 * </p>
 * <p>
 * This source are based on the <a
 * href="http://www.ibm.com/developerworks/java/library/j-genericdao.html">Per
 * Mellqvist article: Don't repeat the DAO!</a><br>
 * </p>
 * 
 * @author cilea
 * 
 */
public class GenericDaoHibernateImpl<T, PK extends Serializable> implements
		GenericDao<T, PK>, NamedQueryExecutor<T> {
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * The class of the Entity managed by this DAO
	 */
	private Class<T> type;

	private SessionFactory sessionFactory;

	public GenericDaoHibernateImpl(Class<T> type) {
		this.type = type;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public PK create(T newInstance) {
		return (PK) getSessionFactory().getCurrentSession().save(newInstance);
	}

	/**
	 * {@inheritDoc}
	 */
	public T read(PK id) {
		return (T) getSessionFactory().getCurrentSession().get(type, id);
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(T transientObject) {
		getSessionFactory().getCurrentSession().update(transientObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public T merge(T transientObject) {
		return (T) getSessionFactory().getCurrentSession().merge(
				transientObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveOrUpdate(T transientObject) {
		getSessionFactory().getCurrentSession().saveOrUpdate(transientObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T persistentObject) {
		getSessionFactory().getCurrentSession().delete(persistentObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> executeFinder(Method method, Object[] queryArgs) {
		return (List<T>) buildQuery(method, queryArgs).list();
	}

	/**
	 * {@inheritDoc}
	 */
	public T executeUnique(Method method, Object[] queryArgs) {
		return (T) buildQuery(method, queryArgs).uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean executeBoolean(Method method, Object[] queryArgs) {
		return (Boolean) buildQuery(method, queryArgs).uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public Double executeDouble(Method method, Object[] queryArgs) {
		return (Double) buildQuery(method, queryArgs).uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public long executeCounter(Method method, Object[] queryArgs) {
		return (Long) buildQuery(method, queryArgs).uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer executeIdFinder(Method method, Object[] queryArgs) {
		return (Integer) buildQuery(method, queryArgs).uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> executePaginator(Method method, Object[] queryArgs,
			String sort, boolean inverse, int firstResult, int maxResults) {
		log.debug("==>GenericDaoHibernateImpl: executePaginator(" + firstResult
				+ ", " + maxResults + ")");
		Query query = buildQuery(method, new String[] { sort,
				inverse ? "desc" : "asc" }, queryArgs);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return (List<T>) query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer executeDelete(Method method, Object[] queryArgs) {
		return buildQuery(method, queryArgs).executeUpdate();
	}

	private Query buildQuery(Method method, Object[] queryArgs) {
		return buildQuery(method, null, queryArgs);
	}

	/**
	 * We support only named query without named parameter OR with only named
	 * parameter named like par&lt;idx&gt;
	 * 
	 * @param method
	 * @param querySuffixes
	 * @param queryArgs
	 * @return
	 */
	private Query buildQuery(Method method, String[] querySuffixes,
			Object[] queryArgs) {
		StringBuffer sb = new StringBuffer();
		sb.append(type.getSimpleName()).append('.').append(method.getName());
		if (querySuffixes != null) {
			for (int i = 0; i < querySuffixes.length; i++) {
				sb.append('.').append(querySuffixes[i]);
			}
		}
		String queryName = new String(sb);
		Query namedQuery = getSessionFactory().getCurrentSession()
				.getNamedQuery(queryName);
		String[] namedParameters = namedQuery.getNamedParameters();

		if (namedParameters.length > 0) {
			for (int i = 0; i < namedParameters.length; i++) {
				Object arg = queryArgs[i];
				if (arg instanceof Collection) {

					namedQuery.setParameterList("par" + i, (Collection) arg);
				} else {

					namedQuery.setParameter("par" + i, arg);
				}
			}
		} else if (queryArgs != null) {
			for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				namedQuery.setParameter(i, arg);
			}
		}
		return namedQuery;
	}

	public T executeSingleResult(Method method, Object[] queryArgs) {
		Query query = buildQuery(method, queryArgs);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer executeMax(Method method, Object[] queryArgs) {
		return (Integer) buildQuery(method, queryArgs).uniqueResult();
	}
}
