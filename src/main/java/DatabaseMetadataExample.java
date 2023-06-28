import java.sql.*;

public class DatabaseMetadataExample {
    public static void main(String[] args) {
        String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
        String USERNAME = "postgres";
        String PASSWORD = "postgres";

        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            DatabaseMetaData metadata = connection.getMetaData();

            String[] tableTypes = {"TABLE"};
            ResultSet tables = metadata.getTables(null, null, "%", tableTypes);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);

                ResultSet columns = metadata.getColumns(null, null, tableName, "%");

                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    System.out.println("\tColumn: " + columnName + " (" + columnType + ")");
                }

                ResultSet foreignKeys = metadata.getImportedKeys(connection.getCatalog(), null, tableName);

                while (foreignKeys.next()) {
                    String fkTableName = foreignKeys.getString("PKTABLE_NAME");
                    String fkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
                    String fkName = foreignKeys.getString("FK_NAME");
                    System.out.println("\tForeign key: " + fkName + " referencing " + fkTableName + "(" + fkColumnName + ")");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
