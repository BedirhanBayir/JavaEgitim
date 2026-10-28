public class Day5 {

    static double add(double a, double b) {
        return a + b;
    }

    static double subtract(double a, double b) {
        return a - b;
    }

    static double multiply(double a, double b) {
        return a * b;
    }

    static double divide(double a, double b) {
        if (b == 0) {
            System.out.println("Hata: Sıfıra bölme!");
            return 0;
        }
        return a / b;
    }

    static boolean isEven(int number) {
        return number % 2 == 0;
    }

    static int findMax(int[] numbers) {
        int max = numbers[0];
        for (int n : numbers) {
            if (n > max) max = n;
        }
        return max;
    }

    static void printSeparator() {
        System.out.println("----------------------------");
    }

    static String greet(String name) {
        return "Merhaba, " + name + "!";
    }

    public static void main(String[] args) {
        printSeparator();
        System.out.println("Toplama   : " + add(10, 5));
        System.out.println("Çıkarma   : " + subtract(10, 5));
        System.out.println("Çarpma    : " + multiply(10, 5));
        System.out.println("Bölme     : " + divide(10, 4));
        System.out.println("Bölme     : " + divide(10, 0));
        printSeparator();
        System.out.println("5 çift mi?: " + isEven(5));
        System.out.println("8 çift mi?: " + isEven(8));
        printSeparator();
        int[] nums = {3, 17, 8, 42, 6};
        System.out.println("Max       : " + findMax(nums));
        printSeparator();
        System.out.println(greet("Zeynep"));
    }
}
