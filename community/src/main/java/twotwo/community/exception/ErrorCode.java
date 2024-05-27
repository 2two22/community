package twotwo.community.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CANNOT_FOLLOW_YOURSELF("자기 자신을 팔로우할 수 없습니다."),

    FAILED_UPLOAD_FILE("파일 업로드에 실패하였습니다"),
    NOT_SUPPORTED_IMAGE("지원하지 않는 이미지 타입입니다."),

    CANNOT_COVERT_IMAGE("이미지를 변환하는 데 에러가 발생했습니다."),
    CHANGE_IMPOSSIBLE_PINNED_ANSWER("레디스 메세지 브로커 과정에서 실패했습니다."),
    NOT_POST_OWNER("웹 소켓 연결에 오류가 발생했습니다."),
    NOT_FOUND_POST("해당 채팅방 정보가 없습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_FILE_FORMAT("유효하지 않은 파일 형식입니다."),
    CANNOT_ANSWER_YOURSELF("배치작업에 실패하였습니다"),
    NOT_FOUND_QNA_ANSWER_PIN("등록되지 않은 회원입니다."),
    FAILED_GET_COMMIT_INFO("커밋 정보를 가져오는 데 실패했습니다."),
    FAILED_CONNECT_GITHUB("깃헙과 연결에 실패했습니다."),
    INTERNAL_ERROR("내부 서버 오류가 발생했습니다.");

    private final String description;
}
