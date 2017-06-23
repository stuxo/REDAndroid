package ch.redacted.app;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ch.redacted.data.DataManager;
import ch.redacted.data.local.PreferencesHelper;
import ch.redacted.data.model.Index;
import ch.redacted.data.remote.ApiService;
import ch.redacted.data.remote.WhatManagerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock ApiService mMockApiService;
    @Mock WhatManagerService mMockWmService;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockApiService, mMockPreferencesHelper);
    }

    @Test
    public void loadIndexWithError() {
        when(mMockApiService.index())
                .thenReturn(Single.<Index>error(new RuntimeException()));

        mDataManager.loadIndex().subscribe(new TestObserver<Index>());
        // Verify right calls to helper methods
        verify(mMockApiService).index();
    }
}
