package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileListFragment extends Fragment implements FileAdapter.FileItemEventListener {
    private String path;
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);

        recyclerView = view.findViewById(R.id.rv_files);
        File currentFolder = new File(path);


        if(StorageHelper.isExternalStorageReadable()){
            File[] files = currentFolder.listFiles();

            fileAdapter = new FileAdapter(Arrays.asList(files), this);

            recyclerView.setAdapter(fileAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        }

        TextView pathTv = view.findViewById(R.id.tv_files_path);
        pathTv.setText(currentFolder.getName().equalsIgnoreCase("false")?"ExternalStorage" : currentFolder.getName());

        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            //((MainActivity) getActivity()).listFiles(path + File.separator + file.getName());
            ((MainActivity) getActivity()).listFiles(file.getPath());
        }
    }

    @Override
    public void onDeleteFileItemClick(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            if(file.delete()){
                fileAdapter.deleteFile(file);
            }
        }

    }

    @Override
    public void onCopyFileItemClick(File file) {

        if(StorageHelper.isExternalStorageWritable()){
            try {
                copy(file,getDestinationFile(file.getName()));
                Toast.makeText(getContext(), "File is copied", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMOveFileItemClick(File file) {
        if(StorageHelper.isExternalStorageWritable()) {
            try {
                copy(file, getDestinationFile(file.getName()));
                onDeleteFileItemClick(file);
                Toast.makeText(getContext(), "File is copied", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private File getDestinationFile(String fileName){
        return new File(getContext().getExternalFilesDir(null).getPath()+File.separator+"Destination"+File.separator+fileName);
    }


    public void createNewFolder(String folderName) {
        if(StorageHelper.isExternalStorageWritable()){

            File newFodler = new File(path + File.separator + folderName);
            if (!newFodler.exists()) {
                if (newFodler.mkdir()) {
                    fileAdapter.addFile(newFodler);
                    recyclerView.scrollToPosition(0);
                }
            }
        }
    }

    private void copy(File source , File destination) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;
        while ((length=fileInputStream.read(buffer))>0){
            fileOutputStream.write(buffer,0,length);
        }


        fileInputStream.close();
        fileOutputStream.close();
    }
}
