package org.tinyurl.service;

import org.bson.types.ObjectId;
import org.tinyurl.dao.TinyurlDAO;
import org.tinyurl.entity.Tinyurl;
import org.tinyurl.response.TinyurlData;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TinyurlService implements ITinyurlService {
    private UrlShortener urlShortener;
    private TinyurlDAO tinyurlDAO;

    public TinyurlService(TinyurlDAO tinyurlDAO) {
        this.tinyurlDAO = tinyurlDAO;
        this.urlShortener = new UrlShortener();
    }

    @Override
    public String shorten(String longUrl) throws Exception {
        Tinyurl tinyurl = tinyurlDAO.getShortUrl(longUrl);
        if (tinyurl != null) {
            return tinyurl.getShortUrl();
        }

        String shortenUrl = getShortenUrl(longUrl);

        tinyurl = new Tinyurl();
        tinyurl.setLongUrl(longUrl);
        tinyurl.setShortUrl(shortenUrl);
        tinyurl.setTimestamp(String.valueOf(System.currentTimeMillis()));
        tinyurlDAO.save(tinyurl);

        return shortenUrl;
    }

    private String getShortenUrl(String longUrl) throws Exception {
        String alias = "";
        try {
            alias = urlShortener.hash(longUrl);
        } catch (Exception e) {
            throw new Exception("Error generating alias: " + e.getMessage());
        }
        return "http://localhost:8080/tinyurl/" + alias;
    }

    @Override
    public String get(String shortUrl) throws Exception {
        Optional<Tinyurl> matchingEntry = tinyurlDAO.getAllEntries()
                .stream()
                .filter(entry -> Objects.equals(entry.getShortUrl(), shortUrl))
                .findFirst();

        return matchingEntry.map(Tinyurl::getLongUrl)
                .orElseThrow(() -> new Exception("URL not found"));
    }

    @Override
    public void deleteUrl(String shortUrl) throws Exception {
        Tinyurl tinyurl = tinyurlDAO.getEntity(shortUrl);

        try {
            tinyurlDAO.delete(tinyurl);
        } catch (Exception e) {
            throw new Exception("Error deleting tinyurl: " + e.getMessage());
        }
    }

    @Override
    public List<TinyurlData> getAll() {
        List<Tinyurl> allUrls = tinyurlDAO.getAllEntries();

        return allUrls.stream()
                .map(TinyurlData::new)
                .collect(Collectors.toList());
    }
}
