package com.lambtoncollege.treasuretrove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TreasureUnlock extends AppCompatActivity {

    Button levelChange;
    ImageView homeButton, restarLevel,unlockedObject;
    int whichLevel;
    TextView objectunlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_unlock);
        levelChange = (Button)findViewById(R.id.levelchange);
        homeButton = (ImageView)findViewById(R.id.homebutton);
        restarLevel = (ImageView)findViewById(R.id.restartlevel);
        unlockedObject = (ImageView)findViewById(R.id.unlockedObject);
        objectunlock = (TextView)findViewById(R.id.objectunlock);
        Intent intent = getIntent();
        whichLevel = intent.getIntExtra("level",0);

        switch (whichLevel){
            case 1 :
                unlockedObject.setImageResource(R.drawable.monkey);
                objectunlock.setText("Monkey");
                break;

            case 2:
                unlockedObject.setImageResource(R.drawable.lamp);
                objectunlock.setText("Lamp");
                break;

            case 3:
                unlockedObject.setImageResource(R.drawable.jasmin);
                objectunlock.setText("jasmin");
                break;
            default:
                Toast.makeText(getApplicationContext(),"something not well",Toast.LENGTH_LONG);
        }

        levelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (whichLevel){
                    case 1 :
                        Intent intent = new Intent(getApplicationContext(),GameActivityOne.class);
                        intent.putExtra("Mode", "Touch");
                        startActivity(intent);
                        finish();
                        break;

                    case 2:
                        Intent intent1 = new Intent(getApplicationContext(),GameActivityTwo.class);
                        intent1.putExtra("Mode", "Touch");
                        startActivity(intent1);
                        finish();
                        break;

                    case 3:
                        Intent intent2 = new Intent(getApplicationContext(),GameActivity.class);
                        intent2.putExtra("Mode", "Touch");
                        startActivity(intent2);
                        finish();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),"something not well",Toast.LENGTH_LONG);
            }


            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
        restarLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (whichLevel){
                    case 1 :
                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                        intent.putExtra("Mode", "Touch");
                        startActivity(intent);
                        finish();
                        break;

                    case 2:
                        Intent intent1 = new Intent(getApplicationContext(),GameActivityOne.class);
                        intent1.putExtra("Mode", "Touch");
                        startActivity(intent1);
                        finish();
                        break;

                    case 3:
                        Intent intent2 = new Intent(getApplicationContext(),GameActivityTwo.class);
                        intent2.putExtra("Mode", "Touch");
                        startActivity(intent2);
                        finish();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),"something not well",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
