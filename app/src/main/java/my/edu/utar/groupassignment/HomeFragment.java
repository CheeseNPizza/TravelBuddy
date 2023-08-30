package my.edu.utar.groupassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    private SearchView searchPost;
    private RecyclerView recyclerView;
    private ArrayList<DataClass> dataList;
    private MyAdapter adapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        searchPost = rootView.findViewById(R.id.searchPost);
        searchPost.clearFocus();
        searchPost.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);

        // Retrieve data from Firebase and update dataList
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DataClass data = postSnapshot.getValue(DataClass.class);

                    Log.d("HomeFragment", "Retrieved data: " + data.toString());

                    dataList.add(data);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HomeFragment", "Database Error: " + databaseError.getMessage());
            }
        });

        recyclerView_story = rootView.findViewById(R.id.recycler_view_story);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_story.setLayoutManager(linearLayoutManager);
        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(getContext(), storyList);
        recyclerView_story.setAdapter(storyAdapter);
        readStory();

        return rootView;
    }

    private void filterList(String text) {
        ArrayList<DataClass> filteredList = new ArrayList<>();
        for(DataClass item : dataList){
            if(item.getCaption().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilteredList(filteredList);
        }
    }

    private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent = System.currentTimeMillis();
                storyList.clear();

                storyList.add(new Story("",0,0,"", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                Log.e("CurrentUser", "Users: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Log.e("DatabaseUser", "Users: " + userSnapshot.getKey());
                    if(!userSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.e("OtherUser", "Users: " + userSnapshot.getKey());
                        for (DataSnapshot storySnapshot : userSnapshot.getChildren()) {
                            Story story = storySnapshot.getValue(Story.class);
                            if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
                                storyList.add(story);
                            }
                        }
                    }
                }
                printStoryList();
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Database Error: " + error.getMessage());
            }
        });
    }

    private void printStoryList() {
        for (Story story : storyList) {
            Log.d("StoryList", "Story ID: " + story.getStoryid());
            Log.d("StoryList", "User ID: " + story.getUserid());
            Log.d("StoryList", "Image URL: " + story.getImageurl());
            Log.d("StoryList", "Timestart: " + story.getTimestart());
            Log.d("StoryList", "Timeend: " + story.getTimeend());
            Log.d("StoryList", "----------------------");
        }
    }
}

