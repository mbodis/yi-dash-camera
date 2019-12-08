package sk.svb.yi_dash_camera.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import sk.svb.yi_dash_camera.R;
import sk.svb.yi_dash_camera.gui.adapter.VideoListAdapter;
import sk.svb.yi_dash_camera.utils.Callback;
import sk.svb.yi_dash_camera.model.Video;
import sk.svb.yi_dash_camera.service.DownloadService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private View loadingView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
        toggleViewLoading(false, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retrieve_items:
                toggleViewLoading(true, false);
                // TODO make sure you are connected to camera
                new DownloadService(getApplicationContext(), new Callback() {
                    @Override
                    public void execute(Object o) {
                        if (o instanceof List) {
                            List<Video> l = (List<Video>) o;
                            toggleViewLoading(false, l.size() > 0);
                            reloadListAdapter(l);
                        }
                    }
                }, new Callback() {
                    @Override
                    public void execute(Object o) {
                        if (o instanceof String) {
                            Toast.makeText(getApplicationContext(), (String) o, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }
    }

    private void setupLayout() {
        loadingView = findViewById(R.id.loading_view);
        button = findViewById(R.id.retrieve_items);
        button.setOnClickListener(this);
        recyclerView = findViewById(android.R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void toggleViewLoading(boolean loadingInProgress, boolean listItemsExists) {
        recyclerView.setVisibility(!loadingInProgress && listItemsExists ? View.VISIBLE : View.GONE);
        button.setVisibility(!loadingInProgress && !listItemsExists ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(loadingInProgress ? View.VISIBLE : View.GONE);
    }

    private void reloadListAdapter(List<Video> list) {
        VideoListAdapter videoListAdapter = new VideoListAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(videoListAdapter);
        videoListAdapter.notifyDataSetChanged();
    }
}
