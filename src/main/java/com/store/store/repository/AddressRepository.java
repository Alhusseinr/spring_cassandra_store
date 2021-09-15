package com.store.store.repository;

import com.store.store.model.Address;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface AddressRepository extends CassandraRepository<Address, UUID> {
    @AllowFiltering
    Address findAddressByUserId(UUID userId);
}
