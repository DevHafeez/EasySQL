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
    implementation 'com.github.DevHafeez:EasySQL:1.0.0'
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

## License

This project is licensed under the MIT License.

Feel free to modify and customize this template according to your specific needs. Make sure to update the installation instructions and license section with the appropriate information for your project.

Remember to include the necessary attribution and license information as per the licenses used in your project.
