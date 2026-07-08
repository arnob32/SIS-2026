package com.example.User_Service.Controller;

import com.example.User_Service.model.User;
import com.example.User_Service.model.UserRole;
import com.example.User_Service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "common/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "common/register";
    }

    @PostMapping("/register/save")
    public String saveUser(@RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role,
                           @RequestParam(required = false) String phoneNumber,
                           @RequestParam(required = false) String city,
                           @RequestParam(required = false) String specialization,
                           @RequestParam(required = false) String district,
                           Model model) {
        try {
            User user = userService.register(fullName, email, password, UserRole.valueOf(role));
            if (phoneNumber != null || city != null || specialization != null) {
                if (role.equals("TECHNICIAN")) {
                    userService.updateTechnicianFields(user.getUserId(), fullName,
                            phoneNumber, city, specialization, district);
                } else {
                    userService.updateUser(user.getUserId(), fullName, phoneNumber, city);
                }
            }
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "common/register";
        }
    }



    @GetMapping("/dashboard")
    public String dashboardRouter(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return switch (user.getRole()) {
            case CITIZEN -> "redirect:/citizen/dashboard";
            case AUTHORITY -> "redirect:/authority/dashboard";
            case TECHNICIAN -> "redirect:/technician/dashboard";
        };
    }


    @GetMapping("/citizen/dashboard")
    public String citizenDashboard(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "citizen/dashboard";
    }

    @GetMapping("/citizen/profile/edit")
    public String citizenEditForm(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "citizen/edit-profile";
    }

    @PostMapping("/citizen/profile/edit")
    public String citizenUpdate(Authentication auth,
                                @RequestParam String fullName,
                                @RequestParam(required = false) String phoneNumber,
                                @RequestParam(required = false) String city) {
        User user = userService.findByEmail(auth.getName());
        userService.updateUser(user.getUserId(), fullName, phoneNumber, city);
        return "redirect:/citizen/dashboard";
    }

    

    @GetMapping("/authority/dashboard")
    public String authorityDashboard(Authentication auth, Model model) {
        User me = userService.findByEmail(auth.getName());
        model.addAttribute("me", me);
        model.addAttribute("citizens", userService.findByRole(UserRole.CITIZEN));
        model.addAttribute("authorities", userService.findByRole(UserRole.AUTHORITY));
        model.addAttribute("technicians", userService.findByRole(UserRole.TECHNICIAN));
        return "authority/dashboard";
    }

    @GetMapping("/authority/users/{id}")
    public String authorityUserDetail(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "authority/user-detail";
    }

    @PostMapping("/authority/users/{id}/deactivate")
    public String deactivate(@PathVariable String id) {
        userService.deactivateUser(id);
        return "redirect:/authority/dashboard";
    }

    @PostMapping("/authority/users/{id}/delete")
    public String delete(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/authority/dashboard";
    }

    @GetMapping("/authority/profile/edit")
    public String authorityEditForm(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "authority/edit-profile";
    }

    @PostMapping("/authority/profile/edit")
    public String authorityUpdate(Authentication auth,
                                  @RequestParam String fullName,
                                  @RequestParam(required = false) String phoneNumber,
                                  @RequestParam(required = false) String city) {
        User user = userService.findByEmail(auth.getName());
        userService.updateUser(user.getUserId(), fullName, phoneNumber, city);
        return "redirect:/authority/dashboard";
    }



    @GetMapping("/technician/dashboard")
    public String technicianDashboard(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "technician/dashboard";
    }

    @GetMapping("/technician/profile/edit")
    public String technicianEditForm(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "technician/edit-profile";
    }

    @PostMapping("/technician/profile/edit")
    public String technicianUpdate(Authentication auth,
                                   @RequestParam String fullName,
                                   @RequestParam(required = false) String phoneNumber,
                                   @RequestParam(required = false) String city,
                                   @RequestParam(required = false) String specialization,
                                   @RequestParam(required = false) String district) {
        User user = userService.findByEmail(auth.getName());
        userService.updateTechnicianFields(user.getUserId(), fullName,
                phoneNumber, city, specialization, district);
        return "redirect:/technician/dashboard";
    }



    @GetMapping("/api/users")
    @ResponseBody
    public List<Map<String, Object>> getAllUsers() {
        return userService.findAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/api/users/{id}")
    @ResponseBody
    public Map<String, Object> getUserById(@PathVariable String id) {
        return toDto(userService.findById(id));
    }

    @GetMapping("/api/users/role/{role}")
    @ResponseBody
    public List<Map<String, Object>> getByRole(@PathVariable String role) {
        return userService.findByRole(UserRole.valueOf(role.toUpperCase()))
                .stream().map(this::toDto).toList();
    }

    private Map<String, Object> toDto(User u) {
        return Map.of(
                "userId", u.getUserId(),
                "fullName", u.getFullName(),
                "email", u.getEmail(),
                "role", u.getRole().name(),
                "active", u.isActive()
        );
    }
}
