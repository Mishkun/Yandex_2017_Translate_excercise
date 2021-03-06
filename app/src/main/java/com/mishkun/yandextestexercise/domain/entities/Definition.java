package com.mishkun.yandextestexercise.domain.entities;

import java.util.List;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class Definition {
    private String original;
    private TranslationDirection direction;
    private String translation;
    private String transcription;
    private List<DefinitionItem> definitionItems;

    public Definition(String original, TranslationDirection direction, String translation, String transcription, List<DefinitionItem> definitionItems) {
        this.original = original;
        this.direction = direction;
        this.translation = translation;
        this.transcription = transcription;
        this.definitionItems = definitionItems;
    }

    public String getTranslation() {
        return translation;
    }

    public String getTranscription() {
        return transcription;
    }

    public List<DefinitionItem> getDefinitionItems() {
        return definitionItems;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public TranslationDirection getDirection() {
        return direction;
    }

    public void setDirection(TranslationDirection direction) {
        this.direction = direction;
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
