package sk.svb.yi_dash_camera.gui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sk.svb.yi_dash_camera.R;
import sk.svb.yi_dash_camera.model.Video;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private Context ctx;
    private List<Video> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name, path, size, time;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.video_list_item_cv);
            name = cv.findViewById(R.id.video_list_item_name);
            path = cv.findViewById(R.id.video_list_item_path);
            size = cv.findViewById(R.id.video_list_item_size);
            time = cv.findViewById(R.id.video_list_item_time);
        }
    }

    public VideoListAdapter(Context ctx, List<Video> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
        return new VideoListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final VideoListAdapter.ViewHolder holder, final int position) {

        final Video video = list.get(position);

        holder.name.setText(video.getName());
        holder.path.setText(video.getPath());
        holder.size.setText(video.getSize());
        holder.time.setText(video.getTime());

        holder.cv.setOnClickListener(view -> {
            Toast.makeText(ctx, "TODO action: " + video.getPath(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}