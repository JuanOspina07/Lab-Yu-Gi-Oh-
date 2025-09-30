package yugioh;

import yugioh.ui.MainWindow;

import javax.swing.*;

/**
 * Clase principal: abre la ventana del juego.
 */
public class Main {
    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}

