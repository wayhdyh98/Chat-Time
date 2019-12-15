package com.example.realtimechat;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.realtimechat.fragments.MainFragment;
import com.example.realtimechat.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    public String name;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        request_user_name();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            openFragment(new SettingsFragment());
            return true;
        } else if (id == android.R.id.home) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            MainFragment main = new MainFragment();
            main.setArguments(bundle);
            openFragment(main);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFragment(Fragment fragment) {
        openFragment(fragment, false);
    }

    private void openFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackstack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Name");
        builder.setIcon(R.drawable.user_circle);
        final EditText input_field = new EditText(this);
        int margin = dpToPx(20);
        input_field.setSingleLine();
        input_field.setHint("Your name");
        builder.setView(input_field, margin, 0, margin, 0);
        builder.setPositiveButton("OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = input_field.getText().toString();
                bundle.putString("name", name);
                MainFragment main = new MainFragment();
                main.setArguments(bundle);
                openFragment(main);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
