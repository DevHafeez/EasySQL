# EasySQL

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
    implementation 'com.github.DevHafeez:EasySQL:v1.0.0'
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

### Updating data

```groovy
easySQL.updateIn("users")
        .set("designation", "Senior Sales Manager")
        .executeUpdate(new EasySQL.UpdateCallback() {
            @Override
            public void onUpdateSuccess(int rowsAffected) {
                // Update successful
            }

            @Override
            public void onUpdateFailure() {
                // Update failed
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

## Please Note

This is an initial version 1.0.0, some users may face issues, if you face any issue, please highlight it for me, I will keep the repo updated insha'Allah!

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
