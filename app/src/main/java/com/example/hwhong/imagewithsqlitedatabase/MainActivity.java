package com.example.hwhong.imagewithsqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button save, get;
    private ImageView imageView;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = (Button) findViewById(R.id.save);
        get = (Button) findViewById(R.id.get);
        imageView = (ImageView) findViewById(R.id.imageView);

        //String path, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler
        database = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        //Binary Large Object as a column value in a row of a database table. (blob)
        database.execSQL("create table if not exists tb (a blob)");

        save.setOnClickListener(this);
        get.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.save:
                try {
                    FileInputStream stream = new FileInputStream("/storage/emulated/0/spotify/spotify.png");
                    byte[] image = new byte[stream.available()];
                    stream.read(image);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("img", image);

                    database.insert("tb", null, contentValues);

                    stream.close();

                    Toast.makeText(getApplicationContext(), "Insert Success", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.get:

                Cursor cursor = database.rawQuery("select * from tb", null);

                if (cursor.moveToNext()) {
                    byte[] array = cursor.getBlob(0);

                    Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
                    imageView.setImageBitmap(bitmap);
                }

                break;
        }
    }
}
