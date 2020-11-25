import com.jetbrains.handson.httpapi.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class OrderRouteTests {
    @Test
    fun testGetOrder() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/order/2020-04-06-01").apply {
                assertEquals(
                        """{"number":"2020-04-06-01","contents":[{"item":"Ham Sandwich","amount":2,"price":5.5},{"item":"Water","amount":1,"price":1.5},{"item":"Beer","amount":3,"price":2.3},{"item":"Cheesecake","amount":1,"price":3.75}]}""",
                        response.content
                )
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testGetOrder2() {
        val array = arrayOf("a", "b", "c", "d", "e")
        for (param in array) {
            withTestApplication({ module(testing = false) }) {
                handleRequest(HttpMethod.Get, "/order/2020-04-06-01").apply {
                    assertEquals(
                            """{"number":"2020-04-06-01","contents":[{"item":"Ham Sandwich","amount":2,"price":5.5},{"item":"Water","amount":1,"price":1.5},{"item":"Beer","amount":3,"price":2.3},{"item":"Cheesecake","amount":1,"price":3.75}]}""",
                            response.content
                    )
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
