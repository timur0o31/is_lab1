package ru.itmo.tim.entity;

import ru.itmo.tim.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "import_operation")
public class ImportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Status status;
    private Long count;

    public long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
