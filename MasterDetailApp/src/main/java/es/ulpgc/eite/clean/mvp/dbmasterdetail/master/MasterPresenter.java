package es.ulpgc.eite.clean.mvp.dbmasterdetail.master;


import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.GenericPresenter;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.Mediator;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.ModelItem;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.Navigator;


public class MasterPresenter extends GenericPresenter
    <Master.PresenterToView, Master.PresenterToModel, Master.ModelToPresenter, MasterModel>
    implements Master.ViewToPresenter, Master.ModelToPresenter,
    Master.MasterToDetail, Master.ToMaster, Master.DetailToMaster {


  private boolean hideToolbar;
  private ModelItem selectedItem;
  private ModelItem itemToDelete;
  //private boolean hideContent;
  private boolean hideProgress;


  /**
   * Operation called during VIEW creation in {@link GenericActivity#onResume(Class, Object)}
   * Responsible to initialize MODEL.
   * Always call {@link GenericPresenter#onCreate(Class, Object)} to initialize the object
   * Always call  {@link GenericPresenter#setView(ContextView)} to save a PresenterToView reference
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onCreate(Master.PresenterToView view) {
    Log.d(TAG, "calling onCreate() method");
    super.onCreate(MasterModel.class, this);

    setView(view);

    Log.d(TAG, "calling startingMasterScreen() method");
    // Debe llamarse al arrancar el maestro para fijar su estado inicial

    Mediator app = (Mediator) getView().getApplication();
    app.startingMasterScreen(this);
  }

  /**
   * Operation called by VIEW after its reconstruction.
   * Always call {@link GenericPresenter#setView(ContextView)}
   * to save the new instance of PresenterToView
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onResume(Master.PresenterToView view) {
    Log.d(TAG, "calling onResume() method");
    setView(view);
    Log.d(TAG, "calling resumingMasterScreen() method");

    if(!configurationChangeOccurred()) {
      Mediator app = (Mediator) getView().getApplication();
      // Debe llamarse cada vez que se reinicia el maestro para actualizar su estado
      app.resumingMasterScreen(this);
      MasterPresenter event= new MasterPresenter();

    }
  }

  /**
   * Helper method to inform Presenter that a onBackPressed event occurred
   * Called by {@link GenericActivity}
   */
  @Override
  public void onBackPressed() {
    Log.d(TAG, "calling onResume() method");
  }

  /**
   * Hook method called when the VIEW is being destroyed or
   * having its configuration changed.
   * Responsible to maintain MVP synchronized with Activity lifecycle.
   * Called by onDestroy methods of the VIEW layer, like: {@link GenericActivity#onDestroy()}
   *
   * @param isChangingConfiguration true: configuration changing & false: being destroyed
   */
  @Override
  public void onDestroy(boolean isChangingConfiguration) {
    Log.d(TAG, "calling onDestroy() method");
    super.onDestroy(isChangingConfiguration);

    // Si giramos la pantalla debemos fijar si la barra de tareas será visible o no
    if(isChangingConfiguration) {
      hideToolbar = !hideToolbar;
      MasterPresenter event= new MasterPresenter();

  }}


  /////////////////////////////////////////////////////////////////////////////////////
  // Master To Detail ////////////////////////////////////////////////////////////////


  @Override
  public Context getManagedContext(){
    return getActivityContext();
  }

  /**
   * Llamado por el mediador para recoger el estado a pasar al detalle
   *
   * @return item seleccionado en la lista al hacer click
   */
  @Override
  public ModelItem getSelectedItem() {
    return selectedItem;
  }


  @Override
  public boolean getToolbarVisibility() {
    return !hideToolbar;
  }

  /////////////////////////////////////////////////////////////////////////////////////
  // Model To Presenter //////////////////////////////////////////////////////////////


  @Override
  public void onErrorDeletingItem(ModelItem item) {
    if(isViewRunning()) {
      getView().showError(getModel().getErrorMessage());
    }
  }

  /**
   * Llamado desde el modelo cuando finaliza la tarea para la obtención del contenido de la lista
   *
   * @param items como contenido de la lista a mostrar en pantalla
   */
  @Override
  public void onLoadItemsTaskFinished(List<ModelItem> items) {
    // Una vez finaliza la tarea para la obtención del contenido de la lista,
    // hacemos desaparecer de pantalla el círculo de progreso
    // y actualizamos el contenido de la lista
    hideProgress = true;
    checkVisibility();
    getView().setRecyclerAdapterContent(items);


  }

  /**
   * Llamado desde el modelo cuando comienza la tarea para la obtención del contenido de la lista
   */
  @Override
  public void onLoadItemsTaskStarted() {
    hideProgress = false;
    checkVisibility();
  }


  /////////////////////////////////////////////////////////////////////////////////////
  // Detail To Master ////////////////////////////////////////////////////////////////

  /**
   * Llamado por el mediador cada vez que se reinicia el maestro despues de haber
   * actualizado su estado si fuera el caso
   */
  @Override
  public void onScreenResumed() {
    // Se verifica si ha cambiado el estado del maestro debido a acciones en el detalle y,
    // de ser el caso, se actualiza el mismo. Aquí, la única posibilidad es el borrado de
    // uno de los elementos de la lista
    if(itemToDelete != null) {
      Log.d(TAG, "calling deleteItem() method");
      getModel().deleteItem(itemToDelete);

  }}

  /**
   * Llamado por el mediador al reiniciar el maestro para actualizar su estado en función
   * de acciones realizadas por el detalle
   *
   * @param item a borrar de la lista
   */
  @Override
  public void setItemToDelete(ModelItem item) {
    itemToDelete = item;

  }

  /////////////////////////////////////////////////////////////////////////////////////
  // To Master ///////////////////////////////////////////////////////////////////////

  /**
   * Llamado por el mediador despues de haber fijado el estado inicial del maestro
   * al arrancar la app
   */
  @Override
  public void onScreenStarted() {
    Log.d(TAG, "calling onScreenStarted() method");
    // Comprobamos si debemos mostrar o no la barra de tareas en función
    // de la orientación actúal de la pantalla
    int screenOrientation = getActivityContext().getResources().getConfiguration().orientation;
    if (hideToolbar || screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
      hideToolbar = true;
    } else {
      hideToolbar = false;
    }
  }

  @Override
  public void setDatabaseValidity(boolean valid) {
    getModel().setDatabaseValidity(valid);


  }

  @Override
  public void setToolbarVisibility(boolean visible) {
    hideToolbar = !visible;


  }

  /////////////////////////////////////////////////////////////////////////////////////
  // View To Presenter ///////////////////////////////////////////////////////////////


  /**
   * Llamado al hacer click en uno de los elementos de la lista
   *
   * @param item seleccionado en la lista al hacer click
   */
  @Override
  public void onItemClicked(ModelItem item) {
    selectedItem = item;
    Log.d(TAG, "calling goToDetailScreen() method");

    // Al haber hecho click en uno de los elementos de la lista del maestro es necesario
    // arrancar el detalle pasándole el estado inicial correspondiente que, en este caso,
    // es el item seleccionado. Será el mediador quien se encargue de obtener este estado
    // desde el maestro y pasarselo al detalle
    Navigator app = (Navigator) getView().getApplication();
    app.goToDetailScreen(this);
  }


  /**
   * Llamado para restaurar el contenido inicial de la lista del maestro
   * ya que pueden haberse borrado elementos desde el detalle
   */
  @Override
  public void onRestoreActionClicked() {
    Log.d(TAG, "calling reloadItems() method");
    // Llamado para restaurar el contenido inicial de la lista y su funcionamiento es
    // semejante al encargado de cargar los datos la primera vez
    getModel().reloadItems();
  }

  /**
   * Llamado desde la vista cada vez que se reinicia el maestro.
   * Esta llamada puede hacerse por giro de pantalla o por finalización del detalle pero,
   * en cualquier caso, habrá que actualizar el contenido de la lista
   */
  @Override
  public void onResumingContent() {
    Log.d(TAG, "calling loadItems() method");
    // Si la tarea para la obtención del contenido de la lista ha finalizado,
    // el contenido estará disponible inmediatamente, sino habrá que esperar su finalización.
    // En cualquier caso, el presentador será notificado desde el modelo
    getModel().loadItems();
  }


  /////////////////////////////////////////////////////////////////////////////////////

  private void checkVisibility(){
    if(isViewRunning()) {
      if (hideToolbar) {
        getView().hideToolbar();
      }
      if (hideProgress) {
        getView().hideProgress();
      } else {
        getView().showProgress();
      }
    }
  }}


