package com.mishkun.yandextestexercise.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Mishkun on 12.04.2017.
 */

@Scope
@Retention(RUNTIME)
public @interface PerActivity {}
