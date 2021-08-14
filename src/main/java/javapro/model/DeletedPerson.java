package javapro.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@RequiredArgsConstructor
@Setter
@Getter
@Entity
public class DeletedPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Value("person_id")
    private int personId;
    private String type;

}
