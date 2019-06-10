
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/university";
        String user = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(url, user, password);
        String line = "";
        do {
            System.out.println("Please write the command");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            line = reader.readLine();
            PreparedStatement preparedStatement = null;
            String department_name = "";
            if (line.contains("Who is head of department")) {
                preparedStatement = connection.prepareStatement("SELECT* FROM departments");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (line.contains(resultSet.getString(2)))
                        System.out.print("Head of " + resultSet.getString(2) + " department is " + resultSet.getString(3));
                }
            } else if (line.contains("Show statistic")) {
                preparedStatement = connection.prepareStatement("SELECT d.name, lectors.degree FROM lectors\n" +
                        "JOIN ref_departments_lectors rdl ON lectors.id = rdl.id_lectors\n" +
                        "JOIN departments d ON rdl.id_departments = d.id");
                ResultSet resultSet = preparedStatement.executeQuery();
                int count = 0;
                int count2 = 0;
                int count3 = 0;
                while (resultSet.next()) {
                    if (line.contains(resultSet.getString(1))) {
                        if (resultSet.getString(2).equals("assistant"))
                            count++;
                        if (resultSet.getString(2).equals("associate professor"))
                            count2++;
                        if (resultSet.getString(2).equals("professor"))
                            count3++;
                    }
                }
                System.out.println("assistant " + count + "\nassociate professor " + count2 + "\nprofessor " + count3);
            } else if (line.contains("Show the average salary for department")) {
                preparedStatement = connection.prepareStatement("SELECT d.name, lectors.salary FROM lectors\n" +
                        "JOIN ref_departments_lectors rdl ON lectors.id = rdl.id_lectors\n" +
                        "JOIN departments d ON rdl.id_departments = d.id");
                ResultSet resultSet = preparedStatement.executeQuery();
                int count = 0;
                int salary = 0;
                while (resultSet.next()) {
                    if (line.contains(resultSet.getString(1))) {
                        salary += resultSet.getInt(2);
                        count++;
                        department_name = resultSet.getString(1);
                    }
                }
                System.out.println("The average salary of " + department_name + " is " + salary / count);
            } else if (line.contains("Show count of employee for")) {
                preparedStatement = connection.prepareStatement("SELECT d.name, lectors.surname FROM lectors\n" +
                        "JOIN ref_departments_lectors rdl ON lectors.id = rdl.id_lectors\n" +
                        "JOIN departments d ON rdl.id_departments = d.id");
                ResultSet resultSet = preparedStatement.executeQuery();
                int count = 0;
                while (resultSet.next()) {
                    if (line.contains(resultSet.getString(1)))
                        count++;
                }
                System.out.println(count);
            } else if (line.contains("Global search by")) {
                preparedStatement = connection.prepareStatement("SELECT lectors.surname, lectors.name FROM lectors");
                ResultSet resultSet = preparedStatement.executeQuery();
                CharSequence c = line.subSequence(17, line.length());
                while (resultSet.next()) {
                    if (resultSet.getString(1).contains(c) || resultSet.getString(2).contains(c))
                        System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
                }
            } else
                System.out.println("Wrong command! Try again.");
        }
        while (line.length() > 0);
        connection.close();
        }

    }


