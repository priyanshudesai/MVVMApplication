package com.pd.mvvmapplication.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.pd.mvvmapplication.Dao.ActorDao;
import com.pd.mvvmapplication.Model.Actor;

@Database(entities = {Actor.class}, version = 4)
public abstract class ActorDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "ActorDatabase";

    public abstract ActorDao actorDao();

    private static volatile ActorDatabase INSTANCE;

    public static ActorDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (ActorDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, ActorDatabase.class,
                            DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsynTask(INSTANCE);
        }
    };
    static class PopulateAsynTask extends AsyncTask<Void,Void,Void>{

        private ActorDao actorDao;
        PopulateAsynTask(ActorDatabase actorDatabase){
            actorDao = actorDatabase.actorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            actorDao.deleteAll();
            return null;
        }
    }
}
