package org.solarus_games.solarus;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class Solarus extends FragmentActivity
        implements QuestFragment.OnListFragmentInteractionListener, QuestDetails.OnFragmentInteractionListener{

    private final int CHOOSE_FOLDER_INTENT = 123;
    private final int CHOOSE_FILE_INTENT = 125;
    private final int REQUEST_READ_STORAGE = 124;
    private final String TAG = "SolarusEngine";
    private final static String QUEST_DETAILS_DIALOG_TAG = "quest_dialog";
    private QuestFragment mQuestList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_launcher);
        final FloatingActionButton add_file_button = findViewById(R.id.add_file_button);
        add_file_button.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               addQuestFile(v);
           }
        });
        final FloatingActionButton add_folder_button = findViewById(R.id.add_folder_button);
        add_folder_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addQuestFolder(v);
            }
        });
        boolean hasPermission = (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        mQuestList = (QuestFragment) getSupportFragmentManager().findFragmentById(R.id.quest_list);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }
    }

    public void addQuestFile(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,CHOOSE_FILE_INTENT);
    }

    public void addQuestFolder(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent,CHOOSE_FOLDER_INTENT);
    }



    public void addQuestPath(String path) {
        mQuestList.addQuest(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_FILE_INTENT && resultCode==RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            try {
                String path = PathUtils.getPath(this, selectedfile);
                addQuestPath(path);
            } catch (Exception e) {
                Log.e(TAG,"Exception : " + e.getMessage());
            }
        }
        if(requestCode==CHOOSE_FOLDER_INTENT && resultCode==RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            try {
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(selectedfile,
                        DocumentsContract.getTreeDocumentId(selectedfile));
                String path = PathUtils.getPath(this, docUri);

                addQuestPath(path);
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

    ///List related methods
    @Override
    public void onQuestDetailClick(Quest quest) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        QuestDetails newDialog = QuestDetails.newInstance(quest.path);
        newDialog.show(ft,QUEST_DETAILS_DIALOG_TAG);
    }

    @Override
    public void onQuestLaunchClick(Quest quest) {
        Intent intent = new Intent(Solarus.this,SolarusEngine.class);
        Bundle b = new Bundle();
        b.putString("quest_path",quest.path);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onQuestDeleteRequest(Quest q) {
        mQuestList.removeQuest(q);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(QUEST_DETAILS_DIALOG_TAG);
        if(dialog != null) {
            ft.remove(dialog);
        }
        ft.commit();
    }
}
