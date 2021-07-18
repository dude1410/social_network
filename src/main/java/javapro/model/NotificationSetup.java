package javapro.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
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
