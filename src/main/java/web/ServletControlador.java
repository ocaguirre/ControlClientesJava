package web;

import datos.ClienteDaoJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion != null) {

            switch (accion) {
                case "editar": {
                    try {
                        this.editarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
                break;
                case "eliminar": {
                    try {
                        this.eliminarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
                break;
                default:
                    this.accionDefault(request, response);

            }

        } else {
            this.accionDefault(request, response);
        }

    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Cliente> clientes = new ClienteDaoJDBC().select();
            System.out.println("clientes = " + clientes);
            HttpSession sesion = request.getSession();
            sesion.setAttribute("clientes", clientes);
            sesion.setAttribute("totalClientes", clientes.size());
            sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
            //request.getRequestDispatcher("clientes.jsp").forward(request, response);

            response.sendRedirect("clientes.jsp");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

    }

    private double calcularSaldoTotal(List<Cliente> clientes) {
        double saldoTotal = 0;

        for (Cliente cliente : clientes) {
            saldoTotal += cliente.getSaldo();
        }

        return saldoTotal;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion != null) {

            switch (accion) {
                case "insertar": {
                    try {
                        this.insertarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
                break;
                case "editar": {
                    try {
                        this.modificarCliente(request, response);
                    } catch (SQLException ex) {
                        ex.printStackTrace(System.out);
                    }
                    break;
                }

                default:
                    this.accionDefault(request, response);

            }

        } else {
            this.accionDefault(request, response);
        }
    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //recuperamos valores del formulario agregar cliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !saldoString.equals("")) {
            saldo = Double.parseDouble(saldoString);
        }

        //creamos nuevo cliente en bdd
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        int registrosModificados = new ClienteDaoJDBC().insert(cliente);
        System.out.println("registrosModificados = " + registrosModificados);

        //redirigimos hacia accion default
        this.accionDefault(request, response);
    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

//recuperamos cliente a editar
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        Cliente cliente = new ClienteDaoJDBC().selectOneRegister(new Cliente(idCliente));
        System.out.println("cliente = " + cliente);
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";

        request.getRequestDispatcher(jspEditar).forward(request, response);

    }

    private void modificarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //recuperamos valores del formulario editar cliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !saldoString.equals("")) {
            saldo = Double.parseDouble(saldoString);
        }

        //actualizamos cliente en bdd
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        int registrosModificados = new ClienteDaoJDBC().update(cliente);
        System.out.println("registrosModificados = " + registrosModificados);

        //redirigimos hacia accion default
        this.accionDefault(request, response);
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        //recuperamos cliente a eliminar
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        int registros = new ClienteDaoJDBC().delete(new Cliente(idCliente));
        
        this.accionDefault(request, response);
        
    }

}
