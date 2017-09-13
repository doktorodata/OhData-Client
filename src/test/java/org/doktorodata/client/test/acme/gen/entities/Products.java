
package org.doktorodata.client.test.acme.gen.entities;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.annotation.Generated;

import org.doktorodata.ohdata.client.entityaccess.BaseEntityTools;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

@Generated("DoktorOData - OhData-Client")
public class Products
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Products";
    private Integer ID;
    private String Name;
    private String Description;
    private GregorianCalendar ReleaseDate;
    private GregorianCalendar DiscontinuedDate;
    private Integer Rating;
    private BigDecimal Price;

    public String getEntityName() {
        return _ENTITY_NAME;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer _ID) {
        ID = _ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String _Name) {
        Name = _Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String _Description) {
        Description = _Description;
    }

    public GregorianCalendar getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(GregorianCalendar _ReleaseDate) {
        ReleaseDate = _ReleaseDate;
    }

    public GregorianCalendar getDiscontinuedDate() {
        return DiscontinuedDate;
    }

    public void setDiscontinuedDate(GregorianCalendar _DiscontinuedDate) {
        DiscontinuedDate = _DiscontinuedDate;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer _Rating) {
        Rating = _Rating;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal _Price) {
        Price = _Price;
    }

    public String getKey() {
        return BaseEntityTools.convertToString((ID));
    }

}
