package org.example.Amazon;

import org.example.Amazon.Cost.DeliveryPrice;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmazonUnitTest {

    @Test
    @DisplayName("structural")
    void ItemTest() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2.0));
        when(shoppingCart.getItems()).thenReturn(items);

        for (Item item : items) {
            assertEquals(item.getName(),"Toy" );
            assertEquals(item.getPricePerUnit(),2);
            assertEquals(item.getType(), ItemType.OTHER);
            assertEquals(item.getQuantity(), 2);
        }
    }

    @Test
    @DisplayName("specification")
    void calculate() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2.0), new Item(ItemType.ELECTRONIC, "laptop", 2, 2.0));
        when(shoppingCart.getItems()).thenReturn(items);

        PriceRule priceRule = mock(PriceRule.class);
        when(priceRule.priceToAggregate(items)).thenReturn(15.50);
        Amazon amazon = new Amazon(shoppingCart, List.of(priceRule));

        assertEquals(amazon.calculate(), 15.50);
    }

    @Test
    @DisplayName("specification")
    void calculateEmptyCart() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        List<Item> items = List.of();
        when(shoppingCart.getItems()).thenReturn(items);

        PriceRule priceRule = mock(PriceRule.class);
        when(priceRule.priceToAggregate(items)).thenReturn(0.0);
        Amazon amazon = new Amazon(shoppingCart, List.of(priceRule));

        assertEquals(amazon.calculate(), 0);
    }

    @Test
    @DisplayName("specification")
    void calculateWMultiRules() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2.0), new Item(ItemType.ELECTRONIC, "laptop", 2, 2.0));
        when(shoppingCart.getItems()).thenReturn(items);

        PriceRule priceRule1 = mock(PriceRule.class);
        PriceRule priceRule2 = mock(PriceRule.class);
        when(priceRule1.priceToAggregate(items)).thenReturn(10.0);
        when(priceRule2.priceToAggregate(items)).thenReturn(11.5);

        Amazon amazon = new Amazon(shoppingCart, List.of(priceRule1, priceRule2));

        assertEquals(amazon.calculate(), 21.5);
    }

    @Test
    @DisplayName("structural")
    void addToCart() {
        ShoppingCart shoppingCart = mock(ShoppingCart.class);
        PriceRule priceRule = mock(PriceRule.class);
        Amazon amazon = new Amazon(shoppingCart, List.of(priceRule));
        Item addItem = new Item(ItemType.OTHER, "toy", 1, 1);
        amazon.addToCart(addItem);
        verify(shoppingCart, times(1)).add(addItem);
    }

    @Test
    @DisplayName("structural")
    void deliveryPriceTest() {
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2), new Item(ItemType.ELECTRONIC, "laptop", 2, 2));

        assertEquals(deliveryPrice.priceToAggregate(items), 5);
    }

    @Test
    @DisplayName("specification")
    void deliveryPriceTest0() {
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        List<Item> items = List.of();

        assertEquals(0, deliveryPrice.priceToAggregate(items));
    }

    @Test
    @DisplayName("structural")
    void deliveryPrice2() {
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2));

        assertEquals(12.5, deliveryPrice.priceToAggregate(items));
    }

    @Test
    @DisplayName("structural")
    void deliveryPrice12() {
        DeliveryPrice deliveryPrice = new DeliveryPrice();
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2),
                new Item(ItemType.OTHER, "Toy", 2,2),
                new Item(ItemType.ELECTRONIC, "laptop", 2, 2));

        assertEquals(20, deliveryPrice.priceToAggregate(items));
    }

    @Test
    @DisplayName("specification")
    void noElectronics() {
        ExtraCostForElectronics electronics = new ExtraCostForElectronics();
        List<Item> items = List.of(new Item(ItemType.OTHER, "Toy", 2,2));

        assertEquals(0, electronics.priceToAggregate(items));

    }

    @Test
    @DisplayName("specification")
    void hasElectronics() {
        ExtraCostForElectronics electronics = new ExtraCostForElectronics();
        List<Item> items = List.of(new Item(ItemType.ELECTRONIC, "Toy", 2,2));

        assertEquals(7.5, electronics.priceToAggregate(items));
    }
}