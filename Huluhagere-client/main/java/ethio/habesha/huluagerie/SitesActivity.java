package ethio.habesha.huluagerie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SitesActivity extends AppCompatActivity {

    TextView site_name;
    TextView service1, service2;
    TextView artifact1, artifact2;
    TextView openingHour, closingHour;

    private String TAG = "SITE";

    DataController.Site target_site;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        site_name = findViewById(R.id.site_name);
        service1 = findViewById(R.id.service1);
        service2 = findViewById(R.id.service2);
        artifact1 = findViewById(R.id.artifact1);
        artifact2 = findViewById(R.id.artifact2);
        openingHour = findViewById(R.id.openingHour);
        closingHour = findViewById(R.id.closingHour);

        Intent intent = getIntent();
        target_site = (DataController.Site) intent.getSerializableExtra("site");
        populateSite(target_site);

        final Context thisContext = this;

        FloatingActionButton fabDelete = findViewById(R.id.delete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(thisContext);
                alertDialog.setTitle("Delete Site!");
                alertDialog.setMessage("Are you sure, you want to delete "+target_site.name+"?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSite(target_site.name);
                            }
                        });
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do noting
                            }
                        });
                alertDialog.create().show();
            }
        });

        FloatingActionButton fabEdit = findViewById(R.id.edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSiteIntent = new Intent(SitesActivity.this, AddSiteActivity.class);
                addSiteIntent.putExtra("update", true);
                addSiteIntent.putExtra("site", target_site);
                startActivity(addSiteIntent);
            }
        });

    }

    private void deleteSite(String name) {
        
        DataController dataController = DataController.getDataController();

        DataController.NameWrapper wrapper = new DataController.NameWrapper();
        wrapper.name = name;

        dataController.removeSite(new Callback<DataController.GeneralResponse>() {

            @Override
            public void onResponse(@NonNull Call<DataController.GeneralResponse> call,
                                   @NonNull Response<DataController.GeneralResponse> response) {

                if(response.isSuccessful()) {
                    DataController.GeneralResponse resp = response.body();
                    if(resp == null) {
                        Utility.toastText(getApplicationContext(), TAG,
                                "Null Response from the server");
                        return;
                    }
                    if(resp.success) {
                        Utility.toastText(getApplicationContext(), TAG,
                                "Site successfully deleted");
                        Intent intent = new Intent(SitesActivity.this,
                                SitesListActivity.class);
                        startActivity(intent);
                    } else {
                        Utility.toastText(getApplicationContext(), TAG, resp.error);
                    }
                } else {
                    Utility.toastText(getApplicationContext(), TAG, "could not delete site");
                }

            }

            @Override
            public void onFailure(@NonNull Call<DataController.GeneralResponse> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        }, wrapper);

    }

    private void populateSite(DataController.Site site) {
        // set site name and location
        if(site == null) {
            Utility.toastText(getApplicationContext(), TAG,"Site is null");
            return;
        }
        site_name.setText(site.name);
        // set artifacts
        ArrayList<DataController.Artifact> artifacts = site.artifacts;
        if(artifacts.size() == 1) {
            artifact1.setText(artifacts.get(0).name);
        } else if(artifacts.size() > 1) {
            artifact1.setText(artifacts.get(0).name);
            artifact2.setText(artifacts.get(1).name);
        }
        // set services
        ArrayList<DataController.Service> services = site.site_services;
        if(services.size() == 1) {
            service1.setText(services.get(0).name);
        } else if(services.size() > 1) {
            service1.setText(services.get(0).name);
            service2.setText(services.get(1).name);
        }
        // set opening and closing hours
        if(site.opening_hour != null) {
            openingHour.setText(site.opening_hour.toString());
        }
        if(site.closing_hour != null) {
            closingHour.setText(site.closing_hour.toString());
        }
    }

}

