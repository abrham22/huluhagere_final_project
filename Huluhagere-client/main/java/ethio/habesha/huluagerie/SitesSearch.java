package ethio.habesha.huluagerie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SitesSearch extends AppCompatActivity {

    Button btnSearch;
    EditText site_name;

    TextView txtError;
    String TAG = "SEARCH_SITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_sites_search);
        txtError = (TextView)findViewById(R.id.error_message);
        site_name = (EditText) findViewById(R.id.site_name);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtError.setVisibility(View.INVISIBLE);
                String name = site_name.getText().toString();
                if (!name.equalsIgnoreCase("")) {
                    searchSite(name);
                }
            }
        });
    }

    private void searchSite(String name) {

        DataController dataController = DataController.getDataController();

        dataController.fetchSite(new Callback<DataController.Site>() {

            @Override
            public void onResponse(@NonNull Call<DataController.Site> call,
                                   @NonNull Response<DataController.Site> response) {

                if (response.isSuccessful()) {

                    DataController.Site site = response.body();

                    Utility.toastText(getApplicationContext(), TAG, "Site found");
                    // remove from recycler view
                    Intent intent = new Intent(SitesSearch.this, SitesActivity.class);
                    intent.putExtra("site", site);
                    startActivity(intent);

                } else {
                    Utility.toastText(getApplicationContext(), TAG, "Site not found");
                    txtError.setVisibility(View.VISIBLE);
                    txtError.setText("No result found, try with a different name!");
                }

            }

            @Override
            public void onFailure(Call<DataController.Site> call, Throwable t) {
                t.printStackTrace();
                txtError.setVisibility(View.VISIBLE);
                txtError.setText("Error connecting to database");
            }

        }, name);

    }

}
