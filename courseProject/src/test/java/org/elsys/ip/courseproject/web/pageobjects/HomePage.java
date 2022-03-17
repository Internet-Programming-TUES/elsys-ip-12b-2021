package org.elsys.ip.courseproject.web.pageobjects;

import org.elsys.ip.courseproject.web.selenium.SeleniumConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends AbstractPage {
    @FindBy(id="regBtn")
    WebElement registrationButton;

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    public RegistrationPage openRegistration() {
        registrationButton.click();
        return PageFactory.initElements(webDriver, RegistrationPage.class);
    }

    public boolean isAuthenticated() {
        return webDriver.findElements(By.id("logoutBtn")).size() == 1 &&
                webDriver.findElements(By.id("logBtn")).isEmpty();
    }

    public static HomePage open(WebDriver webDriver, int port ) {
        webDriver.get("http://localhost:" + port);
        return PageFactory.initElements(webDriver, HomePage.class);
    }
}
