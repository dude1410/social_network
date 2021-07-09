package javapro.util;

import javapro.model.Person;
import javapro.model.dto.auth.AuthorizedPerson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;

@SpringBootTest(classes = {PersonToDtoMapper.class})
class PersonToDtoMapperTest {

    private final PersonToDtoMapper testableMapper;

    @Autowired
    public PersonToDtoMapperTest(PersonToDtoMapper testableMapper) {
        this.testableMapper = testableMapper;
    }

    @Test
    void testConvertToDto() {
        assertThat(testableMapper.convertToDto(new Person()), isA(AuthorizedPerson.class));
    }
}
