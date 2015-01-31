package io.github.gatimus.twittersearches;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class SearchResults extends ListActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private TweetAdapter statusAdapter;
    private ArrayList<Status> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(android.R.id.list);
        progressBar = (ProgressBar) findViewById(android.R.id.empty);
        listView.setEmptyView(progressBar);
        tweets = new ArrayList<Status>();
        statusAdapter = new TweetAdapter(getApplicationContext(), tweets);
        statusAdapter.setNotifyOnChange(true);
        listView.setAdapter(statusAdapter);

    }


    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            TwitterThread tt = new TwitterThread();
            tt.execute(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            TwitterThread tt = new TwitterThread();
            tt.execute(query);
        }
    }


    public class TwitterThread extends AsyncTask<String, Void, List<Status>>{

        @Override
        protected List<twitter4j.Status> doInBackground(String... params) {
            Log.d(getClass().getSimpleName(), "doInBackground");
            List<twitter4j.Status> statusList = new ArrayList<twitter4j.Status>();
            ConfigurationBuilder cb = new ConfigurationBuilder();
            Log.d(getClass().getSimpleName(), "ConfigurationBuilder");
            cb.setDebugEnabled(BuildConfig.DEBUG)
                    .setOAuthConsumerKey("W8GmAeFhiPgJQorQeR2u04fAX")
                    .setOAuthConsumerSecret("lqtqj31FTa8K3ZwJqtD80buHHfYc11QzgEKr9l0hCdwkkT7S0z")
                    .setOAuthAccessToken("602173599-kF42sLKv3mhyUrtsS3D2lBShgedD09kTPxT29xIb")
                    .setOAuthAccessTokenSecret("CvddjEVyQqYYD3zsFzZl7ldjnphTjp1ui3ykJfcEOcGKS");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter t = tf.getInstance();
            Query query = new Query(params[0]);
            query.setLocale(Locale.getDefault().toString());
            query.setLang(Locale.getDefault().getLanguage());
            query.setResultType(Query.ResultType.mixed);
            try {
                QueryResult result = t.search(query);
                Log.d(getClass().getSimpleName(), "query");
                //tweets.removeAll(tweets);
                for (twitter4j.Status status : result.getTweets()) {
                    statusList.add(status);
                    Log.d(getClass().getSimpleName(), status.getText());
                }
            } catch (TwitterException e) {
                Log.e(getClass().getSimpleName(), e.toString());
            }

            return statusList;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statusList){
            tweets.addAll(statusList);
            statusAdapter.notifyDataSetChanged();
        }
    }
}
