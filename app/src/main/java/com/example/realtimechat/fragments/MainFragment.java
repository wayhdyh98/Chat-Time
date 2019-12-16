package com.example.realtimechat.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechat.ChatRoom;
import com.example.realtimechat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final String SWITCH_KEY = "switch";
    private Button add_room;
    private EditText room_name;
    private ListView listView;
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList();

    private SharedPreferences preferences;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean switchValue = preferences.getBoolean(SWITCH_KEY, false);
        if (switchValue) {
            room_name.setVisibility(View.GONE);
            add_room.setVisibility(View.GONE);
        } else {
            room_name.setVisibility(View.VISIBLE);
            add_room.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        add_room = (Button)view.findViewById(R.id.btnAdd_room);
        room_name = (EditText)view.findViewById(R.id.etNeme_room);
        listView = (ListView)view.findViewById(R.id.listView);
        name = getArguments().getString("name");

        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, list_of_rooms){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };

        listView.setAdapter(arrayAdapter);

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(room_name.getText().toString(),"");
                room_name.setText("");
                root.updateChildren(map);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent I = new Intent(getActivity(), ChatRoom.class);
                I.putExtra("room_name", ((TextView)view).getText().toString());
                I.putExtra("user_name", name);
                startActivity(I);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                delete_dialog(view);
                return true;
            }
        });

        return view;
    }

    private void delete_dialog(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure want to delete this room?");
        builder.setIcon(R.drawable.trash);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String rn = ((TextView)view).getText().toString();
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child(rn);
                dR.removeValue();
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
            } });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG).show();
            } });
        builder.show();
    }

}
