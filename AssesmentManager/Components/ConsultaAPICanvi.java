package AssesmentManager.Components;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class ConsultaAPICanvi {
    double BRLExchangeRate;
    double EURExchangeRate;

    public double BRLCanvi() {

        try {
            URL url = new URL("https://v6.exchangerate-api.com/v6/e96f766e22fe3085e29bd8e1/pair/AUD/BRL");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            BRLExchangeRate = jsonResponse.getDouble("conversion_rate");

        } catch (
                MalformedURLException e) {
            System.out.println("URL inválida: " + e.getMessage());
        } catch (
                ProtocolException e) {
            System.out.println("Erro no protocolo: " + e.getMessage());
        } catch (
                IOException e) {
            System.out.println("Erro de entrada/saída: " + e.getMessage());
        }

        return BRLExchangeRate;
    }

    public double EURCanvi() {

        try {
            URL url = new URL("https://v6.exchangerate-api.com/v6/e96f766e22fe3085e29bd8e1/pair/AUD/EUR");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            EURExchangeRate = jsonResponse.getDouble("conversion_rate");

        } catch (
                MalformedURLException e) {
            System.out.println("URL inválida: " + e.getMessage());
        } catch (
                ProtocolException e) {
            System.out.println("Erro no protocolo: " + e.getMessage());
        } catch (
                IOException e) {
            System.out.println("Erro de entrada/saída: " + e.getMessage());
        }

        return EURExchangeRate;
    }
}
