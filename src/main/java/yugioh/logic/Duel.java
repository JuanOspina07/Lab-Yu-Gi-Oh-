package yugioh.logic;

import yugioh.model.Card;
import java.util.*;

public class Duel {
    private List<Card> playerCards;
    private List<Card> aiCards;
    private int playerScore = 0;
    private int aiScore = 0;
    private BattleListener listener;
    private Random random = new Random();

    public Duel(List<Card> playerCards, List<Card> aiCards, BattleListener listener) {
        this.playerCards = new ArrayList<>(playerCards);
        this.aiCards = new ArrayList<>(aiCards);
        this.listener = listener;
    }

    public boolean isFinished() {
        return playerScore == 2 || aiScore == 2;
    }

    public void playTurn(Card playerChoice, boolean playerAttackMode) {
        if (isFinished()) return;

        // Máquina elige carta y modo aleatorio
        Card aiChoice = aiCards.get(random.nextInt(aiCards.size()));
        boolean aiAttackMode = random.nextBoolean();

        String modoJugador = playerAttackMode ? "ATAQUE" : "DEFENSA";
        String modoCPU = aiAttackMode ? "ATAQUE" : "DEFENSA";

        String winner;

        //Si ambos están en defensa seria empate
        if (!playerAttackMode && !aiAttackMode) {
            winner = "Empate";
        } else {
            // Lógica de combate normal
            int valorJugador = playerAttackMode ? playerChoice.getAtk() : playerChoice.getDef();
            int valorCPU = aiAttackMode ? aiChoice.getAtk() : aiChoice.getDef();

            if (valorJugador > valorCPU) {
                playerScore++;
                winner = "Jugador";
            } else if (valorJugador < valorCPU) {
                aiScore++;
                winner = "CPU";
            } else {
                winner = "Empate";
            }
        }

        // Notificar resultado del turno
        listener.turno(
                playerChoice.getName() + " (" + modoJugador + ")",
                aiChoice.getName() + " (" + modoCPU + ")",
                winner
        );

        listener.puntuacion(playerScore, aiScore);

        if (isFinished()) {
            String duelWinner = (playerScore > aiScore) ? "Jugador" : "CPU";
            listener.ganador(duelWinner);
        }
    }



    public int getPlayerScore() {
        return playerScore;
    }

    public int getAiScore() {
        return aiScore;
    }
}
