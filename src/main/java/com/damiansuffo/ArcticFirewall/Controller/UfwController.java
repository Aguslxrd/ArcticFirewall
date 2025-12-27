package com.damiansuffo.ArcticFirewall.Controller;

import com.damiansuffo.ArcticFirewall.Ufw.UfwExecutor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Data
@Controller
public class UfwController {
    private final UfwExecutor ufwExecutor;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("status", ufwExecutor.status());
        return "index";
    }

    @PostMapping("/ufw/enable")
    public String enableUfw() {
        ufwExecutor.enable();
        return "redirect:/";
    }

    @PostMapping("/ufw/disable")
    public String disableUfw() {
        ufwExecutor.disable();
        return "redirect:/";
    }
}
