package ch.redacted;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;


import com.facebook.stetho.Stetho;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

import ch.redacted.app.R;
import ch.redacted.data.NotificationService;
import ch.redacted.injection.component.ApplicationComponent;
import ch.redacted.injection.component.DaggerApplicationComponent;
import ch.redacted.injection.module.ApplicationModule;

/**
 * Created by fatih on 11.9.2015.
 * This base application keeps the MySoup session with the site active and takes care of loading
 * images using Glide
 */

@ReportsCrashes(
		formUri = "http://127.0.0.1:5984/acra-redacted/_design/acra-storage/_update/report",
		reportType = org.acra.sender.HttpSender.Type.JSON,
		httpMethod = org.acra.sender.HttpSender.Method.PUT,
		formUriBasicAuthLogin="reporteruser",
		formUriBasicAuthPassword="reporteruser",
		mode = ReportingInteractionMode.DIALOG,
		resDialogOkToast = R.string.reportsent,
		resDialogCommentPrompt = R.string.crashcomment,
		resDialogText = R.string.crashdialog,
		resDialogTitle = R.string.crashdialog_title,
		resDialogPositiveButtonText = R.string.send,
		resDialogNegativeButtonText = R.string.dontsend,
		resDialogTheme = android.R.style.Theme_DeviceDefault_Dialog
)

public class REDApplication extends Application {

	//TODO: Developers put your local Gazelle install IP here instead of testing on the live site
	//I recommend setting up with Vagrant: https://github.com/dr4g0nnn/VagrantGazelle
	public static final String DEFAULT_SITE = "https://redacted.ch";
//		public static final String DEFAULT_SITE = "https://pthdev.pw";
	ApplicationComponent mApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		startNotificationService();

//		if (BuildConfig.DEBUG) {
        Stetho.initializeWithDefaults(this);
//		}
//		initSoup(DEFAULT_SITE);
//		if (SettingsActivity.lightThemeEnabled(getApplicationContext())) {
//			AppCompatDelegate.setDefaultNightMode(
//					AppCompatDelegate.MODE_NIGHT_NO);
//		} else {
//			AppCompatDelegate.setDefaultNightMode(
//					AppCompatDelegate.MODE_NIGHT_YES);
//		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		final ACRAConfiguration config;
		try {
			config = new ConfigurationBuilder(this)
                    .setCustomReportContent(
                    		ReportField.REPORT_ID,
                    		ReportField.APP_VERSION_CODE,
                    		ReportField.APP_VERSION_NAME,
                    		ReportField.PHONE_MODEL,
                    		ReportField.ANDROID_VERSION,
                    		ReportField.BUILD,
                    		ReportField.BRAND,
                    		ReportField.PRODUCT,
                    		ReportField.TOTAL_MEM_SIZE,
                    		ReportField.AVAILABLE_MEM_SIZE,
                    		ReportField.BUILD_CONFIG,
                    		ReportField.STACK_TRACE,
                    		ReportField.STACK_TRACE_HASH,
                    		ReportField.DISPLAY,
                    		ReportField.USER_COMMENT,
                    		ReportField.USER_APP_START_DATE,
                    		ReportField.USER_CRASH_DATE,
                    		ReportField.DUMPSYS_MEMINFO,
                    		ReportField.INSTALLATION_ID,
                    		ReportField.DEVICE_FEATURES,
                    		ReportField.SETTINGS_SYSTEM,
                    		ReportField.SETTINGS_GLOBAL,
                    		ReportField.THREAD_DETAILS
					)
                    .build();

			ACRA.init(this, config);
		} catch (ACRAConfigurationException e) {
			e.printStackTrace();
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void startNotificationService() {

		JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
		ComponentName componentName = new ComponentName(getApplicationContext(), NotificationService.class);
		//poll every hour for now
		JobInfo jobInfo = new JobInfo.Builder(1, componentName).setRequiresCharging(false).setRequiresDeviceIdle(false).setPeriodic(1000 * 60 * 60).build();
		jobScheduler.schedule(jobInfo);
	}

	public static REDApplication get(Context context) {
		return (REDApplication) context.getApplicationContext();
	}

	public ApplicationComponent getComponent() {
		if (mApplicationComponent == null) {
			mApplicationComponent = DaggerApplicationComponent.builder()
					.applicationModule(new ApplicationModule(this))
					.build();
		}
		return mApplicationComponent;
	}

	// Needed to replace the component with a test specific one
	public void setComponent(ApplicationComponent applicationComponent) {
		mApplicationComponent = applicationComponent;
	}

}
