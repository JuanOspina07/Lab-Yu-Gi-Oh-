package yugioh.logic;

import yugioh.api.YgoApiClient;
import yugioh.model.Card;

/**
 * Clase que maneja la lógica de un duelo simple al mejor de 3 rondas.
 */
public class Duel {
    private YgoApiClient api;
    private BattleListener listener;
    private int playerScore;
    private int aiScore;

    public Duel(YgoApiClient api, BattleListener listener) {
        this.api = api;
        this.listener = listener;
        this.playerScore = 0;
        this.aiScore = 0;
    }

    /**
     * Inicia un duelo al mejor de 3 rondas.
     */
    public void startBestOfThree() throws Exception {
        while (playerScore < 2 && aiScore < 2) {
            playRound();
        }

        String winner = (playerScore == 2) ? "Jugador" : "CPU";
        listener.onDuelEnded(winner);
    }

    /**
     * Juega una sola ronda: jugador vs CPU con cartas aleatorias.
     */
    private void playRound() throws Exception {
        Card playerCard = api.getRandomMonsterCard();
        Card aiCard = api.getRandomMonsterCard();

        String roundWinner;

        if (playerCard.getAtk() > aiCard.getAtk()) {
            playerScore++;
            roundWinner = "Jugador";
        } else if (playerCard.getAtk() < aiCard.getAtk()) {
            aiScore++;
            roundWinner = "CPU";
        } else {
            roundWinner = "Empate";
        }

        // Notificar eventos al listener
        listener.onTurn(playerCard.toString(), aiCard.toString(), roundWinner);
        listener.onScoreChanged(playerScore, aiScore);
    }
}
