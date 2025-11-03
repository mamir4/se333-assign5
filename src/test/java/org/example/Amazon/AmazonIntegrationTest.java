package org.example.Amazon;

import org.example.Amazon.Cost.DeliveryPrice;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.RegularCost;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {

    private ShoppingCartAdaptor shoppingCart;
    private Database db;
    private Amazon amazon;

    @BeforeEach
    void setup() {
        db = new Database();
        db.resetDatabase();
        shoppingCart = new ShoppingCartAdaptor(db);
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    @DisplayName("structural")
    void addAndRetrieveAmazon() {
        Item toy = new Item(ItemType.OTHER, "toy", 1, 2);

        amazon = new Amazon(shoppingCart, List.of(new RegularCost()));
        amazon.addToCart(toy);
        assertEquals("toy", shoppingCart.getItems().get(0).getName());
    }

    @Test
    @DisplayName("specification")
    void emptyCartCalculate() {
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new ExtraCostForElectronics(), new DeliveryPrice()));
        assertEquals(0, amazon.calculate());
    }

    @Test
    @DisplayName("specification")
    void calculateTotalRegCost() {
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        amazon = new Amazon(shoppingCart, List.of(new RegularCost()));

        assertEquals(2, amazon.calculate());
    }

    @Test
    @DisplayName("specification")
    void deliveryCost0() {
        //shoppingCart.add(new Item());
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new DeliveryPrice()));
        assertEquals(0, amazon.calculate());
    }

    @Test
    @DisplayName("structural")
    void deliveryCost2() {
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        shoppingCart.add( new Item(ItemType.OTHER, "book", 1, 2));
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new DeliveryPrice()));

        assertEquals(9, amazon.calculate());
    }

    @Test
    @DisplayName("structural")
    void deliveryCost5() {
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        shoppingCart.add( new Item(ItemType.OTHER, "book", 1, 2));
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        shoppingCart.add( new Item(ItemType.OTHER, "book", 1, 2));
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new DeliveryPrice()));

        assertEquals(22.5, amazon.calculate());
    }


    @Test
    @DisplayName("specification")
    void hasElectronic() {
        shoppingCart.add(new Item(ItemType.ELECTRONIC, "phone", 1, 2));
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new ExtraCostForElectronics()));
        assertEquals(9.5, amazon.calculate());
    }

    @Test
    @DisplayName("specification")
    void noElectronic() {
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new ExtraCostForElectronics()));
        assertEquals(2, amazon.calculate());
    }

    @Test
    @DisplayName("specification")
    void fullCostTest() {
        shoppingCart.add(new Item(ItemType.OTHER, "toy", 1, 2));
        shoppingCart.add(new Item(ItemType.ELECTRONIC, "phone", 1, 2));
        amazon = new Amazon(shoppingCart, List.of(new RegularCost(), new ExtraCostForElectronics(), new DeliveryPrice()));

        assertEquals(16.5, amazon.calculate());
    }


}