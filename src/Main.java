import javax.swing.*;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.text.ParseException;


public class Main {
    public Main() throws IOException {
    }

    public static void main(String args[]) throws ParseException {

        try {

            String url = "jdbc:sqlanywhere:UserID=dba;Password=sql;Host=192.168.10.85:2638;ServerName=d4wvlad;DatabaseName=d4w";

            Connection con = DriverManager.getConnection(url);

            Statement stmt = con.createStatement();
            String sql = "SELECT patients_cart_num, firstname, surname, last_visit\n" +
                    "FROM patients INNER JOIN (SELECT pat_id, last_visit\n" +
                    "FROM (SELECT pat_id, MAX(app_date) AS last_visit\n" +
                    "FROM a_appointments\n" +
                    "WHERE pat_id IS NOT NULL\n" +
                    "GROUP BY pat_id) X\n" +
                    "WHERE last_visit BETWEEN '2021-01-27' AND '2022-01-28' // Период последнего визита в книге записи\n" +
                    "GROUP BY pat_id, last_visit) AS apps\n" +
                    "ON patients.patient_id = apps.pat_id";

            ResultSet rs = stmt.executeQuery(sql);

            BufferedWriter writer = new BufferedWriter(new FileWriter("result.csv"));

            try {
                while (rs.next()) {
                    Integer card_num = rs.getInt(1);
                    String firstName = rs.getString(2);
                    String lastName = rs.getString(3);
                    Date lastvisit = rs.getDate(4);
                    System.out.println("empId:" + card_num);
                    System.out.println("firstName:" + firstName);
                    System.out.println("lastName:" + lastName);
                    System.out.println("last visit:" + lastvisit);
                    System.out.println("");

                    writer.write(card_num+";");
                    writer.write(firstName+";");
                    writer.write(lastName+";");
                    writer.write(lastvisit+";"+"\n");

                }
                writer.flush();

                writer.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }







        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection error!", "Title", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}