package com.hafeez.sampleeasysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hafeez.easysql.EasySQL;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EasySQL easySQL = new EasySQL(this, "my_database.db");

        easySQL.createTable("users")
                .addColumn("id", EasySQL.COLUMN_TYPE_INT_PRIMARY_KEY)
                .addColumn("name", EasySQL.COLUMN_TYPE_TEXT)
                .addColumn("age", EasySQL.COLUMN_TYPE_INTEGER)
                .addColumn("designation", EasySQL.COLUMN_TYPE_TEXT)
                .addColumn("is_active", EasySQL.COLUMN_TYPE_INTEGER)
                .setOnTableCreatedListener(new EasySQL.OnTableCreatedListener() {
                    @Override
                    public void onTableCreated() {
                        // Table creation successful
                    }
                })
                .setOnErrorListener(new EasySQL.OnErrorListener() {
                    @Override
                    public void onError(String errorMessage) {
                        // Error occurred during table creation
                    }
                });

        easySQL.insertInto("users")
                .add("name", "John")
                .add("age", 25)
                .add("designation", "Sales Manager")
                .add("is_active", true)
                .executeInsert(new EasySQL.InsertCallback() {
                    @Override
                    public void onInsertSuccess(long rowId) {
                        // Insertion successful
                    }

                    @Override
                    public void onInsertFailure() {
                        // Insertion failed
                    }
                });

        List<HashMap<String, Object>> userList = easySQL.selectFrom("users")
                .where("designation", "Sales Manager");

        for (HashMap<String, Object> user : userList) {
            String name = (String) user.get("name");
            int age = (int) user.get("age");
            String designation = (String) user.get("designation");
            boolean isActive = (boolean) user.get("is_active");

            // Use the data as needed
        }

        easySQL.updateIn("users")
                .set("age", 30)
                .executeUpdate(new EasySQL.UpdateCallback() {
                    @Override
                    public void onUpdateSuccess(int rowsAffected) {
                        // Update operation successful
                    }

                    @Override
                    public void onUpdateFailure() {
                        // Update operation failed
                    }
                });

        easySQL.deleteTable("users", new EasySQL.DeleteCallback() {
            @Override
            public void onDeleteSuccess(int rowsAffected) {
                // Table deletion successful
            }

            @Override
            public void onDeleteFailure() {
                // Table deletion failed
            }
        });

        easySQL.deleteFrom("users")
                .with("designation", "Sales Manager")
                .executeDelete(new EasySQL.DeleteCallback() {
                    @Override
                    public void onDeleteSuccess(int rowsAffected) {
                        // Deletion successful
                    }

                    @Override
                    public void onDeleteFailure() {
                        // Deletion failed
                    }
                });


    }
}