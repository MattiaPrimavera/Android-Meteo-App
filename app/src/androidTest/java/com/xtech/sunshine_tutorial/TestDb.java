package com.xtech.sunshine_tutorial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import java.util.HashSet;

public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();
    private WeatherDbHelper helper;
    private SQLiteDatabase db;

    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    /* First method called when running JUnit test */
    public void setUp() {
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        this.helper = new WeatherDbHelper(context);
        this.db = this.helper.getReadableDatabase();
        deleteTheDatabase(); // In order to have a clean state for tests
        createDb();
    }

    @SmallTest
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

//        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);

        //Check database correctly opened
        assertEquals(true, this.db.isOpen());

        // Test table creation
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COL_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COL_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        c.moveToFirst();

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.WeatherEntry.TABLE_NAME + ")",
                null);
        Cursor dbCursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> weatherColumnHashSet = new HashSet<String>();
        weatherColumnHashSet.add(WeatherContract.WeatherEntry._ID);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_LOC_KEY);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_DATE);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_DESC);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_WEATHER_ID);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_MIN_TEMP);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_MAX_TEMP);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COL_HUMIDITY);
        Log.d("test", "test: testing");
        columnNameIndex = c.getColumnIndex("name");
        Log.d("test", "colum index : " + Integer.toString(columnNameIndex));

        for (int i = 0; i < columnNames.length; i++) {
            weatherColumnHashSet.remove(columnNames[i]);
        }

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                weatherColumnHashSet.isEmpty());

        db.close();
    }

    public void testLocationTable() {
        // First step: Get reference to writable database

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)

        // Insert ContentValues into database and get a row ID back

        // Query the database and receive a Cursor back

        // Move the cursor to a valid database row

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database

    }

    public void createDb(){

    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        // First step: Get reference to writable database

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)

        // Insert ContentValues into database and get a row ID back

        // Query the database and receive a Cursor back

        // Move the cursor to a valid database row

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation() {
        return -1L;
    }

}

