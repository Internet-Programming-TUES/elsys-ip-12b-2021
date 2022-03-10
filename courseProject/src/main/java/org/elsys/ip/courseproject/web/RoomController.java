package org.elsys.ip.courseproject.web;

import org.elsys.ip.courseproject.error.RoomAlreadyExistException;
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
        model.addAttribute("room", new RoomDto());
        model.addAttribute("rooms", roomService.getAllRooms());

        return "rooms";
    }

    @PostMapping("/rooms")
    public String createRoom(Model model, @ModelAttribute("room") @Valid RoomDto roomDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            return "rooms";
        }

        try {
            RoomDto room = roomService.createRoom(roomDto);
            return "redirect:/room?id=" + room.getId();
        } catch (RoomAlreadyExistException uaeEx) {
            bindingResult.rejectValue("name", "room", "Room with name " + roomDto.getName() + " already exists.");
            model.addAttribute("rooms", roomService.getAllRooms());
            return "rooms";
        }
    }

    @GetMapping("/room")
    public String singleRoom(WebRequest request, Model model, @RequestParam("id") String roomId ) {
        RoomDto room = null;
        try {
            room = roomService.getRoom(roomId);
        } catch (RoomNotExistException e) {
            model.addAttribute("message", "Room with id " + roomId + " doesn't exist.");
            return "error";
        }

        model.addAttribute("room", room);
        return "room";
    }

}
