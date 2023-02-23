import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class pantalla {
    JPanel pantalla1;
    private JTable datosTable;
    private JTextField placaTField;
    private JTextField marcaTField;
    private JTextField modeloTField;
    private JTextField colorTField;
    private JComboBox gasolinaBox;
    private JComboBox estadoBox;
    private JComboBox paisBox;
    private JButton mostrarListaButton;
    private JButton registrarButton;
    private JButton borrarButton;
    private JButton buscarButton;
    private JButton actualizarButton;
    Connection con;

    public pantalla() {
        mostrarListaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conectar();
                    registrarButton.setEnabled(true);
                    placaTField.setEnabled(true);
                    buscarButton.setEnabled(true);
                    actualizarButton.setEnabled(false);
                    limpiarEntradas();
                    mostrarDatos();
                    con.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al mostrar los Datos");
                    throw new RuntimeException(ex);
                }
            }
        });
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                try{
                    PreparedStatement ps;
                    ps = con.prepareStatement("insert into carros (placa, marca, modelo, color, uso, gasolina, pais) values (?,?,?,?,?,?,?) ");
                    ps.setInt(1, Integer.parseInt(placaTField.getText()));
                    ps.setString(2, marcaTField.getText());
                    ps.setString(3, modeloTField.getText());
                    ps.setString(4, colorTField.getText());
                    ps.setString(5, estadoBox.getSelectedItem().toString());
                    ps.setString(6, gasolinaBox.getSelectedItem().toString());
                    ps.setString(7, paisBox.getSelectedItem().toString());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Los datos han sido guardados correctamente");
                    mostrarDatos();
                    limpiarEntradas();
                    con.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al registrar");
                    throw new RuntimeException(ex);
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                try{
                    PreparedStatement ps;
                    ps = con.prepareStatement("Select * from carros Where placa = ?");
                    ps.setString(1, placaTField.getText());
                    ResultSet rs;
                    rs = ps.executeQuery();

                    if (rs.next()){
                        marcaTField.setText(rs.getString("marca"));
                        modeloTField.setText(rs.getString("modelo"));
                        colorTField.setText(rs.getString("color"));
                        estadoBox.setSelectedItem(rs.getString("uso"));
                        gasolinaBox.setSelectedItem(rs.getString("gasolina"));
                        paisBox.setSelectedItem(rs.getString("pais"));

                        actualizarButton.setEnabled(true);
                        registrarButton.setEnabled(false);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "El carro con Nº " + placaTField.getText() +" no existe");
                    }
                    con.close();
                    rs.close();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al Buscar");
                    throw new RuntimeException(ex);
                }
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                try{
                    PreparedStatement ps;
                    ps = con.prepareStatement("UPDATE carros SET placa = ?, marca = ?, modelo = ?, color = ?, uso = ?, gasolina = ?, pais = ? WHERE placa =" + placaTField.getText());
                    ps.setInt(1, Integer.parseInt(placaTField.getText()));
                    ps.setString(2, marcaTField.getText());
                    ps.setString(3, modeloTField.getText());
                    ps.setString(4, colorTField.getText());
                    ps.setString(5, estadoBox.getSelectedItem().toString());
                    ps.setString(6, gasolinaBox.getSelectedItem().toString());
                    ps.setString(7, paisBox.getSelectedItem().toString());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Los datos han sido actualizados correctamente");
                    registrarButton.setEnabled(true);
                    mostrarDatos();
                    limpiarEntradas();
                    con.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar");
                    throw new RuntimeException(ex);
                }
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                try{
                    PreparedStatement ps;
                    ps = con.prepareStatement("DELETE from carros Where placa = ?");
                    ps.setInt(1, Integer.parseInt(placaTField.getText()));
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "El vehículo con placa " + placaTField.getText() + " fue eliminado");
                    mostrarDatos();
                    limpiarEntradas();
                    con.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Algo salio mal, intente de nuevo");
                    throw new RuntimeException(ex);
                }
            }
        });
        //Traigo los datos de mi tabla para que se presenten en el Combo Box
        this.estadoBox.setModel(optener_estadoVehiculo()); //Estado vehiculo
        this.gasolinaBox.setModel(optener_gasolina()); //Gasolina
        this.paisBox.setModel(optener_pais()); //Pais

        datosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                placaTField.setEnabled(false);
                registrarButton.setEnabled(false);
                actualizarButton.setEnabled(true);
                buscarButton.setEnabled(false);
                int fila = datosTable.getSelectedRow();
                placaTField.setText(datosTable.getValueAt(fila, 0).toString());
                marcaTField.setText(datosTable.getValueAt(fila, 1).toString());
                modeloTField.setText(datosTable.getValueAt(fila, 2).toString());
                colorTField.setText(datosTable.getValueAt(fila, 3).toString());
                estadoBox.setSelectedItem(datosTable.getValueAt(fila, 4).toString());
                gasolinaBox.setSelectedItem(datosTable.getValueAt(fila, 5).toString());
                paisBox.setSelectedItem(datosTable.getValueAt(fila, 6).toString());
            }
        });
    }

    //Metodo para solo colocar la consulta SQL dentro de los metodos
    public ResultSet consulta(String sql){
        ResultSet res = null;
        conectar();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            res = ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error consulta"+e);
        }
        return res;
    }

    //Metodos para obtener los datos de mis tablas des la BD
    //Estado Vehiculo
    public DefaultComboBoxModel optener_estadoVehiculo(){
        DefaultComboBoxModel listaModelo = new DefaultComboBoxModel();
        listaModelo.addElement("Escoger estado");
        ResultSet res = this.consulta("Select * from estadoVehiculo order by Caracteristica");
        try{
            while(res.next()){
                listaModelo.addElement(res.getString("Caracteristica"));
            }
            res.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return listaModelo;
    }
    //Tipo de Gasolina
    public DefaultComboBoxModel optener_gasolina(){
        DefaultComboBoxModel listaModelo = new DefaultComboBoxModel();
        listaModelo.addElement("Escoger tipo de gasolina");
        ResultSet res = this.consulta("Select * from gasolina order by Nombre_Gasolina");
        try{
            while(res.next()){
                listaModelo.addElement(res.getString("Nombre_Gasolina"));
            }
            res.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return listaModelo;
    }
    //Pais
    public DefaultComboBoxModel optener_pais(){
        DefaultComboBoxModel listaModelo = new DefaultComboBoxModel();
        listaModelo.addElement("Seleccionar Pais");
        ResultSet res = this.consulta("Select * from pais order by Pais");
        try{
            while(res.next()){
                listaModelo.addElement(res.getString("Pais"));
            }
            res.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return listaModelo;
    }
    //Limpiar entradas
    public void limpiarEntradas(){
        placaTField.setText("");
        marcaTField.setText("");
        modeloTField.setText("");
        colorTField.setText("");
        estadoBox.setSelectedIndex(0);
        gasolinaBox.setSelectedIndex(0);
        paisBox.setSelectedIndex(0);
    }
    //Metodo para mostrar Datos
    public void mostrarDatos(){
        DefaultTableModel mod = new DefaultTableModel();
        mod.addColumn("Placa");
        mod.addColumn("Marca");
        mod.addColumn("Modelo");
        mod.addColumn("Color");
        mod.addColumn("Estado");
        mod.addColumn("Tip.Gasolina");
        mod.addColumn("País");
        datosTable.setModel(mod);
        String consultaSQL = "Select * from carros";

        String data[] = new String[7];

        Statement st;

        try{
            st = con.createStatement();
            ResultSet r = st.executeQuery(consultaSQL);
            while (r.next()) {
                data[0] = r.getString(1);
                data[1] = r.getString(2);
                data[2] = r.getString(3);
                data[3] = r.getString(4);
                data[4] = r.getString(5);
                data[5] = r.getString(6);
                data[6] = r.getString(7);
                mod.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar datos");
            throw new RuntimeException(e);
        }
    }
    //Conectar con la BD
    public void conectar(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehiculos", "root", "mysql");
            System.out.println("La coneccion fue exitosa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
