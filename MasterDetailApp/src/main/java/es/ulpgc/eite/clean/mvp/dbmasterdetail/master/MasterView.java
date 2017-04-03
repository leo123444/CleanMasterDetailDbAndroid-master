package es.ulpgc.eite.clean.mvp.dbmasterdetail.master;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.R;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.app.ModelItem;
import es.ulpgc.eite.clean.mvp.dbmasterdetail.utils.RealmRecyclerViewAdapter;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

/**
 * An activity representing a list of Items.
 */
public class MasterView
    extends GenericActivity<Master.PresenterToView, Master.ViewToPresenter, MasterPresenter>
    implements Master.PresenterToView {

  private Toolbar toolbar;
  private RecyclerView recyclerView;
  private ProgressBar progressView;
 //private ListView list;

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setContentView(R.layout.activity_item_list);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionbar = getSupportActionBar();
    if (actionbar != null) {
      actionbar.setTitle(getString(R.string.title_item_list));
    }

    progressView = (ProgressBar) findViewById(R.id.progress_circle);
    recyclerView = (RecyclerView) findViewById(R.id.item_list);
    //recyclerView.setAdapter(new ModelItemRecyclerViewAdapter());
    Realm realm = Realm.getDefaultInstance();
    recyclerView.setAdapter(
        new ModelItemRecyclerViewAdapter(realm.where(ModelItem.class).findAllAsync()));
//    list=(ListView)findViewById(R.id.list);
//    List<ModelItem> content;
//ModelItem model1= new ModelItem("1","0","2");
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    content.add(model1);
//    ArrayAdapter arrayad= new ArrayAdapter(getActivityContext(),android.R.layout.simple_list_item_1,content);
//    list.setAdapter(arrayad);
  }

  /**
   * Method that initialized MVP objects
   * {@link super#onResume(Class, Object)} should always be called
   */
  @Override
  protected void onResume() {
    super.onResume(MasterPresenter.class, this);
    getPresenter().onResumingContent();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_master, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_restore) {
      getPresenter().onRestoreActionClicked();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  /////////////////////////////////////////////////////////////////////////////////////
  // Presenter To View ///////////////////////////////////////////////////////////////


  @Override
  public void hideProgress() {
    progressView.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideToolbar() {
    toolbar.setVisibility(View.GONE);
  }


  @Override
  public void showError(String msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
  }

  @Override
  public void showProgress() {
    progressView.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);
  }

  @Override
  public void setRecyclerAdapterContent(List<ModelItem> items) {
    if(recyclerView != null) {
      ModelItemRecyclerViewAdapter recyclerAdapter =
          (ModelItemRecyclerViewAdapter) recyclerView.getAdapter();
      recyclerAdapter.setItemList(items);
    }
  }


  /////////////////////////////////////////////////////////////////////////////////////




  public class ModelItemRecyclerViewAdapter
      extends RealmRecyclerViewAdapter<ModelItem, ModelItemRecyclerViewAdapter.ViewHolder> {

    private List<ModelItem> items= new List<ModelItem>() {
      @Override
      public int size() {
        return 0;
      }

      @Override
      public boolean isEmpty() {
        return false;
      }

      @Override
      public boolean contains(Object o) {
        return false;
      }

      @NonNull
      @Override
      public Iterator<ModelItem> iterator() {
        return null;
      }

      @NonNull
      @Override
      public Object[] toArray() {
        return new Object[0];
      }

      @NonNull
      @Override
      public <T> T[] toArray(@NonNull T[] a) {
        return null;
      }

      @Override
      public boolean add(ModelItem modelItem) {
        return false;
      }

      @Override
      public boolean remove(Object o) {
        return false;
      }

      @Override
      public boolean containsAll(@NonNull Collection<?> c) {
        return false;
      }

      @Override
      public boolean addAll(@NonNull Collection<? extends ModelItem> c) {
        return false;
      }

      @Override
      public boolean addAll(int index, @NonNull Collection<? extends ModelItem> c) {
        return false;
      }

      @Override
      public boolean removeAll(@NonNull Collection<?> c) {
        return false;
      }

      @Override
      public boolean retainAll(@NonNull Collection<?> c) {
        return false;
      }

      @Override
      public void clear() {

      }

      @Override
      public ModelItem get(int index) {
        return null;
      }

      @Override
      public ModelItem set(int index, ModelItem element) {
        return null;
      }

      @Override
      public void add(int index, ModelItem element) {

      }

      @Override
      public ModelItem remove(int index) {
        return null;
      }

      @Override
      public int indexOf(Object o) {
        return 0;
      }

      @Override
      public int lastIndexOf(Object o) {
        return 0;
      }

      @Override
      public ListIterator<ModelItem> listIterator() {
        return null;
      }

      @NonNull
      @Override
      public ListIterator<ModelItem> listIterator(int index) {
        return null;
      }

      @NonNull
      @Override
      public List<ModelItem> subList(int fromIndex, int toIndex) {
        return null;
      }
    };

    public ModelItemRecyclerViewAdapter( OrderedRealmCollection<ModelItem> items) {
      super(items, true);


    }


    public void setItemList(List<ModelItem> items) {

//      ModelItem modelItem= new ModelItem("0","SO","Windows");
//      ModelItem modelItem1= new ModelItem("1","SO","Ubuntu");
//      items.add(modelItem);
//      items.add(modelItem1);
      this.items=items;
      notifyDataSetChanged();
    }


    @Override
    public ModelItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_list_content, parent, false);
      return new ModelItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ModelItemRecyclerViewAdapter.ViewHolder holder, int position) {
      holder.item = items.get(position);
      holder.item = getData().get(position);
      holder.contentView.setText(items.get(position).getContent());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getPresenter().onItemClicked(holder.item);
        }
      });
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public final View itemView;
      public final TextView contentView;
      public ModelItem item;

      public ViewHolder(View view) {
        super(view);
        itemView = view;
        contentView = (TextView) view.findViewById(R.id.item_content);
      }

      @Override
      public String toString() {
        return super.toString() + " '" + contentView.getText() + "'";
      }
    }
  }

  /*
  private class ModelItemRecyclerViewAdapter
      extends RecyclerView.Adapter<ModelItemRecyclerViewAdapter.ViewHolder> {

    private List<ModelItem> items;

    public ModelItemRecyclerViewAdapter() {
      items = new ArrayList<>();
    }

    public void setItemList(List<ModelItem> items) {
      this.items = items;
      notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.item = items.get(position);
      holder.contentView.setText(items.get(position).getContent());
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getPresenter().onItemClicked(holder.item);
        }
      });
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public final View itemView;
      public final TextView contentView;
      public ModelItem item;

      public ViewHolder(View view) {
        super(view);
        itemView = view;
        contentView = (TextView) view.findViewById(R.id.item_content);
      }

      @Override
      public String toString() {
        return super.toString() + " '" + contentView.getText() + "'";
      }
    }
  }
  */

}
