package org.elsys.ip.courseproject.service;

import org.elsys.ip.courseproject.error.RoomAlreadyExistException;
import org.elsys.ip.courseproject.error.RoomAlreadyStartedException;
import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.model.*;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
    private QuestionRepository questionRepository;

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

    public void startGame(String roomId) throws RoomNotExistException, RoomAlreadyStartedException {
        Room room = getRoomEntityById(roomId);
        if (room.getStartedTime() != null) {
            throw new RoomAlreadyStartedException("Room with id " + roomId + " is already started.");
        }

        room.setStartedTime(LocalDateTime.now());
        roomRepository.save(room);
    }

    public void answer(String roomId, String questionId, String answerId) throws RoomNotExistException {
        Room room = getRoomEntityById(roomId);
        //TODO: Add check for missing question or answer
        Question question = questionRepository.findById(UUID.fromString(questionId)).get();
        Answer answer = question.getAnswers().stream().filter(a -> a.getId().toString().equals(answerId)).findFirst().get();
        User user = myself();

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestion(question);
        userAnswer.setAnswer(answer);
        userAnswer.setUser(user);

        List<UserAnswer> previousAttempts = room.getUserAnswers().stream().filter(a -> a.getUser().getId().equals(user.getId()) && a.getQuestion().getId().equals(question.getId())).collect(Collectors.toList());
        room.getUserAnswers().removeAll(previousAttempts);
        room.getUserAnswers().add(userAnswer);
        roomRepository.save(room);
    }

    private RoomDto convert(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId().toString());
        dto.setName(room.getName());
        dto.setOwner(convert(room.getAdmin(), room));
        dto.setParticipants(room.getParticipants().stream().map(x -> convert(x, room)).collect(Collectors.toList()));
        dto.setCurrentUserParticipant(room.getParticipants().contains(myself()));
        dto.setCurrentUserOwner(room.getAdmin().equals(myself()));
        dto.setStartedTime(room.getStartedTime());
        return dto;
    }

    private UserDto convert(User user, Room room) {
        UserDto dto = new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        long score = room.getUserAnswers().stream().filter(a -> a.getUser().getId().equals(user.getId())).filter(a -> a.getAnswer().isCorrect()).count();
        dto.setScore((int) score);
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
