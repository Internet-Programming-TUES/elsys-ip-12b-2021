package org.elsys.ip.courseproject.web;

import org.elsys.ip.courseproject.error.UserAlreadyExistException;
import org.elsys.ip.courseproject.model.User;
import org.elsys.ip.courseproject.service.UserService;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class RoomController {
    @Autowired
    private UserService userService;

    @GetMapping("/rooms")
    public String allRooms(WebRequest request, Model model) {
        model.addAttribute("newRoom", new RoomDto());
        model.addAttribute("rooms", new ArrayList<RoomDto>());

        return "rooms";
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute("room") @Valid RoomDto roomDto, BindingResult bindingResult) {
        return "room";
    }

    @GetMapping("/room")
    public String singleRoom(WebRequest request, Model model, @RequestParam("id") String roomId ) {
        RoomDto roomDto = new RoomDto();
        roomDto.setName("Generated");
        roomDto.setId(roomId);
        model.addAttribute("room", roomDto);
        return "room";
    }

}
