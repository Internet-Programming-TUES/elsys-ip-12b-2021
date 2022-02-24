package org.elsys.ip.courseproject.service;

import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.model.Room;
import org.elsys.ip.courseproject.model.RoomRepository;
import org.elsys.ip.courseproject.model.User;
import org.elsys.ip.courseproject.model.UserRepository;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    public RoomDto createRoom(RoomDto roomDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User admin = userRepository.findByEmail(currentUserEmail);

        //TODO: Check if name is unique

        Room room = new Room();
        room.setName(roomDto.getName());
        room.setAdmin(admin);

        roomRepository.save(room);

        roomDto.setId(room.getId().toString());
        return roomDto;
    }

    public RoomDto getRoom(String roomId) throws RoomNotExistException {
        Optional<Room> room = roomRepository.findById(UUID.fromString(roomId));
        if (room.isEmpty()) {
            throw new RoomNotExistException("Room with id " + roomId + " doesn't exist.");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomId);
        roomDto.setName(room.get().getName());

        return roomDto;
    }

    public List<RoomDto> getAllRooms() {
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
                .map(this::convert).collect(Collectors.toList());
    }

    private RoomDto convert(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId().toString());
        dto.setName(room.getName());
        return dto;
    }
}
