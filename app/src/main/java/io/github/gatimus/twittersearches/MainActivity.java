package io.github.gatimus.twittersearches;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TwitterThread tt = new TwitterThread();
        tt.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TwitterThread extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            // Connect to the filter endpoint, tracking the term "twitterapi"
            Hosts host = new HttpHosts(Constants.STREAM_HOST);
            StreamingEndpoint endpoint = new StatusesFilterEndpoint();
            ((StatusesFilterEndpoint)endpoint).trackTerms(Lists.newArrayList("twitterapi"));

// Drop in the oauth credentials for your app, available on dev.twitter.com
            Authentication auth = new OAuth1(
                    );

// Initialize a queue to collect messages from the stream
            BlockingQueue<String> messages = new LinkedBlockingQueue<String>(100000);

// Build a client and read messages until the connection closes.
            ClientBuilder builder = new ClientBuilder()
                .name("FooBarBaz-StreamingClient")
                .hosts(host)
                .authentication(auth)
                .endpoint(endpoint)
                .processor(new StringDelimitedProcessor(messages));
            Client client = builder.build();
            client.connect();


            while (!client.isDone()) {
                try{
                    String message = messages.take();
                    // Do something with message
                    Log.d(getClass().getSimpleName(), message);
                }catch (Exception e){
                    Log.e(getClass().getSimpleName(), e.toString());
                }
            }

            return null;
        }
    }
}
