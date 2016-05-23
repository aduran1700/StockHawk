package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;
import com.db.chart.view.animation.easing.LinearEase;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Quote;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StockActivity extends AppCompatActivity {
    private LineChartView mChart;
    int low;
    int high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        mChart = (LineChartView) findViewById(R.id.linechart);

        Quote quote = getIntent().getParcelableExtra(MyStocksActivity.QUOTE_ITEM);

        new StockHistoricalDataAsyncTask().execute(quote);
    }


    class StockHistoricalDataAsyncTask extends AsyncTask<Quote, Void, LineSet> {
        private OkHttpClient client = new OkHttpClient();

        String fetchData(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }


        @Override
        protected LineSet doInBackground(Quote... params) {
            Quote aQuote = params[0];
            String stockSymbol = aQuote.symbol;
            ArrayList<Quote> quotes = new ArrayList<>();

            StringBuilder urlStringBuilder = new StringBuilder();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal = Calendar.getInstance();
            String endDate = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, -7);
            String startDate = dateFormat.format(cal.getTime());

            // Base URL for the Yahoo query
            urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
            urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = \""+stockSymbol
                    +"\" and startDate = \""+startDate+"\" and endDate = \""+ endDate +"\""));

            // finalize the URL for the API query.
            urlStringBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
                    + "org%2Falltableswithkeys&callback=");

            String urlString;
            urlString = urlStringBuilder.toString();
            try {
                String getResponse = fetchData(urlString);
                quotes = Utils.quoteHistoryJsonToContentVals(getResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }

            LineSet dataSet = new LineSet();
            float low = 0;
            float high = 0;

            for(int i = quotes.size()-1; i >= 0; i--){
                Quote quote = quotes.get(i);

                try {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = dateFormat.parse(quote.date);
                    dateFormat = new SimpleDateFormat("MMM dd");
                    String day = dateFormat.format(date);
                    Float price = quote.close;

                    if(i == quotes.size()-1) {
                        low = price;
                        high = price;

                    } else {
                        if(low > price)
                            low = price;

                        if(high < price)
                            high = price;
                    }

                    dataSet.addPoint(new Point(day, price));


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            StockActivity.this.low = Math.round(low)-5;
            StockActivity.this.high = Math.round(high)+5;
            dataSet.setColor(Color.parseColor("#b3b5bb"))
                    .setFill(Color.parseColor("#2d374c"))
                    .setDotsColor(Color.parseColor("#ffc755"))
                    .setThickness(4)
                    .endAt(quotes.size());




            //quotes.add(0, quote);
            return dataSet;
        }

        @Override
        protected void onPostExecute(LineSet dataSet) {


            mChart.addData(dataSet);


            mChart.setBorderSpacing(Tools.fromDpToPx(15))
                    .setAxisBorderValues(low, high)
                    .setYLabels(AxisController.LabelPosition.NONE)
                    .setLabelsColor(Color.parseColor("#6a84c3"))
                    .setXAxis(false)
                    .setYAxis(false);

            Animation animation = new Animation().setEasing(new LinearEase());
            mChart.show(animation);
        }
    }
}
