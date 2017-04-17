package com.mishkun.yandextestexercise.data.mappers;

import com.mishkun.yandextestexercise.data.responses.DictionaryResponse;
import com.mishkun.yandextestexercise.domain.entities.Definition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class DictionaryResponseMapper {
    static public Definition transform(DictionaryResponse dictionaryResponse) {
        String transcription = dictionaryResponse.definitions.get(0).transcription;
        String text = dictionaryResponse.definitions.get(0).original;
        List<Definition.DefinitionItem> definitionItems = new ArrayList<>();
        for (DictionaryResponse.DefinitionResponse definitionResponse : dictionaryResponse.definitions) {

            for (DictionaryResponse.TranslationDefinitionResponse translationResponse : definitionResponse.translationDefinitionResponses)
                definitionItems.add(new Definition.DefinitionItem(flattenTextResponse(translationResponse.synonyms),
                                                                  flattenTextResponse(translationResponse.meanings)));
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
