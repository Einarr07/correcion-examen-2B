import javax.swing.*;

public class main extends pantalla{
    public static void main(String[] args){
        JFrame frame = new JFrame("Mateo Congo - CRUD VEHICULOS");
        frame.setContentPane(new pantalla().pantalla1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1300,480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
