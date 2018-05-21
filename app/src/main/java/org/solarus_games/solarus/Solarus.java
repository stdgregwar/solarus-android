package org.solarus_games.solarus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

public class Solarus extends Activity {

    private final int CHOOSE_FOLDER_INTENT = 123;
    private final int REQUEST_READ_STORAGE = 124;
    private final String TAG = "SolarusEngine";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_launcher);
        final FloatingActionButton b = findViewById(R.id.add_button);
        b.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               addQuest(v);
           }
        });
        boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }
    }

    public void addQuest(View v) {
        Log.d(TAG, "Clicked");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent,CHOOSE_FOLDER_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_FOLDER_INTENT && resultCode==RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            try {
                Uri uri = selectedfile;
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                        DocumentsContract.getTreeDocumentId(uri));
                String path = PathUtils.getPath(this, docUri);
                Intent intent = new Intent(Solarus.this,SolarusEngine.class);
                Bundle b = new Bundle();
                b.putString("quest_path",path);
                intent.putExtras(b);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG,"Exception : " + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    finish();
                    startActivity(getIntent());
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(getBaseContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
