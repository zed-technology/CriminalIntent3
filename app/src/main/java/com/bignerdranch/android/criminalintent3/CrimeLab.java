package com.bignerdranch.android.criminalintent3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent3.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent3.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent3.database.CrimeDBSchema;
import com.bignerdranch.android.criminalintent3.database.CrimeDBSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }


    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() });
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // columns = null выбираются все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return contentValues;
    }



}
