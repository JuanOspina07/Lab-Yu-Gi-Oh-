package yugioh.ui;

import yugioh.api.YgoApiClient;
import yugioh.logic.BattleListener;
import yugioh.logic.Duel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal para mostrar el duelo.
 */
public class MainWindow extends JFrame {
    private JTextArea logArea;
    private JLabel scoreLabel;

    public MainWindow() {
        setTitle("Yu-Gi-Oh! Duel");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Log del duelo
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // Marcador
        scoreLabel = new JLabel("Marcador -> Jugador: 0 | CPU: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(scoreLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Botón para iniciar el duelo
        JButton startButton = new JButton("Iniciar Duelo");
        add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startDuel());
    }

    /**
     * Ejecuta un duelo usando la lógica y muestra resultados en la interfaz.
     */
    private void startDuel() {
        logArea.setText(""); // limpiar log
        scoreLabel.setText("Marcador -> Jugador: 0 | CPU: 0");

        new Thread(() -> {
            try {
                YgoApiClient api = new YgoApiClient();
                Duel duel = new Duel(api, new BattleListener() {
                    @Override
                    public void onTurn(String playerCard, String aiCard, String winner) {
                        appendLog("Jugador: " + playerCard);
                        appendLog("CPU: " + aiCard);
                        appendLog("Ganador de la ronda: " + winner);
                        appendLog("-----------------------------------");
                    }

                    @Override
                    public void onScoreChanged(int playerScore, int aiScore) {
                        scoreLabel.setText("Marcador -> Jugador: " + playerScore + " | CPU: " + aiScore);
                    }

                    @Override
                    public void onDuelEnded(String winner) {
                        appendLog("🏆 El ganador del duelo es: " + winner);
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "¡El ganador es " + winner + "!",
                                "Fin del duelo",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                duel.startBestOfThree();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n");
        });
    }
}
