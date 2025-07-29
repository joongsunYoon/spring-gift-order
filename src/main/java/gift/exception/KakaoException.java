package gift.exception;

import gift.external.kakao.dto.KakaoErrorResponse;

public class KakaoException extends RuntimeException {

    private final int status;
    private final KakaoErrorResponse response;

    public KakaoException(KakaoErrorResponse response) {
        super("카카오 인증 에러");
        this.status = 500;
        this.response = response;
    }

    public int getStatus(){
        return status;
    }

    public KakaoErrorResponse getResponse() {
        return response;
    }
}
