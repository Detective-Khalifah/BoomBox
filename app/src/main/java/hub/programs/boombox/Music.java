package hub.programs.boombox;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.fragment.app.Fragment;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Music extends Fragment {

    ListView mListView;
    ArrayList<String> musics;
    ArrayAdapter<String> mArrayAdapter;
    String[] songs;


    public Music() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // to display menu in action bar
        //setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music, container, false);
        mListView = view.findViewById(R.id.musicListView);

        askStoragePermissions();

        return view;
    }

    public ArrayList<File> findMusics(File file){

        ArrayList<File> musicLists = new ArrayList<File>();

        File[] files = file.listFiles();

        for(File singleFile: files ){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                musicLists.addAll(findMusics(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".m4a") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".m4b") || singleFile.getName().endsWith(".ogg") || singleFile.getName().endsWith(".aac")){
                    musicLists.add(singleFile);
                }
            }
        }

        return musicLists;

    }

    public void display(){

        Log.i(Music.class.getName(), "Directory state: " + Environment.getExternalStorageState()); // returns state of accessible/non-accessible directory
        Log.i(Music.class.getName(), "Environment::" + Environment.getDataDirectory()); // returns /data

        final ArrayList<File> allSongs = findMusics(new File(String.valueOf(Environment.getExternalStorageDirectory())));
//        final ArrayList<File> allSongs = findMusics(Music.this.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        songs = new String[allSongs.size()];

        Log.i(Music.class.getName(), "songs before:: " + Arrays.toString(songs));
        for(int i=0;i<allSongs.size();i++){
            songs[i] = allSongs.get(i).getName().replace(".mp3","").replace(".m4a","").replace(".wav","").replace(".m4b","").replace(".ogg", "").replace(".aac", "");
        }
        Log.i(Music.class.getName(), "songs after:: " + Arrays.toString(songs));

        mArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,songs);
        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // start music player when song name is clicked
                String songName = mListView.getItemAtPosition(position).toString();
                Intent play = new Intent(getActivity(),Player.class);
                play.putExtra("songs",allSongs).putExtra("songName",songName).putExtra("position",position);
                startActivity(play);
            }
        });

    }

    public void askStoragePermissions(){
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown (com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

}


