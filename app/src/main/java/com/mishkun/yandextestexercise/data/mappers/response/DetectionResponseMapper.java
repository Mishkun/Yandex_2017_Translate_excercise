package com.mishkun.yandextestexercise.data.mappers.response;

import com.mishkun.yandextestexercise.data.responses.DetectionResponse;
import com.mishkun.yandextestexercise.domain.entities.Language;

/**
 * Created by Mishkun on 17.04.2017.
 */

public class DetectionResponseMapper {

    public static Language transform(DetectionResponse detectionResponse) {
        return new Language(detectionResponse.getLanguageCode(), null);
    }
}
