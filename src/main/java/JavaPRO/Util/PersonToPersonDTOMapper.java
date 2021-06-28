package JavaPRO.Util;

import JavaPRO.model.Country;
import JavaPRO.model.DTO.PersonDTO;
import JavaPRO.model.DTO.TownDTO;
import JavaPRO.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonToPersonDTOMapper {

    private final ModelMapper modelMapper;

    public PersonToPersonDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Person.class, PersonDTO.class);
        modelMapper.createTypeMap(JavaPRO.model.Town.class, TownDTO.class);
        modelMapper.createTypeMap(Country.class, JavaPRO.model.DTO.CountryDTO.class);

    }

    public PersonDTO convertToDto(Person entity) {
        return modelMapper.map(entity, PersonDTO.class);
    }

}
