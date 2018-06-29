
package ethio.habesha.huluagerie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedRoadmapActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SavedRoadmapAdaptor mRoadmapAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String TAG = "ROADMAP";

    Button generatePlans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_roadmaps);

        generatePlans = findViewById(R.id.generate_plans);

        generatePlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedRoadmapActivity.this,
                        PreferenceActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView =  findViewById(R.id.recyclerview_saved_roadmap);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRoadmapAdapter = new SavedRoadmapAdaptor();
        mRecyclerView.setAdapter(mRoadmapAdapter);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        loadRoadMaps(email);

    }

    private void loadRoadMaps(String email) {

        DataController dataController = new DataController();

        dataController.fetchRoadmaps(new Callback<ArrayList<DataController.Roadmap>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<DataController.Roadmap>> call,
                                   @NonNull Response<ArrayList<DataController.Roadmap>> response) {
                if(response.isSuccessful()) {
                    ArrayList<DataController.Roadmap> roadMaps = response.body();
                    loadRoadmapData(roadMaps);
                } else {
                    Utility.toastText(getApplicationContext(), TAG, "could not fetch roadMaps");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<DataController.Roadmap>> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        }, email);
    }

    private void loadRoadmapData(ArrayList<DataController.Roadmap> roadMaps) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        showRoadmapView();
        mRoadmapAdapter.setRoadmapData(roadMaps);
    }

    private void showRoadmapView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the roadmap data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.roadmap, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Intent intent = new Intent(SavedRoadmapActivity.this,
                    LoginActivity.class);
            int SAVE_ROADMAP = 23;
            startActivityForResult(intent, SAVE_ROADMAP);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}