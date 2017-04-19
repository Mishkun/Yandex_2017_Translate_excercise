package com.mishkun.yandextestexercise.data.mappers;

import android.util.Log;

import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.data.responses.DictionaryResponse;
import com.mishkun.yandextestexercise.domain.entities.Definition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class DictionaryResponseMapper {
    private static final String TAG = DetectionResponseMapper.class.getSimpleName();

    static public Definition transform(DictionaryResponse dictionaryResponse) {
        // If dictionary response is not supported, return nothing
        if (dictionaryResponse.definitions.size() == 0)
            return new Definition(null, null, null);
        String transcription = dictionaryResponse.definitions.get(0).transcription;
        String text = dictionaryResponse.definitions.get(0).original;
        List<Definition.DefinitionItem> definitionItems = new ArrayList<>();
        for (DictionaryResponse.DefinitionResponse definitionResponse : dictionaryResponse.definitions) {
            for (DictionaryResponse.TranslationDefinitionResponse translationResponse : definitionResponse.translationDefinitionResponses) {
                Definition.DefinitionItem definitionItem = new Definition.DefinitionItem(
                        translationResponse.synonyms != null ? flattenTextResponse(translationResponse.synonyms) : new ArrayList<String>(),
                        translationResponse.meanings != null ? flattenTextResponse(translationResponse.meanings) : null);
                definitionItem.getSynonyms().add(translationResponse.translation);
                definitionItems.add(definitionItem);
            }
        }
        return new Definition(text, transcription, definitionItems);
    }

    static private List<String> flattenTextResponse(List<DictionaryResponse.TextResponse> textResponses) {
        List<String> texts = new ArrayList<>();
        for (DictionaryResponse.TextResponse textResponse : textResponses) {
            texts.add(textResponse.text);
        }

        return texts;
    }
}
