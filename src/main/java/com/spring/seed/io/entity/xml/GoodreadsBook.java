package com.spring.seed.io.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "work")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsBook {

    @XmlElement(name = "original_publication_year")
    private int publication_year;

    @XmlElement(name = "original_publication_month")
    private int publication_month;

    @XmlElement(name = "original_publication_day")
    private int publication_day;

    @XmlElement(name = "average_rating")
    private String average_rating;

    @XmlElement(name = "best_book")
    private GoodreadsBestBook bestBook;
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
