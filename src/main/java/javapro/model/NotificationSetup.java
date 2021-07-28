package javapro.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "notification_setup")
public class NotificationSetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "notification_type", nullable = false)
    private String notificationtype;

    @Column(name = "enable", nullable = false)
    private Boolean enable;

    @Column(name = "person_id", nullable = false)
    private int personId;
}
