package io.github.gatimus.twittersearches;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Status;

public class TweetAdapter extends ArrayAdapter<Status> {

    private Context context;
    private List<Status> tweets;

    public TweetAdapter(Context context, List<Status> objects) {
        super(context, R.layout.tweet_list_item, objects);
        Log.v(getClass().getSimpleName(), "construct");
        this.context = context;
        tweets = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(getClass().getSimpleName(), "getView");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tweet_list_item, parent, false);
        TextView text = (TextView) rowView.findViewById(android.R.id.text1);
        TextView hashTags = (TextView) rowView.findViewById(android.R.id.text2);
        TextView userName = (TextView) rowView.findViewById(R.id.userName);
        ImageView userImage = (ImageView) rowView.findViewById(R.id.userImage);

        text.setText(tweets.get(position).getText().toString());
        HashtagEntity[] hashtagEntities = tweets.get(position).getHashtagEntities();
        StringBuilder stringBuilder = new StringBuilder();
        for(HashtagEntity hashTag : hashtagEntities){
            stringBuilder.append(" #" + hashTag.getText());
        }
        hashTags.setText(stringBuilder.toString());
        userName.setText(tweets.get(position).getUser().getScreenName());

        Picasso picasso = Picasso.with(context);
        if(BuildConfig.DEBUG){
            picasso.setIndicatorsEnabled(true);
        }

        picasso.load(tweets.get(position).getUser().getBiggerProfileImageURL())
                .placeholder(android.R.drawable.stat_sys_download)
                .error(android.R.drawable.stat_sys_download)
                .into(userImage);

        return rowView;
    } //getView

}
