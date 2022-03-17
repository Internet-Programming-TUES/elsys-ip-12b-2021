package org.elsys.ip.courseproject.web.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends AbstractPage {
    @FindBy(id="username")
    WebElement email;

    @FindBy(id="password")
    WebElement password;

    @FindBy(className = "btn-lg")
    WebElement button;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public HomePage login(
            String email,
            String password
    ) {
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        button.click();

        return PageFactory.initElements(webDriver, HomePage.class);
    }
}
