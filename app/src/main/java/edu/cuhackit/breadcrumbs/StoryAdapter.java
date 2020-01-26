package edu.cuhackit.breadcrumbs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHolder> {
    public List<StoryClass> stories;

    public StoryAdapter(List<StoryClass> input){
        stories = input;
    }

    public static class StoryHolder extends RecyclerView.ViewHolder {
        public LinearLayout storyLayout;
        public StoryHolder(LinearLayout ll){
            super(ll);
            storyLayout = ll;
        }
    }

    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout ll;
        ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.story_view, parent, false);
        return new StoryHolder(ll);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryHolder holder, int position) {
        StoryClass current = stories.get(position);
        ((ImageView)holder.storyLayout.findViewById(R.id.storyImage)).setImageBitmap(current.getImg());
        ((TextView)holder.storyLayout.findViewById(R.id.storyCaption)).setText(current.getCap());
    }

    @Override
    public int getItemCount() {
        if(stories == null) return 0;
        else {
            return stories.size();
        }
    }
}
