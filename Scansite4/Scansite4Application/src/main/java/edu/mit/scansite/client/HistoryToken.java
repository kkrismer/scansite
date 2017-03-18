package edu.mit.scansite.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses a history token string from left to right, and allows access to the
 * current token and parameters.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public class HistoryToken {

  /**
   * Separator-character between history tokens Example: token1&token2&token3
   */
  public static final String TOKEN_SEPARATOR = "&";

  /**
   * Separator-character between parameters, or before the first parameter
   * Example: token1&token2;param1=value1;param2=value2
   */
  public static final String PARAM_SEPARATOR = ";";
  public static final String PARAM_ASSIGNMENT = "=";

  private String token;
  private String[] tokenArr;
  private int currentIndex = -1;
  private String current = "";
  private Map<String, String> params = new HashMap<String, String>();

  /**
   * @param token
   *          A token as given by a HistoryChangeEvent.
   */
  public HistoryToken(String token) {
    this.token = token;
    if (token != null) {
      tokenArr = token.split(TOKEN_SEPARATOR);
      setNext();
    }
  }

  /**
   * Moves the cursor to the next token in the given token string.
   * 
   * @return TRUE, if successful, otherwise FALSE.
   */
  public boolean next() {
    if (hasNext()) {
      return setNext();
    }
    return false;
  }

  /**
   * Moves the cursor forward to the next token.
   * 
   * @return TRUE, if successful, otherwise FALSE.
   */
  private boolean setNext() {
    boolean success = true;
    currentIndex++;
    current = "";
    params.clear();
    String t = tokenArr[currentIndex];
    if (!t.contains(PARAM_SEPARATOR)) {
      current = t;
    } else {
      String[] paramArr = t.split(PARAM_SEPARATOR);
      if (paramArr != null) {
        if (paramArr.length >= 1) {
          current = paramArr[0];
          if (paramArr.length > 1) {
            for (int i = 1; i < paramArr.length; ++i) {
              String[] currentParamArr = paramArr[i].split(PARAM_ASSIGNMENT);
              if (currentParamArr.length == 2) {
                params.put(currentParamArr[0], currentParamArr[1]);
              }
            }
          }
        } else if (paramArr.length == 0) {
          success = false;
        }
      } else {
        success = false;
      }
    }
    return success;
  }

  /**
   * @return TRUE, if there is another token following the current one, FALSE
   *         otherwise.
   */
  public boolean hasNext() {
    if (token != null && currentIndex + 1 < tokenArr.length
        && tokenArr[currentIndex + 1] != null) {
      return true;
    }
    return false;
  }

  /**
   * @return The current token.
   */
  public String getCurrentToken() {
    return current;
  }

  /**
   * @return The current token's parameters.
   */
  public Map<String, String> getParameters() {
    return params;
  }

  /**
   * @return TRUE if the currentToken has parameters. false otherwise.
   */
  public boolean currentHasParameters() {
    return !params.isEmpty();
  }

  /**
   * @return The complete token as given as constructor arg.
   */
  public String getCompleteToken() {
    return token;
  }

  /**
   * Builds a historytoken from a given token, a navigationTarget and
   * parameters. The navigationTarget is added to the given token. The
   * parameters, if given, are added to the token at the end of the string.
   * Examples: - Token given, parameters given: ..
   * token&navTarget;param1=val1;param2=val2 - Token given, no parameters: ..
   * token&navTarget - No token or params: .. navTarget - no token, but params:
   * .. navTarget;param1=val1
   * 
   * @param token
   *          <i>optional</i> A history token (can also be an already valid
   *          token).
   * @param navTarget
   *          <b>mandatory</b> a navigationTarget.
   * @param params
   *          <i>optional</i> Parameters as key-value pairs.
   * @return The historytoken string.
   */
  public static String buildToken(String token, String navTarget,
      Map<String, String> params) {
    StringBuilder tok = new StringBuilder();
    if (navTarget != null) {
      if (token != null) {
        tok.append(token);
        if (!token.endsWith(TOKEN_SEPARATOR)) {
          tok.append(TOKEN_SEPARATOR);
        }
      }
      tok.append(navTarget);
      if (params != null) {
        for (String key : params.keySet()) {
          tok.append(PARAM_SEPARATOR).append(key).append(PARAM_ASSIGNMENT)
              .append(params.get(key));
        }
      }
    }
    return tok.toString();
  }

  @Override
  public String toString() {
    return token;
  }
}
