package com.example.m_hike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.m_hike.model.Hike;
import com.example.m_hike.model.Observation;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase database;
    private static final String DBNAME = "hike_db";
    public static final String TABLE_NAME = "hike";

    public static final String HIKE_ID = "_id"; //_ under score is need for cursor adapter // use only in primary key
    public static final String HIKE_NAME = "hike_name";
    public static final String HIKE_LOCATION = "hike_location";
    public static final String HIKE_DATE = "hike_date";
    public static final String HIKE_PARKING = "hike_parking";
    public static final String HIKE_LENGTH = "hike_length";
    public static final String HIKE_DIFFICULTY = "hike_difficulty";
    public static final String HIKE_START_POINT = "hike_start_point";
    public static final String HIKE_END_POINT = "hike_end_point";
    public static final String HIKE_DESCRIPTION = "hike_description";

    public static final String OBSERVATION_TABLE_NAME = "hike_observation";
    public static final String O_ID = "_id";
    public static final String O_ANIMALS = "animals";
    public static final String O_VEGETATION = "vegetation";
    public static final String O_WEATHER = "weather";
    public static final String O_TRAILS = "trails";
    public static final String O_DATE = "date";
    public static final String O_TIME = "time";
    public static final String O_COMMENT = "comment";
    public static final String O_IMAGE = "image";
    public static final String O_HIKE_ID = "o_hike_id";

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageBytes;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 5);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableCreate = "create table " + TABLE_NAME + "("
                + HIKE_ID + " integer primary key autoincrement,"
                + HIKE_NAME + " text,"
                + HIKE_LOCATION + " text,"
                + HIKE_DATE + " text,"
                + HIKE_PARKING + " text,"
                + HIKE_LENGTH + " text,"
                + HIKE_DIFFICULTY + " text,"
                + HIKE_START_POINT + " text,"
                + HIKE_END_POINT + " text,"
                + HIKE_DESCRIPTION +
                ")";
        db.execSQL(tableCreate);

        String observationTableCreate = "create table " + OBSERVATION_TABLE_NAME + "("
                + O_ID + " integer primary key autoincrement,"
                + O_ANIMALS + " text,"
                + O_VEGETATION + " text,"
                + O_WEATHER + " text,"
                + O_TRAILS + " text,"
                + O_DATE + " text,"
                + O_TIME + " text,"
                + O_COMMENT + " text,"
                + O_IMAGE + " blob,"
                + O_HIKE_ID + " integer,"
                + "FOREIGN KEY (" + O_HIKE_ID + ")"
                + "REFERENCES " + TABLE_NAME + "(" + HIKE_ID + ")"
                + "ON DELETE CASCADE" +
                ")";
        db.execSQL(observationTableCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "drop table if exists " + TABLE_NAME;
        db.execSQL(dropTable);
        String dropObservationTable = "drop table if exists " + TABLE_NAME;
        db.execSQL(dropObservationTable);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;"); // use that if you use foreign key
    }

    public long saveHikeDetails(Hike hike) {

        ContentValues values = new ContentValues();
        values.put(HIKE_NAME, hike.getName());
        values.put(HIKE_LOCATION, hike.getLocation());
        values.put(HIKE_DATE, hike.getDateOfHike());
        values.put(HIKE_PARKING, hike.getParkingAvailable());
        values.put(HIKE_LENGTH, hike.getLengthOfHeight());
        values.put(HIKE_DIFFICULTY, hike.getDifficulty());
        values.put(HIKE_START_POINT, hike.getStartPoint());
        values.put(HIKE_END_POINT, hike.getEndPoint());
        values.put(HIKE_DESCRIPTION, hike.getDescription());

        Long hike_id = database.insertOrThrow(TABLE_NAME, null, values);
        return hike_id;

    }

    public long saveObservation(Observation observation) {

        if (observation.getImage() != null) {
            Bitmap imageToStoreBitmap = observation.getImage();

            byteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            imageBytes = byteArrayOutputStream.toByteArray();
        } else {
            imageBytes = null;
        }


        ContentValues values = new ContentValues();
        values.put(O_ANIMALS, observation.getAnimals());
        values.put(O_VEGETATION, observation.getVegetation());
        values.put(O_WEATHER, observation.getWeather());
        values.put(O_TRAILS, observation.getTrails());
        values.put(O_DATE, observation.getDate());
        values.put(O_TIME, observation.getTime());
        values.put(O_COMMENT, observation.getComments());
        values.put(O_IMAGE, imageBytes);
        values.put(O_HIKE_ID, observation.getHike_id());

        Long observation_id = database.insertOrThrow(OBSERVATION_TABLE_NAME, null, values);
        return observation_id;
    }

    public ArrayList<Hike> getAllHike() {

        database = getReadableDatabase();

        Cursor result = database.query(
                TABLE_NAME,
                new String[]{
                        HIKE_ID, HIKE_NAME, HIKE_LOCATION, HIKE_DATE, HIKE_PARKING, HIKE_LENGTH,
                        HIKE_DIFFICULTY, HIKE_START_POINT,
                        HIKE_END_POINT, HIKE_DESCRIPTION
                },
                null,
                null,
                null,
                null,
                null);
        result.moveToFirst();

        ArrayList<Hike> hike_array = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            int id = result.getInt(0);
            String name = result.getString(1);
            String location = result.getString(2);
            String date = result.getString(3);
            String parking = result.getString(4);
            String length = result.getString(5);
            String difficulty = result.getString(6);
            String startPoint = result.getString(7);
            String endPoint = result.getString(8);
            String description = result.getString(9);

            Hike hike = new Hike(id, name, location, date, parking, length, difficulty, startPoint, endPoint, description);
            hike_array.add(hike);

            result.moveToNext();


        }
        return hike_array;

    }

    public ArrayList<Hike> getSearchHike(String searchText) {
        database = getReadableDatabase();

        Cursor result = database.query(
                TABLE_NAME,
                new String[]{
                        HIKE_ID, HIKE_NAME, HIKE_LOCATION, HIKE_DATE, HIKE_PARKING, HIKE_LENGTH,
                        HIKE_DIFFICULTY, HIKE_START_POINT,
                        HIKE_END_POINT, HIKE_DESCRIPTION
                },
                HIKE_NAME + " LIKE ? OR " + HIKE_LOCATION + " LIKE ? OR " + HIKE_LENGTH + " LIKE ? OR " + HIKE_DATE + " LIKE ?",
                new String[]{"%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%", "%" + searchText + "%"},
                null,
                null,
                null);
        result.moveToFirst();

        ArrayList<Hike> hike_array = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            int id = result.getInt(0);
            String name = result.getString(1);
            String location = result.getString(2);
            String date = result.getString(3);
            String parking = result.getString(4);
            String length = result.getString(5);
            String difficulty = result.getString(6);
            String startPoint = result.getString(7);
            String endPoint = result.getString(8);
            String description = result.getString(9);

            Hike hike = new Hike(id, name, location, date, parking, length, difficulty, startPoint, endPoint, description);
            hike_array.add(hike);

            result.moveToNext();

        }
        return hike_array;

    }

    public ArrayList<Observation> getAllObservationByHikeID(int o_hike_id) {
        database = getReadableDatabase();

        Cursor result = database.query(
                OBSERVATION_TABLE_NAME,
                new String[]{
                        O_ID, O_ANIMALS, O_VEGETATION, O_WEATHER, O_TRAILS,
                        O_DATE, O_TIME, O_COMMENT, O_IMAGE, O_HIKE_ID
                },
                O_HIKE_ID + "=?",
                new String[]{String.valueOf(o_hike_id)},
                null,
                null,
                null);
        result.moveToFirst();
        ArrayList<Observation> observation = new ArrayList<>();

        for (int i = 0; i < result.getCount(); i++) {
            int id = result.getInt(0);
            String animals = result.getString(1);
            String vegetation = result.getString(2);
            String weather = result.getString(3);
            String trails = result.getString(4);
            String date = result.getString(5);
            String time = result.getString(6);
            String comment = result.getString(7);

            byte[] image = result.getBlob(8);
            Bitmap bitmap = null;
            if (image != null) {
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            }

            int h_id = result.getInt(9);

            Observation obs = new Observation(id, animals, vegetation, weather, trails, date, time, comment, bitmap, h_id);

            observation.add(obs);

            result.moveToNext();

        }
        return observation;
    }

    public Hike getHikeWithHikeId(int hike_id) {
        database = getReadableDatabase();
        Cursor result = database.query(
                TABLE_NAME,
                new String[]{
                        HIKE_ID, HIKE_NAME, HIKE_LOCATION, HIKE_DATE, HIKE_PARKING, HIKE_LENGTH,
                        HIKE_DIFFICULTY, HIKE_START_POINT,
                        HIKE_END_POINT, HIKE_DESCRIPTION
                },
                HIKE_ID + "=?",
                new String[]{String.valueOf(hike_id)},
                null,
                null,
                null);
        result.moveToFirst();

        int id = result.getInt(0);
        String name = result.getString(1);
        String location = result.getString(2);
        String date = result.getString(3);
        String parking = result.getString(4);
        String length = result.getString(5);
        String difficulty = result.getString(6);
        String startPoint = result.getString(7);
        String endPoint = result.getString(8);
        String description = result.getString(9);

        Hike hike = new Hike(id, name, location, date, parking, length, difficulty, startPoint, endPoint, description);

        return hike;

    }

    public Observation getObservationWithId(int observation_id) {
        database = getReadableDatabase();
        Cursor result = database.query(
                OBSERVATION_TABLE_NAME,
                new String[]{
                        O_ID, O_ANIMALS, O_VEGETATION, O_WEATHER, O_TRAILS, O_DATE, O_TIME,
                        O_COMMENT, O_IMAGE, O_HIKE_ID
                },
                O_ID + "=?",
                new String[]{String.valueOf(observation_id)},
                null, null, null);

        result.moveToFirst();

        int id = result.getInt(0);
        String animals = result.getString(1);
        String vegetation = result.getString(2);
        String weather = result.getString(3);
        String trails = result.getString(4);
        String date = result.getString(5);
        String time = result.getString(6);
        String comment = result.getString(7);

        byte[] image = result.getBlob(8);
        Bitmap bitmap = null;
        if (image != null) {
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        int h_id = result.getInt(9);

        Observation obs = new Observation(id, animals, vegetation, weather, trails, date, time, comment, bitmap, h_id);

        return obs;
    }

    public void updateHikeDetails(Hike hike) {
        ContentValues values = new ContentValues();
        values.put(HIKE_NAME, hike.getName());
        values.put(HIKE_LOCATION, hike.getLocation());
        values.put(HIKE_DATE, hike.getDateOfHike());
        values.put(HIKE_PARKING, hike.getParkingAvailable());
        values.put(HIKE_LENGTH, hike.getLengthOfHeight());
        values.put(HIKE_DIFFICULTY, hike.getDifficulty());
        values.put(HIKE_START_POINT, hike.getStartPoint());
        values.put(HIKE_END_POINT, hike.getEndPoint());
        values.put(HIKE_DESCRIPTION, hike.getDescription());

        database.update(TABLE_NAME, values, HIKE_ID + "=?", new String[]{String.valueOf(hike.getHikeId())});
    }

    public void updateObservation(Observation observation) {

        Bitmap imageToStoreBitmap = observation.getImage();

        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        imageBytes = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(O_ANIMALS, observation.getAnimals());
        values.put(O_VEGETATION, observation.getVegetation());
        values.put(O_WEATHER, observation.getWeather());
        values.put(O_TRAILS, observation.getTrails());
        values.put(O_DATE, observation.getDate());
        values.put(O_TIME, observation.getTime());
        values.put(O_COMMENT, observation.getComments());
        values.put(O_IMAGE, imageBytes);
        values.put(O_HIKE_ID, observation.getHike_id());

        database.update(OBSERVATION_TABLE_NAME, values, O_ID + "=?", new String[]{String.valueOf(observation.get_id())});

    }

    public void deleteHikeDetails(int hike_id) {
        database.delete(TABLE_NAME, HIKE_ID + "=?", new String[]{String.valueOf(hike_id)});
    }

    public void deleteAllHike() {
        database.delete(TABLE_NAME, "1", null);
    }

    public void deleteObservationByObservationId(int o_id) {
        database.delete(OBSERVATION_TABLE_NAME, O_ID + "=?", new String[]{String.valueOf(o_id)});
    }

    public void deleteAllObservationByHikeId(int hike_id) {
        database.delete(OBSERVATION_TABLE_NAME, O_HIKE_ID + "=?", new String[]{String.valueOf(hike_id)});
    }
}
