package org.elsys.ip.courseproject.service;

import org.assertj.core.util.Arrays;
import org.elsys.ip.courseproject.error.RoomAlreadyExistException;
import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.error.UserAlreadyExistException;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    private UserDto myself;
    private UserDto secondUser;

    private Authentication authenticationMock;

    @Test
    public void createRoom() throws Exception {
        RoomDto newRoom = roomService.createRoom("New room");

        assertThat(newRoom.getId()).isNotEmpty();
        assertThat(newRoom.getName()).isEqualTo("New room");
    }

    @Test
    public void createRoomExistingName() throws Exception {
        roomService.createRoom("New room");
        assertThatThrownBy(() -> roomService.createRoom("New room"))
                .isInstanceOf(RoomAlreadyExistException.class);
    }

    @Test
    public void getRoomById() throws Exception {
        RoomDto room = roomService.createRoom("New room");

        RoomDto roomById = roomService.getRoom(room.getId());

        assertThat(roomById).isEqualTo(room);
    }

    @Test
    public void getAllRooms() throws Exception {
        assertThat(roomService.getAllRooms()).hasSize(0);

        RoomDto room1 = roomService.createRoom("New room");

        assertThat(roomService.getAllRooms()).hasSize(1);
        assertThat(roomService.getAllRooms().get(0)).isEqualTo(room1);

        RoomDto room2 = roomService.createRoom("New room 2");

        assertThat(roomService.getAllRooms()).hasSize(2);
        assertThat(new HashSet<>(roomService.getAllRooms())
                .containsAll(List.of(room1, room2))).isTrue();
    }

    @Test
    public void getInvalidRoom() throws Exception {
        assertThatThrownBy(() -> roomService.getRoom(UUID.randomUUID().toString()))
                .isInstanceOf(RoomNotExistException.class);

        assertThatThrownBy(() -> roomService.getRoom("InvalidId"))
                .isInstanceOf(RoomNotExistException.class);
    }

    @Test
    public void joinMyselfRoom() throws RoomAlreadyExistException, RoomNotExistException {
        RoomDto room = roomService.createRoom("New room");

        assertThat(room.getParticipants()).hasSize(1);
        assertThat(room.getParticipants().get(0)).isEqualTo(myself);

        RoomDto updatedRoom = roomService.addMyselfAsParticipant(room.getId());

        assertThat(updatedRoom.getParticipants()).hasSize(1);
        assertThat(updatedRoom.getParticipants().get(0)).isEqualTo(myself);

        RoomDto roomById = roomService.getRoom(room.getId());
        assertThat(roomById.getParticipants()).isEqualTo(updatedRoom.getParticipants());
    }

    @Test
    public void leaveJoinRoom() throws RoomAlreadyExistException, RoomNotExistException {
        RoomDto room = roomService.createRoom("New room");
        RoomDto updatedRoom = roomService.removeMyselfAsParticipant(room.getId());
        assertThat(updatedRoom.getParticipants()).hasSize(0);

        RoomDto roomById = roomService.getRoom(room.getId());
        assertThat(roomById.getParticipants()).isEqualTo(updatedRoom.getParticipants());

        RoomDto updatedRoom2 = roomService.addMyselfAsParticipant(room.getId());
        assertThat(updatedRoom2.getParticipants()).hasSize(1);
        assertThat(updatedRoom2.getParticipants().get(0)).isEqualTo(myself);
    }

    @Test
    public void joinAnotherUser() throws RoomAlreadyExistException, RoomNotExistException {
        RoomDto room = roomService.createRoom("New room");

        //Login as another user
        Mockito.when(authenticationMock.getName()).thenReturn("second@admin.com");
        RoomDto updatedRoom = roomService.addMyselfAsParticipant(room.getId());
        assertThat(updatedRoom.getParticipants()).hasSize(2);
        assertThat(updatedRoom.getParticipants().containsAll(List.of(myself, secondUser))).isTrue();

        Mockito.when(authenticationMock.getName()).thenReturn("admin@admin.com");
        RoomDto updatedRoom2 = roomService.removeMyselfAsParticipant(room.getId());
        assertThat(updatedRoom2.getParticipants()).hasSize(1);
        assertThat(updatedRoom2.getParticipants().get(0)).isEqualTo(secondUser);
    }

    @BeforeEach
    public void setUp() throws UserAlreadyExistException {
        myself = new UserDto();
        myself.setFirstName("Admin");
        myself.setLastName("Admin");
        myself.setEmail("admin@admin.com");
        myself.setPassword("password");
        userService.registerNewUserAccount(myself);

        secondUser = new UserDto();
        secondUser.setFirstName("Second");
        secondUser.setLastName("User");
        secondUser.setEmail("second@admin.com");
        secondUser.setPassword("password");
        userService.registerNewUserAccount(secondUser);

        authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getName()).thenReturn("admin@admin.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContext);
    }
}
