package algonquin.cst2335.finalproject.ui;

import java.util.List;

public class DictionaryEntry {
    private String word;
    private List<String> definitions;

    public DictionaryEntry(String word, List<String> definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }
}


