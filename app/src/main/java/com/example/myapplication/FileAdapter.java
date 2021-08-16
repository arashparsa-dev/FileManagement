package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    List<File> files;
    private FileItemEventListener fileItemEventListener;

    public FileAdapter(List<File> files,FileItemEventListener fileItemEventListener) {
        this.files = new ArrayList<>(files);
        this.fileItemEventListener = fileItemEventListener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file,parent,false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bindFile(files.get(position));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public  class FileViewHolder extends RecyclerView.ViewHolder {

        private TextView fileNameTv;
        private ImageView fileIconIv;
        private View moreIv;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileIconIv = itemView.findViewById(R.id.iv_file);
            fileNameTv = itemView.findViewById(R.id.tv_item_fileName);
            moreIv = itemView.findViewById(R.id.iv_file_more);
        }
        public void  bindFile(File file){
            if (file.isDirectory()){
                fileIconIv.setImageResource(R.drawable.ic_folder_black_32dp);
            }else
                fileIconIv.setImageResource(R.drawable.ic_file_black_32dp);

            fileNameTv.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //static b instance variable class khodesh dastresi nadare pas hazf mikonim "static"
                    fileItemEventListener.onFileItemClick(file);
                }
            });

            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_file_item,popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menuItem_delete:
                                        fileItemEventListener.onDeleteFileItemClick(file);
                                    break;
                                case R.id.menuItem_copy:
                                    fileItemEventListener.onCopyFileItemClick(file);
                                    break;
                                case R.id.menuItem_move:
                                    fileItemEventListener.onMOveFileItemClick(file);
                                    break;
                            }

                            return false;
                        }
                    });
                }
            });

        }
    }

    public void deleteFile(File file) {
        //if u do not need index
        //files.remove(file);
        //need index to show delete animation to user
        int index = files.indexOf(file);
        if(index>-1){
            files.remove(index);
            notifyItemRemoved(index);
        }


    }


    public  interface FileItemEventListener{
        void onFileItemClick(File file);

        void onDeleteFileItemClick(File file);

        void onCopyFileItemClick(File file);

        void onMOveFileItemClick(File file);
    }

        public void  addFile(File file){
        files.add(0,file);
        notifyItemInserted(0);
        }
}
