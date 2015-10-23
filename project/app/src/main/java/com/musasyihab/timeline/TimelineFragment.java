package com.musasyihab.timeline;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.musasyihab.timeline.model.Collaborator;
import com.musasyihab.timeline.model.Timeline;
import com.musasyihab.timeline.service.ResponseData;
import com.musasyihab.timeline.service.RestCallback;
import com.musasyihab.timeline.service.RestClient;
import com.musasyihab.timeline.service.RestError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.client.Response;

public class TimelineFragment extends Fragment {
    private ArrayList<Timeline> timelines = new ArrayList<Timeline>();
    private TimelineAdapter mAdapter;
    RecyclerView timelineList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        timelines = new ArrayList<Timeline>();
        timelines.add(null);
        getTimeline();
        View v = inflater.inflate(R.layout.timeline_layout, container, false);
        timelineList = (RecyclerView) v.findViewById(R.id.timelineList);
        mAdapter = new TimelineAdapter(getActivity().getApplicationContext(), timelines);
        initiateTimeline();
        timelineList.setAdapter(mAdapter);
        timelineList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    private void initiateTimeline(){
        for(int i=0; i<mAdapter.getItemCount(); i++){
            mAdapter.notifyItemRemoved(i);
        }
        for(int i=1; i<timelines.size(); i++){
            mAdapter.notifyItemInserted(i);
        }
        /*
        Timeline item = new Timeline();
        item.setStartTime(Integer.parseInt("06:00".substring(0, 2)));
        item.setEndTime(Integer.parseInt("07:00".substring(0, 2)));
        item.setTitle("Olahraga");
        item.setDesc("");
        timelines.add(item);
        item = new Timeline();
        item.setStartTime(Integer.parseInt("07:00".substring(0, 2)));
        item.setEndTime(Integer.parseInt("08:00".substring(0, 2)));
        item.setTitle("Sarapan");
        item.setDesc("Bubur kacang ijo.");
        ArrayList<Collaborator> colabs = new ArrayList<Collaborator>();
        colabs.add(new Collaborator());
        item.setCollaborators(colabs);
        timelines.add(item);
        item = new Timeline();
        item.setStartTime(Integer.parseInt("09:00".substring(0, 2)));
        item.setEndTime(Integer.parseInt("16:00".substring(0, 2)));
        item.setTitle("Kerja");
        item.setDesc("");
        colabs = new ArrayList<Collaborator>();
        colabs.add(new Collaborator());
        colabs.add(new Collaborator());
        colabs.add(new Collaborator());
        item.setCollaborators(colabs);
        timelines.add(item);
        */
    }

    private void getTimeline(){
        RestClient.get().getTimeline(new RestCallback<ResponseData>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(getActivity(), "Something went wrong. Please try again later.", Toast.LENGTH_SHORT);
            }

            @Override
            public void success(ResponseData responseData, Response response) {
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String result = sb.toString();

                JSONObject responseObject = null;

                try {
                    responseObject = new JSONObject(result);
                    JSONArray responseArray = responseObject.getJSONArray("timelines");
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject explrObject = responseArray.getJSONObject(i);
                        Timeline item = new Timeline();
                        String[] split = explrObject.getString("start_time").split(":");
                        item.setStartTime(Integer.parseInt(split[0]));
                        if(explrObject.getString("end_time")!=null && !explrObject.getString("end_time").equals("null")){
                            split = explrObject.getString("end_time").split(":");
                            item.setEndTime(Integer.parseInt(split[0]));
                        }
                        item.setTitle(explrObject.getString("title"));
                        if(explrObject.getString("description")!=null) {
                            item.setDesc(explrObject.getString("description"));
                        }
                        if(explrObject.has("colaborator")){
                            JSONArray colabs = explrObject.getJSONArray("colaborator");
                            ArrayList<Collaborator> collaborators = new ArrayList<Collaborator>();
                            for(int j=0; j<colabs.length(); j++){
                                JSONObject obj = colabs.getJSONObject(j);
                                Collaborator person = new Collaborator();
                                person.setFullName(obj.getString("full_name"));
                                person.setId(obj.getInt("id"));
                                person.setAvatar(obj.getString("avatar"));
                                collaborators.add(person);
                            }
                            item.setCollaborators(collaborators);
                        }
                        timelines.add(item);
                    }
                    initiateTimeline();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initiateTimeline();
            }
        });
    }
}
