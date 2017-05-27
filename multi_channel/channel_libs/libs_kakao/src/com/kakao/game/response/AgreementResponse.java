package com.kakao.game.response;

import com.kakao.auth.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static com.kakao.network.response.ResponseBody.toMap;

/**
 * Created by house.dr on 2016. 10. 18..
 */

public class AgreementResponse extends JSONObjectResponse {
    private Boolean checkAgreement;
    private String params = "";

    public AgreementResponse(ResponseData responseData) throws ResponseBody.ResponseBodyException, ApiResponseStatusError, UnsupportedEncodingException {
        super(responseData);
        checkAgreement = (body.getString("agreementPopup").equals("y"));
        ResponseBody optBody = body.optBody("agreement", null);
        Map<String, String> map = toMap(body);


        for (Map.Entry<String, String> entry : map.entrySet()) {
            if ((!entry.getKey().equals("agreementPopup") && !entry.getKey().equals("agreement")) ||  ((entry.getKey().equals("agreement") && optBody == null))) {
                params = params + entry.getKey() + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8") + "&";
            }
        }

        if (optBody != null) {
            Map<String, String> agreementMap = toMap(optBody);
            for (Map.Entry<String, String> entry : agreementMap.entrySet()) {
                params = params + entry.getKey() + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8")  + "&";
            }
        }

        params = params.substring(0, params.length() - 1);
    }

    public Boolean getAgreementPopup() {
        return checkAgreement;
    }

    public String getParams() {
        return params;
    }
}
