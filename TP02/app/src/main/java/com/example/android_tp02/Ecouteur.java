package com.example.android_tp02;

import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Ecouteur implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    final Chat chat;
    Settings settings;
    Socket socket;
    //private String ip = "134.59.2.27";
    private String ip = "78.243.124.47";
    private int port = 10101;


    Ecouteur(final Chat chat, Settings settings){
        this.chat=chat;
        this.settings=settings;

        try{
            socket=IO.socket("http://"+ip+":"+port);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on("chatevent", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageSent = new JSONObject(args[0].toString());
                    String pseudo = messageSent.getString("userName");
                    String message = messageSent.getString("message");
                    System.out.println(pseudo+" "+message);
                    chat.addMessage(pseudo+" > "+message, Color.BLACK);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on("connected list", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data=(JSONObject) args[0];
                try{
                    JSONArray connected=data.getJSONArray("connected");
                    for(int i=0;i<connected.length();i++){
                        chat.addMessage((String) connected.get(i), Color.RED);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String msg=chat.getText();
        String name=settings.getName();
        try {
            JSONObject obj = new JSONObject(" { userName : "+name+"; message : "+msg+"}");
            socket.emit("chatevent",obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            connect();
        }else {
            disconnect();
        }
    }

    void connect(){
        socket.connect();
    }

    void disconnect(){
        socket.disconnect();
    }

    void askConnected(){
        JSONObject obj = new JSONObject();
        socket.emit("queryconnected",obj);
    }
}
