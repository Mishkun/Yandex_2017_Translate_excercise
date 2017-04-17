package com.mishkun.yandextestexercise.domain.entities;

import java.util.List;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class Definition {
    private String text;
    private String transcription;
    private List<DefinitionItem> definitionItems;

    public Definition(String text, String transcription, List<DefinitionItem> definitionItems) {
        this.text = text;
        this.transcription = transcription;
        this.definitionItems = definitionItems;
    }

    public String getText() {
        return text;
    }

    public String getTranscription() {
        return transcription;
    }

    public List<DefinitionItem> getDefinitionItems() {
        return definitionItems;
    }

    public static class DefinitionItem {
        private List<String> synonyms;
        private List<String> meanings;

        public DefinitionItem(List<String> synonyms, List<String> meanings) {
            this.synonyms = synonyms;
            this.meanings = meanings;
        }

        public List<String> getSynonyms() {
            return synonyms;
        }

        public List<String> getMeanings() {
            return meanings;
        }
    }
}