package ru.itmo.tim.responseDto;

import ru.itmo.tim.enums.Status;

public class ImportOperationResponseDto {
    private Long id;
    private Status status;
    private Long count;

    public ImportOperationResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
