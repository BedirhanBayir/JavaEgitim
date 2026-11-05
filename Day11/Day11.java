public class Day11 {
    public static void main(String[] args) {

        // Animal a = new Animal("X"); // DERLEME HATASI: Animal abstract

        Animal[] animals = {
            new Dog("Karabaş"),
            new Eagle("Kartal"),
            new Duck("Donald")
        };

        System.out.println("===== Hayvan Sesleri (polimorfizm) =====");
        for (Animal animal : animals) {
            animal.makeSound();
        }

        System.out.println("\n===== Yetenekler =====");
        for (Animal animal : animals) {
            System.out.println("> " + animal.getName());
            animal.breathe();

            if (animal instanceof Flyable flyer) {
                flyer.fly();
            }
            if (animal instanceof Swimmable swimmer) {
                swimmer.swim();
                swimmer.dive(); // default metot
            }
            System.out.println();
        }

        // Interface tipiyle referans: sadece "uçabilenler" listesi
        Flyable[] flyers = { new Eagle("Şahin"), new Duck("Papi") };
        System.out.println("===== Uçabilenler =====");
        for (Flyable f : flyers) {
            f.fly();
        }
    }
}
