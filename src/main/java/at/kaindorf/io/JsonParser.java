package at.kaindorf.io;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    private final String input;
    private int index = 0;

    public JsonParser(@NonNull String input) {
        this.input = input;
    }

    /**
     * Parses the input JSON string.
     * @return the parsed JSON value
     */
    public JsonValue parse() {
        return parseValue();
    }

    private JsonValue parseValue() {
        skipWhitespace();

        if (!hasNext()) {
            throw new RuntimeException("Unexpected end of input");
        }

        if (hasExpected("true")) {
            return new JsonBoolean(true);
        } else if (hasExpected("false")) {
            return new JsonBoolean(false);
        } else if (hasExpected("null")) {
            return new JsonNull();
        } else if (hasExpected('"')) {
            return new JsonString(parseString());
        } else if (hasExpected('[')) {
            return parseArray();
        } else if (hasExpected('{')) {
            return parseObject();
        } else if (isDigit(peek()) || peek() == '-') {
            return new JsonNumber(parseNumber());
        } else {
            throw new RuntimeException("Unexpected character '" + peek() + "'");
        }
    }

    /**
     * Parses an object from the input.
     * @return the parsed object
     */
    private JsonObject parseObject() {
        expect('{');
        Map<String, JsonValue> map = new LinkedHashMap<>();
        skipWhitespace();

        if (hasExpected('}')) {
            advance();
            return new JsonObject(map);
        }

        while (true) {
            String key = parseString();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            JsonValue value = parseValue();
            map.put(key, value);
            skipWhitespace();

            if (hasExpected(',')) {
                advance();
                skipWhitespace();
            } else if (hasExpected('}')) {
                advance();
                break;
            } else {
                throw new RuntimeException("Expected ',' or '}' but got '" + peek() + "'");
            }
        }

        return new JsonObject(map);
    }

    /**
     * Parses an array from the input.
     * @return the parsed array
     */
    private JsonArray parseArray() {
        expect('[');
        List<JsonValue> values = new ArrayList<>();
        skipWhitespace();

        if (hasExpected(']')) {
            advance();
            return new JsonArray(values);
        }

        while (true) {
            values.add(parseValue());
            skipWhitespace();

            if (hasExpected(',')) {
                advance();
                skipWhitespace();
            } else if (hasExpected(']')) {
                advance();
                break;
            } else {
                throw new RuntimeException("Expected ',' or ']' but got '" + peek() + "'");
            }
        }

        return new JsonArray(values);
    }

    /**
     * Parses a number from the input.
     * @return the parsed number
     */
    private Number parseNumber() {
        StringBuilder sb = new StringBuilder();
        if (peek() == '-') {
            sb.append(advance());
        }

        while (hasNext() && isDigit(peek())) {
            sb.append(advance());
        }

        if (hasNext() && peek() == '.') {
            sb.append(advance());

            if (!hasNext() || !isDigit(peek())) {
                throw new RuntimeException("Expected digit after '.' but got '" + peek() + "'");
            }

            do {
                sb.append(advance());
            } while (hasNext() && isDigit(peek()));
        }

        if (hasNext() && (peek() == 'e' || peek() == 'E')) {
            sb.append(advance());
            if (hasNext() && (peek() == '+' || peek() == '-')) {
                sb.append(advance());
            }

            while (hasNext() && isDigit(peek())) {
                sb.append(advance());
            }
        }

        String number = sb.toString();

        if (number.contains(".")) {
            return Double.parseDouble(number);
        } else {
            return Long.parseLong(number);
        }
    }

    /**
     * Parses a string from the input.
     * @return the parsed string
     */
    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (hasNext() && peek() != '"') {
            char c = advance();
            if (c == '\\') {
                c = advance(); // Handle escape characters
                if (c == 'n') sb.append('\n');
                else if (c == 't') sb.append('\t');
                else if (c == 'r') sb.append('\r');
                else if (c == 'b') sb.append('\b');
                else if (c == 'f') sb.append('\f');
                else if (c == '"' || c == '\\') sb.append(c);
                // TODO: Handle "\\uXXXX" according to spec
            } else {
                sb.append(c);
            }
        }
        expect('"');
        return sb.toString();
    }

    /**
     * Returns the current character in the input and advances the index.
     * @return the current character
     */
    private Character advance() {
        return input.charAt(index++);
    }

    /**
     * Returns the current character in the input without advancing the index.
     * @return the current character
     */
    private Character peek() {
        return input.charAt(index);
    }

    /**
     * Checks if there are more characters in the input.
     * @return true if there are more characters, false otherwise
     */
    private boolean hasNext() {
        return index < input.length();
    }

    /**
     * Skips any whitespace characters in the input.
     */
    private void skipWhitespace() {
        while (hasNext() && isWhitespace(peek())) {
            advance();
        }
    }

    /**
     * Checks if the next character in the input is the expected character.
     * @param expected the expected character
     * @throws RuntimeException if the next character does not match the expected character
     */
    private void expect(char expected) {
        if (hasNext() && peek() == expected) {
            advance();
        } else {
            throw new RuntimeException("Expected '" + expected + "' but got '" + peek() + "'");
        }
    }

    /**
     * Checks if the next character in the input is the expected character.
     * @param expected the expected character
     * @return true if the next character matches the expected character, false otherwise
     */
    private boolean hasExpected(char expected) {
        return hasNext() && peek() == expected;
    }

    /**
     * Checks if the next characters in the input match the expected string.
     * @param expected the expected string
     * @throws RuntimeException if the next characters do not match the expected string
     */
    private void expect(String expected) {
        for (char c : expected.toCharArray()) {
            if (!hasNext() || peek() != c) {
                throw new RuntimeException("Expected '" + expected + "' but got '" + peek() + "'");
            }
        }

        index += expected.length();
    }

    /**
     * Checks if the next characters in the input match the expected string.
     * @param expected the expected string
     * @return true if the next characters match the expected string, false otherwise
     */
    private boolean hasExpected(String expected) {
        int start = index;

        for (char c : expected.toCharArray()) {
            if (!hasNext() || peek() != c) {
                index = start;
                return false;
            }
            advance();
        }

        return true;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
}
