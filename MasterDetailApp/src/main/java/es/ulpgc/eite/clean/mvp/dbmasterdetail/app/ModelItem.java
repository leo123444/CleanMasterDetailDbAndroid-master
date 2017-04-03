package es.ulpgc.eite.clean.mvp.dbmasterdetail.app;

import io.realm.RealmObject;

/**
 * Created by Luis on 19/11/16.
 */

public class ModelItem extends RealmObject{

  private String id;
  private String content;
  private String details;

  public ModelItem() {
    this.id = "0";
    this.content = "SO";
    this.details = "descripcion";
  }

  public ModelItem(String id, String content, String details) {
    this.id = id;
    this.content = content;
    this.details = details;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return content;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ModelItem){
      ModelItem item = (ModelItem) obj;
      if(item.getId() == getId()){
        return true;
      }
    }
    return false;
  }
}
