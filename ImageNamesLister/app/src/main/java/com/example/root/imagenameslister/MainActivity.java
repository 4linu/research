package com.example.root.imagenameslister;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.RandomAccessFile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button b1 = (Button) findViewById(R.id.button1);
        final TextView tv1 = (TextView) findViewById(R.id.textView1);

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tv1.setText("No img found");
                String buff = "";
                try {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.v("IMPORTANT_TAG","Permission is granted");
                    }
                    else {
                        Log.v("IMPORTANT_TAG","Permission is denied");
                    }
                    File flist = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (flist != null) {
                        File[] files = flist.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                buff = buff + "|" + file.getName();
                            }
                            buff += "\n";
                            for (File file : files) {
                                if (file.getName().endsWith(".txt")) {
                                    buff += "\n#File " + file.getName() + " content: ";
                                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                                    String line;
                                    while ((line = raf.readLine()) != null) {
                                        buff += line + "\n";
                                    }
                                    raf.close();
                                }
                            }
                            tv1.setText(buff);
                        }

                    }
                } catch (Exception e)
                {
                    Log.e("IMAGE_NAMES_LISTER","ImageNamesLister cannot list images, error " + e.getMessage());
                    tv1.setText("Exception occured");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
