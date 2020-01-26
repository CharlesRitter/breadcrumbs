package edu.cuhackit.breadcrumbs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StoryModel extends AndroidViewModel {

    private static LiveData<List<StoryModel>> storyList;

    public StoryModel(@NonNull Application application) {
        super(application);
    }
}
