package com.mishkun.yandextestexercise.domain.providers;

import io.reactivex.Observable;

/**
 * Created by Mishkun on 16.04.2017.
 */

public interface GuessPrefsProvider {
    public Observable<Boolean> getGuessingUserPrefs();
}
