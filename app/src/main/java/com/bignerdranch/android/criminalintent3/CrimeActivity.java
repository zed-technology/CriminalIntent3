package com.bignerdranch.android.criminalintent3;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CTIME_ID = "com.bignerdranch.android.criminalintent3.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CTIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CTIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
