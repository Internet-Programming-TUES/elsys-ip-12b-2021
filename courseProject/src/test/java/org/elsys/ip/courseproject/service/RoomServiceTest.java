package org.elsys.ip.courseproject.service;

import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.error.UserAlreadyExistException;
import org.elsys.ip.courseproject.model.Answer;
import org.elsys.ip.courseproject.model.Question;
import org.elsys.ip.courseproject.model.QuestionRepository;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Test
    public void createRoom() throws Exception {
        RoomDto newRoom = roomService.createRoom("New room");

        assertThat(newRoom.getId()).isNotEmpty();
        assertThat(newRoom.getName()).isEqualTo("New room");
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
    }

    //TODO: Test unique name
    //TODO: Test invalid room id

    @BeforeEach
    public void setUp() throws UserAlreadyExistException {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Admin");
        userDto.setLastName("Admin");
        userDto.setEmail("admin@admin.com");
        userDto.setPassword("password");
        userService.registerNewUserAccount(userDto);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("admin@admin.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
