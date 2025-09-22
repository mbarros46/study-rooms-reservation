package br.com.fiap.epictaskg.room;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        model.addAttribute("room", new Room());  // Adicionar objeto room vazio para o formulário
        return "rooms/list";
    }

    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("room", new Room());
        return "rooms/form";
    }

    @PostMapping
    public String create(@ModelAttribute Room room) {
        if (room.getActive() == null) room.setActive(Boolean.TRUE);
        roomRepository.save(room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomRepository.findById(id).orElseThrow());
        return "rooms/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Room form) {
        Room r = roomRepository.findById(id).orElseThrow();
        r.setName(form.getName());
        r.setCapacity(form.getCapacity());
        r.setLocation(form.getLocation());
        r.setActive(form.getActive() == null ? Boolean.TRUE : form.getActive());
        roomRepository.save(r);
        return "redirect:/rooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        roomRepository.deleteById(id);
        return "redirect:/rooms";
    }
}
