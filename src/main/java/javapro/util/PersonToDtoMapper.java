package javapro.util;

import javapro.model.Country;
import javapro.model.dto.auth.AuthorizedPerson;
import javapro.model.dto.TownDTO;
import javapro.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonToDtoMapper {

    private final ModelMapper modelMapper;


    public PersonToDtoMapper() {

        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Person.class, AuthorizedPerson.class);
        modelMapper.createTypeMap(javapro.model.Town.class, TownDTO.class);
        modelMapper.createTypeMap(Country.class, javapro.model.dto.CountryDTO.class);


    }

    public AuthorizedPerson convertToDto(Person entity) {
        return modelMapper.map(entity, AuthorizedPerson.class);
    }

}
