package JavaPRO.Util;

import JavaPRO.model.Country;
import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import JavaPRO.model.DTO.TownDTO;
import JavaPRO.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonToDtoMapper {

    private final ModelMapper modelMapper;


    public PersonToDtoMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Person.class, AuthorizedPerson.class);
        modelMapper.createTypeMap(JavaPRO.model.Town.class, TownDTO.class);
        modelMapper.createTypeMap(Country.class, JavaPRO.model.DTO.CountryDTO.class);


    }

    public AuthorizedPerson convertToDto(Person entity) {
        return modelMapper.map(entity, AuthorizedPerson.class);
    }

}
