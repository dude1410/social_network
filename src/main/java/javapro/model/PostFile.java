package javapro.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "post_file")
public class PostFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "FK_post_id"))
    private Post post;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false, length = 1000)
    private String path;


}
