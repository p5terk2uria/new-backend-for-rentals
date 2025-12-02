package productservice.config;

public abstract class BaseController {

    protected <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    protected <T> ApiResponse<T> success(T data) {
        return success("Success", data);
    }

    protected ApiResponse<?> failure(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
