import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class main {

    private static final String url = "jdbc:mysql://localhost:3306/mydb";
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static void main(String[] args) {
        String query = "SELECT * FROM test";
        String dropTable = "DROP TABLE IF EXISTS test";
        String creatTable = "CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(45) DEFAULT('Alex'))";
        String insert = "INSERT INTO test (name) VALUES (?)";
        String update = "UPDATE test SET name=? WHERE name='Philip'";
        String delete = "DELETE FROM test WHERE name=?";

        try {
            //
            Properties prop = new Properties();
            String fileName = "src/db.config";
            try (FileInputStream fis = new FileInputStream(fileName)) {
                prop.load(fis);
            } catch (IOException ignored) {
            }

            //Подключение к БД
            con = DriverManager.getConnection(url, prop.getProperty("user"), prop.getProperty("password"));
            stmt = con.createStatement();
            stmt.executeUpdate(dropTable);

            //Создание таблицы (CREATE)
            stmt.executeUpdate(creatTable);

            //Заполнение таблицы (INSERT)
            PreparedStatement pstmtInsert = con.prepareStatement(insert);
            pstmtInsert.setString(1, "Mikle");
            pstmtInsert.executeUpdate();
            pstmtInsert.setString(1, "Philip");
            pstmtInsert.executeUpdate();
            pstmtInsert.close();

            //Чтение таблицы (READ)
            System.out.println("Request before update");
            printer(query);

            //Обновление таблицы (UPDATE)
            PreparedStatement pstmtUpdate = con.prepareStatement(update);
            pstmtUpdate.setString(1, "Inga");
            pstmtUpdate.executeUpdate();
            pstmtUpdate.close();

            System.out.println("Query after update");
            printer(query);

            //Удаление данных (DELETE)
            PreparedStatement pstmtDelete = con.prepareStatement(delete);
            pstmtDelete.setString(1, "Inga");
            pstmtDelete.executeUpdate();
            pstmtDelete.close();

            System.out.println("Delete data");
            printer(query);


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();

        } finally {

            try { con.close(); } catch(SQLException ignored) { }
            try { stmt.close(); } catch(SQLException ignored) { }
            try { rs.close(); } catch(SQLException ignored) { }
        }
    }

    private static void printer(String query) throws SQLException {
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            System.out.printf("id: %d, name: %s \n", id, name);
        }
    }


}

