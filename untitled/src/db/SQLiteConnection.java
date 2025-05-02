package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    //connection string
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    //conexao/criacao do banco sqlite
    public static Connection connect(){
        try{
            return DriverManager.getConnection(URL);
        }catch(SQLException ex){
            System.out.println("Erro ao conectar SQL: " + ex.getMessage());
            return null;
        }
    }
}
