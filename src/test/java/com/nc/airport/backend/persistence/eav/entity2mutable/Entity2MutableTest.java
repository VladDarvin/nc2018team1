package com.nc.airport.backend.persistence.eav.entity2mutable;

import com.nc.airport.backend.model.BaseEntity;
import com.nc.airport.backend.persistence.eav.Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.builder.impl.DefaultEntityBuilder;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.ValidNoFieldsEntity;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.datetime.ValidDateTimeEntity;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.enumfield.ValidEnumEntity;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.reference.ValidReferenceEntity;
import com.nc.airport.backend.persistence.eav.entity2mutable.entity.value.ValidValueEntity;
import com.nc.airport.backend.persistence.eav.entity2mutable.impl.DefaultEntity2Mutable;
import com.nc.airport.backend.persistence.eav.entity2mutable.parser.impl.DefaultEntityParser;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

@Log4j2
public class Entity2MutableTest {
    private Entity2Mutable e2m = new DefaultEntity2Mutable(new DefaultEntityParser(), new DefaultEntityBuilder());

    private <T extends BaseEntity> void givenValidEntity_whenConverted2MutAndBack_thenEqual(T expectedBE){
        log.info("Expected entity : {}", expectedBE);

        Mutable mutable = e2m.convertEntityToMutable(expectedBE);
        log.info("Converted mutable : {}", mutable);

        BaseEntity actualBE = e2m.convertMutableToEntity(mutable, expectedBE.getClass());
        log.info("Converted entity : {}", actualBE);

        Assert.assertEquals(expectedBE, actualBE);
    }

    @Test
    public void givenValidNoFieldsEnt_whenConverted_thenEqual(){
        givenValidEntity_whenConverted2MutAndBack_thenEqual(new ValidNoFieldsEntity());
    }

//    @Ignore
    @Test
    public void givenValidDateTimeEnt_whenConverted_thenEqual(){
        // TODO: 18.12.2018 make scan include superclass
        givenValidEntity_whenConverted2MutAndBack_thenEqual(new ValidDateTimeEntity());
    }

//    @Ignore
    @Test
    public void givenValidEnumEnt_whenConverted_thenEqual(){
        // TODO: 18.12.2018 implement enum conversion and enable test
        givenValidEntity_whenConverted2MutAndBack_thenEqual(new ValidEnumEntity());
    }

//    @Ignore
    @Test
    public void givenValidReferenceEnt_whenConverted_thenEqual(){
        // TODO: 18.12.2018 make scan include superclass
        givenValidEntity_whenConverted2MutAndBack_thenEqual(new ValidReferenceEntity());
    }

//    @Ignore
    @Test
    public void givenValidValueEnt_whenConverted_thenEqual(){
        // TODO: 18.12.2018 make scan include superclass
        givenValidEntity_whenConverted2MutAndBack_thenEqual(new ValidValueEntity());
    }

}
