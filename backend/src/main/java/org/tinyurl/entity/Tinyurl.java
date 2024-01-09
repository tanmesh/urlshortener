package org.tinyurl.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

@Getter
@Setter
public class Tinyurl {
    @Id
    private ObjectId id;

    private String longUrl;

    private String shortUrl;

    private String timestamp;
}
