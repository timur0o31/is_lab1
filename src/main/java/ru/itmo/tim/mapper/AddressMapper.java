package ru.itmo.tim.mapper;

import ru.itmo.tim.entity.Address;
import ru.itmo.tim.requestDto.AddressRequestDto;
import ru.itmo.tim.responseDto.AddressResponseDto;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressMapper {
    public AddressMapper() {
    }

    public AddressResponseDto toResponseDto(Address address) {
        if (address == null) {
            return null;
        }
        AddressResponseDto dto = new AddressResponseDto();
        dto.setStreet(address.getStreet());
        dto.setZipCode(address.getZipCode());
        return dto;
    }

    public Address toCreateEntity(AddressRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setZipCode(dto.getZipCode());
        return address;
    }

    public void toUpdateEntity(Address entity, AddressRequestDto dto) {
        if (dto == null || entity == null) return;
        entity.setStreet(dto.getStreet());
        entity.setZipCode(dto.getZipCode());
    }
}
