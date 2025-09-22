package br.com.fiap.epictaskg.reservation;

import br.com.fiap.epictaskg.room.RoomRepository;
import br.com.fiap.epictaskg.user.User;
import br.com.fiap.epictaskg.user.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 ReservationService reservationService,
                                 RoomRepository roomRepository,
                                 UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/mine")
    public String mine(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String email = (String) principal.getAttributes().getOrDefault("email", "unknown@example.com");
        
        // Se o email for null no GitHub, usar o login como fallback
        if (email == null || "unknown@example.com".equals(email)) {
            Object login = principal.getAttributes().get("login");
            if (login != null) {
                email = login + "@users.noreply.github.com";
            }
        }
        
        final String finalEmail = email;
        User user = userRepository.findByEmail(finalEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + finalEmail));
        model.addAttribute("reservations",
                reservationRepository.findAll().stream().filter(r -> r.getUser().getId().equals(user.getId())).toList());
        return "reservations/mine";
    }

    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        return "reservations/form";
    }

    @PostMapping
    public String create(@AuthenticationPrincipal OAuth2User principal,
                         @RequestParam Long roomId,
                         @RequestParam String startAtIso,
                         @RequestParam String endAtIso) {
        String email = (String) principal.getAttributes().getOrDefault("email", "unknown@example.com");
        
        // Se o email for null no GitHub, usar o login como fallback
        if (email == null || "unknown@example.com".equals(email)) {
            Object login = principal.getAttributes().get("login");
            if (login != null) {
                email = login + "@users.noreply.github.com";
            }
        }
        
        final String finalEmail = email;
        User user = userRepository.findByEmail(finalEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + finalEmail));

        OffsetDateTime start = OffsetDateTime.parse(startAtIso).withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime end = OffsetDateTime.parse(endAtIso).withOffsetSameInstant(ZoneOffset.UTC);

        reservationService.create(roomId, user.getId(), start, end);
        return "redirect:/reservations/mine";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("reservations", reservationRepository.findAll());
        return "reservations/admin";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        reservationService.approve(id);
        return "redirect:/reservations/admin";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return "redirect:/reservations/admin";
    }
}
