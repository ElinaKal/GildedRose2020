package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

	@Test
	public void testTheTruth() {
		assertTrue(true);
	}
	@Test
	public void exampleTest() {
		//create an inn, add an item, and simulate one day
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.oneDay();

		//access a list of items, get the quality of the one set
		List<Item> items = inn.getItems();
		int quality = items.get(0).getQuality();

		//assert quality has decreased by one
		assertEquals("Failed quality for Dexterity Vest", 19, quality);
	}

	@Test public void testUpdateQualityForAllItems() {
	    // Initialize GildedRose with a variety of items
	    GildedRose inn = new GildedRose();
	    inn.setItem(new Item("+5 Dexterity Vest", 10, 20)); // Regular
	    inn.setItem(new Item("Aged Brie", 2, 0));          // Aged Brie
	    inn.setItem(new Item("Elixir of the Mongoose", 5, 7)); // Regular
	    inn.setItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80)); // Legendary
	    inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)); // Backstage pass
	    inn.setItem(new Item("Conjured Mana Cake", 3, 6)); // Regular

	    inn.oneDay();

	    // Get the updated list of items
	    List<Item> items = inn.getItems();

	    // Assert changes for "+5 Dexterity Vest" (Regular item)
	    assertEquals(9, items.get(0).getSellIn());
	    assertEquals(19, items.get(0).getQuality());

	    // Assert changes for "Aged Brie"
	    assertEquals(1, items.get(1).getSellIn());
	    assertEquals(1, items.get(1).getQuality());

	    // Assert changes for "Elixir of the Mongoose" (Regular item)
	    assertEquals(4, items.get(2).getSellIn());
	    assertEquals(6, items.get(2).getQuality());

	    // Assert no changes for "Sulfuras, Hand of Ragnaros" (Legendary item)
	    assertEquals(0, items.get(3).getSellIn());
	    assertEquals(80, items.get(3).getQuality());

	    // Assert changes for "Backstage passes to a TAFKAL80ETC concert"
	    assertEquals(14, items.get(4).getSellIn());
	    assertEquals(21, items.get(4).getQuality());

	    // Assert changes for "Conjured Mana Cake" (Regular item)
	    assertEquals(2, items.get(5).getSellIn());
	    assertEquals(5, items.get(5).getQuality());
	}
	
	@Test public void testAgedBrieQualityIncreases() {		// Create an inn, add Aged Brie, and simulate one day
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 10, 20));
		inn.oneDay();

		List<Item> items = inn.getItems();
		int quality = items.get(0).getQuality();

		// Assert the quality has increased by 1
		assertEquals("Aged Brie quality should increase by 1", 21, quality);
	}

	@Test public void testBackstagePassesQualityIncreases() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
		inn.oneDay();
		// Assert quality has increased by 1
		assertEquals("Backstage passes quality should increase by 1", 21, inn.getItems().get(0).getQuality());
	}

	@Test public void testBackstagePassesQualityAtSellIn11() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 11, 20));
		inn.oneDay();
		// Assert quality has increased by 1
		assertEquals("Backstage passes quality should increase by 1", 21, inn.getItems().get(0).getQuality());
	}

	@Test public void testBackstagePassesQualityAtSellIn6() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 6, 20));
		inn.oneDay();
		// Assert quality has increased by 2
		assertEquals("Backstage passes quality should increase by 1", 22, inn.getItems().get(0).getQuality());
	}

	@Test public void testBackstagePassesQualityIncreasesNearExpiration() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20));
		inn.oneDay();
		// Assert quality has increased by 2
		assertEquals("Backstage passes quality should increase by 2 if sell-in < 11", 22, inn.getItems().get(0).getQuality());
	}
	
	@Test public void testBackstagePassesQualityIncreasesVeryNearExpiration() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20));
		inn.oneDay();
		// Assert quality has increased by 3
		assertEquals("Backstage passes quality should increase by 3 if sell-in < 6", 23, inn.getItems().get(0).getQuality());
	}

	@Test public void testBackstagePassesQualityIncreasesVeryNearExpiration2() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 9, 49));
		inn.oneDay();
		// Assert quality has increased by 2, but not >50
		assertEquals("Backstage passes quality should increase by 2 if sell-in < 11", 50, inn.getItems().get(0).getQuality());
	}
	@Test public void testBackstagePassesQualityIncreasesVeryNearExpiration3() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 48));
		inn.oneDay();

		// Assert quality has increased by 3, but not >50
		assertEquals("Backstage passes quality should increase by 3 if sell-in < 11", 50, inn.getItems().get(0).getQuality());
	}

	@Test public void testBackstagePassesQualityDropsToZeroAfterConcert() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20));
		inn.oneDay();

		// Assert quality drops to 0 after the concert
		assertEquals("Backstage passes quality should drop to 0 after the concert", 0, inn.getItems().get(0).getQuality());
	}
	
	@Test public void testQualityDoesNotExceedFiftyForAgedBrie() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 10, 50));
		inn.oneDay();

		// Assert quality is still 50 (it can't exceed 50)
		assertEquals("Aged Brie quality should not exceed 50", 50, inn.getItems().get(0).getQuality());
	}
	
	@Test public void testRegularItemDecreasesQuality() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.oneDay();

		assertEquals("Failed: Regular item's quality did not decrease by 1", 19, inn.getItems().get(0).getQuality());
	}

	@Test public void testMainPrintsOMGHAI() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outContent));
		GildedRose.main(new String[]{});
		System.setOut(originalOut);
		String output = outContent.toString();
		assertTrue("Failed: Output does not contain 'OMGHAI!'", output.contains("OMGHAI!"));
	} 

	@Test public void testSulfurasQualityDoesNotChange() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		inn.oneDay();

		assertEquals("Failed: Sulfuras quality should not change", 80, inn.getItems().get(0).getQuality());
	}

	@Test public void testWithNegativeQualityValue() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("jotain", 0, -1));
		inn.oneDay();

		// Verify quality did not change
		assertEquals("Quality is never negative", -1, inn.getItems().get(0).getQuality());
	}
	@Test public void testWithNegativeQualitySulfurasValue() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", -1, 1));
		inn.oneDay();
		// Verify quality did not change
		assertEquals("Quality is never negative", 1, inn.getItems().get(0).getQuality());
	}

	@Test public void testQualityDoesNotGoBelowZero() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Elixir of the Mongoose", 5, 0));
		inn.oneDay();

		// Verify the quality remains at 0
		assertEquals("Failed: Quality should not go below 0", 0, inn.getItems().get(0).getQuality());
	}
	@Test public void testQualityDecreasesToZero() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Elixir of the Mongoose", 5, 1));
		inn.oneDay();
		// Verify the quality is 0
		assertEquals("Failed: Quality should decrease to 0", 0, inn.getItems().get(0).getQuality());
	}
	@Test public void testSellInDecreasesToZero() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Elixir of the Mongoose", 0, 3));
		inn.oneDay();
		// Assert the quality has decreased by 2
		assertEquals("Failed. Elixir of the Mongoose quality should decrease by 1", 1, inn.getItems().get(0).getQuality());
	}	 

	@Test public void testSellInDecreasesToZeroWithBrie() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 0, 3));
		inn.oneDay();

		//assert quality has increased by 1
		assertEquals("Failed quality for Aged Brie", 4, inn.getItems().get(0).getQuality());
	}

	@Test public void testSellInDecreasesToZeroWithJotain() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Jotain", 0, 1));
		inn.oneDay();
		//assert quality has increased by 2 because sellIn <0, but quality >=0 
		assertEquals("Failed quality for Jotain", 0, inn.getItems().get(0).getQuality());
	}

	@Test public void testSellInDecreasesToZeroWithBrie2() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", 0, 49));
		inn.oneDay();
		//assert quality has increased by 1
		assertEquals("Quality for Aged Brie", 50, inn.getItems().get(0).getQuality());
	}
	
	@Test public void testQualityDegradesTwiceAsFastAfterSellDate() {
	    GildedRose inn = new GildedRose();
	    inn.setItem(new Item("+5 Dexterity Vest", 0, 10));
	    inn.setItem(new Item("Elixir of the Mongoose", 1, 10));
	    inn.oneDay();

	    // Quality drops by 2 after sell-by date
	    assertEquals(8, inn.getItems().get(0).getQuality()); 
	 // Quality drops by 1 at sell date
	    assertEquals(9, inn.getItems().get(1).getQuality());
	}


	@Test public void testMultipleItems() {
		GildedRose inn = new GildedRose();
		// Add multiple items
		inn.setItem(new Item("Elixir of the Mongoose", 5, 10));
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.oneDay();

		assertEquals("Failed: Quality for Elixir should decrease", 9, inn.getItems().get(0).getQuality());
		assertEquals("Failed: Sulfuras quality should not change", 80, inn.getItems().get(1).getQuality());
		assertEquals("Failed: Quality for Dexterity Vest should decrease", inn.getItems().get(2).getQuality());
	} 

}
