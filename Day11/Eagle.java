public class Eagle extends Animal implements Flyable {

    public Eagle(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println(name + ": Kreeee!");
    }

    @Override
    public void fly() {
        System.out.println(name + " yükseklerde süzülüyor!");
    }
}
