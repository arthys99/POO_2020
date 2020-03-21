package com.example.android_tp02;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import io.socket.emitter.Emitter;


public class ChatActivity extends Activity implements Chat {
    Button envoyer;
    ScrollView scroll;
    TextView chat;
    EditText msg;
    Switch connexion;
    Ecouteur listener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);

        envoyer= (Button) findViewById(R.id.envoyer);
        scroll=(ScrollView) findViewById(R.id.scroll);
        chat=(TextView) findViewById(R.id.chat);
        msg=(EditText) findViewById(R.id.message);
        connexion=(Switch) findViewById(R.id.connexion);

        Settings settings=new Settings();
        listener=new Ecouteur(this,settings);
        envoyer.setOnClickListener(listener);
        connexion.setOnCheckedChangeListener(listener);
    }

    @Override
    public String getText() {
        return msg.getText().toString();
    }

    @Override
    public void addMessage(final String message, final int couleur) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SpannableString s=new SpannableString(message);
                s.setSpan(new ForegroundColorSpan(couleur),0,message.length(),0);
                chat.append(s+"\n");
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public boolean onCreateOptionMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.listesconnectes){
            listener.askConnected();
        }
        return true;
    }
}
