package com.tealium.remotecommanddispatcher

import com.tealium.dispatcher.TealiumEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteCommandParserTest {

    @Test
    fun mapDispatchValidResultantMap() {
        val dispatch = TealiumEvent(
                "test_dispatch",
                mapOf("product_id" to arrayListOf("ABC123"),
                        "product_name" to arrayListOf("milk chocolate"),
                        "product_brand" to arrayListOf("see's"),
                        "product_unit_price" to arrayListOf("19.99"),
                        "product_quantity" to arrayListOf("1"),
                        "customer_id" to "CUST234324",
                        "customer_full_name" to "Test Name",
                        "order_id" to "ORD239847",
                        "event_title" to "ecommerce_purchase",
                        "order_tax_amount" to "1.99",
                        "order_shipping_amount" to "5.00"))
        val lookup = mapOf("campaign_keywords" to "cp1",
                "campaign" to "campaign",
                "checkout_option" to "checkout_option",
                "checkout_step" to "checkout_step",
                "content" to "content",
                "content_type" to "content_type",
                "coupon" to "coupon",
                "product_brand" to "item_brand",
                "product_category" to "item_category",
                "product_id" to "item_id",
                "product_list" to "item_list",
                "product_location_id" to "item_location_id",
                "product_name" to "item_name",
                "product_variant" to "item_variant",
                "campaign_medium" to "medium",
                "product_unit_price" to "price",
                "product_quantity" to "quantity",
                "search_keyword" to "search_term",
                "order_shipping_amount" to "shipping",
                "order_tax_amount" to "tax",
                "order_id" to "transaction_id",
                "order_total" to "value",
                "event_title" to "event_name",
                "tealium_event" to "command_name",
                "customer_id" to "user_id")

        val expectedMap = mapOf(
                "item_id" to arrayListOf("ABC123"),
                "item_name" to arrayListOf("milk chocolate"),
                "item_brand" to arrayListOf("see's"),
                "price" to arrayListOf("19.99"),
                "quantity" to arrayListOf("1"),
                "user_id" to "CUST234324",
                "transaction_id" to "ORD239847",
                "event_name" to "ecommerce_purchase",
                "tax" to "1.99", "shipping" to "5.00",
                "command_name" to "test_dispatch")
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertEquals(expectedMap, result)
        assertTrue(result.containsKey("command_name"))
        assertEquals(dispatch.eventName, result["command_name"])
    }

    @Test
    fun mapDispatchWithDotParamsResultantMap() {
        val dispatch = TealiumEvent(
                "purchase",
                mapOf("content" to "someContent",
                        "content_type" to "someContentType",
                        "coupon" to "someCoupon",
                        "product_brand" to arrayOf("someBrand"),
                        "product_category" to arrayOf("someCategory"),
                        "product_id" to arrayOf("someId"),
                        "product_list" to arrayOf("someList"),
                        "product_location_id" to "someLocationId",
                        "product_name" to arrayOf("someName"),
                        "product_variant" to arrayOf("someVariant"),
                        "campaign_medium" to "someMedium",
                        "product_unit_price" to arrayOf(0.00),
                        "product_quantity" to arrayOf(1),
                        "search_keyword" to "someSearchTerm",
                        "order_shipping_amount" to 5.00,
                        "order_tax_amount" to 3.00,
                        "order_id" to "ABC123",
                        "order_total" to 20.00,
                        "event_title" to "order",
                        "customer_id" to "cust1234"))
        val lookup = mapOf("content" to "content",
                "content_type" to "content_type",
                "coupon" to "purchase.coupon",
                "product_brand" to "event.item_brand",
                "product_category" to "event.item_category",
                "product_id" to "event.item_id",
                "product_list" to "event.item_list",
                "product_location_id" to "event.item_location_id",
                "product_name" to "event.item_name",
                "product_variant" to "event.item_variant",
                "campaign_medium" to "event.medium",
                "product_unit_price" to "event.price",
                "product_quantity" to "event.quantity",
                "search_keyword" to "event.search_term",
                "order_shipping_amount" to "purchase.shipping",
                "order_tax_amount" to "purchase.tax",
                "order_id" to "purchase.transaction_id",
                "order_total" to "purchase.value",
                "event_title" to "event_name",
                "tealium_event" to "command_name",
                "customer_id" to "user.user_id")

        val expectedMap = mapOf("coupon" to "someCoupon",
                "shipping" to 5.00,
                "tax" to 3.00,
                "transaction_id" to "ABC123",
                "value" to 20.00
        )
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey("command_name"))
        assertEquals("purchase", result["command_name"])
        assertTrue(result.containsKey("purchase"))
        assertEquals(expectedMap, result["purchase"])
    }

    @Test
    fun mapDispatchWithListDestinationsAndDotParamsResultantMap() {
        val dispatch = TealiumEvent(
                "purchase",
                mapOf("content" to "someContent",
                        "content_type" to "someContentType",
                        "coupon" to "someCoupon",
                        "product_brand" to arrayOf("someBrand"),
                        "product_category" to arrayOf("someCategory"),
                        "product_id" to arrayOf("someId"),
                        "product_list" to arrayOf("someList"),
                        "product_location_id" to "someLocationId",
                        "product_name" to arrayOf("someName"),
                        "product_variant" to arrayOf("someVariant"),
                        "campaign_medium" to "someMedium",
                        "product_unit_price" to arrayOf(0.00),
                        "product_quantity" to arrayOf(1),
                        "search_keyword" to "someSearchTerm",
                        "order_shipping_amount" to 5.00,
                        "order_tax_amount" to 3.00,
                        "order_id" to "ABC123",
                        "order_total" to 20.00,
                        "event_title" to "order",
                        "customer_id" to "cust1234"))
        val lookup = mapOf("content" to "content",
                "content_type" to "content_type",
                "coupon" to "purchase.coupon, event.discount_code",
                "product_brand" to "item_brand",
                "product_category" to "item_category",
                "product_id" to "item_id",
                "product_list" to "item_list",
                "product_location_id" to "item_location_id",
                "product_name" to "item_name",
                "product_variant" to "item_variant",
                "campaign_medium" to "medium",
                "product_unit_price" to "price",
                "product_quantity" to "quantity",
                "search_keyword" to "search_term",
                "order_shipping_amount" to "purchase.shipping",
                "order_tax_amount" to "purchase.tax",
                "order_id" to "purchase.transaction_id",
                "order_total" to "purchase.value",
                "event_title" to "event_name",
                "tealium_event" to "command_name",
                "customer_id" to "user.user_id")

        val expectedMap = mapOf("coupon" to "someCoupon",
                "shipping" to 5.00,
                "tax" to 3.00,
                "transaction_id" to "ABC123",
                "value" to 20.00
        )
        val expectedEventMap = mapOf("discount_code" to "someCoupon")
        val expectedUserMap = mapOf("user_id" to "cust1234")
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey("command_name"))
        assertEquals("purchase", result["command_name"])

        assertTrue(result.containsKey("purchase"))
        assertEquals(expectedMap, result["purchase"])

        assertTrue(result.containsKey("event"))
        assertEquals(expectedEventMap, result["event"])

        assertTrue(result.containsKey("user"))
        assertEquals(expectedUserMap, result["user"])
    }

    @Test
    fun mapDispatchWithListDestinationsParamsResultantMap() {
        val dispatch = TealiumEvent(
                "test_event",
                mapOf("content" to "someContent",
                        "content_type" to "someContentType",
                        "coupon" to "someCoupon",
                        "product_brand" to arrayOf("someBrand"),
                        "product_category" to arrayOf("someCategory"),
                        "product_id" to arrayOf("someId"),
                        "product_list" to arrayOf("someList"),
                        "product_location_id" to "someLocationId",
                        "product_name" to arrayOf("someName"),
                        "product_variant" to arrayOf("someVariant"),
                        "campaign_medium" to "someMedium",
                        "product_unit_price" to arrayOf(0.00),
                        "product_quantity" to arrayOf(1),
                        "search_keyword" to "someSearchTerm",
                        "order_shipping_amount" to 5.00,
                        "order_tax_amount" to 3.00,
                        "order_id" to "ABC123",
                        "order_total" to 20.00,
                        "event_title" to "order",
                        "customer_id" to "cust1234"))
        val lookup = mapOf("content" to "content",
                "content_type" to "content_type",
                "coupon" to "coupon, discount_code",
                "product_brand" to "event.item_brand",
                "product_category" to "event.item_category",
                "product_id" to "event.item_id",
                "product_list" to "event.item_list",
                "product_location_id" to "event.item_location_id",
                "product_name" to "event.item_name",
                "product_variant" to "event.item_variant",
                "campaign_medium" to "event.medium",
                "product_unit_price" to "event.price",
                "product_quantity" to "event.quantity",
                "search_keyword" to "event.search_term",
                "order_shipping_amount" to "purchase.shipping",
                "order_tax_amount" to "purchase.tax",
                "order_id" to "purchase.transaction_id",
                "order_total" to "purchase.value",
                "event_title" to "event_name",
                "tealium_event" to "command_name",
                "customer_id" to "user.user_id")

        val expectedMap = mapOf("coupon" to "someCoupon", "discount_code" to "someCoupon")
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey("command_name"))
        assertEquals("test_event", result["command_name"])
        assertEquals(expectedMap["coupon"], result["coupon"])
        assertEquals(expectedMap["discount_code"], result["discount_code"])
    }

    @Test
    fun mapDispatchWithSingleObjectMapping() {
        val dispatch = TealiumEvent(
                "level_up",
                mapOf("level" to 19))
        val eventKey = "event"
        val lookup = mapOf("level" to "$eventKey.fb_level")
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey(eventKey))
        assertEquals(1, result.keys.count())
        (result[eventKey] as? Map<*, *>)?.let {
            assertEquals(19, it["fb_level"])
        }
    }

    @Test
    fun mapDispatchWithMultipleObjectMappings() {
        val dispatch = TealiumEvent(
                "level_up",
                mapOf("level" to 19,
                        "product_availability" to "In Stock"))
        val eventKey = "event"
        val lookup = mapOf("level" to "$eventKey.fb_level",
                "product_availability" to "$eventKey.fb_product_availability")

        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey(eventKey))
        (result[eventKey] as? Map<*, *>)?.let {
            assertEquals(19, it["fb_level"])
            assertEquals("In Stock", it["fb_product_availability"])
        }
    }

    @Test
    fun mapDispatchWithObjectMappingsAndExtraProperties() {
        val dispatch = TealiumEvent(
                "purchase",
                mapOf("order_total" to 5,
                        "order_currency" to "USD",
                        "product_color" to "red"
                ))
        val purchaseKey = "purchase"
        val purchasePropertiesKey = "purchase_properties"
        val lookup = mapOf(
                "order_total" to "$purchaseKey.fb_purchase_amount",
                "order_currency" to "$purchaseKey.fb_purchase_currency",
                "product_color" to "$purchasePropertiesKey.fb_product_color"
        )
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey(purchaseKey))
        assertTrue(result.containsKey(purchasePropertiesKey))

        (result[purchaseKey] as? Map<*, *>)?.let {
            assertEquals(5, it["fb_purchase_amount"])
            assertEquals("USD", it["fb_purchase_currency"])
        }
        (result[purchasePropertiesKey] as? Map<*, *>)?.let {
            assertEquals("red", it["fb_product_color"])
        }
    }

    @Test
    fun mapDispatchWithMultipleDotParams() {
        val dispatch = TealiumEvent(
                "purchase",
                mapOf("order_total" to 5,
                        "order_currency" to "USD",
                        "product_color" to "red"
                ))
        val purchaseKey = "purchase"
        val purchasePropertiesKey = "purchase_properties"
        val lookup = mapOf(
                "order_total" to "$purchaseKey.fb_purchase_amount",
                "order_currency" to "$purchaseKey.fb_purchase_currency",
                "product_color" to "$purchasePropertiesKey.fb_product_color"
        )
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey(purchaseKey))
        assertTrue(result.containsKey(purchasePropertiesKey))

        (result[purchaseKey] as? Map<*, *>)?.let {
            assertEquals(5, it["fb_purchase_amount"])
            assertEquals("USD", it["fb_purchase_currency"])
        }
        (result[purchasePropertiesKey] as? Map<*, *>)?.let {
            assertEquals("red", it["fb_product_color"])
        }
    }

    @Test
    fun mapDispatchWithMultipleDotParamsWithSameNameSourceAndDestinations() {
        val dispatch = TealiumEvent(
                "user_event",
                mapOf("event_name" to "level_up",
                        "current_level" to 10,
                        "high_score" to 5000))
        val eventKey = "event"
        val propertiesKey = "event_properties"
        val lookup = mapOf(
                "event_name" to "event_name",
                "current_level" to "$eventKey.current_level",
                "high_score" to "$eventKey.high_score,$propertiesKey.high_score"
        )
        val result = RemoteCommandParser.mapPayload(dispatch.payload(), lookup)
        assertTrue(result.containsKey(eventKey))
        assertTrue(result.containsKey(propertiesKey))

        (result[eventKey] as? Map<*, *>)?.let {
            assertEquals(10, it["current_level"])
            assertEquals(5000, it["high_score"])
        }
        (result[propertiesKey] as? Map<*, *>)?.let {
            assertEquals(5000, it["high_score"])
        }
    }
}