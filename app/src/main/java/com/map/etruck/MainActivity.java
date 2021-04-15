package com.map.etruck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.map.etruck.Fragments.MapFragmet;

public class MainActivity extends AppCompatActivity {
FragmentTransaction fragmentTransction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragmet(new MapFragmet(),null);
    }
    private void replaceFragmet(Fragment fragment,Bundle bundle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
            fragmentTransction.replace(R.id.mainActivity_Container, fragment, fragment.toString());
            fragmentTransction.commit();
        } else {
            fragmentTransction.replace(R.id.mainActivity_Container, fragment, fragment.toString());
            fragmentTransction.commit();
        }
    }
}