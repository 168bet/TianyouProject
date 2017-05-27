package com.kakao.game.response;

import com.kakao.auth.network.response.JSONObjectResponse;
import com.kakao.game.StringSet;
import com.kakao.game.response.model.InvitationSender;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseBodyArray;
import com.kakao.network.response.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by house.dr on 16. 9. 19..
 */
public class InvitationSenderListResponse extends JSONObjectResponse {
    private final List<InvitationSender> invitationSenderList;

    public InvitationSenderListResponse(ResponseData responseData) throws ResponseBody.ResponseBodyException, ApiResponseStatusError {
        super(responseData);
        ResponseBodyArray responseInvitationSenders = body.getArray(StringSet.invitation_sender_list);
        this.invitationSenderList = responseInvitationSenders.getConvertedList(ARRAY_CONVERTER);
    }

    public List<InvitationSender> getInvitationSenderList() {
        return invitationSenderList;
    }

    public static final ResponseBodyArray.ArrayConverter<ResponseBody, InvitationSender> ARRAY_CONVERTER = new ResponseBodyArray.ArrayConverter<ResponseBody, InvitationSender>() {
        @Override
        public ResponseBody fromArray(JSONArray array, int i) throws ResponseBody.ResponseBodyException {
            try {
                return new ResponseBody(HttpURLConnection.HTTP_OK, array.getJSONObject(i));
            } catch (JSONException e) {
                throw new ResponseBody.ResponseBodyException("");
            }
        }

        @Override
        public InvitationSender convert(ResponseBody o) throws ResponseBody.ResponseBodyException {
            return new InvitationSender(o);
        }
    };
}
