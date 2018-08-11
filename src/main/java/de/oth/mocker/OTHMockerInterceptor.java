package de.oth.mocker;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Interceptor for mock objects.
 */
class OTHMockerInterceptor implements MethodInterceptor {
    private ArrayList<LogItem> callHistory = new ArrayList<LogItem>();

    /**
     * This method is executed, when methods are called on mocked object.
     *<p>If application is in normal mode, method call is inserted in history. Null is returned always.</p>
     * <p>
     * For verification mode:
     * <ul>
     *   <li>number of the method's calls is compared to what was given in the last call to verify</li>
     *   <li>if verification failed, AssertionError is thrown</li>
     *   <li>verification mode is turned off</li>
     * </ul>
     *</p>
     *
     * @param obj mocked object
     * @param method method, that was called
     * @param methodArgs arguments of the called method
     * @return null
     * @throws AssertionError
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] methodArgs, MethodProxy proxy) throws AssertionError
    {
        if (!Mocker.isVerifying) {
            callHistory.add(new LogItem(method, methodArgs));
        }
        else {
            int times = 0;
            for (LogItem x : callHistory)
            {
                if (x.method.equals(method) && Arrays.equals(x.args, methodArgs))
                    times++;
            }
            Mocker.isVerifying = false;
            if (Mocker.verifier.type == Verifier.Type.EXACTLY && Mocker.verifier.times!=times)
                 throw new AssertionError(String.format("Verification failure: expected number of calls %d, but was: %d.", Mocker.verifier.times, times));
            if (Mocker.verifier.type == Verifier.Type.AT_LEAST && times<Mocker.verifier.times)
                 throw new AssertionError(String.format("Verification failure: expected number of calls is at least %d, but was: %d.", Mocker.verifier.times, times));
            if (Mocker.verifier.type == Verifier.Type.AT_MOST && times>Mocker.verifier.times)
                 throw new AssertionError(String.format("Verification failure: expected number of calls is at most %d, but was: %d.", Mocker.verifier.times, times));
        }
        return null;
    }

    /**
     * Contains information about particular method call in the MockerInterceptor history.
     */
    class LogItem {
        Method method;
        Object[] args;

        LogItem(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }
    }
}
