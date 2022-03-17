package org.elsys.ip.courseproject.web.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegistrationPage extends AbstractPage {
    @FindBy(id="firstName")
    WebElement firstName;

    @FindBy(id="lastName")
    WebElement lastName;

    @FindBy(id="email")
    WebElement email;

    @FindBy(id="password")
    WebElement password;

    @FindBy(id="matchingPassword")
    WebElement matchingPassword;

    @FindBy(tagName = "button")
    WebElement button;

    public RegistrationPage(WebDriver webDriver) {
        super(webDriver);
    }

    public LoginPage register(
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        this.matchingPassword.sendKeys(password);
        button.click();

        return PageFactory.initElements(webDriver, LoginPage.class);
    }
}
