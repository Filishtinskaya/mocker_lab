package de.oth.mocker;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Interceptor for spy objects
 */
public class OTHSpyInterceptor extends OTHMockerInterceptor {
    Object spied;

    OTHSpyInterceptor(Object spied) {
        this.spied = spied;
    }

    /**
     * Does the same as {@link OTHMockerInterceptor#intercept(Object, Method, Object[], MethodProxy)}, but executes a method in non-verifying mode.
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] methodArgs, MethodProxy proxy) throws AssertionError {
        super.intercept(obj, method, methodArgs, proxy);
        if (!Mocker.isVerifying)
            try {
                return method.invoke(spied, methodArgs);
            }
            catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
            return null;
    }

}
