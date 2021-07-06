package javapro.Util;

import javapro.model.Country;
import javapro.model.dto.PersonDTO;
import javapro.model.dto.TownDTO;
import javapro.model.Person;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonToPersonDTOMapper {

    private final ModelMapper modelMapper;

    public PersonToPersonDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Person.class, PersonDTO.class);
        modelMapper.createTypeMap(javapro.model.Town.class, TownDTO.class);
        modelMapper.createTypeMap(Country.class, javapro.model.dto.CountryDTO.class);

    }

    public PersonDTO convertToDto(Person entity) {
        return modelMapper.map(entity, PersonDTO.class);
    }

}
