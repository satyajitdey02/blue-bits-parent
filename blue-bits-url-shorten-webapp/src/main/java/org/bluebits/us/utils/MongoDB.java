package org.bluebits.us.utils;

import com.mongodb.*;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by satyajit on 5/18/15.
 */
public class MongoDB {

  private static final Logger LOGGER = Logger.getLogger(MongoDB.class);
  private static final MongoDB instance = new MongoDB();

  private static final String DB_HOST = "127.0.0.1";
  private static final int DB_PORT = 27017;
  private static final String DB_NAME = "gb_db";

  private static final int SOCKET_TIMEOUT = 60000;
  private static final int CONNECTION_TIMEOUT = 15000;
  private static final int MAX_CONNECTION_IDLE_TIME = 600000;

  private final Datastore datastore;

  private MongoDB() {
    MongoClientOptions mongoOptions = MongoClientOptions.builder()
        .socketTimeout(SOCKET_TIMEOUT)
        .connectTimeout(CONNECTION_TIMEOUT)
        .maxConnectionIdleTime(MAX_CONNECTION_IDLE_TIME)
        .readPreference(ReadPreference.primaryPreferred())
        .build();
    MongoClient mongoClient;
    mongoClient = new MongoClient(new ServerAddress(DB_HOST, DB_PORT), mongoOptions);

    mongoClient.setWriteConcern(WriteConcern.SAFE);

    datastore = new Morphia().createDatastore(mongoClient, DB_NAME);
    datastore.ensureIndexes();
    datastore.ensureCaps();

    System.out.println(String.format("Connection to database '%s:%s/%s' initialized", DB_HOST, DB_PORT, DB_NAME));
    LOGGER.info(String.format("Connection to database '%s:%s/%s' initialized", DB_HOST, DB_PORT, DB_NAME));
  }

  public static MongoDB getInstance() {
    return instance;
  }

  public Datastore getDatastore() {
    return datastore;
  }
}
