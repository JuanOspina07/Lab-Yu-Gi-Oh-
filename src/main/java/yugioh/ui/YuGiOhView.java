package yugioh.ui;

import yugioh.api.YgoApiClient;
import yugioh.logic.BattleListener;
import yugioh.logic.Duel;
import yugioh.model.Card;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class YuGiOhView extends JFrame {

    private JPanel Jugador;
    private JPanel Maquina;
    private JPanel Header;
    private JLabel Titulo;
    private JLabel NombreCarta1;
    private JLabel NombreCarta2;
    private JLabel NombreCarta3;
    private JLabel AtaqueCarta1;
    private JLabel AtaqueCarta2;
    private JLabel AtaqueCarta3;
    private JLabel DefensaCarta1;
    private JLabel DefensaCarta2;
    private JLabel DefensaCarta3;
    private JLabel NombreCarta4;
    private JLabel NombreCarta5;
    private JLabel NombreCarta6;
    private JLabel AtaqueCarta4;
    private JLabel AtaqueCarta5;
    private JLabel AtaqueCarta6;
    private JLabel DefensaCarta4;
    private JLabel DefensaCarta5;
    private JLabel DefensaCarta6;
    private JPanel rootPanel;
    private JButton Imagen1;
    private JButton Imagen2;
    private JButton Imagen3;
    private JButton Imagen4;
    private JButton Imagen5;
    private JButton imagen6;
    private JTextArea Info;
    private JButton IniciarDuelo;
    private JLabel Subtitulo1;
    private JLabel Subtitulo2;
    private JButton Estado;
    private JScrollPane PanelScroll;
    private boolean modoAtaque = true; // true = ataque, false = defensa

    private Duel duel;
    private List<Card> playerCards = new ArrayList<>();
    private List<Card> aiCards = new ArrayList<>();
    private ImageIcon backIcon;

    public YuGiOhView() {
        setTitle("Yu-Gi-Oh");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        //Fondo de la aplicación
        try {
            URL fondoUrl = getClass().getResource("/fondo.jpg");
            if (fondoUrl != null) {
                ImageIcon fondoIcon = new ImageIcon(fondoUrl);
                Image fondoEscalado = fondoIcon.getImage().getScaledInstance(1920, 1200, Image.SCALE_SMOOTH);
                JLabel fondoLabel = new JLabel(new ImageIcon(fondoEscalado));
                fondoLabel.setLayout(new BorderLayout());

                // Añadimos el rootPanel encima del fondo
                fondoLabel.add(rootPanel, BorderLayout.CENTER);

                // Hacemos transparentes los paneles
                rootPanel.setOpaque(false);
                Jugador.setOpaque(false);
                Maquina.setOpaque(false);
                Header.setOpaque(false);

                setContentPane(fondoLabel);
            } else {
                System.out.println("No se encontró la imagen de fondo ");
                setContentPane(rootPanel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setContentPane(rootPanel);
        }

        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        Titulo.setText("Yu-Gi-Oh");
        Titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        Titulo.setForeground(Color.WHITE);
        Titulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtítulos
        Subtitulo1.setText("Jugador");
        Subtitulo1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        Subtitulo1.setForeground(new Color(80, 180, 255));

        Subtitulo2.setText("CPU");
        Subtitulo2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        Subtitulo2.setForeground(new Color(255, 100, 100));

        // Botón Iniciar Duelo
        IniciarDuelo.setText("Iniciar Duelo");
        IniciarDuelo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        IniciarDuelo.setBackground(new Color(0, 153, 76));
        IniciarDuelo.setForeground(Color.WHITE);
        IniciarDuelo.setFocusPainted(false);
        IniciarDuelo.setBorder(new EmptyBorder(10, 25, 10, 25));
        IniciarDuelo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        IniciarDuelo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                IniciarDuelo.setBackground(new Color(0, 200, 90));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                IniciarDuelo.setBackground(new Color(0, 153, 76));
            }
        });

        // Botón Estado
        Estado.setText("Modo: ATAQUE");
        Estado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        Estado.setBackground(new Color(60, 60, 180));
        Estado.setForeground(Color.WHITE);
        Estado.setFocusPainted(false);
        Estado.setBorder(new EmptyBorder(10, 25, 10, 25));
        Estado.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Estado.addActionListener(e -> {
            modoAtaque = !modoAtaque;
            if (modoAtaque) {
                Estado.setText("Modo: ATAQUE");
                Estado.setBackground(new Color(60, 60, 180));
                appendInfo("\nHas cambiado al modo ATAQUE.\n");
            } else {
                Estado.setText("Modo: DEFENSA");
                Estado.setBackground(new Color(180, 60, 60));
                appendInfo("\nHas cambiado al modo DEFENSA.\n");
            }
        });

        // TextArea + Scroll
        Info.setText("Presiona 'Iniciar Duelo' para comenzar.");
        Info.setFont(new Font("Consolas", Font.PLAIN, 15));
        Info.setEditable(false);
        Info.setBackground(new Color(250, 250, 255));
        Info.setBorder(new LineBorder(new Color(180, 180, 180), 2, true));
        Info.setMargin(new Insets(12, 12, 12, 12));
        Info.setLineWrap(true);
        Info.setWrapStyleWord(true);

        if (PanelScroll.getViewport().getView() != Info) {
            PanelScroll.setViewportView(Info);
        }

        PanelScroll.getVerticalScrollBar().setUnitIncrement(16);

        Info.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void scrollToBottom() {
                SwingUtilities.invokeLater(() -> {
                    JScrollBar bar = PanelScroll.getVerticalScrollBar();
                    bar.setValue(bar.getMaximum());
                });
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) { scrollToBottom(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { scrollToBottom(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { scrollToBottom(); }
        });

        // Cargar fondo de cartas
        try {
            URL backUrl = getClass().getResource("/back.png");
            if (backUrl != null) {
                ImageIcon original = new ImageIcon(backUrl);
                Image img = original.getImage().getScaledInstance(160, 230, Image.SCALE_SMOOTH);
                backIcon = new ImageIcon(img);
            }
        } catch (Exception e) {
            backIcon = null;
        }

        configurarBotonCarta(Imagen1);
        configurarBotonCarta(Imagen2);
        configurarBotonCarta(Imagen3);
        configurarBotonCarta(Imagen4);
        configurarBotonCarta(Imagen5);
        configurarBotonCarta(imagen6);

        ocultarCartasCPU();
        ocultarCartasJugador();
        ocultarLabelsJugador();
        ocultarLabelsCPU();

        IniciarDuelo.addActionListener(e -> iniciarDuelo());
    }
    //EL text area no se actualizaba bien por lo tanto se ha hecho este método
    private void appendInfo(String texto) {
        SwingUtilities.invokeLater(() -> {
            Info.append(texto);
            Info.setCaretPosition(Info.getDocument().getLength());
            JScrollBar vertical = PanelScroll.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    //Configuracion de los botones de las cartas
    private void configurarBotonCarta(JButton boton) {
        boton.setPreferredSize(new Dimension(160, 230));
        boton.setBorder(new LineBorder(new Color(180, 180, 180), 2, true));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setBackground(new Color(50, 50, 90));
        boton.setText("");
    }
    //Ocultar las cartas del jugador
    private void ocultarCartasJugador() {
        JButton[] imagenes = {Imagen1, Imagen2, Imagen3};
        for (JButton img : imagenes) img.setIcon(backIcon);
    }
    //Ocultar las cartas de la CPU

    private void ocultarCartasCPU() {
        JButton[] imagenes = {Imagen4, Imagen5, imagen6};
        JLabel[] nombres = {NombreCarta4, NombreCarta5, NombreCarta6};
        JLabel[] atks = {AtaqueCarta4, AtaqueCarta5, AtaqueCarta6};
        JLabel[] defs = {DefensaCarta4, DefensaCarta5, DefensaCarta6};
        for (int i = 0; i < 3; i++) {
            imagenes[i].setIcon(backIcon);
            nombres[i].setVisible(false);
            atks[i].setVisible(false);
            defs[i].setVisible(false);
        }
    }
    //Ocultar los labels del jugador
    private void ocultarLabelsJugador() {
        JLabel[] nombres = {NombreCarta1, NombreCarta2, NombreCarta3};
        JLabel[] atks = {AtaqueCarta1, AtaqueCarta2, AtaqueCarta3};
        JLabel[] defs = {DefensaCarta1, DefensaCarta2, DefensaCarta3};
        for (int i = 0; i < 3; i++) {
            nombres[i].setVisible(false);
            atks[i].setVisible(false);
            defs[i].setVisible(false);
        }
    }
    //Ocultar los labels de la CPU
    private void ocultarLabelsCPU() {
        JLabel[] nombres = {NombreCarta4, NombreCarta5, NombreCarta6};
        JLabel[] atks = {AtaqueCarta4, AtaqueCarta5, AtaqueCarta6};
        JLabel[] defs = {DefensaCarta4, DefensaCarta5, DefensaCarta6};
        for (int i = 0; i < 3; i++) {
            nombres[i].setVisible(false);
            atks[i].setVisible(false);
            defs[i].setVisible(false);
        }
    }
    // Mostrar la carta en el botón y los labels
    private void mostrarCarta(JButton btnImagen, JLabel lblNombre, JLabel lblAtk, JLabel lblDef, Card carta) {
        try {
            java.net.URL url = new java.net.URL(carta.getImageUrl());
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(160, 230, Image.SCALE_SMOOTH);
            btnImagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btnImagen.setText("Sin imagen");
        }

        lblNombre.setText(carta.getName());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setForeground(new Color(240, 240, 240));

        lblAtk.setText("ATK: " + carta.getAtk());
        lblAtk.setForeground(new Color(0, 200, 0));
        lblDef.setText("DEF: " + carta.getDef());
        lblDef.setForeground(new Color(255, 60, 60));

        lblNombre.setVisible(true);
        lblAtk.setVisible(true);
        lblDef.setVisible(true);
    }
    // Lógica para iniciar el duelo
    private void iniciarDuelo() {
        YgoApiClient api = new YgoApiClient();
        try {
            bloquearCartasJugador();
            Info.setText("Cargando cartas, por favor espera...\n");

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                public Void doInBackground() throws Exception {
                    playerCards.clear();
                    aiCards.clear();
                    for (int i = 0; i < 3; i++) {
                        playerCards.add(api.obtenerCarta());
                        aiCards.add(api.obtenerCarta());
                    }
                    return null;
                }

                @Override
                public void done() {
                    try {
                        mostrarCarta(Imagen1, NombreCarta1, AtaqueCarta1, DefensaCarta1, playerCards.get(0));
                        mostrarCarta(Imagen2, NombreCarta2, AtaqueCarta2, DefensaCarta2, playerCards.get(1));
                        mostrarCarta(Imagen3, NombreCarta3, AtaqueCarta3, DefensaCarta3, playerCards.get(2));
                        ocultarCartasCPU();

                        Info.setText("¡El duelo ha comenzado! Elige una carta para atacar.\n");

                        duel = new Duel(playerCards, aiCards, new BattleListener() {
                            @Override
                            public void turno(String playerCard, String aiCard, String winner) {
                                SwingUtilities.invokeLater(() -> {
                                    mostrarCartasCPU(aiCard);
                                    appendInfo("\nJugador eligió: " + playerCard);
                                    appendInfo("\nCPU eligió: " + aiCard);
                                    appendInfo("\nGanador de la ronda: " + winner + "\n");
                                    appendInfo("Marcador -> Jugador: " + duel.getPlayerScore() + " | CPU: " + duel.getAiScore() + "\n");
                                });
                            }

                            @Override
                            public void puntuacion(int playerScore, int aiScore) {}

                            @Override
                            public void ganador(String winner) {
                                SwingUtilities.invokeLater(() -> {
                                    JButton[] imgs = {Imagen4, Imagen5, imagen6};
                                    JLabel[] nombres = {NombreCarta4, NombreCarta5, NombreCarta6};
                                    JLabel[] atks = {AtaqueCarta4, AtaqueCarta5, AtaqueCarta6};
                                    JLabel[] defs = {DefensaCarta4, DefensaCarta5, DefensaCarta6};
                                    for (int i = 0; i < aiCards.size(); i++) {
                                        mostrarCarta(imgs[i], nombres[i], atks[i], defs[i], aiCards.get(i));
                                    }
                                    appendInfo("\n=== El duelo ha terminado ===\n");
                                    appendInfo("Revelando cartas de la CPU...\n");

                                    Timer timer = new Timer(1500, e -> {
                                        appendInfo("\n=== El ganador del duelo es: " + winner + " ===\n");
                                        JOptionPane.showMessageDialog(rootPanel, "El ganador es: " + winner);
                                    });
                                    timer.setRepeats(false);
                                    timer.start();
                                });
                            }
                        });

                        configurarAccion(Imagen1, playerCards.get(0));
                        configurarAccion(Imagen2, playerCards.get(1));
                        configurarAccion(Imagen3, playerCards.get(2));
                        habilitarCartasJugador();

                    } catch (Exception e) {
                        Info.setText("Error al mostrar cartas: " + e.getMessage());
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            Info.setText("Error al obtener cartas: " + ex.getMessage());
            habilitarCartasJugador();
        }
    }
    // Mostrar la carta que la cpu ha elegido
    private void mostrarCartasCPU(String aiCardName) {
        JButton[] imgs = {Imagen4, Imagen5, imagen6};
        JLabel[] nombres = {NombreCarta4, NombreCarta5, NombreCarta6};
        JLabel[] atks = {AtaqueCarta4, AtaqueCarta5, AtaqueCarta6};
        JLabel[] defs = {DefensaCarta4, DefensaCarta5, DefensaCarta6};

        String nombreLimpio = aiCardName.replaceAll("\\s*\\(.*?\\)\\s*", "").trim();

        for (int i = 0; i < 3; i++) {
            Card c = aiCards.get(i);
            if (c.getName().equalsIgnoreCase(nombreLimpio)) {
                mostrarCarta(imgs[i], nombres[i], atks[i], defs[i], c);
                break;
            }
        }
    }
    // Bloquear las cartas del jugador para que no pueda clicar mientras se carga las cartas
    private void bloquearCartasJugador() {
        JButton[] botones = {Imagen1, Imagen2, Imagen3};
        for (JButton b : botones) {
            b.setEnabled(false);
            b.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        }
    }
    // Habilitar las cartas del jugador
    private void habilitarCartasJugador() {
        JButton[] botones = {Imagen1, Imagen2, Imagen3};
        for (JButton b : botones) {
            b.setEnabled(true);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
    // Configurar la acción del boton del estado de la carta (ataque o defensa)
    private void configurarAccion(JButton boton, Card carta) {
        for (ActionListener al : boton.getActionListeners()) boton.removeActionListener(al);
        boton.addActionListener(e -> duel.playTurn(carta, modoAtaque));
    }
    // Main para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new YuGiOhView().setVisible(true));
    }
}
