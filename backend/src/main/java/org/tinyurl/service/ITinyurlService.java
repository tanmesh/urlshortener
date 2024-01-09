package org.tinyurl.service;

import org.tinyurl.response.TinyurlData;

import java.util.List;

public interface ITinyurlService {
    String shorten(String longUrl) throws Exception;

    String get(String shortUrl) throws Exception;

    void deleteUrl(String shortUrl) throws Exception;

    List<TinyurlData> getAll();
}
