package com.pd.mvvmapplication.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pd.mvvmapplication.Dao.ActorDao;
import com.pd.mvvmapplication.Database.ActorDatabase;
import com.pd.mvvmapplication.Model.Actor;

import java.util.List;

public class ActorRepository {

    private ActorDatabase database;
    private LiveData<List<Actor>> getAllActors;
    public ActorRepository(Application application){
        database = ActorDatabase.getInstance(application);
        getAllActors = database.actorDao().getAllActors();
    }

    public void insert(List<Actor> actorList){
        new InsertAsyncTask(database).execute(actorList);
    }

    public LiveData<List<Actor>> getAllActor(){
        return getAllActors;
    }

    static class InsertAsyncTask extends AsyncTask<List<Actor>, Void, Void>{
        private ActorDao actorDao;
        InsertAsyncTask(ActorDatabase actorDatabase){
            actorDao = actorDatabase.actorDao();
        }
        @Override
        protected Void doInBackground(List<Actor>... lists) {
            actorDao.insert(lists[0]);
            return null;
        }
    }
}
