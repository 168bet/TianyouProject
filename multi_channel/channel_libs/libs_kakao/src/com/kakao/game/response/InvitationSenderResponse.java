package com.kakao.game.response;

import com.kakao.auth.network.response.JSONObjectResponse;
import com.kakao.game.StringSet;
import com.kakao.game.response.model.InvitationSender;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseBody.ResponseBodyException;
import com.kakao.network.response.ResponseData;

/**
 * Created by house.dr on 16. 2. 13..
 */
public class InvitationSenderResponse extends JSONObjectResponse {
    private final InvitationSender invitationSender;

    public InvitationSenderResponse(ResponseData responseData) throws ResponseBodyException, ApiResponseStatusError {
        super(responseData);

        ResponseBody sender = this.body.optBody(StringSet.invitation_sender, null);
        if (sender == null) {
            this.invitationSender = null;
        } else {
            this.invitationSender = new InvitationSender(sender);
        }
    }

    public InvitationSender getInvitationSender() {
        return invitationSender;
    }
}
