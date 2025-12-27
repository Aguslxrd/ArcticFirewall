package com.damiansuffo.ArcticFirewall.Controller;

import com.damiansuffo.ArcticFirewall.Ufw.UfwExecutor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Data
@Controller
public class UfwController {
    private final UfwExecutor ufwExecutor;

    public String index(Model model) {
        model.addAttribute("status", ufwExecutor.status());
        return "index";
    }
}
