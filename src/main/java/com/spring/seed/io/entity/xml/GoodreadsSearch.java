package com.spring.seed.io.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsSearch {

    @XmlElement(name = "results")
    private GoodreadsResult results;

}
