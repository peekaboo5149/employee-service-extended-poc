package com.deloitte.employees.common.mapper;

public interface DataMapper<DOMAIN, ENTITY> {

    ENTITY toEntity(DOMAIN domain);

    DOMAIN toDomain(ENTITY entity);

}
