package yugioh.api;

import yugioh.model.Card;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONObject;
import org.json.JSONArray;

public class YgoApiClient {
    private static final String RANDOM_CARD_URL = "https://db.ygoprodeck.com/api/v7/randomcard.php";
    private HttpClient client;

    public YgoApiClient() {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    public Card getRandomMonsterCard() throws Exception {
        while (true) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RANDOM_CARD_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código HTTP: " + response.statusCode());
            System.out.println("Respuesta cruda (primeros 200 chars): " +
                    response.body().substring(0, Math.min(200, response.body().length())));

            if (!response.body().trim().startsWith("{")) {
                System.out.println("⚠️ Respuesta inesperada, reintentando...");
                Thread.sleep(1000);
                continue;
            }

            // 👉 Parsear JSON con array "data"
            JSONObject json = new JSONObject(response.body());
            JSONArray dataArray = json.getJSONArray("data");
            JSONObject cardJson = dataArray.getJSONObject(0);

            String type = cardJson.optString("type");

            if (type.toLowerCase().contains("monster")) {
                String name = cardJson.getString("name");
                int atk = cardJson.optInt("atk", 0);
                int def = cardJson.optInt("def", 0);

                JSONArray images = cardJson.getJSONArray("card_images");
                String imageUrl = images.getJSONObject(0).getString("image_url");

                return new Card(name, atk, def, imageUrl);
            }
        }
    }
}
