package com.rainmonth.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.rainmonth.utils.log.LogUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class SampleIntentService extends IntentService {
    public static final String TAG = SampleIntentService.class.getSimpleName();
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.rainmonth.service.action.FOO";
    private static final String ACTION_BAZ = "com.rainmonth.service.action.BAZ";

    private static final String INTENT_NAME = "com.rainmonth.service.extra.INTENT_NAME";
    private static final String INTENT_MESSAGE = "com.rainmonth.service.extra.INTENT_MESSAGE";

    public SampleIntentService() {
        super("SampleIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SampleIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(INTENT_NAME, param1);
        intent.putExtra(INTENT_MESSAGE, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SampleIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(INTENT_NAME, param1);
        intent.putExtra(INTENT_MESSAGE, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(INTENT_NAME);
                final String param2 = intent.getStringExtra(INTENT_MESSAGE);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(INTENT_NAME);
                final String param2 = intent.getStringExtra(INTENT_MESSAGE);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        LogUtils.d(TAG, "handleActionFoo() executed");
        LogUtils.d(TAG, param1 + "" + param2);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        LogUtils.d(TAG, "handleActionBaz() executed");
        LogUtils.d(TAG, param1 + "" + param2);
    }
}
