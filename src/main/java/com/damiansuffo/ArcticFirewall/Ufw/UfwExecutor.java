package com.damiansuffo.ArcticFirewall.Ufw;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
