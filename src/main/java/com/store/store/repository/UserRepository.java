package com.store.store.repository;

import com.store.store.model.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CassandraRepository<User, UUID> {
    @AllowFiltering
    User findByEmail(String email);
}
