package com.hei.school.endpoint.rest.controller.health;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoredIntController {

  @GetMapping("/stored-int")
  public ResponseEntity<Object> getStoredInt() throws InterruptedException {
    Thread.sleep(10_000);
    File file = new File("/tmp/stored-int.txt");
    Random random = new Random();
    try {
      int number;
      if (file.exists()) {
        number = Integer.parseInt(Files.readString(file.toPath()).trim());
      } else {
        number = random.nextInt(1000);
        Files.writeString(file.toPath(), String.valueOf(number));
      }
      return ResponseEntity.ok(Collections.singletonMap("storedInt", number));
    } catch (IOException | NumberFormatException e) {
      return ResponseEntity.internalServerError()
          .body(Collections.singletonMap("error", "Erreur: " + e.getMessage()));
    }
  }
}
