package org.elsys.ip.courseproject.service;

import org.elsys.ip.courseproject.error.RoomAlreadyExistException;
import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.model.Room;
import org.elsys.ip.courseproject.model.RoomRepository;
import org.elsys.ip.courseproject.model.User;
import org.elsys.ip.courseproject.model.UserRepository;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
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

    public RoomDto createRoom(String name) throws RoomAlreadyExistException {
        if (roomRepository.findByName(name).isPresent()) {
            throw new RoomAlreadyExistException("Room with name " + name + " already exists.");
        }

        User admin = myself();

        Room room = new Room();
        room.setName(name);
        room.setAdmin(admin);
        room.getParticipants().add(admin);

        roomRepository.save(room);

        return convert(room);
    }

    public RoomDto getRoom(String roomId) throws RoomNotExistException {
        return convert(getRoomEntityById(roomId));
    }

    public List<RoomDto> getAllRooms() {
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
                .map(this::convert).collect(Collectors.toList());
    }

    public RoomDto addMyselfAsParticipant(String roomId) throws RoomNotExistException {
        Room room = getRoomEntityById(roomId);
        room.getParticipants().add(myself());
        roomRepository.save(room);
        return convert(room);
    }

    public RoomDto removeMyselfAsParticipant(String roomId) throws RoomNotExistException {
        Room room = getRoomEntityById(roomId);
        room.getParticipants().remove(myself());
        roomRepository.save(room);
        return convert(room);
    }

    private RoomDto convert(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId().toString());
        dto.setName(room.getName());
        dto.setParticipants(room.getParticipants().stream().map(x -> convert(x)).collect(Collectors.toList()));
        dto.setCurrentUserParticipant(room.getParticipants().contains(myself()));
        return dto;
    }

    private UserDto convert(User user) {
        UserDto dto = new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    private User myself() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        return userRepository.findByEmail(currentUserEmail);
    }

    private Room getRoomEntityById(String roomId) throws RoomNotExistException {
        Optional<Room> room = Optional.empty();
        try {
            room = roomRepository.findById(UUID.fromString(roomId));
        } catch (Exception ex) {
            //Do nothing
        }
        if (room.isEmpty()) {
            throw new RoomNotExistException("Room with id " + roomId + " doesn't exist.");
        }

        return room.get();
    }
}
