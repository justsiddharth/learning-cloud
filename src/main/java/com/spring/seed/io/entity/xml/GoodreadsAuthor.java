package com.spring.seed.io.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsAuthor {

    @XmlElement(name = "name")
    private String name;
}

//<author>
//<id type="integer">1077326</id>
//<name>J.K. Rowling</name>
//</author>