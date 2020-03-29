package com.andrewlevada.certus.logic.livepapers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.List;
import java.util.Map;

public class DataNode {
    public static final String DEBUG_TAG = "DataNode";

    Object value;

    public Object getValue() {
        return value;
    }

    public DataNode(DataSnapshot snapshot) {
        value = snapshot.getValue();

        // Define data type
        if (value instanceof Boolean
                || value instanceof String
                || value instanceof Long
                || value instanceof Double) {
            // Value

        } else if (value instanceof Map) {
            // JSON next

        } else if (value instanceof List) {
            // Array

        } else {
            // Undefined
            Log.e(DEBUG_TAG, "Undefined type");
        }
    }
}
