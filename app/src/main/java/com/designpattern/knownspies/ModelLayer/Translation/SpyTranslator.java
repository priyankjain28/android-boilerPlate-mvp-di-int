package com.designpattern.knownspies.ModelLayer.Translation;

import com.designpattern.knownspies.ModelLayer.DTOs.SpyDTO;
import com.designpattern.knownspies.ModelLayer.Database.Realm.Spy;

import io.realm.Realm;

public interface SpyTranslator {
    SpyDTO translate(Spy from);
    Spy translate(SpyDTO dto, Realm realm);
}
