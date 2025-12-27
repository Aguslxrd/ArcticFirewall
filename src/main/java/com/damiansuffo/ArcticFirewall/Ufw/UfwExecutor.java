package com.damiansuffo.ArcticFirewall.Ufw;

import com.damiansuffo.ArcticFirewall.Dto.UfwRule;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UfwExecutor {

    private static final Object UFW_LOCK = new Object();

    public String status() {
        return execute("sudo","ufw" , "status", "verbose");
    }

    public String enable() {
        synchronized (UFW_LOCK) {
            return execute("sudo", "ufw", "--force", "enable");
        }
    }

    public String disable() {
        synchronized (UFW_LOCK) {
            return execute("sudo", "ufw", "disable");
        }
    }

    public String statusNumbered() {
        return execute("ufw", "status", "numbered");
    }

    private boolean isSshRule(int number) {
        String rules = execute("ufw", "status", "numbered");

        for (String line : rules.split("\n")) {
            if (line.contains("[" + number + "]")) {
                return line.contains("22/tcp") || line.toLowerCase().contains("ssh");
            }
        }
        return false;
    }

    public String addRule(String port, String action) {
        synchronized (UFW_LOCK) {
            // Validar acción
            if (!action.equalsIgnoreCase("allow") &&
                    !action.equalsIgnoreCase("deny") &&
                    !action.equalsIgnoreCase("reject")) {
                return "ERROR: acción inválida. Usa 'allow', 'deny' o 'reject'.";
            }

            // Validar puerto
            if (!port.matches("\\d+(/tcp|/udp)?")) {
                return "ERROR: puerto inválido. Ej: 8080/tcp";
            }

            try {
                return execute("sudo", "ufw", action.toLowerCase(), port);
            } catch (Exception e) {
                return "ERROR al agregar la regla: " + e.getMessage();
            }
        }
    }

    public String deleteRule(int number) {
        String rulesOutput = execute("sudo", "ufw", "status", "numbered");

        // Proteger puertos críticos
        for (String line : rulesOutput.split("\n")) {
            if (line.contains("[" + number + "]") &&
                    (line.contains("22/tcp") || line.toLowerCase().contains("ssh"))) {
                return "ERROR: No se permite eliminar reglas SSH (puerto 22)";
            }
        }

        try {
            return execute("sudo", "ufw", "--force", "delete", String.valueOf(number));
        } catch (Exception e) {
            return "ERROR al eliminar la regla: " + e.getMessage();
        }
    }


    private static final Set<String> PROTECTED_PORTS = Set.of(
            "22/tcp", "80/tcp", "443/tcp"
    );

    public List<UfwRule> rules() {

        String output = execute("sudo", "ufw", "status", "numbered");
        List<UfwRule> rules = new ArrayList<>();

        for (String line : output.split("\n")) {

            if (!line.trim().startsWith("[")) continue;

            line = line.replace("[", "").replace("]", "").trim();
            String[] parts = line.split("\\s+");

            int number = Integer.parseInt(parts[0]);
            String port = parts[1];
            String action = parts[2];
            String from = String.join(" ", java.util.Arrays.copyOfRange(parts, 3, parts.length));

            boolean protectedRule = PROTECTED_PORTS.contains(port);

            rules.add(new UfwRule(number, port, action, from, protectedRule));
        }

        return rules;
    }



    //Esto ejecuta ufw y captura su salida.
    private String execute(String... command) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
        } catch (Exception e) {
            return "ERROR al ejecutar UFW: " + e.getMessage();
        }
        return output.toString();
    }


}
