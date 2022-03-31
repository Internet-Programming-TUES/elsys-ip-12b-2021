package org.elsys.ip.courseproject.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class RoomDto {
    private String id;

    @NotNull
    @NotEmpty
    @Size(min=5, message = "{label.rooms.name.minSize.validation}")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private UserDto owner;

    private List<UserDto> participants;

    public List<UserDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserDto> participants) {
        this.participants = participants;
    }

    private boolean isCurrentUserParticipant;

    public boolean isCurrentUserParticipant() {
        return isCurrentUserParticipant;
    }

    public void setCurrentUserParticipant(boolean currentUserParticipant) {
        isCurrentUserParticipant = currentUserParticipant;
    }

    private boolean isCurrentUserOwner;

    public boolean isCurrentUserOwner() {
        return isCurrentUserOwner;
    }

    public void setCurrentUserOwner(boolean currentUserOwner) {
        isCurrentUserOwner = currentUserOwner;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return Objects.equals(id, roomDto.id) && Objects.equals(name, roomDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
