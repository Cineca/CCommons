package it.cilea.osd.common.dao.impl;

import it.cilea.osd.common.dao.NamedQueryExecutor;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * This class is the Advisor responsible to execute the appropriate named query
 * when a method is invoked on a DAO interface.
 * 
 * @author cilea
 * 
 */
public class NamedQueryIntroductionAdvisor extends DefaultIntroductionAdvisor {

	public NamedQueryIntroductionAdvisor() {
		super(new IntroductionInterceptor() {
            /**
             * Execute the appropriate method of the genericDAO implementation
             * basing on the start characters of the original invoked method on
             * the DAO class
             */
			public Object invoke(MethodInvocation mi) throws Throwable {
				NamedQueryExecutor genericDao = (NamedQueryExecutor) mi
						.getThis();
				String methodName = mi.getMethod().getName();
				if (methodName.startsWith("find")) {
					Object[] args = mi.getArguments();
					return genericDao.executeFinder(mi.getMethod(), args);
				} else if (methodName.startsWith("unique")){
					Object[] args = mi.getArguments();
					return genericDao.executeUnique(mi.getMethod(), args);					
				}else if (methodName.startsWith("count")) {
					Object[] args = mi.getArguments();
					return genericDao.executeCounter(mi.getMethod(), args);
				} else if (methodName.startsWith("delete") && !methodName.equals("delete")) {
					Object[] args = mi.getArguments();
					return genericDao.executeDelete(mi.getMethod(), args);
				}else if (methodName.startsWith("idFind")) {
					Object[] args = mi.getArguments();
					return genericDao.executeIdFinder(mi.getMethod(), args);
				} else if (methodName.startsWith("paginate")) {
					Object[] args = mi.getArguments();
					String sort = (String) args[args.length - 4];
					boolean inverse = (Boolean) args[args.length - 3];
					int firstResult = (Integer) args[args.length - 2];
					int maxResults = (Integer) args[args.length - 1];
					args = Arrays.asList(args).subList(0, args.length - 4)
							.toArray();
					return genericDao.executePaginator(mi.getMethod(), args,
							sort, inverse, firstResult, maxResults);
				} else if (methodName.startsWith("is") || methodName.startsWith("has")
							|| methodName.startsWith("check")){
					Object[] args = mi.getArguments();
					return genericDao.executeBoolean(mi.getMethod(), args);					
				} else if (methodName.startsWith("sum")){
					Object[] args = mi.getArguments();
					return genericDao.executeDouble(mi.getMethod(), args);					
				} else if  (methodName.startsWith("singleResult")){
					Object[] args = mi.getArguments();
					return genericDao.executeSingleResult(mi.getMethod(), args);
				} else {				
					return mi.proceed();
				}
			}

			public boolean implementsInterface(Class intf) {
				return intf.isInterface()
						&& NamedQueryExecutor.class.isAssignableFrom(intf);
			}
		});
	}

}
