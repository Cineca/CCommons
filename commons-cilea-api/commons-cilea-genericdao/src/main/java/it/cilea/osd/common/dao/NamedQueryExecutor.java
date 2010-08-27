package it.cilea.osd.common.dao;

import java.lang.reflect.Method;
import java.math.BigDecimal;
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
	
	BigDecimal executeIdFinder(Method method, Object[] queryArgs);
	
	T executeUnique(Method method, Object[] queryArgs);
	
	Integer  executeDelete(Method method, Object[] queryArgs);

	Boolean executeBoolean(Method method, Object[] queryArgs);
	
	Double executeDouble(Method method, Object[] queryArgs);

	T executeSingleResult(Method method, Object[] args);
}
