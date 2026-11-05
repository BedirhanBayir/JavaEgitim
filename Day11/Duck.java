public class Duck extends Animal implements Flyable, Swimmable {

    public Duck(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println(name + ": Vak vak!");
    }

    @Override
    public void fly() {
        System.out.println(name + " uçuyor!");
    }

    @Override
    public void swim() {
        System.out.println(name + " yüzüyor!");
    }
}
