package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddNewFolderDialog.AddNewFolderCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.iv_main_addNewFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewFolderDialog().show(getSupportFragmentManager(),null);
            }
        });

        if(StorageHelper.isExternalStorageReadable()){
            //getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            //masir poshe apk shoma dakhel poshe data namayesh mide
            File externalFilesDir = getExternalFilesDir(null);
            listFiles(externalFilesDir.getPath(),false);
        }


    }

    public void  listFiles(String path,boolean addToBackStack){

        FileListFragment fileListFragment = new FileListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path",path);
        fileListFragment.setArguments(bundle);


        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer,fileListFragment);
        if(addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }
    public void listFiles(String path){
        this.listFiles(path,true);
    }


    @Override
    public void onCreateFolderButtonClick(String folderName) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if(fragment instanceof FileListFragment){
            ((FileListFragment) fragment).createNewFolder(folderName);
        }


    }
}