package org.bluebits.us.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.bluebits.us.dao.UrlDAO;
import org.bluebits.us.entities.UrlEntity;
import org.bluebits.us.entities.UrlLongIdEntity;
import org.bluebits.us.exceptions.UrlShortenDAOException;
import org.bluebits.us.utils.Base62Codec;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by satyajit on 5/18/15.
 */
public class UrlDAOImpl extends BasicDAO<UrlEntity, Long> implements UrlDAO {

  public UrlDAOImpl(Datastore ds) {
    super(ds);
  }

  @Override
  public UrlEntity saveUrl(UrlEntity urlEntity) throws UrlShortenDAOException {
    if (exists("url", urlEntity.getUrl())) {
      return findByLongUrl(urlEntity.getUrl());
    }

    UrlLongIdEntity urlLongIdEntity = new UrlLongIdEntity(getDs());
    getDs().save(urlLongIdEntity);

    Long longId = urlLongIdEntity.getMyLongId();
    urlEntity.setId(longId);
    urlEntity.setBase62Code(Base62Codec.encode(longId.intValue()));

    getDs().save(urlEntity);
    return urlEntity;
  }

  @Override
  public UrlEntity updateUrl(UrlEntity urlEntity) throws UrlShortenDAOException {
    if (urlEntity == null || urlEntity.getId() == null) {
      throw new UrlShortenDAOException(String.format("Update failed. URL: %s is not shortened before", urlEntity.getUrl()));
    }

    getDs().save(urlEntity);
    return urlEntity;
  }

  @Override
  public void deleteUrl(UrlEntity urlEntity) throws UrlShortenDAOException {
    if (urlEntity == null || urlEntity.getId() == null) {
      throw new UrlShortenDAOException(String.format("Delete failed. URL: %s is not shortened before", urlEntity.getUrl()));
    }

    getDs().delete(urlEntity);
  }

  @Override
  public UrlEntity findById(Long id) throws UrlShortenDAOException {
    if (id == null) {
      throw new UrlShortenDAOException("id can not be null");
    }

    UrlEntity urlEntity = findOne("_id", id);
    return urlEntity;
  }

  @Override
  public UrlEntity findByLongUrl(String longUrl) throws UrlShortenDAOException {
    if (StringUtils.isBlank(longUrl)) {
      throw new UrlShortenDAOException("URL can not be null/empty");
    }

    UrlEntity urlEntity = findOne("url", longUrl);
    return urlEntity;
  }

  @Override
  public UrlEntity findByBase62Code(String base62Code) throws UrlShortenDAOException {
    if (base62Code == null) {
      throw new UrlShortenDAOException("id can not be null");
    }

    UrlEntity urlEntity = findOne("base62Code", base62Code);
    return urlEntity;
  }
}
