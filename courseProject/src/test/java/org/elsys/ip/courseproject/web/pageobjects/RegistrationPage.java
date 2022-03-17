package org.elsys.ip.courseproject.web.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationPage extends AbstractPage {
    @FindBy(id = "firstName")
    WebElement firstName;

    @FindBy(id = "lastName")
    WebElement lastName;

    @FindBy(id = "email")
    WebElement email;

    @FindBy(id = "password")
    WebElement password;

    @FindBy(id = "matchingPassword")
    WebElement matchingPassword;

    @FindBy(tagName = "button")
    WebElement button;

    @FindBy(css = "#firstName ~ .error")
    List<WebElement> firstNameErrors;

    @FindBy(css = "#lastName ~ .error")
    List<WebElement> lastNameErrors;

    @FindBy(css = "#email ~ .error")
    List<WebElement> emailErrors;

    @FindBy(css = "#password ~ .error")
    List<WebElement> passwordErrors;

    @FindBy(css = "form > .error")
    List<WebElement> globalErrors;

    public RegistrationPage(WebDriver webDriver) {
        super(webDriver);
    }

    public LoginPage validRegister(
            String firstName,
            String lastName,
            String email,
            String password
    ) {
        register(firstName, lastName, email, password, password);

        return PageFactory.initElements(webDriver, LoginPage.class);
    }

    public void register(
            String firstName,
            String lastName,
            String email,
            String password,
            String matchingPassword
    ) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        this.matchingPassword.sendKeys(matchingPassword);
        button.click();
    }

    public void assertErrors(
            Set<String> firstNameErrors,
            Set<String> lastNameErrors,
            Set<String> emailErrors,
            Set<String> passwordErrors,
            Set<String> globalErrors
    ) {
        assertThat(
                this.firstNameErrors.stream().map(x -> x.getText())
                        .collect(Collectors.toSet()).equals(
                                firstNameErrors)).isTrue();
        assertThat(
                this.lastNameErrors.stream().map(x -> x.getText())
                        .collect(Collectors.toSet())
                        .equals(lastNameErrors)).isTrue();
        assertThat(
                this.emailErrors.stream().map(x -> x.getText())
                        .collect(Collectors.toSet())
                        .equals(emailErrors)).isTrue();
        assertThat(
                this.passwordErrors.stream().map(x -> x.getText())
                        .collect(Collectors.toSet())
                        .equals(passwordErrors)).isTrue();
        assertThat(
                this.globalErrors.stream().map(x -> x.getText())
                        .collect(Collectors.toSet())
                        .equals(globalErrors)).isTrue();
    }
}
