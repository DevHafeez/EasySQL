Latest Release: [![](https://jitpack.io/v/DevHafeez/EasySQL.svg)](https://jitpack.io/#DevHafeez/EasySQL)
# EasySQL & EasyCache - For Android

<img src="https://github.com/DevHafeez/EasySQL/blob/main/EasySQL/EasySQL.jpg" width="225px" height="225px" />

EasySQL is a lightweight and user-friendly SQLite wrapper for Android, designed to simplify common database operations. With EasySQL, you can easily create tables, insert data, select data with WHERE clauses, update data, and delete data with just a few lines of code.

## Features

- Simplified database management for Android SQLite.
- Fluent API for easy and readable code.
- Callbacks for handling success and failure events.
- Error handling with customizable error listeners.
- Supports common data types such as text, integer, real, blob, and boolean.

## Getting Started

### Installation

In your project's root build.gradle file, add the JitPack repository to the allprojects section:

```groovy
allprojects {
    repositories {
        // other repositories
        maven { url 'https://jitpack.io' }
    }
}
```

Add the EasySQL dependency to the dependencies section:

```groovy
dependencies {
    // other dependencies
    implementation 'com.github.DevHafeez:EasySQL:Tag'
}
```

## Usage Example
### Creating a table
 ```groovy
EasySQL easySQL = new EasySQL(context, "my_database.db");
easySQL.createTable("users")
        .addColumn("name", EasySQL.COLUMN_TYPE_TEXT)
        .addColumn("age", EasySQL.COLUMN_TYPE_INTEGER)
        .addColumn("designation", EasySQL.COLUMN_TYPE_TEXT)
        .addColumn("is_active", EasySQL.COLUMN_TYPE_INTEGER)
        .setOnTableCreatedListener(new EasySQL.OnTableCreatedListener() {
            @Override
            public void onTableCreated() {
                // Table created successfully
            }
        })
        .setOnErrorListener(new EasySQL.OnErrorListener() {
            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
```

### Inserting data

```groovy
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
```

### Getting data

```groovy
List<HashMap<String, Object>> userList;
List<String> usersStringList;

userList = easySQL.selectFrom("users").selectAll();//.where("name","John");
usersStringList = new ArrayList<>();
for (HashMap<String, Object> user : userList) {
     String nameStr = (String) user.get("name");
     String typeStr = (String) user.get("type");
     usersStringList.add(nameStr + " as " + typeStr);
}
```

### Updating data

```groovy
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
```

### Deleting data

```groovy
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
```
## EasyCache

EasyCache uses SharedPreferences and gson to store objects as strings and other common types of variables, Here is the demo use of EasyCache:

```grrovy
// context, key(as String), defaultValueOfSameType
        if (EasyCache.getBoolean(this, "myKey", false)) {
            EasyCache.storeBoolean(this, "myKey", true);
        }
        EasyCache.getString(this, "myKey", "DefaultValue");
        EasyCache.getInt(this, "myKey", 0);
        EasyCache.getObject(this, "MyKey", HashMap.class, null);
```

## License
```groovy
MIT License

Copyright (c) 2023 Hafeez Ul Haq

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
