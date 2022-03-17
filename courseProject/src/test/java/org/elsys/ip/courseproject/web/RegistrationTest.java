package org.elsys.ip.courseproject.web;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.elsys.ip.courseproject.web.pageobjects.HomePage;
import org.elsys.ip.courseproject.web.pageobjects.LoginPage;
import org.elsys.ip.courseproject.web.pageobjects.RegistrationPage;
import org.elsys.ip.courseproject.web.selenium.SeleniumConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
        HomePage homePage = HomePage.open(webDriver, port);
        RegistrationPage registrationPage = homePage.openRegistration();
        LoginPage loginPage = registrationPage.validRegister("First Name", "Last Name", "email@email.com", "qwerty");
        homePage = loginPage.login("email@email.com", "qwerty");
        assertThat(homePage.isAuthenticated()).isTrue();
    }

    @Test
    public void registrationValidation() {
        HomePage homePage = HomePage.open(webDriver, port);
        RegistrationPage registrationPage = homePage.openRegistration();
        registrationPage.register("", "", "", "", "1");
        registrationPage.assertErrors(
                Set.of("must not be empty"),
                Set.of("must not be empty"),
                Set.of("must not be empty", "Invalid email"),
                Set.of("must not be empty"),
                Set.of("Passwords don't match")
        );

        registrationPage.register("First Name", "", "", "", "1");
        registrationPage.assertErrors(
                Sets.newHashSet(),
                Set.of("must not be empty"),
                Set.of("must not be empty", "Invalid email"),
                Set.of("must not be empty"),
                Set.of("Passwords don't match")
        );

        registrationPage.register("First Name", "Last Name", "email", "qwerty", "qwerty");
        registrationPage.assertErrors(
                Sets.newHashSet(),
                Sets.newHashSet(),
                Set.of("Invalid email"),
                Sets.newHashSet(),
                Sets.newHashSet()
        );
    }
}
