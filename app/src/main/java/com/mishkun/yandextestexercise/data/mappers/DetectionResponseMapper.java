package com.mishkun.yandextestexercise.data.mappers;

import com.mishkun.yandextestexercise.domain.entities.Language;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class DetectionResponseMapper {

    public Language transform(String keyCode) {
        return new Language(keyCode, null);
    }
}
