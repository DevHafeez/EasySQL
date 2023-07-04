package com.hafeez.sampleeasysql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hafeez.easysql.EasyCache;
import com.hafeez.easysql.EasySQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EasySQL easySQL;
    ListView dataListV;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        easySQL = new EasySQL(this, "my_database.db");
        dataListV = findViewById(R.id.dataListView);
        EditText name = findViewById(R.id.tableName);
        EditText type = findViewById(R.id.valueName);
        View refresh = findViewById(R.id.refreshList);

        refreshList();

        findViewById(R.id.submitValue).setOnClickListener(v -> {
            String nameStr = name.getText().toString().trim();
            String typeStr = type.getText().toString().trim();

            if (nameStr.equals("") || nameStr.equals("")) {
                Toast.makeText(this, "Fill out all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
//            if (!EasyCache.getBoolean(this, "users", false)) {
            easySQL.createTable("users")
                    .addColumn("name", EasySQL.COLUMN_TYPE_TEXT)
                    .addColumn("type", EasySQL.COLUMN_TYPE_TEXT)
                    .setOnTableCreatedListener(new EasySQL.OnTableCreatedListener() {
                        @Override
                        public void onTableCreated() {
                            Toast.makeText(MainActivity.this, "Table created!", Toast.LENGTH_SHORT).show();
//                                EasyCache.storeBoolean(MainActivity.this, "users", true);
                        }
                    })
                    .setOnErrorListener(new EasySQL.OnErrorListener() {
                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(MainActivity.this, "Error creating table!", Toast.LENGTH_SHORT).show();
                        }
                    });
//            }
            easySQL.insertInto("users")
                    .add("name", nameStr)
                    .add("type", typeStr)
                    .executeInsert(new EasySQL.InsertCallback() {
                        @Override
                        public void onInsertSuccess(long rowId) {
                            Toast.makeText(MainActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            type.setText("");
                            refreshList();
                        }

                        @Override
                        public void onInsertFailure() {
                            // Insertion failed
                        }
                    });
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });

        dataListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("Edit or delete this user");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ((String) adapter.getItem(i)).split(" as ")[0];

                        easySQL.deleteFrom("users")
                                .with("name", name)
                                .executeDelete(new EasySQL.DeleteCallback() {
                                    @Override
                                    public void onDeleteSuccess(int rowsAffected) {
                                        Toast.makeText(MainActivity.this, "User " + name + " deleted!", Toast.LENGTH_SHORT).show();
                                        refreshList();
                                    }

                                    @Override
                                    public void onDeleteFailure() {
                                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                dialog.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                        LinearLayout lila1 = new LinearLayout(MainActivity.this);
                        lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
                        final EditText input = new EditText(MainActivity.this);
                        input.setHint("Name");
                        final EditText input1 = new EditText(MainActivity.this);
                        input1.setHint("Type");
                        lila1.addView(input);
                        lila1.addView(input1);
                        alert.setView(lila1);

                        alert.setTitle("Enter values");

                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String name = input.getText().toString().trim();
                                String type = input1.getText().toString().trim();

                                easySQL.updateIn("users")
                                        .whereColumn("name", ((String) adapter.getItem(i)).split(" as ")[0])
                                        .set("name", name)
                                        .set("type", type)
                                        .executeUpdate(new EasySQL.UpdateCallback() {
                                            @Override
                                            public void onUpdateSuccess(int rowsAffected) {
                                                Toast.makeText(MainActivity.this, "User updated!", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onUpdateFailure() {
                                                // Update operation failed
                                            }
                                        });
                            }
                        });
                        alert.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });
                        alert.show();
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
//
        findViewById(R.id.deleteTable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Are you sure?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        easySQL.deleteTable("users", new EasySQL.DeleteCallback() {
                            @Override
                            public void onDeleteSuccess(int rowsAffected) {
                                Toast.makeText(MainActivity.this, "All users deleted!", Toast.LENGTH_SHORT).show();
                                refreshList();
                            }

                            @Override
                            public void onDeleteFailure() {
                                // Table deletion failed
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });

    }

    List<HashMap<String, Object>> userList;
    List<String> usersStringList;

    private void refreshList() {
        userList = easySQL.selectFrom("users").selectAll();//.where("name","John");
        usersStringList = new ArrayList<>();

        for (HashMap<String, Object> user : userList) {
            String nameStr = (String) user.get("name");
            String typeStr = (String) user.get("type");

            usersStringList.add(nameStr + " as " + typeStr);
        }

        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, usersStringList);
        dataListV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}