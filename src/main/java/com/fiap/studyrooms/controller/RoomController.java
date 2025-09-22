package com.fiap.studyrooms.controller;

import com.fiap.studyrooms.model.Room;
import com.fiap.studyrooms.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

  private final RoomRepository repo;

  public RoomController(RoomRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("rooms", repo.findAll());
    model.addAttribute("room", new Room());
    return "rooms/list";
  }

  @PostMapping
  public String create(@ModelAttribute("room") @Valid Room room, BindingResult br, Model model) {
    if (br.hasErrors()) {
      model.addAttribute("rooms", repo.findAll());
      return "rooms/list";
    }
    repo.save(room);
    return "redirect:/rooms";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    repo.deleteById(id);
    return "redirect:/rooms";
  }
}