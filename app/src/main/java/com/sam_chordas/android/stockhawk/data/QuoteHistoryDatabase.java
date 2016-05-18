package com.sam_chordas.android.stockhawk.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by aduran on 5/18/16.
 */
@Database(version = QuoteDatabase.VERSION)
public class QuoteHistoryDatabase {
    private QuoteHistoryDatabase(){}

    public static final int VERSION = 1;

    @Table(QuoteColumns.class) public static final String QUOTES_HISTORY = "quotesHistory";
}