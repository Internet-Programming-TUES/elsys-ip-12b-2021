package org.elsys.ip.courseproject.web;

import org.elsys.ip.courseproject.web.selenium.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.elsys.ip.courseproject.web.selenium.SeleniumConfig.delay;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RegistrationTest {

    @LocalServerPort
    private int port;

    private WebDriver webDriver;

    @BeforeEach
    public void setUp() {
        webDriver = new SeleniumConfig().getDriver();
    }

    @AfterEach
    public void tearDown() {
        webDriver.quit();
    }

    @Test
    public void registration() {
        webDriver.get("http://localhost:" + port);
        webDriver.findElement(By.id("regBtn")).click();
        delay();
        webDriver.findElement(By.id("firstName")).sendKeys("First Name");
        webDriver.findElement(By.id("lastName")).sendKeys("Last Name");
        webDriver.findElement(By.id("email")).sendKeys("email@email.com");
        webDriver.findElement(By.id("password")).sendKeys("qwerty");
        webDriver.findElement(By.id("matchingPassword")).sendKeys("qwerty");
        webDriver.findElement(By.tagName("button")).click();
        delay();
    }
}
