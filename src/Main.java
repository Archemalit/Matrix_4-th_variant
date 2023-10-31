public class Main {
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в программу по работе с матрицами.");

        Matrix m1 = new Matrix();
        Matrix m2 = new Matrix();
     m1.interMatrix();
       m2.interMatrix();

//        m1.output();
//        m2.output();

        Matrix m3 = m1.solve(m2);
        m3.output();
    }
}