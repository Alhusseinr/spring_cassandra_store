package com.store.store.repository;

import com.store.store.model.Item;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface ItemRepository extends CassandraRepository<Item, UUID> {

}
