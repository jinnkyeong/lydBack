package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.*;
import com.loveyourdog.brokingservice.model.dto.responseDto.BasicRequireResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.DiaryResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.InquiryResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import com.loveyourdog.brokingservice.service.InquiryService;
import com.loveyourdog.brokingservice.service.ReservationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "예약 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class ReservationController {


    private final ReservationService reservationService;



    // reservation 생성
    // parameter : requestDto(inquiry, offer 둘 중 하나)
    @PostMapping("/reservations")
    public ResponseEntity<Boolean> createReservation(@RequestBody ReservationRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(reservationService.createReservation(requestDto), HttpStatus.OK);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getReservations(), HttpStatus.OK);
    }

    // reservationId -> reservation
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable("id") Long id
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getReservationById(id), HttpStatus.OK);
    }
    // dogwalker Id -> reservation
    @GetMapping("/reservations/dw/{dwId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByDwId(@PathVariable("dwId") Long dwId
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getReservationsByDwId(dwId), HttpStatus.OK);
    }

    // dogwalker Id, status -> reservation
    @GetMapping("/reservations/dw/{dwId}/{status}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByDwIdAndStatus(@PathVariable("dwId") Long dwId,
                                                                                       @PathVariable("status") int status
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getReservationsByDwIdAndStatus(dwId,status), HttpStatus.OK);
    }
    @GetMapping("/reservations/cus/{cusId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCusId(@PathVariable("cusId") Long cusId
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getReservationsByCusId(cusId), HttpStatus.OK);
    }
    // customer Id, status -> reservation
    @GetMapping("/reservations/cus/{cusId}/{status}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCusIdAndStatus(@PathVariable("cusId") Long cusId,
                                                                                       @PathVariable("status") int status
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getReservationsByCusIdAndStatus(cusId,status), HttpStatus.OK);
    }

    @GetMapping("/basicRequires/{reservId}")
    public ResponseEntity<List<BasicRequireResponseDto>> getBasicRequiresByReservId(@PathVariable("reservId") Long reservId
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getBasicRequiresByReservId(reservId), HttpStatus.OK);
    }

    @GetMapping("/cusRequires/{reservId}")
    public ResponseEntity<List<CusRequireDto>> getCusRequiresByReservId(@PathVariable("reservId") Long reservId
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getCusRequiresByReservId(reservId), HttpStatus.OK);
    }



    @PostMapping("/reservations/mod/{reservId}")
    public ResponseEntity<Boolean> modifyReservation(@PathVariable("reservId") Long reservId,
                                                     @RequestBody ReservationRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(reservationService.modifyReservation(reservId, requestDto), HttpStatus.OK);
    }


    @PostMapping(value = "/diaries", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createDiary(
            @RequestPart(value="imageB", required=false) List<MultipartFile> filesB,
            @RequestPart(value="imageC", required=false) List<MultipartFile> filesC,
            @RequestPart(value = "requestDto") DiaryRequestDto requestDto
    ) throws Exception {
        return new ResponseEntity<>(reservationService.createDiary(filesB,filesC, requestDto), HttpStatus.OK);
    }

    @GetMapping("/diaries/cus/{cusId}")
    public ResponseEntity<List<ReservationResponseDto>> getDiariesByCusId(@PathVariable("cusId") Long cusId
    ) throws Exception {
        return new ResponseEntity<>(reservationService.getDiariesByCusId(cusId), HttpStatus.OK);
    }

    @PostMapping("/purchaseSuccess")
    public ResponseEntity<Boolean> purchase(@RequestBody PurchaseRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(reservationService.purchase(requestDto), HttpStatus.OK);
    }

}
