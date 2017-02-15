package com.spring.seed.io.entity.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "authors")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsAuthors {

    @XmlElement(name = "author")
    private List<GoodreadsAuthor> author;
}

//<author>
//<id type="integer">1077326</id>
//<name>J.K. Rowling</name>
//</author>