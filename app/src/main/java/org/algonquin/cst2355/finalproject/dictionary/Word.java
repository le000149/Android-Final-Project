package org.algonquin.cst2355.finalproject.dictionary;

import java.util.ArrayList;

public class Word {
    private String word;
    private String phonetic;
    private ArrayList<Phonetics> phonetics;
    private ArrayList<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public ArrayList<Phonetics> getPhonetics() {
        return phonetics;
    }

    public ArrayList<Meaning> getMeanings() {
        return meanings;
    }

    public class Phonetics {
        private String text;
        private String audio;

        public String getText() {
            return text;
        }

        public String getAudio() {
            return audio;
        }
    }

    public class Meaning {
        private String partOfSpeech;
        private ArrayList<Definition> definitions;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public ArrayList<Definition> getDefinitions() {
            return definitions;
        }
    }

    public class Definition {
        private String definition;
        private ArrayList<String> synonyms;
        private ArrayList<String> antonyms;

        public String getDefinition() {
            return definition;
        }

        public ArrayList<String> getSynonyms() {
            return synonyms;
        }

        public ArrayList<String> getAntonyms() {
            return antonyms;
        }

    }
}
