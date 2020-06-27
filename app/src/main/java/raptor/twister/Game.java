package raptor.twister;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.speech.SpeechRecognizer.createSpeechRecognizer;

public class Game extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private Button nextButton;
    private ImageView body;
    private Button symbol;
    private Random rnd = new Random();
    private MediaPlayer LeftFootmp;
    private MediaPlayer RightFootmp;
    private MediaPlayer RightHandmp;
    private MediaPlayer LeftHandmp;
    private MediaPlayer Bluemp;
    private MediaPlayer Greenmp;
    private MediaPlayer Yellowmp;
    private MediaPlayer Redmp;
    private SpeechRecognizer sr;
    private SpeechListener sl;
    TextToSpeech tts;
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largestonline retailer on the planet?";
    private String mAnswer = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //mp = MediaPlayer.create(Game.this);
        initAudio();
        mTextView = (TextView)findViewById(R.id.speechText);
        startSpeechRecognizer();
        Log.i("111111","11111111");
        //Beszéd local alapján
        /*tts = new TextToSpeech(this,this);
        tts.setLanguage(Locale.ENGLISH);
        tts.speak("Welcome", TextToSpeech.QUEUE_ADD, null);*/
        requestAudioPermissions();
        nextButton = findViewById(R.id.nextButton);
        symbol = findViewById(R.id.symbolImage);
        body = findViewById(R.id.bodyPartImage);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            randomBodyPart();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        randomColor();
                    }
                }, 2000);
            }
        });
    }
    private void initAudio()
    {
        int LeftFoot = getResources().getIdentifier("left_foot", "raw", getPackageName());
        int RightFoot = getResources().getIdentifier("right_foot", "raw", getPackageName());
        int LeftHand = getResources().getIdentifier("left_hand", "raw", getPackageName());
        int RightHand = getResources().getIdentifier("right_hand", "raw", getPackageName());
        int Blue = getResources().getIdentifier("blue","raw",getPackageName());
        int Yellow = getResources().getIdentifier("yellow","raw",getPackageName());
        int Red = getResources().getIdentifier("red","raw",getPackageName());
        int Green = getResources().getIdentifier("green","raw",getPackageName());
        LeftFootmp = MediaPlayer.create(this,LeftFoot);
        RightFootmp = MediaPlayer.create(this,RightFoot);
        RightHandmp = MediaPlayer.create(this,RightHand);
        LeftHandmp = MediaPlayer.create(this,LeftHand);
        Bluemp = MediaPlayer.create(this,Blue);
        Yellowmp = MediaPlayer.create(this,Yellow);
        Greenmp = MediaPlayer.create(this,Green);
        Redmp = MediaPlayer.create(this,Red);
    }
    private void randomBodyPart()
    {
        int bodyNumber = rnd.nextInt(4);
        switch (bodyNumber)
        {
            case 0: body.setImageResource(R.drawable.left_hand);
                LeftHandmp.start();
                break;
            case 1: body.setImageResource(R.drawable.right_foot);
                RightFootmp.start();
                break;
            case 2: body.setImageResource(R.drawable.left_foot);
                LeftFootmp.start();
                break;
            case 3: body.setImageResource(R.drawable.right_hand);
                RightHandmp.start();
                break;
        }
    }
    private void randomColor()
    {
        int symbolNumber = rnd.nextInt(4);
        switch(symbolNumber)
        {
            case 0: symbol.setBackgroundColor(Color.BLUE);
                Bluemp.start();
                break;
            case 1: symbol.setBackgroundColor(Color.GREEN);
                Greenmp.start();
                break;
            case 2: symbol.setBackgroundColor(Color.RED);
                Redmp.start();
                break;
            case 3: symbol.setBackgroundColor(Color.YELLOW);
                Yellowmp.start();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                mAnswer = results.get(0);

                if (mAnswer.toUpperCase().indexOf("AMAZON") > -1)
                    mTextView.setText("\n\nQuestion: " + mQuestion +
                            "\n\nYour answer is '" + mAnswer +
                            "' and it is correct!");
                else
                    mTextView.setText("\n\nQuestion: " + mQuestion +
                            "\n\nYour answer is '" + mAnswer +
                            "' and it is incorrect!");
            }
        }
    }
    private void startSpeechRecognizer() {
        Intent intent = new Intent
                (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion);
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }
    @Override
    public void onInit(int i) {

    }
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            sr = SpeechRecognizer.createSpeechRecognizer(this);
            sl = new SpeechListener();
            sr.setRecognitionListener(sl);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
            sr.startListening(intent);
        }
    }

}
