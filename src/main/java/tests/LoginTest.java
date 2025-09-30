package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;



public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setUp() {
        // Configura automáticamente el chromedriver
        WebDriverManager.chromedriver().setup();

        // Inicializa el navegador
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        // Inicializa las páginas
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
    }

    //Script 1
    @Test
    public void testAddItemToCart() throws InterruptedException {
        loginPage.enterUsername("standard_user");
        Thread.sleep(1000); // Pausa de 1 segundo para ver el ingreso del usuario

        loginPage.enterPassword("secret_sauce");
        Thread.sleep(1000); // Pausa de 1 segundo para ver el ingreso de la contraseña

        loginPage.clickLogin();
        Thread.sleep(2000); // Pausa de 2 segundos para observar el inicio de sesión

        Assert.assertEquals(productsPage.getPageTitle(), "Products");
        Thread.sleep(1000); // Pausa de 1 segundo para verificar la página de productos

        productsPage.addFirstProductToCart();
        Thread.sleep(1000); // Pausa de 1 segundo para ver la acción de agregar al carrito

        productsPage.goToCart();
        Thread.sleep(2000); // Pausa de 2 segundos para observar la navegación al carrito

        Assert.assertEquals(cartPage.getProductName(), "Sauce Labs Backpack");
    }

    //Script 2
    @Test
    public void testInvalidLogin() {
        loginPage.enterUsername("locked_out_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.");
    }

    //Script 3
    @Test
    public void testProductSorting() throws InterruptedException {
        // Realizar login exitoso
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        Thread.sleep(2000); // Pausa para observar el inicio de sesión

        // Verificar que el login fue exitoso
        Assert.assertEquals(productsPage.getPageTitle(), "Products");

        // Localizar el menú desplegable y seleccionar "Price (high to low)"
        productsPage.selectSortingOption("Price (high to low)");
        Thread.sleep(2000); // Pausa para observar el ordenamiento

        // Verificar que los productos están ordenados correctamente
        double firstProductPrice = productsPage.getFirstProductPrice();
        double lastProductPrice = productsPage.getLastProductPrice();
        Assert.assertTrue(firstProductPrice > lastProductPrice, "Los productos no están ordenados correctamente.");
    }

    //Script 4
    @Test
    public void testCompletePurchaseFlow() throws InterruptedException {
        // Realizar login exitoso
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        Thread.sleep(2000); // Pausa para observar el inicio de sesión

        // Verificar que el login fue exitoso
        Assert.assertEquals(productsPage.getPageTitle(), "Products");

        // Agregar cualquier producto al carrito
        productsPage.addFirstProductToCart();
        Thread.sleep(1000); // Pausa para observar la acción

        // Ir al carrito
        productsPage.goToCart();
        Thread.sleep(2000); // Pausa para observar la navegación al carrito

        // Hacer clic en el botón "Checkout"
        cartPage.clickCheckout();
        Thread.sleep(1000); // Pausa para observar la acción

        // Rellenar el formulario de información del comprador
        cartPage.enterCheckoutInformation("Andres", "Deza", "1234567");
        Thread.sleep(1000); // Pausa para observar el ingreso de datos

        // Hacer clic en "Continue"
        cartPage.clickContinue();
        Thread.sleep(2000); // Pausa para observar la acción

        // Hacer clic en "Finish"
        cartPage.clickFinish();
        Thread.sleep(2000); // Pausa para observar la acción

        // Verificar que se muestra el mensaje de confirmación
        Assert.assertEquals(cartPage.getConfirmationMessage(), "Thank you for your order!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}