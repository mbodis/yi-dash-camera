package sk.svb.yi_dash_camera.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import sk.svb.yi_dash_camera.config.YiDashCameraUrls;
import sk.svb.yi_dash_camera.utils.Callback;
import sk.svb.yi_dash_camera.model.Video;

public class DownloadService {

    private static final String TAG = DownloadService.class.getSimpleName();
    private Callback successCallback;
    private Callback failedCallback;

    public DownloadService(Context ctx, Callback successCallback, Callback failedCallback) {
        this.successCallback = successCallback;
        this.failedCallback = failedCallback;
        this.runCommandStreamOpen(ctx);
    }

    private void runCommandStreamOpen(Context ctx) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, YiDashCameraUrls.URL_STREAM_OPEN, response -> {
            Log.d(TAG, response);
            if (response.contains("<Status>0</Status>")) {
                runCommandConnectToFiles(ctx);
            } else {
                this.failedCallback.execute("open stream: response does not contain Status 0");
            }
        }, error -> {
            this.failedCallback.execute("open stream: failed request");
        });
        Volley.newRequestQueue(ctx).add(stringRequest);
    }

    private void runCommandConnectToFiles(Context ctx) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, YiDashCameraUrls.URL_FILE_COMMAND, response -> {
            Log.d(TAG, response);
            if (response.contains("<Status>0</Status>")) {
                runCommandRetrieveListOfFiles(ctx);
            } else {
                this.failedCallback.execute("connect to files: response does not contain Status 0");
            }
        }, error -> {
            this.failedCallback.execute("connect to files: failed request");
        });
        Volley.newRequestQueue(ctx).add(stringRequest);
    }

    private void runCommandRetrieveListOfFiles(Context ctx) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, YiDashCameraUrls.URL_GET_FILES, response -> {
            Log.d(TAG, response);
            if (response.contains("<ALLFile>")) {
                this.successCallback.execute(parseXmlList(response));
            } else {
                this.failedCallback.execute("list files: does not contain list");
            }
        }, error -> {
            this.failedCallback.execute("list files: failed request");
        });
        Volley.newRequestQueue(ctx).add(stringRequest);
    }

    /**
     * <LIST>
     * <ALLFile><File>
     * <NAME>2019_1203_230107.MP4</NAME>
     * <FPATH>A:\YICarCam\Movie\2019_1203_230107.MP4</FPATH>
     * <SIZE>826055</SIZE>
     * <TIME>2019/12/03 23:01:06</TIME>
     * <ATTR>32</ATTR><SUB>TRUE</SUB><SUBSIZE>451567</SUBSIZE></File>
     * </ALLFile>
     * ...
     * </LIST>
     */
    private List<Video> parseXmlList(String xml) {
        List<Video> list = new ArrayList<>();

        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(new StringReader(xml));
            int event = myParser.getEventType();
            Video video = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("File")) {
                            video = new Video();
                        }
                        if (name.equals("NAME")) {
                            video.setName(myParser.nextText());
                        }
                        if (name.equals("FPATH")) {
                            // from: "A:\YICarCam\Movie\2019_1101_230328.MP4"
                            // to:  "HTTP://192.168.1.254/YICARCAM/MOVIE/2019_1101_230441.MP4
                            String filePath = myParser.nextText();
                            filePath = filePath.toUpperCase();
                            filePath = filePath.replace("A:\\", "http://192.168.1.254/");
                            filePath = filePath.replace("\\", "/");
                            video.setPath(filePath);
                        }
                        if (name.equals("SIZE")) {
                            video.setSize(myParser.nextText());
                        }
                        if (name.equals("TIME")) {
                            video.setTime(myParser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("File")) {
                            list.add(video);
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return list;
    }
}
