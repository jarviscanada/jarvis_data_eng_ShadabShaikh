import scala.io.Source

/**
 * Count number of elements
 * Get the first element
 * Get the last element
 * Get the first 5 elements
 * Get the last 5 elements
 *
 * hint: use the following methods
 * head
 * last
 * size
 * take
 * tails
 */
val ls = List.range(0,10)
val elementCount = ls.size
val firstEle = ls.head
val lastEle= ls.last
val firstFive = ls.take(5)
val lastFive = ls.takeRight(5)

/**
 * Double each number from the numList and return a flatten list
 * e.g.res4: List[Int] = List(2, 3, 4)
 *
 * Compare flatMap VS ls.map().flatten
 */
val numList = List(List(1,2), List(3));
val doubler = numList.flatMap(n => n.map(_ * 2))
val doubler2 = numList.map(n => n.map(_ * 2)).flatten

/**
 * Sum List.range(1,11) in three ways
 * hint: sum, reduce, foldLeft
 *
 * Compare reduce and foldLeft
 * https://stackoverflow.com/questions/7764197/difference-between-foldleft-and-reduceleft-in-scala
 */

val numbList = List.range(1,11)
val sum1 = numbList.sum
val sum2 = numbList.foldLeft(0)((memo: Int, n: Int) => memo + n)
val sum3 = numbList.reduce{(s1: Int, s2: Int) => s1 + s2}

/**
 * Practice Map and Optional
 *
 * Map question1:
 *
 * Compare get vs getOrElse (Scala Optional)
 * countryMap.get("Amy");
 * countryMap.getOrElse("Frank", "n/a");
 */
val countryMap = Map("Amy" -> "Canada", "Sam" -> "US", "Bob" -> "Canada")
countryMap.get("Amy")
countryMap.get("edward")
countryMap.getOrElse("edward", "n/a")


/**
 * Map question2:
 *
 * create a list of (name, country) tuples using `countryMap` and `names`
 * e.g. res2: List[(String, String)] = List((Amy,Canada), (Sam,US), (Eric,n/a), (Amy,Canada))
 */
val names = List("Amy", "Sam", "Eric", "Amy")
val nameTuples = names.map(name => (name, countryMap.getOrElse(name, "n/a")))


/**
 * Map question3:
 *
 * count number of people by country. Use `n/a` if the name is not in the countryMap  using `countryMap` and `names`
 * e.g. res0: scala.collection.immutable.Map[String,Int] = Map(Canada -> 2, n/a -> 1, US -> 1)
 * hint: map(get_value_from_map) ; groupBy country; map to (country,count)
 */
val countryCount = nameTuples.groupBy(_._2).mapValues(_.size)


/**
 * number each name in the list from 1 to n
 * e.g. res3: List[(Int, String)] = List((1,Amy), (2,Bob), (3,Chris))
 */
val names2 = List("Amy", "Bob", "Chris", "Dann")
val numberedList = List.range(1, names2.size+1).zip(names2)


/**
 * SQL questions1:
 *
 * read file lines into a list
 * lines: List[String] = List(id,name,city, 1,amy,toronto, 2,bob,calgary, 3,chris,toronto, 4,dann,montreal)
 */
val lines = Source.fromURL(getClass.getResource("/employees.csv")).getLines().map(_.split(",")).toList.drop(1)

/**
 * SQL questions2:
 *
 * Convert lines to a list of employees
 * e.g. employees: List[Employee] = List(Employee(1,amy,toronto), Employee(2,bob,calgary), Employee(3,chris,toronto), Employee(4,dann,montreal))
 */
class Employee(var id: Int, var name: String, var city: String, var age: Int = 0) {
  override def toString: String = s"Employee($id, $name, $city, $age)"
}
var employees = lines
  .map((arr) => new Employee(arr(0).toInt, arr(1), arr(2), arr(3).toInt))


/**
 * SQL questions3:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 *
 * result:
 * upperCity: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(2,bob,CALGARY,19), Employee(3,chris,TORONTO,20), Employee(4,dann,MONTREAL,21), Employee(5,eric,TORONTO,22))
 */
//employees.foreach(employee => employee.city = employee.city.toUpperCase)
//employees
var upperCity = employees.map((emp: Employee) =>
  new Employee(emp.id, emp.name, emp.city.toUpperCase, emp.age))


/**
 * SQL questions4:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 * WHERE city = 'toronto'
 *
 * result:
 * res5: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(3,chris,TORONTO,20), Employee(5,eric,TORONTO,22))
 */
var upperCities = upperCity.filter(employee => employee.city == "TORONTO")


/**
 * SQL questions5:
 *
 * Implement the following SQL logic using functional programming
 *
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city
 *
 * result:
 * cityNum: scala.collection.immutable.Map[String,Int] = Map(CALGARY -> 1, TORONTO -> 3, MONTREAL -> 1)
 */
val cityCount = upperCity.groupBy(_.city).mapValues(_.size)


/**
 * SQL questions6:
 *
 * Implement the following SQL logic using functional programming
 *
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city,age
 *
 * result:
 * res6: scala.collection.immutable.Map[(String, Int),Int] = Map((MONTREAL,21) -> 1, (CALGARY,19) -> 1, (TORONTO,20) -> 2, (TORONTO,22) -> 1)
 */
val cityAgeCount = upperCity.groupBy(employee => (employee.city, employee.age)).mapValues(_.size)
