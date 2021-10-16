package com.example.animalcrash.util;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Translating HTTP responses
 *
 */

public class TranslateHttpResponse {

    private String responseCode;

    /**
     * Empty constructor
     */
    public TranslateHttpResponse() {
    }

    /**
     * HTTP Response code
     *
     * @param responseCode String   Response code
     */
    public TranslateHttpResponse(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Get response code
     *
     * @return responseCode     String
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Set response code
     *
     * @param responseCode String   HTTP response code
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * TranslateHttpResponse HTTP response code to Dutch
     *
     * @return response    String
     */
    public String translateHttpResponseCode() {
        String response = null;

        switch (responseCode) {
            case "200":
                response = "OK";
                break;
            case "201":
                response = "Aangemaakt";
                break;
            case "202":
                response = "Aanvaard";
                break;
            case "203":
                response = "Niet-gemachtigde informatie";
                break;
            case "204":
                response = "Geen inhoud";
                break;
            case "205":
                response = "Inhoud opnieuw instellen";
                break;
            case "206":
                response = "Gedeeltelijke inhoud";
                break;
            case "207":
                response = "Meerdere statussen";
                break;

            case "300":
                response = "Meerkeuze";
                break;
            case "301":
                response = "Definitief verplaatst";
                break;
            case "302":
                response = "Tijdelijk verplaatst";
                break;
            case "303":
                response = "Zie andere";
                break;
            case "304":
                response = "Niet gewijzigd";
                break;
            case "305":
                response = "Gebruik Proxy";
                break;
            case "306":
                response = "Gereserveerd";
                break;
            case "307":
                response = "Tijdelijke omleiding";
                break;
            case "308":
                response = "Definitieve omleiding";
                break;

            case "400":
                response = "Foute aanvraag";
                break;
            case "401":
                response = "Niet geautoriseerd";
                break;
            case "402":
                response = "Betaalde toegang";
                break;
            case "403":
                response = "Verboden toegang";
                break;
            case "404":
                response = "Niet gevonden";
                break;
            case "405":
                response = "Methode niet toegestaan";
                break;
            case "406":
                response = "Niet aanvaardbaar";
                break;
            case "407":
                response = "Authenticatie op de proxyserver verplicht";
                break;
            case "408":
                response = "Aanvraagtijd verstreken";
                break;
            case "409":
                response = "Conflict";
                break;
            case "410":
                response = "Verdwenen";
                break;
            case "411":
                response = "Lengte benodigd";
                break;
            case "412":
                response = "Niet voldaan aan vooraf gestelde voorwaarde";
                break;
            case "413":
                response = "Aanvraag te groot";
                break;
            case "414":
                response = "Aanvraag URL te lang";
                break;
            case "415":
                response = "Media-type niet ondersteund";
                break;
            case "416":
                response = "Aangevraagd gedeelte niet opvraagbaar";
                break;
            case "417":
                response = "Niet voldaan aan verwachting";
                break;
            case "422":
                response = "Aanvraag kan niet verwerkt worden";
                break;
            case "423":
                response = "Afgesloten";
                break;
            case "424":
                response = "Gefaalde afhankelijkheid";
                break;
            case "426":
                response = "Upgrade nodig";
                break;
            case "428":
                response = "Voorwaarde nodig";
                break;
            case "429":
                response = "Te veel aanvragen";
                break;
            case "431":
                response = "Headers van de aanvraag te lang";
                break;
            case "498":
                response = "Token verlopen/ongeldig";
                break;
            case "499":
                response = "Token vereist";
                break;

            case "500":
                response = "Interne serverfout";
                break;
            case "501":
                response = "Niet geïmplementeerd";
                break;
            case "502":
                response = "Bad Gateway";
                break;
            case "503":
                response = "Dienst niet beschikbaar";
                break;
            case "504":
                response = "Gateway timeout";
                break;
            case "505":
                response = "HTTP-versie wordt niet ondersteund";
                break;
            case "510":
                response = "Niet verlengd";
                break;
            case "511":
                response = "Netwerkauthenticatie vereist";
                break;

            default:
                response = "Onbekende response code";
        }

        return response;
    }
}
