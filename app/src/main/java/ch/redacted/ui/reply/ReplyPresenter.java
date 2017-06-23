package ch.redacted.ui.reply;

import ch.redacted.data.DataManager;
import ch.redacted.data.model.Conversation;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import retrofit2.HttpException;

@ConfigPersistent
public class ReplyPresenter extends BasePresenter<ReplyMvpView> {

    private final DataManager mDataManager;
    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    public ReplyPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ReplyMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }
}
