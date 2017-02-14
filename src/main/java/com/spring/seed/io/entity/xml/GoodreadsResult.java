package com.spring.seed.io.entity.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GoodreadsResult {

    @XmlElement(name = "work")
    List<GoodreadsBook> books;

}
