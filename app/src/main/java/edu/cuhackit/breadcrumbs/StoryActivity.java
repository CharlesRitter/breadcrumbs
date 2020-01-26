package edu.cuhackit.breadcrumbs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.OnConflictStrategy;

import java.util.List;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        StoryModel model = ViewModelProviders.of(this).get(StoryModel.class);

        Intent intent = getIntent();

        double lat = intent.getDoubleExtra("lat", 34.676);
        double lng = intent.getDoubleExtra("lng", -82.8369);

        model.getStoryList(lat, lng).observe(this, obs -> {
            List<StoryClass> storyList = StoryModel.getStoryList(lat, lng).getValue();

            //the RecyclerView
            RecyclerView storyView = findViewById(R.id.stories);

            //use a linear layout manager
            RecyclerView.LayoutManager storyListManager = new LinearLayoutManager(this);
            storyView.setLayoutManager(storyListManager);

            //bind the adapter
            StoryAdapter inputAdapter = new StoryAdapter(storyList);
            storyView.setAdapter(inputAdapter);
        });
    }

    @Override
    protected void onDestroy() {
        StoryModel model = ViewModelProviders.of(this).get(StoryModel.class);
        model.reset();
        super.onDestroy();
    }

    public void post_a_post(View v){
        Intent intent = new Intent(getApplicationContext(), Form.class);
        startActivity(intent);
    }
}
