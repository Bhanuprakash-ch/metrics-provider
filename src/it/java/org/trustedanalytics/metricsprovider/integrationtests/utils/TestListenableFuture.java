/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.metricsprovider.integrationtests.utils;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestListenableFuture<T> implements ListenableFuture<ResponseEntity<T>> {

    private T objectToReturn;

    public TestListenableFuture(T objectToReturn) {
        this.objectToReturn = objectToReturn;
    }

    @Override public void addCallback(
        ListenableFutureCallback<? super ResponseEntity<T>> listenableFutureCallback) {

        listenableFutureCallback.onSuccess(new ResponseEntity<>(objectToReturn, HttpStatus.OK));
    }

    @Override public void addCallback(SuccessCallback<? super ResponseEntity<T>> successCallback,
        FailureCallback failureCallback) {

        throw new NotImplementedException();
    }

    @Override public boolean cancel(boolean mayInterruptIfRunning) {
        throw new NotImplementedException();
    }

    @Override public boolean isCancelled() {
        throw new NotImplementedException();
    }

    @Override public boolean isDone() {
        throw new NotImplementedException();
    }

    @Override public ResponseEntity<T> get() throws InterruptedException, ExecutionException {
        throw new NotImplementedException();
    }

    @Override public ResponseEntity<T> get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {

        throw new NotImplementedException();
    }
}
