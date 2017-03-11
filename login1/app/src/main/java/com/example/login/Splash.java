package com.example.login;

import java.util.Locale;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public class Splash extends Activity {
	TextToSpeech tts;
	static final String text = "Welcome to I.I.T  ";
	AudioManager am;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		am= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		tts= new TextToSpeech(Splash.this,new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if(status != TextToSpeech.ERROR){
					tts.setLanguage(Locale.ENGLISH);
					tts.setSpeechRate((float) 0.75);}

			}
		});

		Thread timer = new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {

				try {
					sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(am.getRingerMode()==AudioManager.RINGER_MODE_SILENT || am.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE){
						Intent first = new Intent(Splash.this, HomePage.class);
						startActivity(first);
					}else {
						tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
						Intent first = new Intent(Splash.this, HomePage.class);
						startActivity(first);
					}
				}
			}
		};
		timer.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		finish();
	}

}
