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
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferenceActivity extends AppCompatActivity {

    private EditText edTourLength;
    private String TAG = "PREFERENCE";

    Button login;

    private ArrayList<String> servicesChecked = new ArrayList<>();
    private String[] serviceStrings = {"entertainment", "culture", "cuisine", "fitness", "nature"};
    private int[] serviceIds = {R.id.entertainment, R.id.culture, R.id.cuisine, R.id.fitness, R.id.nature};
    private ArrayList<String> artifactsChecked = new ArrayList<>();
    private String[] artifactStrings = {"castle", "church", "mosque", "cave", "museum"};
    private int[] artifactIds = {R.id.castle, R.id.church, R.id.mosque, R.id.cave, R.id.museum};
    private ArrayList<String> locationsChecked = new ArrayList<>();
    private String[] locationStrings = {"addisababa", "mekelle", "hawassa", "bahirdar", "adama"};
    private int[] locationIds = {R.id.addisababa, R.id.mekelle, R.id.hawassa, R.id.bahirdar, R.id.adama};
    private ArrayList<String> interestsChecked = new ArrayList<>();
    private String[] interestStrings = {"movies", "swimming", "hiking", "pizza", "gari"};
    private int[] interestIds = {R.id.movies, R.id.swimming, R.id.hiking, R.id.pizza, R.id.gari};


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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreferenceActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        edTourLength = findViewById(R.id.tourLength);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    DataController.Preference pref = getPreference(
                            artifactsChecked, servicesChecked,
                            locationsChecked, interestsChecked,
                            edTourLength.getText().toString()
                    );
                    generateRoadmap(pref, mIdlingResource);
                } else {
                    Utility.toastText(getApplicationContext(), TAG, "Preference not filled");
                }
            }
        });
    }

    private void generateRoadmap(DataController.Preference preference,
                                 @Nullable final IdleSimpleIdlingResource idlingResource) {

        DataController dataController = new DataController();

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        dataController.generateRoadmap(new Callback<DataController.Roadmap>() {
            @Override
            public void onResponse(@NonNull Call<DataController.Roadmap> call,
                                   @NonNull Response<DataController.Roadmap> response) {
                if (response.isSuccessful()) {

                    DataController.Roadmap roadmap = response.body();

                    // verify that site is parsed properly
                    Intent intent = new Intent(PreferenceActivity.this,
                            RoadMapActivity.class);
                    intent.putExtra("is_new", true);
                    intent.putExtra("roadmap", roadmap);

                    // The IdlingResource is null in production.
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }

                    startActivity(intent);

                } else {
                    Utility.toastText(getApplicationContext(), TAG, "Response is not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataController.Roadmap> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }
        }, preference);

    }

    private DataController.Preference getPreference(
            ArrayList<String> artifacts, ArrayList<String> services,
            ArrayList<String> locations, ArrayList<String> interests, String duration) {

        DataController.Preference pref = new DataController.Preference();
        pref.artifacts = listToArtifacts(artifacts);
        pref.services = listToServices(services);
        pref.locations = listToAddresses(locations);
        pref.duration = Float.parseFloat(duration);
        pref.tags = listToArray(interests);
        return pref;

    }

    private boolean validateInputs() {
        return !edTourLength.getText().toString().equals("");
    }

    public void onServicesClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        for (int i = 0; i < serviceStrings.length; i++) {
            if (view.getId() == serviceIds[i]) {
                if (isChecked) {
                    servicesChecked.add(serviceStrings[i]);
                } else {
                    servicesChecked.remove(serviceStrings[i]);
                }
                break;
            }
        }
    }

    public void onArtifactsClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        for (int i = 0; i < artifactStrings.length; i++) {
            if (view.getId() == artifactIds[i]) {
                if (isChecked) {
                    artifactsChecked.add(artifactStrings[i]);
                } else {
                    artifactsChecked.remove(artifactStrings[i]);
                }
                break;
            }
        }
    }

    public void onLocationsClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        for (int i = 0; i < locationStrings.length; i++) {
            if (view.getId() == locationIds[i]) {
                if (isChecked) {
                    locationsChecked.add(locationStrings[i]);
                } else {
                    locationsChecked.remove(locationStrings[i]);
                }
                break;
            }
        }
    }

    public void onInterestsClick(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        for (int i = 0; i < interestStrings.length; i++) {
            if (view.getId() == interestIds[i]) {
                if (isChecked) {
                    interestsChecked.add(interestStrings[i]);
                } else {
                    interestsChecked.remove(interestStrings[i]);
                }
                break;
            }
        }
    }

    private ArrayList<String> listToArray(ArrayList<String> list) {
        return new ArrayList<>(list);
    }

    private ArrayList<DataController.Artifact> listToArtifacts(ArrayList<String> list) {

        ArrayList<DataController.Artifact> artifacts = new ArrayList<>();
        DataController.Artifact artifact;
        for (int i = 0; i < list.size(); i++) {
            artifact = new DataController.Artifact();
            artifact.name = list.get(i);
            artifact.tag = "general";
            artifacts.add(artifact);
        }
        return artifacts;
    }

    private ArrayList<DataController.Service> listToServices(ArrayList<String> list) {

        ArrayList<DataController.Service> services = new ArrayList<>();
        DataController.Service service;
        for (int i = 0; i < list.size(); i++) {
            service = new DataController.Service();
            service.name = list.get(i);
            service.category = new ArrayList<>();
            service.category.add("general");
            service.service_level = 5;
            services.add(service);
        }
        return services;
    }

    private ArrayList<DataController.Address> listToAddresses(ArrayList<String> list) {
        ArrayList<DataController.Address> locations = new ArrayList<>();
        DataController.Address location;
        for (int i = 0; i < list.size(); i++) {
            location = new DataController.Address();
            location.city = list.get(i);
            locations.add(location);
        }
        return locations;
    }

}
