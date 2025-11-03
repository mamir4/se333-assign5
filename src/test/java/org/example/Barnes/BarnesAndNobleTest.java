package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based")
    void getPriceCartTestNull() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);
        BarnesAndNoble bandn = new BarnesAndNoble(bookDatabase, bookProcess);
        PurchaseSummary summary = bandn.getPriceForCart(null);
        assertNull(summary);
    }

    @Test
    @DisplayName("specification-based")
    void getPriceCartTestRetrievable() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);
        Book book = new Book("1234", 2, 2);
        BarnesAndNoble bandn = new BarnesAndNoble(bookDatabase, bookProcess);
        HashMap<String, Integer> order = new HashMap<>();
        order.put("1234", 2);

        when(bookDatabase.findByISBN("1234")).thenReturn(book);
        PurchaseSummary summary = bandn.getPriceForCart(order);

        assertEquals(summary.getTotalPrice(), 4);
    }

    @Test
    @DisplayName("specification-based")
    void getPriceCartNonRetrievable() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);
        Book book = new Book("1234", 2, 2);
        BarnesAndNoble bandn = new BarnesAndNoble(bookDatabase, bookProcess);
        HashMap<String, Integer> order = new HashMap<>();
        order.put("1234", 3);

        when(bookDatabase.findByISBN("1234")).thenReturn(book);
        PurchaseSummary summary = bandn.getPriceForCart(order);

        assertTrue(summary.getUnavailable().containsKey(book));
        assertTrue(summary.getUnavailable().containsValue(1));
    }

    @Test
    @DisplayName("structural-based")
    void ISBNTest() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);
        Book book = new Book("1234", 2, 2);
        BarnesAndNoble bandn = new BarnesAndNoble(bookDatabase, bookProcess);
        HashMap<String, Integer> order = new HashMap<>();
        order.put("1234", 3);

        when(bookDatabase.findByISBN("1234")).thenReturn(book);
        PurchaseSummary summary = bandn.getPriceForCart(order);

        verify(bookDatabase,times(1)).findByISBN("1234");
    }

    @Test
    @DisplayName("structural-based")
    void buyBookTest() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);
        Book book = new Book("1234", 2, 2);
        BarnesAndNoble bandn = new BarnesAndNoble(bookDatabase, bookProcess);
        HashMap<String, Integer> order = new HashMap<>();
        order.put("1234", 3);

        when(bookDatabase.findByISBN("1234")).thenReturn(book);
        PurchaseSummary summary = bandn.getPriceForCart(order);

        verify(bookProcess, times(1)).buyBook(book, 2);
    }
}