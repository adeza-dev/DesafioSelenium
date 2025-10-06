package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static void takeScreenshot(WebDriver driver, String testName, String stepName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // Carpeta raíz de screenshots
            Path rootDir = Paths.get("screenshots");
            Path testDir = rootDir.resolve(testName);  // Subcarpeta por test

            // Crea la carpeta si no existe
            if (!Files.exists(testDir)) {
                Files.createDirectories(testDir);
            }

            // Captura el screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Define la ruta final con timestamp
            Path destination = testDir.resolve(stepName + "_" + timestamp + ".png");

            // Copia el archivo
            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Screenshot guardado: " + destination.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Error al guardar screenshot: " + e.getMessage());
        }
    }
}
