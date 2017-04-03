package es.ulpgc.eite.clean.mvp.dbmasterdetail.detail;

import android.os.Bundle;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;


import java.util.ArrayList;
import java.util.Observable;

import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.R;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.ModelItem;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.ObserverMasterDetail;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.master.MasterView;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MasterView}.
 */
public class DetailView
    extends GenericActivity<Detail.PresenterToView, Detail.ViewToPresenter, DetailPresenter>
    implements Detail.PresenterToView {

  private ModelItem item;
  private Toolbar toolbar;
  private CollapsingToolbarLayout toolbarLayout;
  private AppBarLayout appbarLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_detail);
    toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
   // setSupportActionBar(toolbar);

    // Show the Up button in the action bar.
    ActionBar actionbar = getSupportActionBar();
    if (actionbar != null) {
      actionbar.setDisplayHomeAsUpEnabled(true);
    }

    toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
    appbarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
    appbarLayout.setExpanded(true);
  }



  /**
   * Method that initialized MVP objects
   * {@link super#onResume(Class, Object)} should always be called
   */
  @Override
  public void onResume() {
    super.onResume(DetailPresenter.class, this);
    item = getPresenter().getItem();

    if (toolbarLayout != null && item != null) {
      toolbarLayout.setTitle(item.getContent());
    }

    // Show the dummy content as text in a TextView.
    if (item != null) {
      ((TextView) findViewById(R.id.item_detail)).setText(item.getDetails());
    }

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finishScreen();
      return true;
    } else if (id == R.id.action_delete) {
      getPresenter().onDeleteActionClicked();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  /////////////////////////////////////////////////////////////////////////////////////
  // Presenter To View ///////////////////////////////////////////////////////////////


  @Override
  public void finishScreen() {
    finish();
  }

  @Override
  public void hideToolbar() {
    toolbar.setVisibility(View.GONE);
    appbarLayout.setExpanded(false);
  }
  public class ObservableDetail extends Observable {
    private ArrayList<ObserverMasterDetail> observers;

    public ObservableDetail(){
      this.observers= new ArrayList<ObserverMasterDetail>();
    }

    public void addObservers(ObserverMasterDetail observer){
      observers.add(observer);

    }

    public void notifyChanges(){

      for(ObserverMasterDetail o:observers){
        Object arg= new Object();
        o.update();

      }
  }


}}
