package yugioh.logic;

/**
 * Interfaz para comunicar eventos del duelo hacia la consola o la UI.
 */
public interface BattleListener {
    /**
     * Se ejecuta al finalizar un turno.
     *
     * @param playerCard Carta del jugador
     * @param aiCard Carta del CPU
     * @param winner Ganador de la ronda ("Jugador", "CPU" o "Empate")
     */
    void onTurn(String playerCard, String aiCard, String winner);

    /**
     * Se ejecuta cuando cambia el marcador.
     *
     * @param playerScore Puntos del jugador
     * @param aiScore Puntos del CPU
     */
    void onScoreChanged(int playerScore, int aiScore);

    /**
     * Se ejecuta al finalizar el duelo.
     *
     * @param winner Ganador final del duelo
     */
    void onDuelEnded(String winner);
}
