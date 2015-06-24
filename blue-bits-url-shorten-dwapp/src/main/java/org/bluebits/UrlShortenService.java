package org.bluebits;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bluebits.representations.Url;
import org.bluebits.resources.UrlShortenResources;
import org.bluebits.views.MongoHealthCheck;
import org.bluebits.views.MongoManaged;

/**
 * Created by satyajit on 6/25/15.
 */
public class UrlShortenService extends Service<UrlShortenConfiguration> {

  public static void main(String[] args) throws Exception {
    new UrlShortenService().run(new String[] { "server", "D:\\practise\\vcs\\github\\blue-bits-parent\\blue-bits-url-shorten-dwapp\\src\\main\\resources\\config.yml" });
  }

  @Override
  public void initialize(Bootstrap<UrlShortenConfiguration> bootstrap) {
    bootstrap.setName("urlShorten");
    bootstrap.addBundle(new ViewBundle());
    bootstrap.addBundle(new AssetsBundle());
  }

  @Override
  public void run(UrlShortenConfiguration configuration, Environment environment) throws Exception {
    Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport);
    DB db = mongo.getDB(configuration.mongodb);

    JacksonDBCollection<Url, String> urls = JacksonDBCollection.wrap(db.getCollection("url"), Url.class, String.class);
    MongoManaged mongoManaged = new MongoManaged(mongo);
    environment.manage(mongoManaged);

    environment.addHealthCheck(new MongoHealthCheck(mongo));
    environment.addResource(new UrlShortenResources(urls));
  }
}
