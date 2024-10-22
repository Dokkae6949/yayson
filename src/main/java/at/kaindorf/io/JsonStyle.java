package at.kaindorf.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonStyle {
    private boolean objectMultiline;
    private boolean arrayMultiline;
    private boolean spaceBeforeColon;
    private boolean spaceAfterColon;
    private boolean spaceAfterComma;
    private int indentation;

    public static JsonStyle pretty() {
        JsonStyle format = new JsonStyle();
        format.objectMultiline = true;
        format.arrayMultiline = true;
        format.spaceBeforeColon = false;
        format.spaceAfterColon = true;
        format.spaceAfterComma = true;
        format.indentation = 2;

        return format;
    }

    public static JsonStyle ugly() {
        JsonStyle format = new JsonStyle();
        format.objectMultiline = true;
        format.arrayMultiline = true;
        format.spaceBeforeColon = true;
        format.spaceAfterColon = true;
        format.spaceAfterComma = true;
        format.indentation = 4;

        return format;
    }

    public static JsonStyle compact() {
        JsonStyle format = new JsonStyle();
        format.objectMultiline = false;
        format.arrayMultiline = false;
        format.spaceBeforeColon = false;
        format.spaceAfterColon = false;
        format.spaceAfterComma = false;
        format.indentation = 0;

        return format;
    }
}
