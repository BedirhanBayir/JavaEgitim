public class Dog extends Animal implements Swimmable {

    public Dog(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println(name + ": Hav hav!");
    }

    @Override
    public void swim() {
        System.out.println(name + " köpek stiliyle yüzüyor!");
    }
}
