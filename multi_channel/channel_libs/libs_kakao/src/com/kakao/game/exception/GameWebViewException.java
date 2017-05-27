package com.kakao.game.exception;

/**
 * Created by house.dr on 2016. 10. 17..
 */

public class GameWebViewException extends Throwable {
    private final String message;

    public GameWebViewException(final int errorCode, final String errorMessage, final String requestUrl) {
        StringBuilder message = new StringBuilder("code = ").append(errorCode);
        if(errorMessage != null)
            message.append(", msg = ").append(errorMessage);
        if(requestUrl != null)
            message.append(", url = ").append(requestUrl);
        this.message = message.toString();
    }

    public String getMessage(){
        return message;
    }
}
