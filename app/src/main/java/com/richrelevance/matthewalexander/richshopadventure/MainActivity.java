package com.richrelevance.matthewalexander.richshopadventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.richrelevance.*;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.recommendations.PlacementResponse;
import com.richrelevance.recommendations.PlacementResponseInfo;
import com.richrelevance.recommendations.RecommendedProduct;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.richrelevance.matthewalexander.richshopadventure.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClientConfiguration config = new ClientConfiguration("showcaseparent", "ccfa17d092268c0");

        //config.setApiClientSecret("r5j50mlag06593401nd4kt734i");
        //ClientConfiguration config = new ClientConfiguration("showcaseparent", "ccfa17d092268c0");
        config.setUserId("");
        config.setSessionId(UUID.randomUUID().toString());

        RichRelevance.init(this, config);
        RichRelevance.setLoggingLevel(RRLog.VERBOSE);
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

    public void sendMessage(View view) {
        Intent intent       = new Intent(this, DisplayMessageActivity.class);
        EditText editText   = (EditText) findViewById(R.id.edit_message);
        String message      = editText.getText().toString();

        Placement placement = new Placement(Placement.PlacementType.ADD_TO_CART, "prod1");
        Log.d("MATT", "HERE:");
        RichRelevance.buildRecommendationsForPlacements(placement)
        .setCallback(new Callback<PlacementResponseInfo>() {
            @Override
            public void onResult(PlacementResponseInfo result) {
                PlacementResponse placement = result.getPlacements().get(0);

                RecommendedProduct product = placement.getRecommendedProducts().get(0);
                Log.d("MATT", product.toString());
                product.trackClick();
            }

            @Override
            public void onError(com.richrelevance.Error error) {
                Log.e("MATT", "Error: " + error.getMessage());
            }
        })
        .execute();

        Log.d("MATT", "2");
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
