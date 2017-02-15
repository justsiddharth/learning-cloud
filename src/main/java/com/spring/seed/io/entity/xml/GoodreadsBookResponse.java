package com.spring.seed.io.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "GoodreadsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsBookResponse {
    @XmlElement(name = "book")
    private GoodreadsSingleBook singleBook;
}