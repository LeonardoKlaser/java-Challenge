package repository;

import Models.Usuario;
import db.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRepository {
    public void adicionar(Usuario usuario){
        if(buscarPorEmail(usuario.getEmail()) != null){
            System.out.println("Usuario com este email ja existe!");
            return;
        }
        String sql = "INSERT INTO usuarios(nome, email, tipo, cpf) VALUES(?, ?, ?, ?)";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
           stmt.setString(1,usuario.getNome());
           stmt.setString(2, usuario.getEmail());
           stmt.setString(3, usuario.getRole());
           stmt.setString(4, usuario.getDocument());
           stmt.executeUpdate();
            System.out.println("Usuario adicionado ao banco de dados");
        }catch (SQLException e){
            System.out.println("Erro ao adicionar usuario: " + e.getMessage());
        }
    }

    public Usuario buscarPorEmail(String email){
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Usuario user = new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("cpf")
                );
                user.setId(rs.getInt("id"));
                return user;
            }
        }catch (SQLException e){
            System.out.println("Erro ao buscar usuario: " + e.getMessage());
        }
        return null;
    }

    public boolean isAdmin(String email){
        String sql = "SELECT tipo FROM usuarios WHERE email = ?";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                if(rs.getString("tipo").equals("admin"))
                    return true;
            }
        }catch (SQLException e){
            System.out.println("Erro ao buscar usuario " + e.getMessage());
        }
        return false;
    }

    public Usuario buscarPorId(Integer id){
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try(Connection conn = SQLiteConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Usuario user = new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getString("cpf")
                );
                user.setId(rs.getInt("id"));
                return user;
            }
        }catch (SQLException e){
            System.out.println("Erro ao buscar usuario: " + e.getMessage());
        }
        return null;
    }

}
