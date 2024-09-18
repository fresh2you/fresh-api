package com.zb.fresh_api.api.controller.health;

import com.zb.fresh_api.common.exception.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "헬스체크 API",
        description = "헬스체크"
)
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthCheckController {

    @Operation(
            summary = "헬스 체크",
            description = "서버 헬스 체크를 진행합니다."
    )
    @GetMapping
    public ResponseEntity<String> check() {
        return ResponseEntity.ok().body(ResponseCode.SUCCESS.getMessage());
    }

}
