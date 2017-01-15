package safemessage.viveret.com.safemessage.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by viveret on 1/15/17.
 */

public class SqlHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_SMS =
            "CREATE TABLE ";

    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
