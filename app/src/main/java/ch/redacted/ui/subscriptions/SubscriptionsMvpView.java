package ch.redacted.ui.subscriptions;

import java.util.List;

import ch.redacted.data.model.Subscription;
import ch.redacted.ui.base.MvpView;

public interface SubscriptionsMvpView extends MvpView {

    void showSubscriptionsEmpty();

    void showLoadingProgress(boolean show);

    void showError(String message);

	void showSnackbar(String message);

    void showSubscriptions(List<Subscription.Threads> threads);
}