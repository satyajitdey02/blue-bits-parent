package org.bluebits.us.dao;

import org.bluebits.us.entities.UrlEntity;
import org.bluebits.us.exceptions.UrlShortenDAOException;

/**
 * Created by satyajit on 5/18/15.
 */
public interface UrlDAO {

  public UrlEntity saveUrl(UrlEntity urlEntity) throws UrlShortenDAOException;

  public UrlEntity updateUrl(UrlEntity urlEntity) throws UrlShortenDAOException;

  public void deleteUrl(UrlEntity urlEntity) throws UrlShortenDAOException;

  public UrlEntity findById(Long id) throws UrlShortenDAOException;

  public UrlEntity findByLongUrl(String longUrl) throws UrlShortenDAOException;

  public UrlEntity findByBase62Code(String base62Code) throws UrlShortenDAOException;
}
