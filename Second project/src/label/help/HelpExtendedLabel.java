package label.help;

import label.Label;
import label.ProxyLabel;

public class HelpExtendedLabel implements HelpLabel {

    private final Label label;

    private final String helpText; // can be label

    public HelpExtendedLabel(String helpText, Label label) {
        if (label instanceof ProxyLabel) {
            throw new IllegalArgumentException("TODO"); // TODO
        }

        // TODO validate helpText

        this.helpText = helpText;
        this.label = label;
    }
    @Override
    public String getHelpText() {
        return (helpText != null ? helpText : "");
    }

    @Override
    public String getText() {
        return label.getText();
    }

    // TODO: ask for setter
}
