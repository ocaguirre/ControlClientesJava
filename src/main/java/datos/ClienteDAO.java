
package datos;

import dominio.Cliente;
import java.sql.SQLException;
import java.util.List;


public interface ClienteDAO {
    
    
    public List<Cliente> select() throws SQLException;
    
    public Cliente selectOneRegister(Cliente cliente) throws SQLException;
    
    public int delete(Cliente cliente) throws SQLException;
   
    public int update(Cliente cliente) throws SQLException;
    
    public int insert(Cliente cliente) throws SQLException;
}
