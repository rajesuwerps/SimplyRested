package com.rajesuwerps;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimplyRested {

  private static final String PROMPT = ">> ";

  private HashMap<String, String> headers = new HashMap<>();
  private String baseUrl = "";

  public static void main(String[] args) throws IOException {
    new SimplyRested().go();
  }

  private void go() {
    hi();
    read();
    bye();
  }

  private void read() {
    Console console = System.console();
    if (console != null) {
      String line;
      Scanner scanner;
      while ((line = console.readLine(PROMPT)) != null) {
        scanner = new Scanner(line);
        if (scanner.hasNext()) {
          String token = scanner.next();
          switch (token) {
            case "header":
              parseHeader(scanner);
              break;
            case "url":
              parseUrl(scanner);
              break;
            case "json":
              /* fall through*/
            case "xml":
              setHeader("Accept", "application/" + token);
              break;
            case "auth":
              setHeader("Authorization", scanner.hasNext() ? scanner.next() : null);
              break;
            default:
              if (token.startsWith("/") || token.startsWith("http://")) {
                callUrl(token);
              } else {
                msg("Unrecognized");
              }
              break;
          }
        }
      }
    }
  }


  private void callUrl(String endPoint) {
    HttpURLConnection httpCon = null;
    try {
      if (endPoint.startsWith("http://") && baseUrl.trim().length() != 0) {
        msg("Invalid. When base url is set (using 'url' command), just pass the end point (partial) url");
        return;
      }
      URL url = new URL(baseUrl + endPoint);
      httpCon = (HttpURLConnection) url.openConnection();
      int TIMEOUT = 2 * 1000;
      httpCon.setConnectTimeout(TIMEOUT);
      httpCon.setReadTimeout(TIMEOUT);
      httpCon.setRequestMethod("GET");
      for (Map.Entry<String, String> header : headers.entrySet()) {
                /* assumes non null key  and value See setHeader() */
        httpCon.setRequestProperty(header.getKey(), header.getValue());
      }
      int responseCode = httpCon.getResponseCode();
      msg("" + responseCode);
      if (responseCode == 200) {
        BufferedReader br = new BufferedReader(new InputStreamReader((httpCon.getInputStream())));
        String output;
        while ((output = br.readLine()) != null) {
          msgRaw(output);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (httpCon != null) {
        httpCon.disconnect();
      }
    }

  }

  private void parseUrl(Scanner scanner) {
    baseUrl = scanner.hasNext() ? scanner.next() : null;
    if (baseUrl == null) {
      msgi();
    } else {
      if (!baseUrl.startsWith("http://")) {
        baseUrl = "http://" + baseUrl;
      }
      System.out.println("baseUrl = " + baseUrl);
      msgs();
    }
  }

  private void parseHeader(Scanner scanner) {
    String key = scanner.hasNext() ? scanner.next() : null;
    String value = scanner.hasNext() ? scanner.nextLine() : null;
    setHeader(key, value);
  }

  private void setHeader(String key, String value) {
    if (key != null) {
      if (value == null) {
                /* if you are planning to set null values, check and update httpCon.setRequestProperty() in callUrl() */
        headers.remove(key);
        msg("Removed");
      } else {
        if (value.startsWith("\"")) {
          if (value.endsWith("\"")) {
            value = value.substring(1, value.length());
          } else {
            msg("Missing end quotes?");
          }
        }
        headers.put(key, value);
        msgs();
      }
    } else {
      msgi();
    }
  }

  private void msgs() {
    msg("Set");
  }

  private void msgi() {
    msg("Invalid Syntax");
  }

  private void msgRaw(String s) {
    System.out.printf("%s%s%n", PROMPT, s);
  }

  private void msg(String s) {
    System.out.printf("%s[%s]%n", PROMPT, s);
  }

  private void bye() {
    System.out.println("[Bye now]");
    System.out.println();
  }

  private void hi() {
    String s = "\n" +
        "####################################################################################################\n" +
        "#                                                                                                   \n" +
        "# SimplyRested. A Simple REST CLI                                                                   \n" +
        "#                                                                                                   \n" +
        "# Usage Options:                                                                                    \n" +
        "#                                                                                                   \n" +
        "# To set base url: url <base-url>                                                                   \n" +
        "#     e.g.: url http://api.example.com/                                                             \n" +
        "# Need to be set only once. Need to set before calling endpoint -see below.                         \n" +
        "# Multiple calls ok. Last value takes effect.                                                       \n" +
        "#                                                                                                   \n" +
        "# To set header(s): header <key> <value>                                                            \n" +
        "#     e.g.: header Accept application/json                                                          \n" +
        "#     e.g.: header Authorization DKJ36EI72429238400792DJ3290834                                     \n" +
        "# Call once per header. Can be called anytime. Takes effect for subsequent calls.                   \n" +
        "# Also, see shortcuts below.                                                                        \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# To remove header(s): header <key> <no-value>                                                      \n" +
        "#     e.g.: header Accept                                                                           \n" +
        "# Call once per header. Can be called anytime. Takes effect for subsequent calls.                   \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# To call service endpoint: /<end-point>                                                            \n" +
        "#     e.g.: /myresource                                                                             \n" +
        "# Preceding '/' needed.                                                                             \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# You can also use these shortcuts                                                                  \n" +
        "# ---------------------------------                                                                 \n" +
        "#                                                                                                   \n" +
        "# Shortcut to set JSON:                                                                             \n" +
        "#     json                                                                                          \n" +
        "# Same as: header Accept application/json                                                           \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# Shortcut to set XML:                                                                              \n" +
        "#     xml                                                                                           \n" +
        "# Same as: header Accept application/xml                                                            \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# Shortcut to set Authorization:                                                                    \n" +
        "#     auth <value>                                                                                  \n" +
        "# Same as: header Authorization <value>                                                             \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# You can also call url directly, without first having to sett the base url                         \n" +
        "#     e.g.: http://example.com/api/myresource                                                       \n" +
        "#                                                                                                   \n" +
        "#                                                                                                   \n" +
        "# Currently supports HTTP Only, GET Only                                                            \n" +
        "#                                                                                                   \n" +
        "# To Exit: Ctrl+D or Ctrl+C                                                                         \n" +
        "#                                                                                                   \n" +
        "####################################################################################################\n"
        ;
    System.out.println(s);
  }



}
