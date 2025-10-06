package yugioh.logic;

public interface BattleListener {
    void turno(String playerCard, String aiCard, String winner);
    void puntuacion(int playerScore, int aiScore);
    void ganador(String winner);
}
