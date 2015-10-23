package com.musasyihab.timeline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.musasyihab.timeline.model.Report;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileFragment extends Fragment {

    int selectedReport=0;
    ArrayList<Report> reports = new ArrayList<>();
    ImageButton prev,next;
    TextView month,completedTotal,snoozedTotal,overdueTotal,total;
    ProgressBar completedPercent,snoozedPercent,overduePercent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_detail, container, false);
        prev = (ImageButton) v.findViewById(R.id.profileMonthPrev);
        next = (ImageButton) v.findViewById(R.id.profileMonthNext);
        month = (TextView) v.findViewById(R.id.profileMonth);
        completedTotal = (TextView) v.findViewById(R.id.statCompletedTotal);
        snoozedTotal = (TextView) v.findViewById(R.id.statSnoozedTotal);
        overdueTotal = (TextView) v.findViewById(R.id.statOverdueTotal);
        total = (TextView) v.findViewById(R.id.statTotal);
        completedPercent = (ProgressBar) v.findViewById(R.id.statCompletedPercent);
        snoozedPercent = (ProgressBar) v.findViewById(R.id.statSnoozedPercent);
        overduePercent= (ProgressBar) v.findViewById(R.id.statOverduePercent);

        final Calendar now = Calendar.getInstance();
        selectedReport = now.get(Calendar.MONTH);
        changeMonth(selectedReport);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedReport == 11) {
                    selectedReport=0;
                } else {
                    selectedReport++;
                }
                changeMonth(selectedReport);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedReport == 0) {
                    selectedReport=11;
                } else {
                    selectedReport--;
                }
                changeMonth(selectedReport);
            }
        });

        return v;
    }

    public void initializeData(){
        Report item = new Report("January",120,34,10);
        reports.add(item);
        item = new Report("February",78,64,21);
        reports.add(item);
        item = new Report("March",172,27,18);
        reports.add(item);
        item = new Report("April",108,34,10);
        reports.add(item);
        item = new Report("May",120,37,9);
        reports.add(item);
        item = new Report("June",80,44,14);
        reports.add(item);
        item = new Report("July",187,22,18);
        reports.add(item);
        item = new Report("August",91,31,20);
        reports.add(item);
        item = new Report("September",110,10,7);
        reports.add(item);
        item = new Report("October",45,22,11);
        reports.add(item);
        item = new Report("November",133,32,13);
        reports.add(item);
        item = new Report("December",111,41,23);
        reports.add(item);
    }

    public void changeMonth(int selected){
        Report current = reports.get(selected);
        month.setText(current.getMonth());
        total.setText(current.getTotal()+"");
        completedTotal.setText(current.getCompleted()+"");
        snoozedTotal.setText(current.getSnoozed()+"");
        overdueTotal.setText(current.getOverdue()+"");
        final int completedPercentScoreFinish = (current.getCompletedPercent());
        final int snoozedPercentScoreFinish = (current.getSnoozedPercent());
        ProgressBarUpdate task = new ProgressBarUpdate();
        task.setProgressBar(completedPercent, snoozedPercent);
        task.setProgressFinish(completedPercentScoreFinish, snoozedPercentScoreFinish);
        task.execute();
    }

    private class ProgressBarUpdate extends AsyncTask<Void, ReportParam, Void> {

        ProgressBar barCompleted;
        ProgressBar barSnoozed;
        int completedStatus=0;
        int completedFinish;
        int snoozedStatus=0;
        int snoozedFinish;

        public void setProgressBar(ProgressBar barCompleted, ProgressBar barSnoozed) {
            this.barCompleted = barCompleted;
            this.barSnoozed = barSnoozed;
        }

        public void setProgressFinish(int valueCompleted, int valueSnoozed){
            completedFinish=valueCompleted;
            snoozedFinish=valueSnoozed;
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean finishCompleted = false;
            boolean finishSnoozed = false;
            while(!finishCompleted || !finishSnoozed){
                ReportParam values = new ReportParam();
                if(!finishCompleted) {
                    completedStatus = completedStatus + 20;
                    if (completedStatus > completedFinish) {
                        finishCompleted = true;
                    }
                }
                if(!finishSnoozed) {
                    snoozedStatus = snoozedStatus + 20;
                    if (snoozedStatus > snoozedFinish) {
                        finishSnoozed = true;
                    }
                }
                values.completed = completedStatus;
                values.snoozed = snoozedStatus;
                publishProgress(values);
                SystemClock.sleep(1);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(ReportParam... values) {
            super.onProgressUpdate(values);
            if (this.barCompleted != null && this.barSnoozed != null) {
                barSnoozed.setProgress(values[0].snoozed);
                barCompleted.setProgress(values[0].completed);
            }
        }
    }

    private class ReportParam{
        public int snoozed;
        public int completed;
    }
}

