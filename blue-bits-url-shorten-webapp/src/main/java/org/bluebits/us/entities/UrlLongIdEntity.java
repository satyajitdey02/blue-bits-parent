package org.bluebits.us.entities;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.utils.LongIdEntity;

/**
 * Created by satyajit on 5/17/15.
 */
public class UrlLongIdEntity extends LongIdEntity {
  public UrlLongIdEntity(Datastore ds) {
    super(ds);
  }
}
