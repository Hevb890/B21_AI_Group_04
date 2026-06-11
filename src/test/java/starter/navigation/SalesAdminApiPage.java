package starter.navigation;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;

@DefaultUrl("http://localhost:8080")
public class SalesAdminApiPage extends PageObject {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String LOGIN_URL = "/api/auth/login";
    public static final String SALES_URL = "/api/sales";
    public static final String SELL_PLANT_URL = "/api/sales/plant/";
    public static final String CATEGORIES_URL = "/api/categories";
    public static final String PLANTS_URL = "/api/plants/category/";
}