package com.kakao.usermgmt.request;

import com.kakao.auth.network.request.ApiRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by house.dr on 2016. 10. 20..
 */

public class AddPlusFriendsRequest extends ApiRequest {
    private String plusFriendIds;

    public AddPlusFriendsRequest(String plusFriendIds) {
        this.plusFriendIds = plusFriendIds;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public String getUrl() {
        return ApiRequest.createBaseURL("kapi.kakao.com", "v1/api/talk/friends/add");
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("plus_friend_ids", plusFriendIds);
        return params;
    }
}
