package com.xtech.sunshine_tutorial;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.Map;
import java.util.Set;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class TestUtils {
    public static ContentValues createInsertValues(){
        ContentValues result = new ContentValues();
        result.put(WeatherContract.LocationEntry.COL_CITY_NAME, "Paris");
        return result;
    }

    public static void validateInsertedData(String error, Cursor valueCursor, ContentValues expectedValues){
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
