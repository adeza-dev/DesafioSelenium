package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ScreenshotUtil;

import java.util.Map;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    private String currentTestName;

    @BeforeMethod
    public void setUp(ITestResult result) {
        currentTestName = result.getMethod().getMethodName(); // Detecta el nombre del test

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--incognito");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("user-data-dir=C:/temp/selenium_profile");

        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false
        ));

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
    }

    // Script 1
    @Test
    public void testAddItemToCart() throws InterruptedException {
        loginPage.enterUsername("standard_user");
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "01_usuario_ingresado");

        loginPage.enterPassword("secret_sauce");
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "02_password_ingresado");

        loginPage.clickLogin();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "03_login_exitoso");

        Assert.assertEquals(productsPage.getPageTitle(), "Products");
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "04_ver_pagina_productos");

        productsPage.addFirstProductToCart();
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "05_producto_agregado");

        productsPage.goToCart();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "06_carrito");

        Assert.assertEquals(cartPage.getProductName(), "Sauce Labs Backpack");
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "07_validacion_producto");
    }

    // Script 2
    @Test
    public void testInvalidLogin() throws InterruptedException {
        loginPage.enterUsername("locked_out_user");
        loginPage.enterPassword("secret_sauce");
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "01_datos_invalidos");

        loginPage.clickLogin();
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "02_error_mostrado");

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.");
    }

    // Script 3
    @Test
    public void testProductSorting() throws InterruptedException {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "01_login_sorting");

        Assert.assertEquals(productsPage.getPageTitle(), "Products");

        productsPage.selectSortingOption("Price (high to low)");
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "02_sort_desc");

        double firstProductPrice = productsPage.getFirstProductPrice();
        double lastProductPrice = productsPage.getLastProductPrice();
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "03_verificacion_precios");

        Assert.assertTrue(firstProductPrice > lastProductPrice, "Los productos no están ordenados correctamente.");
    }

    // Script 4
    @Test
    public void testCompletePurchaseFlow() throws InterruptedException {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "01_usuario_ingresado");

        loginPage.clickLogin();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "02_login_exitoso");

        Assert.assertEquals(productsPage.getPageTitle(), "Products");

        productsPage.addFirstProductToCart();
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "03_producto_agregado");

        productsPage.goToCart();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "04_carrito");

        cartPage.clickCheckout();
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "05_checkout");

        cartPage.enterCheckoutInformation("Andres", "Deza", "1234567");
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "06_datos_ingresados");

        cartPage.clickContinue();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "07_continuar");

        cartPage.clickFinish();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "08_finalizar");

        Assert.assertEquals(cartPage.getConfirmationMessage(), "Thank you for your order!");
        ScreenshotUtil.takeScreenshot(driver, currentTestName, "09_confirmacion");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
