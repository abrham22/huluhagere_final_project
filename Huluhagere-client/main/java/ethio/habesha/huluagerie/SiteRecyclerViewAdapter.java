package ethio.habesha.huluagerie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SiteRecyclerViewAdapter extends RecyclerView.Adapter<SiteRecyclerViewAdapter.ViewHolder>{

    private ArrayList<DataController.Site> sites;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageButton mImageButton;
        private boolean isFirst = false;

        ViewHolder(View v){

            super(v);
            textView = (TextView) v.findViewById(R.id.site_name);
            mImageButton = (ImageButton) v.findViewById(R.id.site_image);

        }

        public boolean isFirst() {
            return isFirst;
        }

    }

    @Override
    public SiteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_items,
                parent,false);
        
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vHolder, int position){

        vHolder.textView.setText(sites.get(position).name);
        vHolder.textView.setBackgroundColor(Color.WHITE);
        vHolder.textView.setTextColor(Color.BLACK);

        vHolder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = vHolder.getAdapterPosition();
                Context context = v.getContext();
                Intent siteIntent = new Intent(context, SitesActivity.class);
                siteIntent.putExtra("site", sites.get(pos));
                context.startActivity(siteIntent);
            }
        });

    }

    public void loadSitesData(ArrayList<DataController.Site> sites) {
        this.sites = sites;
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount(){

        if(sites == null) {
            return 0;
        }
        return sites.size();
    }

}