package com.damiansuffo.ArcticFirewall.Controller;

import com.damiansuffo.ArcticFirewall.Dto.UfwRule;
import com.damiansuffo.ArcticFirewall.Ufw.UfwExecutor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@Controller
public class UfwController {
    private final UfwExecutor ufwExecutor;

    @GetMapping("/")
    public String index(Model model) {
        String statusOutput = ufwExecutor.status();
        boolean ufwActive = statusOutput.toLowerCase().contains("status: active");

        model.addAttribute("ufwActive", ufwActive); // true/false para ver si esta activo el servicio o no.
        model.addAttribute("rules", ufwExecutor.rules());
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

    @PostMapping("/rules/add")
    public String addRule(@RequestParam String port,
                          @RequestParam String action,
                          Model model) {
        String result = ufwExecutor.addRule(port, action);

        model.addAttribute("message", result);
        model.addAttribute("ufwActive", ufwExecutor.status().toLowerCase().contains("status: active"));
        model.addAttribute("rules", ufwExecutor.rules());

        return "index";
    }

    @PostMapping("/rules/delete/{number}")
    public String deleteRule(@PathVariable int number, Model model) {
        String result = ufwExecutor.deleteRule(number);

        model.addAttribute("message", result);
        model.addAttribute("ufwActive", ufwExecutor.status().toLowerCase().contains("status: active"));
        model.addAttribute("rules", ufwExecutor.rules());

        return "index";
    }



}
