package org.tinyurl.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.tinyurl.entity.Tinyurl;

import java.util.List;
import java.util.Map;

//public class TinyurlDAO extends AbstractDAO<Tinyurl> {
//    public TinyurlDAO(SessionFactory sessionFactory) {
//        super(sessionFactory);
//    }
//
//    public Tinyurl findById(Long id) {
//        return get(id);
//    }
//}


public class TinyurlDAO extends BasicDAO<Tinyurl, String> {
    public TinyurlDAO(Datastore ds) {
        super(ds);
    }

    public Tinyurl getShortUrl(String longUrl) {
        Query<Tinyurl> query = this.getDatastore().createQuery(Tinyurl.class).filter("longUrl", longUrl);
        return query.get();
    }

    public List<Tinyurl> getAllEntries() {
        Query<Tinyurl> query = this.getDatastore().createQuery(Tinyurl.class);
        return query.asList();
    }

    public Tinyurl getEntity(String shortUrl) {
        return this.getDatastore().createQuery(Tinyurl.class).filter("shortUrl", shortUrl).get();
    }
}