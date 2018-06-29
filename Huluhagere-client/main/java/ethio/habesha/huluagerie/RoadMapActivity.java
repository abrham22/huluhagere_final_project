
package ethio.habesha.huluagerie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoadMapActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RoadMapAdaptor mRoadmapAdapter;
    private TextView mErrorMessageDisplay;

    private String TAG = "ROADMAP";
    private DataController.Roadmap displayedRoadmap;

    Button savedPlans, generatePlans, save_remove;

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
        setContentView(R.layout.roadmap);

        save_remove = findViewById(R.id.save_remove);

        Intent intent = getIntent();

        displayedRoadmap = (DataController.Roadmap) intent.getSerializableExtra("roadmap");

        boolean isNew = intent.getBooleanExtra("is_new", true);
        if (isNew) {
            save_remove.setText("Save");
            save_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RoadMapActivity.this, LoginActivity.class);
                    int SAVE_ROADMAP = 23;
                    startActivityForResult(intent, SAVE_ROADMAP);
                }
            });
        } else {
            save_remove.setText("Remove");
            save_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeRoadMap(displayedRoadmap);
                }
            });
        }


        savedPlans = findViewById(R.id.saved_plans);
        generatePlans = findViewById(R.id.generate_plans);

        savedPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoadMapActivity.this, SavedRoadmapActivity.class);
                intent.putExtra("email", displayedRoadmap.email);
                startActivity(intent);
            }
        });

        generatePlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoadMapActivity.this, PreferenceActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview_roadmap);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRoadmapAdapter = new RoadMapAdaptor();
        loadRoadmapData(displayedRoadmap);

        mRecyclerView.setAdapter(mRoadmapAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            finish();
            return;
        }
        final String email = data.getStringExtra("email");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Roadmap Title");

        final EditText input = new EditText(this);
        input.setId(R.id.road_map_name);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                displayedRoadmap.name = name;
                displayedRoadmap.email = email;
                saveRoadmap(displayedRoadmap, mIdlingResource);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void saveRoadmap(final DataController.Roadmap roadmap,
                             @Nullable final IdleSimpleIdlingResource idlingResource) {

        DataController dataController = DataController.getDataController();

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        dataController.saveRoadmap(new Callback<DataController.GeneralResponse>() {
            @Override
            public void onResponse(Call<DataController.GeneralResponse> call,
                                   Response<DataController.GeneralResponse> response) {

                if (response.isSuccessful()) {

                    DataController.GeneralResponse resp = response.body();

                    if (resp.success) {
                        Utility.toastText(getApplicationContext(), TAG, "Roadmap successfully saved");
                        Intent intent = new Intent(RoadMapActivity.this,
                                SavedRoadmapActivity.class);
                        intent.putExtra("email", roadmap.email);
                        // The IdlingResource is null in production.
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                        startActivity(intent);
                    } else {
                        Utility.toastText(getApplicationContext(), TAG, "Roadmap could not be saved");
                    }

                } else {
                    Utility.toastText(getApplicationContext(), TAG, "Roadmap not saved");
                }

            }

            @Override
            public void onFailure(Call<DataController.GeneralResponse> call, Throwable t) {
                Utility.toastText(getApplicationContext(), TAG, "Roadmap successfully saved");
                t.printStackTrace();
            }

        }, roadmap);
    }

    private void loadRoadmapData(DataController.Roadmap roadmap) {

        showRoadmapView();
        mRoadmapAdapter.setRoadmapData(roadmap);

    }


    private void showRoadmapView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void removeRoadMap(DataController.Roadmap roadmap) {

        String name = roadmap.name;
        final String email = roadmap.email;

        DataController dataController = DataController.getDataController();
        DataController.NameWrapper wrapper = new DataController.NameWrapper();
        wrapper.name = name;

        dataController.removeRoadmap(new Callback<DataController.GeneralResponse>() {
            @Override
            public void onResponse(Call<DataController.GeneralResponse> call,
                                   Response<DataController.GeneralResponse> response) {

                if (response.isSuccessful()) {
                    Utility.toastText(getApplicationContext(), TAG, "Roadmap successfully removed");
                    Intent intent = new Intent(RoadMapActivity.this,
                            SavedRoadmapActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Utility.toastText(getApplicationContext(), TAG, "Roadmap not removed");
                }

            }

            @Override
            public void onFailure(Call<DataController.GeneralResponse> call, Throwable t) {
                Utility.toastText(getApplicationContext(), TAG, "Error connecting database");
                t.printStackTrace();
            }
        }, wrapper);
    }

}