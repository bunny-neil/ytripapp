package com.ytripapp.api.client.feign.support;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import java.util.concurrent.Callable;

public class HystrixConcurrencyStrategy extends com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new HystrixCallableWrapper<T>(callable);
    }

    public static class HystrixCallableWrapper<V> implements Callable<V> {

        Callable<V> actual;
        HystrixRequestContext parentContext;

        public HystrixCallableWrapper(Callable<V> actual) {
            this.actual = actual;
            this.parentContext = HystrixRequestContext.getContextForCurrentThread();
        }

        @Override
        public V call() throws Exception {
            HystrixRequestContext childContext = HystrixRequestContext.getContextForCurrentThread();
            try {
                HystrixRequestContext.setContextOnCurrentThread(parentContext);
                return actual.call();
            }
            finally {
                HystrixRequestContext.setContextOnCurrentThread(childContext);
            }
        }
    }
}
