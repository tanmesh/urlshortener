package org.tinyurl.response;

import lombok.Getter;
import lombok.Setter;
import org.tinyurl.entity.Tinyurl;

@Getter
@Setter
public class TinyurlData {
    private String longUrl;

    private String shortUrl;

    private String timestamp;

    public TinyurlData() {
    }

    public TinyurlData(Tinyurl tinyurl) {
        this.longUrl = tinyurl.getLongUrl();
        this.shortUrl = tinyurl.getShortUrl();
        this.timestamp = tinyurl.getTimestamp();
    }
}
