import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun equalityTest(): Unit {
        val test1 = "test"
        val test2 = "test"

        println(test1 == test2)
        println(test1 === test2)

        println(Person("John") == Person("John")) // false
        println(Person2("John") == Person2("John")) // true (due to default equals)
    }

    @Test
    fun branching() {
        val age = 21
        if (age < 21) {
            println("age < 21")
        } else {
            println("age > 21")
        }

        val price = 10
        val result = if (price < 10) "cheap" else "expensive"

        when (result) {
            "cheap" -> println("cheap")
            "expensive" -> println("expensive")
             else -> println("unknown")
        }

        val x = 5
        val resultNumber = when (x) {
            0, 5 -> "zero or five"
            in 1..4 -> "between 1 and 4"
            !in 12..15 -> "not between 12 and 15"
            !in listOf(1,2,3,4) -> "not between 1 and 4"
            else -> "other"
        }
    }


    @Test
    fun looping() {
        for (i in 1..5) {
            println("for i = $i")
        }

        repeat(5) {
            println("repeat i = $it")
        }

        for (i in 5 downTo 1 step 2) {
            println("i = $i")
        }

        val list = listOf("a", "b", "c")
        for (item in list) {
            println(item)
        }

        // destructuring the index
        for ((index, item) in list.withIndex()) {
            println("$index: $item")
        }

        list.forEach {
            println(it)
        }

        list.forEachIndexed { index, string ->
            println("$index: $string")
        }
    }
}