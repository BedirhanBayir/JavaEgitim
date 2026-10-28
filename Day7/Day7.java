public class Day7 {
    public static void main(String[] args) {

        Student s1 = new Student();
        s1.printInfo();
        System.out.println("Onur öğrencisi: " + s1.isHonorStudent());

        Student s2 = new Student("Ayşe Kaya", "2021001", 3.8, true);
        s2.printInfo();
        System.out.println("Onur öğrencisi: " + s2.isHonorStudent());

        Student s3 = new Student("Berk Arslan", "2021002");
        s3.printInfo();
        System.out.println("Onur öğrencisi: " + s3.isHonorStudent());
    }
}
