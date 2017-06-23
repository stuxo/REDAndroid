package ch.redacted.app.util;

import io.reactivex.observers.TestObserver;
import ch.redacted.util.RxEventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RxEventBusTest {

    private RxEventBus mEventBus;

    @Rule
    // Must be added to every test class that targets app code that uses RxJava
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mEventBus = new RxEventBus();
    }

    @Test
    public void postedObjectsAreReceived() {
        TestObserver<Object> testSubscriber = new TestObserver<>();
        mEventBus.observable().subscribe(testSubscriber);

        Object event1 = new Object();
        Object event2 = new Object();
        mEventBus.post(event1);
        mEventBus.post(event2);

        testSubscriber.assertValues(event1, event2);
    }

    @Test
    public void filteredObservableOnlyReceivesSomeObjects() {
        TestObserver<String> testSubscriber = new TestObserver<>();
        mEventBus.filteredObservable(String.class).subscribe(testSubscriber);

        String stringEvent = "Event";
        Integer intEvent = 20;
        mEventBus.post(stringEvent);
        mEventBus.post(intEvent);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(stringEvent);
    }
}