package com.musasyihab.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.musasyihab.timeline.model.Timeline;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Timeline> timelines;
    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public TimelineAdapter(Context context, ArrayList<Timeline> data) {
        inflater = LayoutInflater.from(context);
        timelines = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View view = inflater.inflate(R.layout.timeline_item, parent, false);
            TimelineViewHolder holder = new TimelineViewHolder(view);
            return holder;
        } else if(viewType == TYPE_HEADER){
            View view = inflater.inflate(R.layout.timeline_header, parent, false);
            TimelineViewHeader header = new TimelineViewHeader(view);
            return header;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Rect bounds = viewHolder.color.getProgressDrawable().getBounds();
        //Drawable progressDrawable = ContextCompat.getDrawable(context, R.drawable.circular_progress_green);
        //viewHolder.color.setProgressDrawable(progressDrawable);
        //viewHolder.color.getProgressDrawable().setBounds(bounds);
        //viewHolder.color.setMax(1000);
        //viewHolder.color.setProgress(999);
        /*if(current.getEndTime()-current.getStartTime()<4){
            viewHolder.color.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_progress_green));
        } else if(current.getEndTime()-current.getStartTime()<8){
            viewHolder.color.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_progress_orange));
        } else {
            viewHolder.color.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_progress_red));
        }*/
        if(holder instanceof TimelineViewHeader){

        }
        else {
            Timeline current = timelines.get(position);
            TimelineViewHolder viewHolder = (TimelineViewHolder) holder;
            if(current==null){
                viewHolder.layout.setVisibility(View.GONE);
                return;
            }
            viewHolder.title.setText(current.getTitle());
            String startTime = "";
            if (current.getStartTime() <= 12) {
                startTime = current.getStartTime() + "am";
            } else {
                startTime = (current.getStartTime() - 12) + "pm";
            }
            String endTime = "";
            if(current.getEndTime()!=0) {
                if (current.getEndTime() <= 12) {
                    endTime = current.getEndTime() + "am";
                } else {
                    endTime = (current.getEndTime() - 12) + "pm";
                }
                viewHolder.time.setText(startTime + "-" + endTime);
            } else {
                viewHolder.time.setText(startTime);
            }

            if (!current.getDesc().equals("")) {
                viewHolder.desc.setText(current.getDesc());
            } else {
                viewHolder.desc.setVisibility(View.GONE);
            }
            if (current.getCollaborators().size() == 0) {
                viewHolder.colabs.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < viewHolder.colabs.getChildCount(); i++) {
                    if (i < current.getCollaborators().size()) {
                        final ImageView ava = ((ImageView) viewHolder.colabs.getChildAt(i));
                        Target loadTarget = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                ava.setImageDrawable(getCircleBitmap(context, bitmap));
                                ava.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                ava.setVisibility(View.INVISIBLE);
                            }
                        };
                        Picasso.with(context).load(current.getCollaborators().get(i).getAvatar()).into(loadTarget);
                    } else {
                        viewHolder.colabs.getChildAt(i).setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        /*String time = holder.time.getText().toString();
        String[] _time = time.split("-");
        int start=0;
        int end=0;
        if(_time[0].contains("pm")){
            _time[0] = _time[0].replace("pm","");
            start=Integer.parseInt(_time[0])+12;
        } else {
            _time[0] = _time[0].replace("am","");
            start=Integer.parseInt(_time[0]);
        }
        if(_time[1].contains("pm")){
            _time[1] = _time[1].replace("pm","");
            end=Integer.parseInt(_time[1])+12;
        } else {
            _time[1] = _time[1].replace("am","");
            end=Integer.parseInt(_time[1]);
        }
        int diff = end-start;*/
        //Log.v("DIFF", "" + diff);
    }

    @Override
    public int getItemCount() {
        return timelines.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        ProgressBar color;
        TextView time;
        TextView title;
        TextView desc;
        LinearLayout colabs;
        ArrayList<ImageView> colab = new ArrayList<ImageView>();
        public TimelineViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.timelineItemLayout);
            color = (ProgressBar) itemView.findViewById(R.id.timelineType);
            time = (TextView) itemView.findViewById(R.id.timelineTime);
            title = (TextView) itemView.findViewById(R.id.timelineTitle);
            desc = (TextView) itemView.findViewById(R.id.timelineDesc);
            colabs = (LinearLayout) itemView.findViewById(R.id.timelineColabs);
            for(int i=0; i<colabs.getChildCount(); i++){
                colab.add((ImageView)colabs.getChildAt(i));
            }
        }
    }

    class TimelineViewHeader extends RecyclerView.ViewHolder{
        public TimelineViewHeader(View itemView) {
            super(itemView);
        }
    }

    public static RoundedBitmapDrawable getCircleBitmap(Context context, Bitmap bitmap) {

        Bitmap cropped;

        if (bitmap.getWidth() >= bitmap.getHeight()){

            cropped = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        }else{

            cropped = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        if(context!=null) {
            final RoundedBitmapDrawable output =
                    RoundedBitmapDrawableFactory.create(context.getResources(), cropped);
            output.setCornerRadius(Math.max(cropped.getWidth(), cropped.getHeight()) / 2.0f);

            output.setAntiAlias(true);

            return output;
        }
        return null;
    }
}
