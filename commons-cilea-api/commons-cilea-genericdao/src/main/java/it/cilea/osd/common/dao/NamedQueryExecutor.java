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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * The interface that a genericDAO implementation needs to implement to support
 * generic named query execution.
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
 *            the Entity class
 */
public interface NamedQueryExecutor<T> {

	List<T> executeFinder(Method method, Object[] queryArgs);

	List<T> executePaginator(Method method, Object[] queryArgs, String sort,
			boolean inverse, int firstResult, int maxResults);

	long executeCounter(Method method, Object[] queryArgs);
	
	Integer executeIdFinder(Method method, Object[] queryArgs);
	
	T executeUnique(Method method, Object[] queryArgs);
	
	Integer  executeDelete(Method method, Object[] queryArgs);

	Boolean executeBoolean(Method method, Object[] queryArgs);
	
	Double executeDouble(Method method, Object[] queryArgs);

	T executeSingleResult(Method method, Object[] args);
	
	Integer executeMax(Method method, Object[] queryArgs);
}
