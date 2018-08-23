package com.designpattern.knownspies.ModelLayer;

import com.designpattern.knownspies.Helpers.Threading;
import com.designpattern.knownspies.ModelLayer.DTOs.SpyDTO;
import com.designpattern.knownspies.ModelLayer.Database.DataLayer;
import com.designpattern.knownspies.ModelLayer.Database.Realm.Spy;
import com.designpattern.knownspies.ModelLayer.Enums.Source;
import com.designpattern.knownspies.ModelLayer.Network.NetworkLayer;
import com.designpattern.knownspies.ModelLayer.Translation.SpyTranslator;
import com.designpattern.knownspies.ModelLayer.Translation.TranslationLayer;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ModelLayerImpl implements ModelLayer {
    private NetworkLayer networkLayer;
    private DataLayer dataLayer;
    private TranslationLayer translationLayer;

    public ModelLayerImpl(NetworkLayer networkLayer, DataLayer dataLayer, TranslationLayer translationLayer) {
        this.networkLayer = networkLayer;
        this.dataLayer = dataLayer;
        this.translationLayer = translationLayer;
    }

    @Override
    public void loadData(Consumer<List<SpyDTO>> onNewResults, Consumer<Source> notifyDataReceived) {
        SpyTranslator translator = translationLayer.translatorFor(SpyDTO.dtoType);

        try {
            dataLayer.loadSpiesFromLocal(translator::translate, onNewResults);
            notifyDataReceived.accept(Source.local);
        } catch (Exception e) {
            e.printStackTrace();
        }

        networkLayer.loadJson(json -> {
            notifyDataReceived.accept(Source.network);
            persistJson(json, ()->dataLayer.loadSpiesFromLocal(translator::translate, onNewResults));
        });


    }

    @Override
    public SpyDTO spyForId(int spyId) {
        Spy spy = dataLayer.spyForId(spyId);
        SpyDTO spyDTO = translationLayer.translate(spy);
        return spyDTO;
    }


    private void persistJson(String json, Action finished) {
        List<SpyDTO> dtos = translationLayer.convertJson(json);

        Threading.async(()->{
           dataLayer.clearSpies(() -> {
               dtos.forEach(dto -> dto.initialize());

               SpyTranslator translator = translationLayer.translatorFor(SpyDTO.dtoType);
               dataLayer.persistDTOs(dtos, translator::translate);

               Threading.dispatchMain(() -> finished.run());
           });

            return true;
        });

    }

}
