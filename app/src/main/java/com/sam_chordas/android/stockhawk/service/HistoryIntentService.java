package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.TaskParams;

/**
 * Created by aduran on 5/18/16.
 */
public class HistoryIntentService extends IntentService {

    public HistoryIntentService() {
        super(HistoryIntentService.class.getName());
    }

    public HistoryIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(StockIntentService.class.getSimpleName(), "History Intent Service");
        HistoryTaskService historyTaskService = new HistoryTaskService(this);
        Bundle args = new Bundle();
        args.putString("symbol", intent.getStringExtra("symbol"));
        // We can call OnRunTask from the intent service to force it to run immediately instead of
        // scheduling a task.
        historyTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
    }
}