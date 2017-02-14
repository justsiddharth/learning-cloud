package com.spring.seed.io.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "best_book")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsBestBook {

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "author")
    private GoodreadsAuthor author;

    @XmlElement(name = "image_url")
    private String image_url;

    @XmlElement(name = "small_image_url")
    private String small_image_url;

}

//<best_book type="Book">
//<id type="integer">3</id>
//<title>Harry Potter and the Sorcerer's Stone (Harry Potter, #1)</title>
//<image_url>https://images.gr-assets.com/books/1474154022m/3.jpg</image_url>
//<small_image_url>https://images.gr-assets.com/books/1474154022s/3.jpg</small_image_url>
//</best_book>