package com.lambtoncollege.treasuretrove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TreasureUnlock extends AppCompatActivity {

    Button levelChange;
    ImageView homeButton, restarLevel,unlockedObject;
    int whichLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_unlock);
        levelChange = (Button)findViewById(R.id.levelchange);
        homeButton = (ImageView)findViewById(R.id.homebutton);
        restarLevel = (ImageView)findViewById(R.id.restartlevel);
        unlockedObject = (ImageView)findViewById(R.id.unlockedObject);
        Intent intent = getIntent();
        whichLevel = intent.getIntExtra("level",0);

        switch (whichLevel){
            case 1 :
                unlockedObject.setImageResource(R.drawable.monkey);
                break;

            case 2:
                unlockedObject.setImageResource(R.drawable.lamp);
                break;

            case 3:
                unlockedObject.setImageResource(R.drawable.jasmin);
                break;
            default:
                Toast.makeText(getApplicationContext(),"something not well",Toast.LENGTH_LONG);
        }

        levelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),GameActivityOne.class);
                intent.putExtra("Mode", "Touch");
                startActivity(intent);
                finish();


//                switch (whichLevel){
//                    case 1 :
////                        Intent intent = new Intent(getApplicationContext(),GameActivityOne.class);
////                        startActivity(intent);
////                        finish();
////                        break;
//                        Toast.makeText(getApplicationContext(),"level 1",Toast.LENGTH_LONG);
//
//                    case 2:
//                        Toast.makeText(getApplicationContext(),"level 2",Toast.LENGTH_LONG);
//                        break;
//
//                    case 3:
//                        Toast.makeText(getApplicationContext(),"level 3",Toast.LENGTH_LONG);
//                        break;
//
//                    default:
//                        Toast.makeText(getApplicationContext(),"something not well",Toast.LENGTH_LONG);
//            }


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
                        Toast.makeText(getApplicationContext(),"level 2",Toast.LENGTH_LONG);
                        break;

                    case 3:
                        Toast.makeText(getApplicationContext(),"level 3",Toast.LENGTH_LONG);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),"something not well",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
