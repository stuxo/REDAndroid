package ch.redacted.app.test.common.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ch.redacted.data.DataManager;
import ch.redacted.data.remote.ApiService;
import ch.redacted.data.remote.WhatManagerService;
import ch.redacted.injection.ApplicationContext;

import static org.mockito.Mockito.mock;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {

    private final Application mApplication;

    public ApplicationTestModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    /************* MOCKS *************/

    @Provides
    @Singleton DataManager provideDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton ApiService provideApiService() {
        return mock(ApiService.class);
    }

    @Provides
    @Singleton WhatManagerService provideWmService() {
        return mock(WhatManagerService.class);
    }
}
