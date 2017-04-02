package es.ulpgc.eite.clean.mvp.dbmasterdetail.app;


import es.ulpgc.eite.clean.mvp.dbmasterdetail.detail.Detail;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.master.Master;

public interface Mediator {

  void startingMasterScreen(Master.ToMaster presenter);
  void resumingMasterScreen(Master.DetailToMaster presenter);
  void startingDetailScreen(Detail.MasterToDetail presenter);
}
