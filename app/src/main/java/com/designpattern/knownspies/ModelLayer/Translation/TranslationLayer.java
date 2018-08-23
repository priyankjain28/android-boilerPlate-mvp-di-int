package com.designpattern.knownspies.ModelLayer.Translation;

import com.designpattern.knownspies.ModelLayer.DTOs.SpyDTO;
import com.designpattern.knownspies.ModelLayer.Database.Realm.Spy;
import com.designpattern.knownspies.ModelLayer.Enums.DTOType;

import java.util.List;

import io.realm.Realm;

public interface TranslationLayer {
    List<SpyDTO> convertJson(String json);

    SpyTranslator translatorFor(DTOType type);

    SpyDTO translate(Spy spy);

    Spy translate(SpyDTO dto, Realm realm);
}
