package ethio.habesha.huluagerie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSiteActivity extends AppCompatActivity {

    EditText site_name, service1, service2;
    EditText artifact1, artifact2;
    Button editButton;

    TextView title;

    DataController.Site target_site;

    private String TAG = "ADD SITE";


    // The Idling Resource which will be null in production.
    @Nullable
    private IdleSimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link IdleSimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdleSimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        site_name = findViewById(R.id.site_name);
        service1 = findViewById(R.id.service1);
        service2 = findViewById(R.id.service2);
        artifact1 = findViewById(R.id.artifact1);
        artifact2 = findViewById(R.id.artifact2);
        editButton = findViewById(R.id.submit);
        title = findViewById(R.id.activityDescription);

        Intent thisIntent = getIntent();
        if(thisIntent.getBooleanExtra("update", false)){
            target_site = (DataController.Site) thisIntent.getSerializableExtra("site");
            populateSite(target_site);
            title.setText(R.string.update_site_label);
            editButton.setText(R.string.update_site_label);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferUpdates();
                    updateSite(target_site, target_site.name);
                }
            });
        } else {
            title.setText(R.string.add_site_label);
            editButton.setText(R.string.add_site_label);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataController.Site site = extractSite();
                    addSite(site, mIdlingResource);
                }
            });
        }

    }

    private DataController.Site extractSite() {

        DataController.Site site = new DataController.Site();
        site.name = site_name.getText().toString();
        site.address = new DataController.Address();
        site.address.city = site_name.getText().toString();

        site.site_services = new ArrayList<>();
        DataController.Service service_one = new DataController.Service();
        DataController.Service service_two = new DataController.Service();
        service_one.name = service1.getText().toString();
        service_two.name = service2.getText().toString();
        site.site_services.add(service_one);
        site.site_services.add(service_two);

        site.artifacts = new ArrayList<>();
        DataController.Artifact artifact_one = new DataController.Artifact();
        DataController.Artifact artifact_two = new DataController.Artifact();
        artifact_one.name = artifact1.getText().toString();
        artifact_two.name = artifact2.getText().toString();
        site.artifacts.add(artifact_one);
        site.artifacts.add(artifact_two);

        return site;
    }

    private void transferUpdates() {
        target_site.name = site_name.getText().toString();
        target_site.address.city = site_name.getText().toString();
        target_site.site_services.get(0).name = service1.getText().toString();
        target_site.site_services.get(1).name = service2.getText().toString();
        target_site.artifacts.get(0).name = artifact1.getText().toString();
        target_site.artifacts.get(1).name = artifact2.getText().toString();
    }

    private void updateSite(final DataController.Site update, String name) {

        DataController dataController = DataController.getDataController();

        DataController.SiteUpdate updateInfo = new DataController.SiteUpdate();
        updateInfo.update = update;
        updateInfo.name = name;
        dataController.updateSite(new Callback<DataController.GeneralResponse>() {

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
                                "Site successfully updated");
                        // remove from recycler view
                        Intent intent = new Intent(AddSiteActivity.this,
                                SitesActivity.class);
                        intent.putExtra("site", update);
                        startActivity(intent);
                    } else {
                        Utility.toastText(getApplicationContext(), TAG, resp.error);
                    }
                } else {
                    Utility.toastText(getApplicationContext(), TAG, "could not update site");
                }

            }

            @Override
            public void onFailure(@NonNull Call<DataController.GeneralResponse> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        }, updateInfo);

    }

    private void addSite(final DataController.Site site, @Nullable final IdleSimpleIdlingResource idlingResource) {

        DataController dataController = DataController.getDataController();

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        dataController.saveSite(new Callback<DataController.GeneralResponse>() {

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
                                "Site successfully added");
                        // remove from recycler view
                        Intent intent = new Intent(AddSiteActivity.this,
                                SitesActivity.class);
                        intent.putExtra("site", site);
                        // The IdlingResource is null in production.
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AddSiteActivity.this,
                                SitesActivity.class);
                        intent.putExtra("site", site);
                        startActivity(intent);
                        Utility.toastText(getApplicationContext(), TAG, resp.message);
                    }
                } else {
                    Intent intent = new Intent(AddSiteActivity.this,
                            SitesActivity.class);
                    intent.putExtra("site", site);
                    startActivity(intent);
                    Utility.toastText(getApplicationContext(), TAG, "could not add site");
                }

            }

            @Override
            public void onFailure(@NonNull Call<DataController.GeneralResponse> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        }, site);

    }

    private void populateSite(DataController.Site site) {

        // set site name and location
        site_name.setText(site.name);
        // set artifacts
        ArrayList<DataController.Artifact> artifacts = site.artifacts;
        Utility.toastText(getApplicationContext(), TAG, "artifacts: "+artifacts.size());
        if(artifacts.size() == 1) {
            artifact1.setText(artifacts.get(0).name);
        } else if(artifacts.size() > 1) {
            artifact1.setText(artifacts.get(0).name);
            artifact2.setText(artifacts.get(1).name);
        }
        // set services
        ArrayList<DataController.Service> services = site.site_services;
        Utility.toastText(getApplicationContext(), TAG, "services: "+services.size());

        if(services.size() == 1) {
            service1.setText(services.get(0).name);
        } else if(services.size() > 1) {
            service1.setText(services.get(0).name);
            service2.setText(services.get(1).name);
        }

    }

}
