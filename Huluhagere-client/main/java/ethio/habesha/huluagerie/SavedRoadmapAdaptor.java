package ethio.habesha.huluagerie;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SavedRoadmapAdaptor extends RecyclerView.Adapter<SavedRoadmapAdaptor.RoadmapAdapterViewHolder> {

    private ArrayList<DataController.Roadmap> mRoadmapData;
    private String TAG = "SAVED_ROADMAP_ADAPTOR";

    class RoadmapAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView mRoadmapTextView;
        final Button deleteButton;
        final Button detailButton;
        final Button renameButton;
        final TextView txtNumDays;

        RoadmapAdapterViewHolder(View view) {
            super(view);
            mRoadmapTextView = (TextView) view.findViewById(R.id.tv_roadmap_name);
            detailButton = (Button)view.findViewById(R.id.detail_btn);
            deleteButton = (Button)view.findViewById(R.id.delete_btn);
            renameButton = (Button)view.findViewById(R.id.rename_btn);
            txtNumDays = (TextView)view.findViewById(R.id.numDays);
        }
    }


    @Override
    public RoadmapAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIdForListItem = R.layout.card_item_saved_roadmaps;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new RoadmapAdapterViewHolder(view);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RoadmapAdapterViewHolder vHolder, int position) {

        String site_name = mRoadmapData.get(position).name;
        vHolder.mRoadmapTextView.setText(site_name);

        Random rand = new Random();
        vHolder.txtNumDays.setText("" + (1 + rand.nextInt(4))+" Day");

        vHolder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = vHolder.getAdapterPosition();
                DataController.Roadmap roadmap = mRoadmapData.get(pos);
                Intent intent = new Intent(v.getContext(), RoadMapActivity.class);
                intent.putExtra("roadmap", roadmap);
                intent.putExtra("is_new", false);
                v.getContext().startActivity(intent);
            }
        });

        vHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = vHolder.getAdapterPosition();
                DataController.Roadmap roadmap = mRoadmapData.get(pos);
                mRoadmapData.remove(pos);
                notifyDataSetChanged();
                removeRoadMap(v.getContext(), roadmap.name);
            }
        });


        vHolder.renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = vHolder.getAdapterPosition();

                final DataController.Roadmap roadmap = mRoadmapData.get(pos);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Enter New Name");

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        roadmap.name = input.getText().toString();
                        notifyDataSetChanged();
                        saveRoadmap(roadmap);
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
        });
    }

    @Override
    public int getItemCount() {
        if (null == mRoadmapData) return 0;
        return mRoadmapData.size();
    }

    public void setRoadmapData(ArrayList<DataController.Roadmap> roadmapData) {
        mRoadmapData = roadmapData;
        notifyDataSetChanged();
    }

    private void removeRoadMap(final Context cxt, final String name) {

        DataController dataController = DataController.getDataController();
        DataController.NameWrapper wrapper = new DataController.NameWrapper();
        wrapper.name = name;

        dataController.removeRoadmap(new Callback<DataController.GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataController.GeneralResponse> call,
                                   @NonNull Response<DataController.GeneralResponse> response) {

                if(response.isSuccessful()) {
                    Utility.toastText(cxt, TAG, "Successfully Removed");
                } else {
                    Utility.toastText(cxt, TAG, "Could not Remove Roadmap");
                }

            }

            @Override
            public void onFailure(@NonNull Call<DataController.GeneralResponse> call,
                                  @NonNull Throwable t) {
                t.printStackTrace();
            }

        }, wrapper);
    }

    private void saveRoadmap(final DataController.Roadmap roadmap) {

        DataController dataController = DataController.getDataController();
        dataController.saveRoadmap(new Callback<DataController.GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataController.GeneralResponse> call,
                                   @NonNull Response<DataController.GeneralResponse> response) {

                if(response.isSuccessful()) {

                    DataController.GeneralResponse resp = response.body();
                    assert resp != null;
                    if(resp.success) {
                        Log.d("SAVING", "Roadmap successfully saved");
                    } else {
                        Log.d("SAVING","Roadmap could not be saved");
                    }

                } else {
                    Log.d("SAVING", "Roadmap not saved");
                }

            }

            @Override
            public void onFailure(@NonNull Call<DataController.GeneralResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }

        }, roadmap);
    }

}