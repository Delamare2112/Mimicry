package com.github.jackkell.mimicryproject.mainactivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.jackkell.mimicryproject.Config;
import com.github.jackkell.mimicryproject.R;
import com.github.jackkell.mimicryproject.databaseobjects.Impersonator;
import com.github.jackkell.mimicryproject.listadpaters.TwitterUserNameAdapter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

//The logic that helps create an Impersonator Creation Activity
public class ImpersonatorCreationActivity extends Activity {

    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json";

    //The EditText field that allows the user to type in the Impersonators name.
    private EditText etImpersonatorName;
    private List<String> twitterUserNames;
    private ListView lvTwitterUsernames;
    private TwitterUserNameAdapter twitterUserNameAdapter;

    @Override
    //This runs when the activity is opened
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impersonator_creation);

        twitterUserNames = new ArrayList<>();
        twitterUserNames.add("");

        etImpersonatorName = (EditText) findViewById(R.id.etImpersonatorName);

        lvTwitterUsernames = (ListView) findViewById(R.id.lvTwitterUsernames);
        twitterUserNameAdapter = new TwitterUserNameAdapter();
        lvTwitterUsernames.setAdapter(twitterUserNameAdapter);

        FloatingActionButton createImpersonatorButton = (FloatingActionButton) findViewById(R.id.fabCreateImpersonator);
        createImpersonatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateImpersonatorButtonClick();
                // TODO: Loading Screen goes here
                Intent impersonatorSelection = new Intent(getApplicationContext(), ImpersonatorSelectionActivity.class);
                startActivity(impersonatorSelection);
                finish();
            }
        });
    }

    @Override
    //An automatically generated function
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_impersonator_creation, menu);
        return true;
    }

    @Override
    //An automatically generated function
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

    //This is what occurs when the Create Impersonator button is tapped
    private void onCreateImpersonatorButtonClick(){
        final Impersonator impersonator = new Impersonator(etImpersonatorName.getText().toString());
        if (validateImpersonator()){
            TwitterAuthConfig authConfig = new TwitterAuthConfig(Config.CONSUMER_KEY, Config.CONSUMER_KEY_SECRET);
            Fabric.with(this, new Twitter(authConfig));
            TwitterSession session = Twitter.getSessionManager().getActiveSession();

            for(final String username : twitterUserNames) {
                OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
                        .apiKey(Config.CONSUMER_KEY)
                        .apiSecret(Config.CONSUMER_KEY_SECRET)
                        .build();
                Token requestToken = service.getRequestToken();
                String authUrl = service.getAuthorizationUrl(requestToken);
                Verifier v = new Verifier("verifier you got from the user");
                Token accessToken = service.getAccessToken(requestToken, v);
                OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
                service.signRequest(accessToken, request);
                Response response = request.send();
                Log.d("Trevor", response.getBody());
            }

//            for (final String username : twitterUserNames) {
//                TwitterCore.getInstance().getApiClient(session).getStatusesService()
//                        .userTimeline(null,
//                                username,
//                                10, //the number of tweets we want to fetch,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                                null,
//                        new Callback<List<Tweet>>() {
//                            @Override
//                            public void success(Result<List<Tweet>> result) {
//                                List<String> tweets = new ArrayList<String>();
//                                for (Tweet t : result.data) {
//                                    tweets.add(t.text);
//                                }
//                                impersonator.addTwitterUser(username, tweets);
//                            }
//
//                            @Override
//                            public void failure(TwitterException exception) {
//                                android.util.Log.d("twittercommunity", "exception " + exception);
//                            }
//                        });
//            }
            impersonator.save();

            Intent impersonatorSelection = new Intent(getApplicationContext(), ImpersonatorSelectionActivity.class);
            startActivity(impersonatorSelection);
            finish();
        } else {
            Toast.makeText(this, "Please fill in required fields.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateImpersonator(){
        boolean isValid = true;

        if (etImpersonatorName.getText().toString().isEmpty()){
            isValid = false;
        }

        for (String twitterUserName : twitterUserNames){
            if (twitterUserName.isEmpty()){
                isValid = false;
            }
        }

        return isValid;
    }

    private void addTextChangedListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            int position;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if this is last edittext, add another.  if not, do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // if string is empty, check if at end of list. If so, do nothing.  If not at end of list, delete edittext
            }
        });
    }
}
