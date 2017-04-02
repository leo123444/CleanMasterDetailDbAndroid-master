package es.ulpgc.eite.clean.mvp.dbmasterdetail.app;

import java.util.Observable;
import java.util.Observer;

import es.ulpgc.eite.clean.mvp.dbmasterdetail.master.MasterPresenter;

/**
 * Created by eleonora on 02/04/2017.
 */

public class ObserverMasterDetail implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof MasterPresenter){
          MasterPresenter mp=(MasterPresenter)arg;
            System.out.println("master has changed");
        }
}}
