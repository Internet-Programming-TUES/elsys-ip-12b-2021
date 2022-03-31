package org.elsys.ip.courseproject.web;

import org.apache.tomcat.jni.Local;
import org.elsys.ip.courseproject.error.*;
import org.elsys.ip.courseproject.model.User;
import org.elsys.ip.courseproject.service.QuestionService;
import org.elsys.ip.courseproject.service.RoomService;
import org.elsys.ip.courseproject.service.UserService;
import org.elsys.ip.courseproject.web.dto.QuestionDto;
import org.elsys.ip.courseproject.web.dto.RoomDto;
import org.elsys.ip.courseproject.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionService questionService;

    @Value("${question.time}")
    private String questionTime;

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
            RoomDto room = roomService.createRoom(roomDto.getName());
            return "redirect:/room?id=" + room.getId();
        } catch (RoomAlreadyExistException uaeEx) {
            bindingResult.rejectValue("name", "room", "Room with name " + roomDto.getName() + " already exists.");
            model.addAttribute("rooms", roomService.getAllRooms());
            return "rooms";
        }
    }

    @GetMapping("/room")
    public String singleRoom(WebRequest request, Model model, @RequestParam("id") String roomId) {
        try {
            RoomDto room = null;
            room = roomService.getRoom(roomId);

            if (room.getStartedTime() != null && room.isCurrentUserParticipant()) {
                long timeInGame = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - room.getStartedTime().toEpochSecond(ZoneOffset.UTC);
                int questionIndex = (int) (timeInGame / Integer.valueOf(questionTime));
                Optional<QuestionDto> question = questionService.getQuestionByIndex(questionIndex);

                if (question.isPresent()) {
                    model.addAttribute("question", question.get());
                    model.addAttribute("room", room);
                    return "question";
                }

                model.addAttribute("room", room);
                return "summary";
            } else {
                model.addAttribute("room", room);
                return "room";
            }
        } catch (BaseException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/room")
    public String joinRoom(Model model, @RequestParam("id") String roomId, @RequestParam("join") boolean join) {
        try {
            if (join) {
                roomService.addMyselfAsParticipant(roomId);
            } else {
                roomService.removeMyselfAsParticipant(roomId);
            }
        } catch (RoomNotExistException e) {
            model.addAttribute("message", "Room with id " + roomId + " doesn't exist.");
            return "error";
        }

        return "redirect:/room?id=" + roomId;
    }

    @PostMapping("/room/start")
    public String startGame(Model model, @RequestParam("id") String roomId) {

        try {
            roomService.startGame(roomId);
        } catch (BaseException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        return "redirect:/room?id=" + roomId;
    }

    @PostMapping("/room/answer")
    public String answer(Model model, @RequestParam("roomId") String roomId, @RequestParam("questionId") String questionId, @RequestParam("answerId") String answerId) {

        try {
            roomService.answer(roomId, questionId, answerId);
        } catch (BaseException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }

        return "redirect:/room?id=" + roomId;
    }

}
