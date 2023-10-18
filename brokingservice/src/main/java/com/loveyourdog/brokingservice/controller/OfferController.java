package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.OfferRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.OfferResponseDto;
import com.loveyourdog.brokingservice.model.entity.Offer;
import com.loveyourdog.brokingservice.service.OfferService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "(도그워커의)제안 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class OfferController {


    private final OfferService offerService;

    // 제안하기
    // parameter : OfferRequestDto
    // return : boolean
    @PostMapping("/offers")
    public ResponseEntity<Boolean> createOffer(@RequestBody OfferRequestDto requestDto){
        return new ResponseEntity<>(offerService.createOffer(requestDto), HttpStatus.OK);
    }
    // 도그워커가 한 제안 찾기 / 고객이 받은 제안 찾기
    @GetMapping("/offers/{userId}/{userType}/{key}")
    public ResponseEntity<List<OfferResponseDto>> getOfferById(@PathVariable("userId") Long userId,
                                                               @PathVariable("userType") String userType,
                                                               @PathVariable("key") int key
    ) {
        return new ResponseEntity<>(offerService.getOfferById(userId,userType,key), HttpStatus.OK);
    }

    @GetMapping("/offers/invalid/{offerId}/{status}")
    public ResponseEntity<Boolean> invalidateOffer(@PathVariable("offerId") Long offerId,
                                                   @PathVariable("status") int status) {
        return new ResponseEntity<>(offerService.invalidateOffer(offerId, status), HttpStatus.OK);
    }

}
