package com.semantica.pocketknife.mock.service;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import com.semantica.pocketknife.calls.MethodCall;
import com.semantica.pocketknife.mock.InterceptionsStore;
import com.semantica.pocketknife.mock.MockUtils;
import com.semantica.pocketknife.mock.StubType;
import com.semantica.pocketknife.mock.dto.QualifiedMethodCall;

public class InlineMockerInterceptionsStore implements InterceptionsStore {

    private class Stub {
        StubType stubType;
        Object stubValue;

        public Stub(StubType stubType, Object stubValue) {
            this.stubType = stubType;
            this.stubValue = stubValue;
        }
    }

    // key: the proxy (mock) instance
    private final Map<Object, Map<MethodCall<Method>, Queue<Stub>>> allInterceptions = new HashMap<>();

    @Override
    public void addInterceptions(QualifiedMethodCall<Method> qualifiedMethodCall, StubType stubType, List<Object> stubValues) {
        Object proxy = qualifiedMethodCall.getInvokedOnInstance();
        Map<MethodCall<Method>, Queue<Stub>> interceptions = allInterceptions.get(proxy);
        if (interceptions == null) {
            interceptions = new HashMap<>();
            allInterceptions.put(proxy, interceptions);
        }
        Queue<Stub> orderedStubs = interceptions.get(qualifiedMethodCall.getMethodCall());
        if (orderedStubs == null) {
            orderedStubs = new ArrayDeque<>(stubValues.size());
            interceptions.put(qualifiedMethodCall.getMethodCall(), orderedStubs);
        }
        for (Object stubValue : stubValues) {
            orderedStubs.add(new Stub(stubType, stubValue));
        }
    }

    @Override
    public Optional<Object> matchExactMethodCallToStoredMatchingMethodCalls(
            QualifiedMethodCall<Method> qualifiedMethodCall) throws Throwable {
        Object proxy = qualifiedMethodCall.getInvokedOnInstance();
        Map<MethodCall<Method>, Queue<Stub>> interceptions = allInterceptions.get(proxy);
        if (interceptions != null) {
            MethodCall<Method> matcherMethodCall = MockUtils
                    .getStoredMatcherMethodCall(qualifiedMethodCall.getMethodCall(), interceptions.keySet());
            if (matcherMethodCall != null) {
                Stub stub = interceptions.get(matcherMethodCall).poll();
                if (stub != null) {
                    if (stub.stubType == StubType.THROWS) {
                        throw (Throwable) stub.stubValue;
                    } else {
                        return Optional.of(stub.stubValue);
                    }
                }
            }
        }
        return Optional.empty();
    }

}
