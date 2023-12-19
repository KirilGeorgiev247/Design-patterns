package flyweight;

import transformations.CensorTransformation;
import transformations.TextTransformation;

import java.util.HashMap;
import java.util.Map;

public class CensorTransformationFactory {
    private final Map<String, CensorTransformation> flyweights = new HashMap<>();

    public TextTransformation getCensorTransformation(String word) {
        if (word.length() <= 4) {
            flyweights.putIfAbsent(word, new CensorTransformation(word));
            return flyweights.get(word);
        } else {
            return new CensorTransformation(word);
        }
    }
}