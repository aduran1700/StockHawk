package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by aduran on 5/28/16.
 */
public class QuoteWidgetIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public QuoteWidgetIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
