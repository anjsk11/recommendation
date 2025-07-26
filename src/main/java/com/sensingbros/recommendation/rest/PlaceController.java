package com.sensingbros.recommendation.rest;

import com.sensingbros.recommendation.model.*;
import com.sensingbros.recommendation.service.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    // UserService 주입
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<ResponseDTO<?>> getPlaceById(@PathVariable Integer placeId) {
        try {
            Optional<PlaceDTO> placeDTOOptional = placeService.getPlaceById(placeId);
            if (placeDTOOptional.isPresent()) {
                PlaceDTO placeDTO = placeDTOOptional.get();
                return ResponseEntity.ok(new ResponseDTO<>(true, placeDTO));
            } else {
                // 장소가 없는 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(false, null));
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(false, e.getMessage()));
        }
    }

}
