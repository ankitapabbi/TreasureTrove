package com.lambtoncollege.treasuretrove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TreasureUnlock extends AppCompatActivity {

    Button levelChange;
    ImageView homeButton, restarLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_unlock);
        levelChange = (Button)findViewById(R.id.levelchange);
        homeButton = (ImageView)findViewById(R.id.homebutton);
        restarLevel = (ImageView)findViewById(R.id.restartlevel);

        levelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),GameActivityOne.class);
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
