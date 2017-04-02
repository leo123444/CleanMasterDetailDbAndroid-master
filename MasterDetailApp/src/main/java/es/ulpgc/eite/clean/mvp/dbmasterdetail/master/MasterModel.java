package es.ulpgc.eite.clean.mvp.dbmasterdetail.master;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.ModelItem;
import io.realm.Realm;


public class MasterModel
    extends GenericModel<Master.ModelToPresenter> implements Master.PresenterToModel {

  private static final int ITEM_COUNT = 9;

  //public List<ModelItem> items = null; // An array of items
  private boolean runningTask;
  private Realm realmDatabase;
  private boolean validDatabase;
  private String errorMsg;


  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Master.ModelToPresenter presenter) {
    super.onCreate(presenter);
    realmDatabase = Realm.getDefaultInstance();
    //validDatabase = true;
    errorMsg = "Error deleting item!";
  }

  /**
   * Called by layer PRESENTER when VIEW pass for a reconstruction/destruction.
   * Usefull for kill/stop activities that could be running on the background Threads
   *
   * @param isChangingConfiguration Informs that a change is occurring on the configuration
   */
  @Override
  public void onDestroy(boolean isChangingConfiguration) {

  }

  /////////////////////////////////////////////////////////////////////////////////////
  // Presenter To Model //////////////////////////////////////////////////////////////


  /**
   * Llamado para recuperar los elementos a mostrar en la lista.
   * Si el contenido ya ha sido fijado antes, se notificará inmediatamente al presentador y,
   * sino es el caso, la notificación se realizará al finalizar la tarea que fija este contenido
   */
  @Override
  public void loadItems() {
    if(!validDatabase && !runningTask) {
      startDelayedTask();
    } else {
      if(!runningTask){
        Log.d(TAG, "calling onLoadItemsTaskFinished() method");
        getPresenter().onLoadItemsTaskFinished(getItemsFromDatabase());
      } else {
        Log.d(TAG, "calling onLoadItemsTaskStarted() method");
        getPresenter().onLoadItemsTaskStarted();
      }
    }

    /*
    if(items == null && !runningTask) {
      startDelayedTask();
    } else {
      if(!runningTask){
        Log.d(TAG, "calling onLoadItemsTaskFinished() method");
        getPresenter().onLoadItemsTaskFinished(items);
      } else {
        Log.d(TAG, "calling onLoadItemsTaskStarted() method");
        getPresenter().onLoadItemsTaskStarted();
      }
    }
    */
  }

  /*
  @Override
  public void deleteItem(ModelItem item) {
    if (items.contains(item)){
      items.remove(item);
    } else {
      getPresenter().onErrorDeletingItem(item);
    }
  }
  */

  @Override
  public void deleteItem(ModelItem item) {
    if (getItemsFromDatabase().contains(item)){
      //items.remove(item);
      deleteDatabaseItem(item);
    } else {
      getPresenter().onErrorDeletingItem(item);
    }
  }

  /**
   * Llamado para recuperar los elementos iniciales de la lista.
   * En este caso siempre se llamará a la tarea asíncrona
   */
  @Override
  public void reloadItems() {
    //items = null;
    deleteAllDatabaseItems();
    validDatabase = false;
    loadItems();
  }

  @Override
  public void setDatabaseValidity(boolean valid) {
    validDatabase = valid;
  }

  @Override
  public String getErrorMessage() {
    return errorMsg;
  }

  /////////////////////////////////////////////////////////////////////////////////////

  /*
  private void addItem(ModelItem item) {
    items.add(item);
  }

  private ModelItem createItem(int position) {
    return new ModelItem(String.valueOf(position), "Item " + position, makeDetails(position));
  }
  */

  private String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position).append("\n");
    for (int count = 0; count < position; count++) {
      builder.append("\nMore details information here.");
    }
    return builder.toString();
  }

  /*
  private void setItems(){
    items = new ArrayList();

    // Add some sample items
    for (int count = 1; count <= ITEM_COUNT; count++) {
      addItem(createItem(count));
    }
  }
  */

  /**
   * Llamado para recuperar los elementos a mostrar en la lista.
   * Consiste en una tarea asíncrona que retrasa un tiempo la obtención del contenido.
   * El modelo notificará al presentador cuando se inicia y cuando finaliza esta tarea.
   */
  private void startDelayedTask() {
    Log.d(TAG, "calling startDelayedTask() method");
    runningTask = true;
    Log.d(TAG, "calling onLoadItemsTaskStarted() method");
    getPresenter().onLoadItemsTaskStarted();

    // Mock Hello: A handler to delay the answer
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        //setItems();
        setDatabaseItemsFromJson();
        runningTask = false;
        validDatabase = true;
        Log.d(TAG, "calling onLoadItemsTaskFinished() method");
        //getPresenter().onLoadItemsTaskFinished(items);
        getPresenter().onLoadItemsTaskFinished(getItemsFromDatabase());
      }
    }, 5000);
  }




  /////////////////////////////////////////////////////////////////////////////////////


  private void deleteAllDatabaseItems(){
    for(ModelItem item: getItemsFromDatabase()){
      deleteDatabaseItem(item);
    }
  }

  private void deleteDatabaseItem(ModelItem item) {
    final String id = item.getId();
    realmDatabase.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.where(ModelItem.class).equalTo("id", id)
            .findAll()
            .deleteAllFromRealm();
      }
    });
    /*
    realmDatabase.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.where(ModelItem.class).equalTo("id", id)
            .findAll()
            .deleteAllFromRealm();
      }
    });
    */
  }

  private void setDatabaseItemsFromJson(){
    //setItemsFromJsonStream("");
    setItemsFromJsonObjectArray();
  }

  private void setItemsFromJsonStream(String filename)  {
    try {

      // Use streams if you are worried about the size of the JSON whether it was persisted on disk
      // or received from the network.
      Context context = getPresenter().getManagedContext();
      InputStream stream = context.getAssets().open(filename);

      // Open a transaction to store items into the realmDatabase
      realmDatabase.beginTransaction();
      try {
        realmDatabase.createAllFromJson(ModelItem.class, stream);
        realmDatabase.commitTransaction();
      } catch (IOException e) {
        // Remember to cancel the transaction if anything goes wrong.
        realmDatabase.cancelTransaction();
      } finally {
        if (stream != null) {
          stream.close();
        }
      }

    } catch (IOException ex) {

    }
  }

  private List<ModelItem> getItemsFromDatabase(){
    return realmDatabase.where(ModelItem.class).findAll();
  }

  /*
  private void setItemsFromDatabase(){
    items = realmDatabase.where(ModelItem.class).findAll();
  }
  */

  private void setItemsFromJsonObjectArray(){
    // Add some sample items
    for (int count = 1; count <= ITEM_COUNT; count++) {
      saveFromJsonObject(createJsonObject(count));
    }
  }

  private JSONObject createJsonObject(int id){
    Map<String, String> item = new HashMap();
    item.put("id", String.valueOf(id));
    item.put("content", "Item " + id);
    item.put("details", makeDetails(id));
    final JSONObject json = new JSONObject(item);
    return json;
  }

  private void saveFromJsonObject(final JSONObject json) {
    realmDatabase.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.createObjectFromJson(ModelItem.class, json);
      }
    });
  }

}
