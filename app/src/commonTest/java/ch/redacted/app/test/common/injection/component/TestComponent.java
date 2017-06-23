package ch.redacted.app.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import ch.redacted.app.test.common.injection.module.ApplicationTestModule;
import ch.redacted.injection.component.ApplicationComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
