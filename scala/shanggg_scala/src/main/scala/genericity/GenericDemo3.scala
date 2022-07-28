package genericity


// 泛型的上限和下限
object GenericDemo3 {

  def printer(petContainer: PetContainer[Pet]): Unit = {
    println(petContainer.pet.name)
  }

  def main(args: Array[String]): Unit = {
    val dog: Dog = new Dog()
    val cat: Cat = new Cat()
    val lion: Lion = new Lion()

    val dogContainer: PetContainer[Dog] = new PetContainer[Dog](dog)
    val catContainer: PetContainer[Cat] = new PetContainer[Cat](cat)
    // Error
//    val lionContainer: PetContainer[Lion] = new PetContainer[Lion](lion)
  }
}

abstract class Animal {
  val name: String
}

abstract class Pet extends Animal {

}

class Dog extends Pet {
  override val name: String = "dog"
}

class Cat extends Pet {
  override val name: String = "cat"
}

class Lion extends Animal {
  override val name: String = "lion"
}

class PetContainer[P <: Pet](val pet: P) {}
