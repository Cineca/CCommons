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
