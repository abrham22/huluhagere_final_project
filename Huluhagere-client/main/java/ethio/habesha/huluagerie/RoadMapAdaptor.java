package ethio.habesha.huluagerie;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class RoadMapAdaptor extends RecyclerView.Adapter<RoadMapAdaptor.RoadmapAdapterViewHolder> {

    private DataController.Roadmap mRoadmapData;

    class RoadmapAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView mRoadmapTextView;
        final Button route;
        final TextView day;

        RoadmapAdapterViewHolder(View view) {
            super(view);
            mRoadmapTextView = view.findViewById(R.id.tv_activity_name);
            day = view.findViewById(R.id.day);
            route = view.findViewById(R.id.routebtn);
        }

    }

    @NonNull
    @Override
    public RoadmapAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_item_roadmap, parent, false);
        return new RoadmapAdapterViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RoadmapAdapterViewHolder roadmapAdapterViewHolder,
                                 final int position) {
        String site_name = mRoadmapData.activities.get(position).site.name;
        roadmapAdapterViewHolder.mRoadmapTextView.setText(site_name);
        roadmapAdapterViewHolder.route.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pos = roadmapAdapterViewHolder.getAdapterPosition();
                String location = mRoadmapData.activities.get(pos).site.name;
                Maps.showMap(v.getContext(), location);
            }
        });
        roadmapAdapterViewHolder.day.setText("Day "+(position+1));

    }

    @Override
    public int getItemCount() {
        if (null == mRoadmapData) return 0;
        return mRoadmapData.activities.size();
    }

    public void setRoadmapData(DataController.Roadmap roadmapData) {
        mRoadmapData = roadmapData;
        notifyDataSetChanged();
    }

}