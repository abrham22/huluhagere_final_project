package ethio.habesha.huluagerie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SitesListActivity extends AppCompatActivity {

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String TAG = "SITES_LIST";

    RecyclerView recyclerView;
    Context context;
    SiteRecyclerViewAdapter recyclerView_Adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SitesListActivity.this,
                        AddSiteActivity.class);
                intent.putExtra("update", false);
                startActivity(intent);
            }
        });

        context = SitesListActivity.this;

        recyclerView = findViewById(R.id.recycler_grid_card_view);

        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
        recyclerViewLayoutManager = new GridLayoutManager(context, 1);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerView_Adapter = new SiteRecyclerViewAdapter();

        recyclerView.setAdapter(recyclerView_Adapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        recyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        loadSites();

    }

    private void loadSites() {

        DataController dataController = DataController.getDataController();

        dataController.fetchSites(new Callback<ArrayList<DataController.Site>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<DataController.Site>> call,
                                   @NonNull Response<ArrayList<DataController.Site>> response) {
                if(response.isSuccessful()) {
                    ArrayList<DataController.Site> loadedSites = response.body();
                    loadSitesData(loadedSites);
                } else {
                    Utility.toastText(getApplicationContext(), TAG, "could not fetch sites");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<DataController.Site>> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void loadSitesData(ArrayList<DataController.Site> loadedSites) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        showSitesData();
        recyclerView_Adapter.loadSitesData(loadedSites);

    }

    private void showSitesData() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the roadmap data is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sites_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(SitesListActivity.this, SitesSearch.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
