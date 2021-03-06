package com.thelittlepets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.thelittlepets.R;

public class FragmentActivity extends android.support.v4.app.FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("Perfil").setIndicator("Perfil", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Doações").setIndicator("Doações", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Saldo").setIndicator("Saldo", null),
                FragmentTab.class, null);
    }
}