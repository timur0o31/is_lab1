package ru.itmo.tim.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.tim.enums.Status;

import javax.persistence.*;
@Entity
@Table(name = "import_operation")
@Setter
@Getter
@NoArgsConstructor
public class ImportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private Long count;
    @Column(length=1000)
    private String message;
    @Column(name="file_key")
    private String fileKey;
    @Column(name="file_name")
    private String fileName;
}
