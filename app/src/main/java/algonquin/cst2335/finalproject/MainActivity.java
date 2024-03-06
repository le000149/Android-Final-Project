package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button buttonApp1 = findViewById(R.id.buttonApp1);
            Button buttonApp2 = findViewById(R.id.buttonApp2);
            Button buttonApp3 = findViewById(R.id.buttonApp3);
            Button buttonApp4 = findViewById(R.id.buttonApp4);

            buttonApp1.setOnClickListener(v -> launchApp("com.example.app1"));
            buttonApp2.setOnClickListener(v -> launchApp("com.example.app2"));
            buttonApp3.setOnClickListener(v -> launchApp("com.example.app3"));
            buttonApp4.setOnClickListener(v -> launchApp("com.example.app4"));
        }

        private void launchApp(String packageName) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                startActivity(intent);
            } else {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
            }
        }
    }


