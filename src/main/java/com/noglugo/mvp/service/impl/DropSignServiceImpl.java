package com.noglugo.mvp.service.impl;

import com.dropbox.sign.ApiClient;
import com.dropbox.sign.ApiException;
import com.dropbox.sign.Configuration;
import com.dropbox.sign.api.EmbeddedApi;
import com.dropbox.sign.api.SignatureRequestApi;
import com.dropbox.sign.auth.HttpBasicAuth;
import com.dropbox.sign.model.*;
import com.noglugo.mvp.service.DropSignService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DropSignServiceImpl implements DropSignService {

    @Value("${dropbox-sign.infos.clientId}")
    private String clientId;

    @Value("${dropbox-sign.infos.role}")
    private String role;

    @Value("${dropbox-sign.infos.templateId}")
    private String templateId;

    @Value("${dropbox-sign.infos.apiKey}")
    private String dropBoxSignApiKey;

    @Override
    public String getEmbeddedSigningUrl(String email, String name) {
        String signingUrl = null;
        ApiClient apiClient = Configuration.getDefaultApiClient();
        HttpBasicAuth apiKey = (HttpBasicAuth) apiClient.getAuthentication("api_key");
        apiKey.setUsername(dropBoxSignApiKey);

        SignatureRequestApi signatureRequestApi = new SignatureRequestApi(apiClient);

        SubSignatureRequestTemplateSigner signer = new SubSignatureRequestTemplateSigner().role(role).emailAddress(email).name(name);

        SubSigningOptions subSigningOptions = new SubSigningOptions()
            .draw(true)
            .type(true)
            .upload(true)
            .phone(false)
            .defaultType(SubSigningOptions.DefaultTypeEnum.DRAW);

        SignatureRequestCreateEmbeddedWithTemplateRequest signatureRequestCreateEmbeddedWithTemplateRequest = new SignatureRequestCreateEmbeddedWithTemplateRequest()
            .clientId(clientId)
            .templateIds(List.of(templateId))
            .subject("NoGluGo Contrat")
            .message("Merci de signer le contrat")
            .signers(List.of(signer))
            .signingOptions(subSigningOptions)
            .testMode(true);

        try {
            SignatureRequestGetResponse response = signatureRequestApi.signatureRequestCreateEmbeddedWithTemplate(
                signatureRequestCreateEmbeddedWithTemplateRequest
            );

            EmbeddedApi embeddedApi = new EmbeddedApi(apiClient);

            String signatureId = Objects
                .requireNonNull(Objects.requireNonNull(response.getSignatureRequest()).getSignatures())
                .get(0)
                .getSignatureId();

            EmbeddedSignUrlResponse result = embeddedApi.embeddedSignUrl(signatureId);

            signingUrl = result.getEmbedded().getSignUrl();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return signingUrl;
    }
}
