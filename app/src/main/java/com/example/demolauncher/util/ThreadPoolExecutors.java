/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demolauncher.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 *  全局线程池可在整个进程中使用
 * <p>
 * 线程池的使用可以很大程度避免任务等待，提高响应效率。
 *
 */
public class ThreadPoolExecutors {

    private static final int THREAD_COUNT = 3;

    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;
    private final Executor singleThreadExecutor;

    private static volatile ThreadPoolExecutors INSTANCE = new ThreadPoolExecutors();

    public synchronized static ThreadPoolExecutors getInstance() {
        return INSTANCE;
    }

    ThreadPoolExecutors(Executor diskIO, Executor networkIO, Executor mainThread , Executor singleThreadExecutor) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
        this.singleThreadExecutor = singleThreadExecutor;
    }

    public ThreadPoolExecutors() {
        this(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(),
                new MainThreadExecutor() , Executors.newSingleThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor singleThreadExecutor(){
        return singleThreadExecutor;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
