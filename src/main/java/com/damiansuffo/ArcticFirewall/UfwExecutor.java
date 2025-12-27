package com.damiansuffo.ArcticFirewall;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
@Service
public class UfwExecutor {

    public String status() {
        return execute("ufw" , "status", "verbose");
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
