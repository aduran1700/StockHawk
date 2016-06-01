package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Quote;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by aduran on 6/1/16.
 */
public class QuoteWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = QuoteWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        null,
                        QuoteColumns.ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_collection_item);

                Quote quote = new Quote();
                quote.symbol = data.getString(data.getColumnIndex(QuoteColumns.SYMBOL));
                quote.close = Float.parseFloat(Utils.truncateBidPrice(
                        data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE))));

                quote.percentChange = data.getString(data.getColumnIndex(QuoteColumns.PERCENT_CHANGE));
                quote.change = data.getString(data.getColumnIndex(QuoteColumns.CHANGE));
                quote.isUp = data.getInt(data.getColumnIndex(QuoteColumns.ISUP));

                views.setTextViewText(R.id.stock_symbol, quote.symbol);
                views.setTextViewText(R.id.change, quote.percentChange);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    views.setContentDescription(R.id.stock_symbol, quote.symbol);
                    views.setContentDescription(R.id.change, quote.percentChange);
                }

                if (quote.isUp == 1){
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);

                } else{
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }

                final Intent fillInIntent = new Intent();

                fillInIntent.putExtra(MyStocksActivity.QUOTE_ITEM, quote);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }


            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_collection_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(0);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
