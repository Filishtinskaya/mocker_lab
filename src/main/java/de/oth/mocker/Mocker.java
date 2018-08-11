package de.oth.mocker;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *Class, that is able to mock and spy objects with verification functionality.
 */
public class Mocker {
    static boolean isVerifying = false;
    static Verifier verifier;

    /**
     * Creates a mock object, that mimics the behaviour of an actual class.
     * Mock also has a history of methods called on it.
     *
     * @param type reflection for the class that is mocked
     * @param <T> type of the mocked object
     * @return a mock of the object
     */
    static public <T> T mock(Class<T> type) {
        Enhancer e = new Enhancer();
        e.setSuperclass(type);
        e.setCallback(new OTHMockerInterceptor());
        return (T) e.create();
    }

    /**
     * Creates a spy object, that acts in the same way as actual object, but also tracks methods, called on it.
     *
     * @param obj object that is spied
     * @param <T> type of the spied object
     * @return spy object
     * @see Mocker#mock(Class)
     */
    static public <T> T spy(T obj) {
        Enhancer e = new Enhancer();
        e.setSuperclass(obj.getClass());
        e.setCallback(new OTHSpyInterceptor(obj));
        return (T) e.create();
    }

    /**
     * Switches application to the verification mode (verification mode is active for the one next funciton call on any mocked object).
     *
     * @param mock mock object
     * @param v information about what needs to be verified
     * @param <T> type of the mock object
     * @return the same mock object
     * @see OTHMockerInterceptor#intercept(Object, Method, Object[], MethodProxy)
     */
    static public <T> T verify(T mock, Verifier v) {
         isVerifying = true;
         verifier = v;
         return mock;
    }

    /**
     * Default version of {@link Mocker#verify(T, Verifier)}, where Verifier is times(1)
     * @param mock
     * @param <T>
     * @return
     */
    static public <T> T verify(T mock) {
        return verify (mock, times(1));
    }

    //methods, that return Verifier object, that contains verification parameters
    static public Verifier times (int i) {
        return new Verifier(Verifier.Type.EXACTLY, i);
    }
    static public Verifier atMost (int i) {
        return new Verifier(Verifier.Type.AT_MOST, i);
    }
    static public Verifier atLeast (int i) {
        return new Verifier(Verifier.Type.AT_LEAST, i);
    }
    static public Verifier never()  {
        return new Verifier (Verifier.Type.EXACTLY, 0);
    }
}
