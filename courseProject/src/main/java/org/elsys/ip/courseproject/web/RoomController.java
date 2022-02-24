package org.elsys.ip.courseproject.web;

import org.elsys.ip.courseproject.error.RoomNotExistException;
import org.elsys.ip.courseproject.error.UserAlreadyExistException;
import org.elsys.ip.courseproject.model.User;
import org.elsys.ip.courseproject.service.RoomService;
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
    private RoomService roomService;

    @GetMapping("/rooms")
    public String allRooms(WebRequest request, Model model) {
        model.addAttribute("newRoom", new RoomDto());
        model.addAttribute("rooms", roomService.getAllRooms());

        return "rooms";
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute("room") @Valid RoomDto roomDto, BindingResult bindingResult) {
        // TODO: Validation
        roomService.createRoom(roomDto);

        return "room";
    }

    @GetMapping("/room")
    public String singleRoom(WebRequest request, Model model, @RequestParam("id") String roomId ) {
        RoomDto room = null;
        try {
            room = roomService.getRoom(roomId);
        } catch (RoomNotExistException e) {
            // TODO: Validation
        }

        model.addAttribute("room", room);
        return "room";
    }

}
