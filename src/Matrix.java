import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class OutCountOfChoices extends Exception
{
    public OutCountOfChoices(final String message) {
        super(message);
    }
}

class notRealFormat extends Exception
{
    public notRealFormat(final String message) {
        super(message);
    }
}

public class Matrix {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static String[] inter_menu = new String[] {"1) Построчный", "2) Поэлементный"};

    public int rowsCount;
    public int colsCount;
    public double[][] array;

    public Scanner scanner = new Scanner(System.in);

    public Boolean checkPatternOfSize(String answer, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(answer);
        return m.matches();
    }

    public Matrix add(Matrix matrix) {
        if (this.colsCount == matrix.colsCount && this.rowsCount == matrix.rowsCount) {
            Matrix summary_matrix = new Matrix();
            summary_matrix.rowsCount = this.rowsCount;
            summary_matrix.colsCount = this.colsCount;
            summary_matrix.array = new double[this.rowsCount][this.colsCount];

            for (int i = 0; i < this.rowsCount; i++) {
                for (int j = 0; j < this.colsCount; j++) {
                    summary_matrix.array[i][j] = this.array[i][j] + matrix.array[i][j];
                }
            }
            return summary_matrix;
        }
        System.out.println("Сложение невозможно.");
        return null;
    }

    public Matrix multiplication(Matrix matrix) {
        String answer;
        Matrix multiplication_matrix = new Matrix();
        if (this.colsCount == matrix.rowsCount) {
            multiplication_matrix.rowsCount = this.rowsCount;
            multiplication_matrix.colsCount = matrix.colsCount;
            multiplication_matrix.array = new double[multiplication_matrix.rowsCount][multiplication_matrix.colsCount];
            for (int i = 0; i < this.rowsCount; i++) {
                for (int w = 0; w < matrix.colsCount; w++) {
                    double summary = 0;
                    for (int j = 0; j < matrix.rowsCount; j++) {
                        summary = summary + (this.array[i][j] * matrix.array[j][w]);
                    }
                    multiplication_matrix.array[i][w] = summary;
                }
            }
            return multiplication_matrix;
        } else if (this.rowsCount == matrix.colsCount) {
            System.out.print("Прямое умножение невозможно. Умножить матрицы в обратном порядке? ");
            answer = this.scanner.next();
            if (answer.equalsIgnoreCase("да") | answer.equalsIgnoreCase("д") | answer.equalsIgnoreCase("yes") | answer.equalsIgnoreCase("y")) {
                multiplication_matrix.rowsCount = matrix.rowsCount;
                multiplication_matrix.colsCount = this.colsCount;
                multiplication_matrix.array = new double[multiplication_matrix.rowsCount][multiplication_matrix.colsCount];
                for (int i = 0; i < matrix.rowsCount; i++) {
                    for (int w = 0; w < this.colsCount; w++) {
                        double summary = 0;
                        for (int j = 0; j < this.rowsCount; j++) {
                            summary = summary + (matrix.array[i][j] * this.array[j][w]);
                        }
                        multiplication_matrix.array[i][w] = summary;
                    }
                }
                return multiplication_matrix;
            } else {
                return null;
            }
        } else {
            System.out.println("Умножение невозможно.");
            return null;
        }
    }

    public static Matrix gauss(Matrix gauss_matrix) {
        for (int i = 0; i < Math.min(gauss_matrix.rowsCount, gauss_matrix.colsCount); i++) {
            double maxNumber = Math.abs(gauss_matrix.array[i][i]);
            int maxRow = i;
            for (int j = i + 1; j < gauss_matrix.rowsCount; j++) {
                if (Math.abs(gauss_matrix.array[j][i]) > maxNumber) {
                    maxNumber = Math.abs(gauss_matrix.array[j][i]);
                    maxRow = j;
                }
            }

            double[] temp_array = gauss_matrix.array[i];
            gauss_matrix.array[i] = gauss_matrix.array[maxRow];
            gauss_matrix.array[maxRow] = temp_array;

            // В треугольную форму снизу
            for (int k = i + 1; k < gauss_matrix.rowsCount; k++) {
                double coef = -(gauss_matrix.array[k][i] / gauss_matrix.array[i][i]);
                // Чтоб не было много чисел после точки
                if (coef % 2 != 1 && coef % 2 != 0) {
                    double temp = gauss_matrix.array[i][i];
                    for (int j = 0; j < gauss_matrix.colsCount; j++) {
                        gauss_matrix.array[i][j] *= gauss_matrix.array[k][i];
                    }
                    for (int j = 0; j < gauss_matrix.colsCount; j++) {
                        gauss_matrix.array[k][j] *= temp;
                    }
                    coef = -(gauss_matrix.array[k][i] / gauss_matrix.array[i][i]);
                }

                for (int j = i; j < gauss_matrix.colsCount; j++) {
                    if (i == j) {
                        gauss_matrix.array[k][j] = 0;
                    } else {
                        gauss_matrix.array[k][j] += coef * gauss_matrix.array[i][j];
                    }
                }
            }
        }
        return gauss_matrix;
    }

    public int search_rank(Matrix matrix) {
        matrix = this.gauss(matrix);
        int result = 0;
        int temp;
        for (int i = 0; i < matrix.rowsCount; i++) {
            temp = 0;
            for (int j = 0; j < matrix.colsCount; j++) {
                if (matrix.array[i][j] == 0) {
                    temp++;
                }
            }
            if (temp == matrix.colsCount) {
                result++;
            }
        }
        return matrix.rowsCount - result;
    }

//    public Matrix pre_solve(Matrix gauss_matrix) {
//
//    }

    public Matrix solve(Matrix matrix) {
//        matrix -> 3 x 1
//        this.array -> 3 x 3
        if (matrix.colsCount != 1) {
            return null;
        }
        Matrix gauss_matrix = new Matrix();
        gauss_matrix.rowsCount = this.rowsCount;
        gauss_matrix.colsCount = this.colsCount + 1;
        gauss_matrix.array = new double[gauss_matrix.rowsCount][gauss_matrix.colsCount];
        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < gauss_matrix.colsCount; j++) {
                if (j == gauss_matrix.colsCount - 1) {
                    gauss_matrix.array[i][j] = matrix.array[i][0];
                } else {
                    gauss_matrix.array[i][j] = this.array[i][j];
                }
            }
        }

        gauss_matrix.array = gauss(gauss_matrix).array;

        if (this.search_rank(this) == this.search_rank(gauss_matrix) && this.search_rank(this) == this.colsCount) {
            for (int i = gauss_matrix.rowsCount - 1; i >= 0 ; i--) {
                for (int k = i - 1; k >= 0; k--) {
                    double coef = -(gauss_matrix.array[k][i] / gauss_matrix.array[i][i]);

                    // Чтоб не было много чисел после точки
                    if (coef % 2 != 1 && coef % 2 != 0) {
                        double temp = gauss_matrix.array[i][i];
                        for (int j = 0; j < gauss_matrix.colsCount; j++) {
                            gauss_matrix.array[i][j] *= gauss_matrix.array[k][i];
                        }
                        for (int j = 0; j < gauss_matrix.colsCount; j++) {
                            gauss_matrix.array[k][j] *= temp;
                        }
                        coef = -(gauss_matrix.array[k][i] / gauss_matrix.array[i][i]);
                    }

                    for (int j = gauss_matrix.colsCount - 1; j >= 0; j--) {
                        if (i == j) {
                            gauss_matrix.array[k][j] = 0;
                        } else {
                            gauss_matrix.array[k][j] += coef * gauss_matrix.array[i][j];
                        }
                    }
                }
            }
            System.out.println("Одно решение.");
            Matrix m_answer = new Matrix();
            m_answer.rowsCount = gauss_matrix.rowsCount;
            m_answer.colsCount = 1;
            m_answer.array = new double[m_answer.rowsCount][m_answer.colsCount];
            for (int i = 0; i < m_answer.rowsCount; i++) {
                m_answer.array[i][0] = gauss_matrix.array[i][gauss_matrix.colsCount - 1] / gauss_matrix.array[i][i];
            }
            return m_answer;
        } else if (this.search_rank(this) != this.search_rank(gauss_matrix)) {
            System.out.println("Система не имеет решений.");
            return null;
        } else {
            System.out.println("Система имеет бесконечное количество решений.");
            return null;
        }
    }

    public void output() {
        if (this.rowsCount * this.colsCount != 0) {
            System.out.println("Матрица (" + this.rowsCount + " * " + this.colsCount + "):");
            int max = 0;
            for (int i = 0; i < this.rowsCount; ++i) {
                for (int j = 0; j < this.colsCount; ++j) {
                    String text = Double.toString(array[i][j]);
                    if (text.length() > max){
                        max = text.length();
                    };
                }
            }
            for (int i = 0; i < this.rowsCount; ++i) {
                for (int j = 0; j < this.colsCount; ++j) {
                    System.out.format("%" + max + "s ", this.array[i][j]);
                }
                System.out.println();
            }
        } else System.out.println("Матрица еще не определена");
    }

    public void interMatrix() {
        System.out.println("Варианты ввода данных в матрицы:");

        for (int i = 0; i < inter_menu.length; i++) {
            System.out.println(inter_menu[i]);
        }
        System.out.print("Введите номер действия: ");
        String str_answer;
        int answer;
        while (true) {
            try {
                str_answer = this.scanner.nextLine();
                answer = Integer.parseInt(str_answer);
                if (answer > inter_menu.length | answer <= 0) {
                    throw new OutCountOfChoices("Something wrong");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.print(ANSI_RED_BACKGROUND + "Вы ввели что-то не то!" + ANSI_RESET + " Введите число ещё раз: ");
//                this.scanner.next();
            } catch (OutCountOfChoices e2) {
                System.out.print(ANSI_RED_BACKGROUND + "Такого действия не существует. Возможно, вы ввели не то число." + ANSI_RESET + " Введите число ещё раз: ");
            }
            }

        System.out.print("Введите размер матрицы через пробел: ");
        String size;
//        String trash = this.scanner.nextLine();
        while (true) {
            try {
                size = this.scanner.nextLine();
                System.out.println(size);
                if (!checkPatternOfSize(size, "[1-9]+ [1-9]+")) {
                    throw new notRealFormat("Something wrong");
                } else {
                    String [] size_array = size.split(" ");
                    this.rowsCount = Integer.parseInt(size_array[0]);
                    this.colsCount = Integer.parseInt(size_array[1]);
                    array = new double[this.rowsCount][this.colsCount];
                    break;
                }
            } catch (notRealFormat e) {
                System.out.print(ANSI_RED_BACKGROUND + "Вы ввели что-то не то!" + ANSI_RESET + " Введите ещё раз (в формате \"число число\"): ");
            }



        }
        String numbers;
        double number;
        if (answer == 1) {
            System.out.print("Введите знак разделителя (один символ, кроме \",0123456789-+\"): ");
            String determinate;
            while (true) {
                try {
                    determinate = this.scanner.nextLine();
                    if (determinate.length() == 1) {
                        if (!".0123456789-+".contains(determinate)) {
                            break;
                        } else {
                            throw new notRealFormat("Something wrong");
                        }
                    } else {
                        throw new notRealFormat("Something wrong");
                    }
                } catch (notRealFormat e) {
                    System.out.print(ANSI_RED_BACKGROUND + "Вы ввели что-то не то!" + ANSI_RESET + " Введите ещё раз: ");
                }

            }
            for (int i = 0; i < this.rowsCount; i++) {
                System.out.format("Введите числа через пробел в строке под номером %d: ", i + 1);
                while (true) {
                    numbers = scanner.nextLine();
                    String[] split_numbers = numbers.split(determinate);
                    try {
                    if (split_numbers.length != this.colsCount) {
                        throw new OutCountOfChoices("Something wrong");
                    } else {
                        for (int j = 0; j < this.colsCount; j++) {
                                number = Double.parseDouble(split_numbers[j]);
                                this.array[i][j] = number;
                            }
                        break;
                        }
                    }
                catch (NumberFormatException e) {
                    System.out.print(ANSI_RED_BACKGROUND + "Вы ввели что-то не то в строке!" + ANSI_RESET);
                    System.out.format(" Введите числа через пробел в строке под номером %d заново: ", i + 1);
//                    Arrays.fill(this.array[i], 0);
//                    this.scanner.next();
                } catch (OutCountOfChoices e2) {
                        System.out.print(ANSI_RED_BACKGROUND + "Количество введеных чисел не совпадает с размером матрицы." + ANSI_RESET);
                        System.out.format(" Введите числа через пробел в строке под номером %d заново: ", i + 1);
                    }
                }
            }
        } else if (answer == 2) {
            String num;
            for (int i = 0; i < this.rowsCount; i++) {
                for (int j = 0; j < this.colsCount; j++) {
                    System.out.format("Введите число на позиции (%d, %d): ", i + 1, j + 1);
                    while (true) {
                        try {
                            num = this.scanner.nextLine();
                            if (num.contains("/")) {
                                String[] split_details = num.split("/");
                                if (split_details.length == 2) {
                                    double first = Double.parseDouble(split_details[0]);
                                    double second = Double.parseDouble(split_details[1]);
                                    this.array[i][j] = first / second;
                                }
                            } else {
                                number = Double.parseDouble(num);
                                this.array[i][j] = number;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.print(ANSI_RED_BACKGROUND + "Вы ввели что-то не то в строке!" + ANSI_RESET);
                            System.out.format(" Введите числа через пробел в строке под номером %d заново: ", i + 1);
//                    this.scanner.next();
                        }
                    }
                }
            }


        }

        }

    }

