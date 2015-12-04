package com.github.jackkell.mimicryproject.mainactivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.jackkell.mimicryproject.Config;
import com.github.jackkell.mimicryproject.databaseobjects.Impersonator;
import com.github.jackkell.mimicryproject.R;
import com.github.jackkell.mimicryproject.listadpaters.ImpersonatorSelectableAdapter;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.List;

// This screen is used to load all of the Impersonators from the database and display them on screen in a List View
// It displays each Impersonator and gives information about their posts
public class ImpersonatorSelectionActivity extends Activity {
    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json";
    // List of impersonators in the database
    private List<Impersonator> impersonators;
    private RecyclerView rvImpersonatorSelection;
    private ImpersonatorSelectableAdapter impersonatorSelectableAdapter;
    private String TAG = "ImpersonatorSelectionActivity";

    @Override
    // This runs when the activity is opened
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impersonator_selection);
        impersonators = Impersonator.listAll(Impersonator.class);

        rvImpersonatorSelection = (RecyclerView) findViewById(R.id.rvImpersonatorSelection);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvImpersonatorSelection.setLayoutManager(linearLayoutManager);
        impersonatorSelectableAdapter = new ImpersonatorSelectableAdapter(impersonators);
        rvImpersonatorSelection.setAdapter(impersonatorSelectableAdapter);

        FloatingActionButton addImpersonatorButton = (FloatingActionButton) findViewById(R.id.fabCreateImpersonator);
        addImpersonatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent impersonatorCreation = new Intent(getApplicationContext(), ImpersonatorCreationActivity.class);
                startActivity(impersonatorCreation);
                finish();
            }
        });

        rvImpersonatorSelection.post(new Runnable() {
            @Override
            public void run() {
                setScrollViewChildrenOnClick();
            }
        });
        Log.d("Trevor", "Test");
        OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
                .apiKey(Config.CONSUMER_KEY)
                .apiSecret(Config.CONSUMER_KEY_SECRET)
                .build();
        Token requestToken = service.getRequestToken();
        String authUrl = service.getAuthorizationUrl(requestToken);
        Verifier v = new Verifier("verifier you got from the user");
        Token accessToken = service.getAccessToken(requestToken, v);
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.twitter.com/1/account/verify_credentials.xml");
        service.signRequest(accessToken, request);
        Response response = request.send();
        Log.d("Trevor", response.getBody());
    }

    @Override
    protected void onResume(){
        super.onResume();
        impersonatorSelectableAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_impersonator_selection, menu);
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

    // Set the list's children's onClick handler
    private void setScrollViewChildrenOnClick(){
        Log.d("ImpersonatoreSelection", rvImpersonatorSelection.getChildCount() + "");
        for (int i = 0; i < rvImpersonatorSelection.getChildCount(); i++) {
            final int index = i;
            rvImpersonatorSelection.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long ID = impersonators.get(index).getId();
                    Log.d(TAG, "ID is " + ID);
                    Intent impersonatorCreation = new Intent(getBaseContext(), ImpersonatorViewActivity.class);
                    impersonatorCreation.putExtra("impersonatorID", ID);
                    impersonatorCreation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(impersonatorCreation);
                }
            });
        }
    }
}
