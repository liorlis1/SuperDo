package com.screenovate.superdo.Model;

import android.content.Context;

import com.screenovate.superdo.ItemListener;
import com.screenovate.superdo.Utils.Constants;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class SuperDoModel {


    private WeakReference<Context> savedContext;
    private OkHttpClient client;
    private WebSocket websocket;
    private ItemListener listener;
    private SuperDoWebSocketListener socketListener;
    private boolean connected = false;

    public SuperDoModel(Context context, ItemListener listener) {
        if(context != null) {
            savedContext = new WeakReference<>(context);
        }
        this.listener = listener;
    }

    public void startLoading() {
        if(connected != true) {
            if (client == null) {
                client = new OkHttpClient();
            }
            Request request = new Request.Builder().url(Constants.URL).build();
            socketListener = new SuperDoWebSocketListener(listener);
            websocket = client.newWebSocket(request, socketListener);
            connected = true;
        }
    }

    public void stopLoading() {
        if(websocket != null && client != null) {
            websocket.close(1000, "Normal close");
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            socketListener.stop();
        }
        client = null;
        websocket = null;
        connected = false;
    }

}
