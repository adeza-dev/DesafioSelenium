package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ProductsPage {
    private WebDriver driver;

    // Locators
    private By pageTitle = By.className("title");
    private By firstProductAddToCartButton = By.cssSelector(".inventory_item:first-child button");
    private By cartIcon = By.id("shopping_cart_container");

    // Constructor
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public void addFirstProductToCart() {
        driver.findElement(firstProductAddToCartButton).click();
    }

    public void selectSortingOption(String option) {
        WebElement dropdown = driver.findElement(By.className("product_sort_container"));
        Select select = new Select(dropdown);
        select.selectByVisibleText(option);
    }

    public double getFirstProductPrice() {
        String priceText = driver.findElement(By.cssSelector(".inventory_item_price")).getText();
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public double getLastProductPrice() {
        List<WebElement> prices = driver.findElements(By.cssSelector(".inventory_item_price"));
        String priceText = prices.get(prices.size() - 1).getText();
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public void goToCart() {
        driver.findElement(cartIcon).click();
    }
}