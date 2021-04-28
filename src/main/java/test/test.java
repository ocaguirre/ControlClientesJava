package test;

import datos.ClienteDaoJDBC;
import dominio.Cliente;
import java.sql.Connection;
import java.sql.SQLException;

public class test {

    public static void main(String[] args) throws SQLException {
        
    Cliente cliente = new Cliente(2);
        System.out.println("cliente = " + cliente);
    Cliente clienteSeleccionado = new ClienteDaoJDBC().selectOneRegister(cliente);
        System.out.println("clienteSeleccionado = " + clienteSeleccionado);
    
    }
}
