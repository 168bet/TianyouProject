package com.kakao.game.response;

import com.kakao.auth.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody.ResponseBodyException;
import com.kakao.network.response.ResponseData;

/**
 * Created by house.dr on 2017. 2. 3..
 */

public class PlusFriendResponse extends JSONObjectResponse {
    private final boolean isFriend;

    public PlusFriendResponse(ResponseData responseData) throws ResponseBodyException, ApiResponseStatusError {
        super(responseData);
        isFriend = body.getBoolean("is_friend");
    }

    public boolean isFriend() {
        return isFriend;
    }
}
