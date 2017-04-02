package es.ulpgc.eite.clean.mvp.dbmasterdetail.app;

import java.util.Observable;
import java.util.Observer;

import es.ulpgc.eite.clean.mvp.dbmasterdetail.detail.Detail;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.master.Master;

/**
 * Created by eleonora on 02/04/2017.
 */

public class ObserverMasterDetail implements Observer,Mediator,Navigator {
    @Override
    public void update(Observable o, Object arg) {

    }


    @Override
    public void startingMasterScreen(Master.ToMaster presenter) {
        
    }

    @Override
    public void resumingMasterScreen(Master.DetailToMaster presenter) {

    }

    @Override
    public void startingDetailScreen(Detail.MasterToDetail presenter) {

    }

    @Override
    public void backToMasterScreen(Detail.DetailToMaster presenter) {

    }

    @Override
    public void goToDetailScreen(Master.MasterToDetail presenter) {

    }
}
