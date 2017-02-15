package com.spring.seed.io.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsSingleBook {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "isbn")
    private String isbn;

    @XmlElement(name = "isbn13")
    private String isbn13;

    @XmlElement(name = "publisher")
    private String publisher;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "num_pages")
    private String num_pages;

    @XmlElement(name = "format")
    private String format;

    @XmlElement(name = "authors")
    private GoodreadsAuthors authors;

    //    @XmlElement(name = "similar_books")
    //    private GoodreadsSimilarBooks similarBooks;

    @XmlElement(name = "image_url")
    private String image_url;

    @XmlElement(name = "small_image_url")
    private String small_image_url;


    @XmlElement(name = "publication_year")
    private int publication_year;

    @XmlElement(name = "publication_month")
    private int publication_month;

    @XmlElement(name = "publication_day")
    private int publication_day;

    @XmlElement(name = "average_rating")
    private String average_rating;
}

//<work>
//<id type="integer">4640799</id>
//<books_count type="integer">472</books_count>
//<ratings_count type="integer">4456372</ratings_count>
//<text_reviews_count type="integer">69077</text_reviews_count>
//<original_publication_year type="integer">1997</original_publication_year>
//<original_publication_month type="integer">6</original_publication_month>
//<original_publication_day type="integer">26</original_publication_day>
//<average_rating>4.43</average_rating>

//</work>
