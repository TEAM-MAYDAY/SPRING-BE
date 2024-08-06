package com.likelion.maydayspring.controller;



import com.likelion.maydayspring.dto.response.ListResponse;
import com.likelion.maydayspring.dto.response.LocationDetailResponse;
import com.likelion.maydayspring.dto.response.LocationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로케이션 API", description = "로케이션 관련 API")
@RestController
@RequestMapping("/api/v1/location")
@ApiResponse(responseCode = "200", description = "OK")
public interface LocationApi {
    @Operation(summary = "전체 location 정보 반환 API", description = "전체 location 정보를 반환합니다.")
    @GetMapping("/all")
    ResponseEntity<ListResponse<LocationResponse>> getAllLocations();



    @Operation(summary = "location 세부 정보 반환 API", description = "로케이션의 세부 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "BAD REQUEST", content = @Content(
        mediaType = "application/json",
        examples = {
            @ExampleObject(name = "LC0002", description = "해당하는 Location이 없을 때 발생합니다.",
                value = """
                                    {"code": "LC0002", "message": "존재하지않는 Location ID입니다."}
                                    """
            )
        }, schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/info/{locationId}")
    ResponseEntity<LocationDetailResponse> getLocationDetail(
        @PathVariable("locationId")
        @Parameter(description = "location id")
            Long locationId
    );
}
