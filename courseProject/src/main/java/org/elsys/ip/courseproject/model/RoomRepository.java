package org.elsys.ip.courseproject.model;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RoomRepository extends CrudRepository<Room, UUID> {
}
