package ch.redacted.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ch.redacted.data.DataManager;
import ch.redacted.data.local.PreferencesHelper;
import ch.redacted.data.remote.ApiService;
import ch.redacted.injection.ApplicationContext;
import ch.redacted.injection.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    ApiService apiService();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    //RxEventBus eventBus();
}
