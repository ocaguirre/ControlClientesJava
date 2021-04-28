package datos;

import dominio.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDaoJDBC implements ClienteDAO {

    private Connection conexionTransaccional;
    private static final String SQL_SELECT = "SELECT id_cliente, nombre, apellido, email, telefono, saldo "
            + "FROM cliente";

    private static final String SQL_SELECT_BY_ID = "SELECT id_cliente, nombre, apellido, email, telefono, saldo "
            + "FROM cliente WHERE id_cliente = ?";

    private static final String SQL_DELETE = "DELETE FROM cliente WHERE id_cliente =?";

    private static final String SQL_UPDATE = "UPDATE cliente SET nombre=?, apellido=?, email=?, telefono=?, saldo=? "
            + "WHERE id_cliente=?";

    private static final String SQL_INSERT = "INSERT INTO cliente (nombre, apellido, email, telefono, saldo) "
            + "VALUES (?,?,?,?,?)";

    public ClienteDaoJDBC() {
    }

    public ClienteDaoJDBC(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    @Override
    public List<Cliente> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente cliente = null;

        List<Cliente> clientes = new ArrayList<Cliente>();

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                double saldo = rs.getDouble("saldo");

                cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
//                cliente.setIdCliente(idCliente);
//                cliente.setNombre(nombre);
//                cliente.setApellido(apellido);
//                cliente.setEmail(email);
//                cliente.setTelefono(telefono);
//                cliente.setSaldo(saldo);

                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            rs.close();
            stmt.close();

            if (this.conexionTransaccional == null) {
                conn.close();
            }

        }

        return clientes;
    }

    @Override
    public int delete(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, cliente.getIdCliente());
            registros = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            stmt.close();
            if (this.conexionTransaccional == null) {
                conn.close();
            }
        }

        return registros;
    }

    @Override
    public int update(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.setDouble(5, cliente.getSaldo());
            stmt.setInt(6,cliente.getIdCliente());
            registros = stmt.executeUpdate();
            System.out.println("******************************************");
            System.out.println("******************************************");
        } catch (SQLException ex) {
            System.out.println("*////////////////////////////////////");
            System.out.println("*////////////////////////////////////");
            ex.printStackTrace(System.out);
        } finally {
            stmt.close();
            if (this.conexionTransaccional == null) {
                conn.close();
            }
        }

        return registros;
    }

    @Override
    public int insert(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.setDouble(5, cliente.getSaldo());
            registros = stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            stmt.close();
            if (this.conexionTransaccional == null) {
                conn.close();
            }
        }
        return registros;
    }

    @Override
    public Cliente selectOneRegister(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, cliente.getIdCliente());
            rs = stmt.executeQuery();
            System.out.println("rs = " + rs);
            //rs.absolute(1); //nos posicionamos en el registro que quremos 
            rs.next();
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String email = rs.getString("email");
            String telefono = rs.getString("telefono");
            double saldo = rs.getDouble("saldo");
            System.out.println("nombre que busco = " + nombre);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setEmail(email);
            cliente.setTelefono(telefono);
            cliente.setSaldo(saldo);
            System.out.println("cliente completo = " + cliente);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            rs.close();
            stmt.close();

            if (this.conexionTransaccional == null) {
                conn.close();
            }

        }

        return cliente;
    }

}
