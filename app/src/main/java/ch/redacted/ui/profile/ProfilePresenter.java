package ch.redacted.ui.profile;

import android.content.Context;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.inject.Inject;

import ch.redacted.data.DataManager;
import ch.redacted.data.local.PreferencesHelper;
import ch.redacted.data.model.Profile;
import ch.redacted.data.model.Recents;
import ch.redacted.injection.ConfigPersistent;
import ch.redacted.ui.base.BasePresenter;
import ch.redacted.util.SharedPrefsHelper;
import retrofit2.HttpException;

@ConfigPersistent
public class ProfilePresenter extends BasePresenter<ProfileMvpView> {

    private final DataManager mDataManager;
    private final PreferencesHelper mPreferenceHelper;
    private CompositeDisposable mSubscription;

    @Inject
    public ProfilePresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        mDataManager = dataManager;
        mPreferenceHelper = preferencesHelper;
    }

    @Override
    public void attachView(ProfileMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.dispose();
    }

    public void loadProfile(final Context context, int id, boolean useCache) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mSubscription = new CompositeDisposable();
        //if this isn't the logged in user, don't ever cache
        if (id != mPreferenceHelper.getUserId()) {
            useCache = false;
        }

        if (useCache) {
            Profile profile = new Profile();
            String s = SharedPrefsHelper.getInstance(context)
                    .readSharedPrefsString(SharedPrefsHelper.FILE_PROFILE, SharedPrefsHelper.KEY_PROFILE, "");
            profile = profile.deserializeFromJson(s);
            if (profile != null) {
                checkParanoia(profile);
                getMvpView().showLoadingProgress(false);
            } else {
                loadProfile(context, id, false);
            }
        } else {
            if (id == 0){
                id = mPreferenceHelper.getUserId();
            }
            final int finalId = id;
            mSubscription.add(mDataManager.getProfile(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Profile>() {
                    @Override public void onSuccess(Profile profile) {
                        checkParanoia(profile);
                        getMvpView().showLoadingProgress(false);

                        //only cache response if this is you
                        if (finalId != mPreferenceHelper.getUserId()) {
                            SharedPrefsHelper.getInstance(context).
                                writeSharedPrefs(SharedPrefsHelper.FILE_PROFILE, SharedPrefsHelper.KEY_PROFILE, profile.serializeToJson(), null,
                                    null);
                            getMvpView().showProfileIsLoggedInUser(true);
                        }
                    }

                    @Override public void onError(Throwable error) {
                        getMvpView().showError(error.getMessage());
                        getMvpView().showLoadingProgress(false);
                    }
                }));
        }
    }

    private void checkParanoia(Profile profile) {
        if (!profile.response.avatar.equals("")) {
            getMvpView().showAvatar(profile.response.avatar);
        } else {
            getMvpView().showDefaultAvatar();
        }
        getMvpView().showUsername(profile.response.username);
        getMvpView().showJoinedDate(profile.response.stats.joinedDate);
        getMvpView().showUserClass(profile.response.personal.mclass);
        if (!profile.response.profileText.equals("")) {
            getMvpView().showUserDescription(profile.response.profileText);
        }
        else {
            getMvpView().showUserDescriptionEmpty();
        }


        //workaround due to paranoid users having "" as lastAccess, rather than null. When this is fixed, change the type on Profile back to Date and let gson auto parse it

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);


        if(profile.response.stats.lastAccess == null || profile.response.stats.lastAccess.equals("")) getMvpView().showLastSeenParanoid(); else
            try {
                getMvpView().showLastSeen(format.parse(profile.response.stats.lastAccess));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        if(profile.response.stats.ratio == null || profile.response.stats.requiredRatio == null) getMvpView().showRatioParanoid(); else getMvpView().showRatio(profile.response.stats.ratio, profile.response.stats.requiredRatio);
        if(profile.response.ranks.uploaded == null) getMvpView().showUploadedParanoid(); else getMvpView().showUploaded(profile.response.ranks.uploaded);
        if(profile.response.ranks.uploads == null) getMvpView().showNumUploadsParanoid(); else getMvpView().showNumUploads(profile.response.ranks.uploads);
        if(profile.response.ranks.downloaded == null) getMvpView().showDownloadedParanoid(); else getMvpView().showDownloaded(profile.response.ranks.downloaded);
        if(profile.response.ranks.requests == null) getMvpView().showRequestsParanoid(); else getMvpView().showRequestsFilled(profile.response.ranks.requests);
        if(profile.response.ranks.bounty == null) getMvpView().showBountySpentParanoid(); else getMvpView().showBountySpent(profile.response.ranks.bounty);
        if(profile.response.ranks.posts == null) getMvpView().showNumForumPostsParanoid(); else getMvpView().showNumForumPosts(profile.response.ranks.posts);
        if(profile.response.ranks.artists == null) getMvpView().showArtistsAddedParanoid(); else getMvpView().showArtistsAdded(profile.response.ranks.artists);
        if(profile.response.ranks.overall == null) getMvpView().showOverallRankParanoid(); else getMvpView().showOverallRank(profile.response.ranks.overall);
    }

    public void loadRecents(int id) {
        checkViewAttached();
        getMvpView().showLoadingProgress(true);

        mDataManager.getRecents(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new DisposableSingleObserver<Recents>() {
                @Override public void onSuccess(Recents value) {
                    if (value.response.snatches != null && value.response.snatches.size() > 0) {
                        getMvpView().showRecentSnatches(value.response.snatches);
                    } else {
                        getMvpView().showRecentSnatchesEmpty();
                    }

                    if (value.response.uploads != null && value.response.uploads.size() > 0) {
                        getMvpView().showRecentUploads(value.response.uploads);
                    } else {
                        getMvpView().showRecentUploadsEmpty();
                    }
                }

                @Override public void onError(Throwable error) {
                    getMvpView().showRecentSnatchesEmpty();
                    getMvpView().showRecentUploadsEmpty();
                }
            });
    }

    public void newMessage(int userId, String subject, String body) {
        mDataManager.sendMessage(String.valueOf(userId), "", subject, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Object>() {
                    @Override public void onSubscribe(Disposable d) {

                    }

                    @Override public void onSuccess(Object value) {
                    }

                    @Override public void onError(Throwable error) {
                        if (error instanceof HttpException) {
                            HttpException httpException = ((HttpException) error);
                            if (httpException.code() == 302) {
                                getMvpView().showSnackbar("Message sent");
                            }
                        }
                        else if (error.getMessage().equals("User not found")){
                            getMvpView().showSnackbar("User not found");
                        }
                        else {
                            getMvpView().showSnackbar(error.getMessage());
                        }
                    }
                });
    }
}
