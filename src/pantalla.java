import javax.swing.*;

public class pantalla {
    private JPanel pantalla1;
    private JTable table1;
    private JTextField placaTField;
    private JTextField marcaTField;
    private JTextField modeloTField;
    private JTextField colorTField;
    private JComboBox tipoBox;
    private JComboBox estadoBox;
    private JComboBox paisBox;
    private JButton mostrarListaButton;
    private JButton registrarButton;
    private JButton borrarButton;
    private JButton buscarButton;
    private JButton actualizarButton;

    public static void main(String[] args){
        JFrame frame = new JFrame("Mateo Congo - CRUD VEHICULOS");
        frame.setContentPane(new pantalla().pantalla1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000,480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
