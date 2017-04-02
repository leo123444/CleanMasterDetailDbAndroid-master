package es.ulpgc.eite.clean.mvp.dbmasterdetail.app;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import es.ulpgc.eite.clean.mvp.dbmasterdetail.detail.DetailPresenter;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.master.MasterPresenter;

import static android.content.ContentValues.TAG;

/**
 * Created by eleonora on 02/04/2017.
 */

public class ObserverMasterDetail implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof MasterPresenter ){
          MasterPresenter mp=(MasterPresenter)arg;
            Log.d(TAG,"master has changed");
        }
        if(arg instanceof DetailPresenter){
            DetailPresenter dp=(DetailPresenter)arg;
           Log.d(TAG,"detail has changed");
        }

    }

}
