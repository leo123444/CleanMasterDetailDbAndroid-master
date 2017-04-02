package es.ulpgc.eite.clean.mvp.dbmasterdetail.app;


import es.ulpgc.eite.clean.mvp.dbmasterdetail.detail.Detail;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.master.Master;

public interface Navigator {

  void backToMasterScreen(Detail.DetailToMaster presenter);
  void goToDetailScreen(Master.MasterToDetail presenter);
}
