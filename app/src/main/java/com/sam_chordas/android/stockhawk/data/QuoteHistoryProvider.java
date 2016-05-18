package com.sam_chordas.android.stockhawk.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by aduran on 5/18/16.
 */
@ContentProvider(authority = QuoteHistoryProvider.AUTHORITY, database = QuoteHistoryDatabase.class)
public class QuoteHistoryProvider {
    public static final String AUTHORITY = "com.sam_chordas.android.stockhawk.data.QuoteHistoryProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String QUOTES_HISTORY = "quoteShistory";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path:paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = QuoteHistoryDatabase.QUOTES_HISTORY)
    public static class QuoteHistory{
        @ContentUri(
                path = Path.QUOTES_HISTORY,
                type = "vnd.android.cursor.dir/quotehistory"
        )
        public static final Uri CONTENT_URI = buildUri(Path.QUOTES_HISTORY);

        @InexactContentUri(
                name = "QUOTE_DAY_ID",
                path = Path.QUOTES_HISTORY + "/*",
                type = "vnd.android.cursor.item/quotehistory",
                whereColumn = QuoteColumns.SYMBOL,
                pathSegment = 1
        )
        public static Uri withSymbol(String symbol){
            return buildUri(Path.QUOTES_HISTORY, symbol);
        }
    }
}