package com.loveyourdog.brokingservice.service;


import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReviewRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import com.loveyourdog.brokingservice.repository.review.ReviewRepository;
import com.loveyourdog.brokingservice.repository.review.ReviewRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    //
    private final ReservationRepository reservationRepository;
    //    private final OfferRepository offerRepository;
//    private final InquiryRepository inquiryRepository;
    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
//    private final CusRequireRepository cusRequireRepository;
//    private final BasicRequireRepository basicRequireRepository;

    private final ReviewRepository reviewRepository;
    private final ReviewRepositoryImpl reviewRepositoryImpl;
    private final StorageService storageService;
    private final AdminService adminService;
    private final ReservationService reservationService;
//

    //
//
    public Long createReview(List<MultipartFile> files, ReviewRequestDto requestDto) throws Exception {
        Dogwalker dogwalker = null;
        Long newReviewId = 0L;
        Reservation reservation = reservationRepository.findById(requestDto.getReservationId()).orElseThrow(
                () -> new NoSuchElementException("id에 해당하는 reservation이 없습니다"));

        // 정산 완료 상태 - 리뷰를 작성 방지
        if (reservation.getStatus() == 6) {
            throw new Exception("정산 완료 상태에서는 리뷰를 작성할 수 없습니다.");
        }

        if (reservation.getReview() == null) {
            Review review = Review.builder()
                    .star(requestDto.getStar())
                    .context(requestDto.getContext())
                    .reservation(reservation)
                    .build();

            if (files != null && files.size() > 0) { // 이미지도 받은 경우
                // S3에 파일 업로드 한 후
                List<Image> images = storageService.uploadToS3("review", files);
                Image image = images.get(0); // 일단 배열로 받지만 파일 1개만 허용할 것
                review.setDirName(image.getDirName());
                review.setFileName(image.getFileName());
                review.setExtension(image.getExtension());
            }

            newReviewId = reviewRepository.save(review).getId(); // DB에 dirname, filename, extension 저장


            // 도그워커 평균 star 갱신
            if (reservation.getInquiry() != null) {
                dogwalker = reservation.getInquiry().getDogwalker();
            } else if (reservation.getOffer() != null) {
                dogwalker = reservation.getOffer().getDogwalker();
            }

            int newStar = adminService.getAverageStar(dogwalker.getStar(), // 기존 star
                    dogwalker.getGoalCnt() - 1, // 기존 goalcnt
                    requestDto.getStar());
            dogwalker.setStar(newStar);
            String gradeDw = reservationService.getDogwalkerGrade(dogwalker.getGoalCnt(), newStar);
            dogwalker.setGrade(gradeDw);
            dogwalkerRepository.save(dogwalker);


        } else {
            throw new Exception("해당 예약에 이미 리뷰가 존재합니다");
        }


        return newReviewId;
    }

    public List<ReviewResponseDto> getReviewsByDwId(Long dwId) {
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();

        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(
                () -> new NoSuchElementException("id에 해당하는 dogwalker가 없습니다"));

        if (dogwalker.getInquiries() != null) {
            for (Inquiry inquiry : dogwalker.getInquiries()) {
                Reservation reservation = inquiry.getReservation();
                if (reservation != null) {
                    Review review = reservation.getReview();
                    if (review != null) {
                        // 소요시간
                        Long seconds = Duration.between(reservation.getStartDt(), reservation.getEndDt()).getSeconds();
                        Long hour = seconds / 3600;
                        Long min = seconds % 3600 / 60;

                        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                                .dogwalkerId(dogwalker.getId())
                                .reviewId(review.getId())
                                .star(review.getStar())
                                .context(review.getContext())
                                .dirName(review.getDirName())
                                .fileName(review.getFileName())
                                .extension(review.getExtension())
                                .customerId(inquiry.getCustomer().getId())
                                .customerNick(inquiry.getCustomer().getNick())
                                .breed(inquiry.getCommision().getBreed())
                                .dogType(inquiry.getCommision().getDogType())
                                .month(inquiry.getCommision().getMonth())
                                .day(inquiry.getCommision().getDay())
                                .hour(hour)
                                .min(min)
                                .commentCnt(review.getComments().size())
                                .addrTown(inquiry.getCustomer().getAddrTown())
                                .addrState(inquiry.getCustomer().getAddrState())
                                .build();
                        reviewResponseDtos.add(responseDto);
                    } else {
                        log.warn("review null");
                    }
                } else {
                    log.warn("reservation null");

                }
            }
        } else {
            log.warn("inquiries null");
        }

        if (dogwalker.getOffers() != null) {
            for (Offer offer : dogwalker.getOffers()) {
                Reservation reservation = offer.getReservation();
                if (reservation != null) {
                    Review review = reservation.getReview();
                    if (review != null) {
                        // 소요시간
                        Long seconds = Duration.between(reservation.getStartDt(), reservation.getEndDt()).getSeconds();
                        Long hour = seconds / 3600;
                        Long min = seconds % 3600 / 60;

                        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                                .dogwalkerId(dogwalker.getId())
                                .reviewId(review.getId())
                                .star(review.getStar())
                                .context(review.getContext())
                                .dirName(review.getDirName())
                                .fileName(review.getFileName())
                                .extension(review.getExtension())
                                .customerId(offer.getCustomer().getId())
                                .customerNick(offer.getCustomer().getNick())
                                .breed(offer.getCommision().getBreed())
                                .dogType(offer.getCommision().getDogType())
                                .month(offer.getCommision().getMonth())
                                .day(offer.getCommision().getDay())
                                .hour(hour)
                                .min(min)
                                .commentCnt(review.getComments().size())
                                .addrTown(offer.getCustomer().getAddrTown())
                                .addrState(offer.getCustomer().getAddrState())
                                .build();
                        reviewResponseDtos.add(responseDto);
                    } else {
                        log.warn("review null");
                    }
                } else {
                    log.warn("reservation null");

                }
            }
        } else {
            log.warn("offers null");
        }

        return reviewResponseDtos;

    }

    public List<ReviewResponseDto> getReviewsByCusId(Long cusId) {
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();

        Customer customer = customerRespository.findById(cusId).orElseThrow(
                () -> new NoSuchElementException("id에 해당하는 dogwalker가 없습니다"));

        if (customer.getInquiries() != null) {
            for (Inquiry inquiry : customer.getInquiries()) {
                Reservation reservation = inquiry.getReservation();
                if (reservation != null) {
                    Review review = reservation.getReview();
                    if (review != null) {
                        // 소요시간
                        Long seconds = Duration.between(reservation.getStartDt(), reservation.getEndDt()).getSeconds();
                        Long hour = seconds / 3600;
                        Long min = seconds % 3600 / 60;

                        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                                .dogwalkerId(inquiry.getDogwalker().getId())
                                .reviewId(review.getId())
                                .star(review.getStar())
                                .context(review.getContext())
                                .dirName(review.getDirName())
                                .fileName(review.getFileName())
                                .extension(review.getExtension())
                                .customerId(customer.getId())
                                .customerNick(customer.getNick())
                                .breed(inquiry.getCommision().getBreed())
                                .dogType(inquiry.getCommision().getDogType())
                                .month(inquiry.getCommision().getMonth())
                                .day(inquiry.getCommision().getDay())
                                .hour(hour)
                                .min(min)
                                .commentCnt(review.getComments().size())
                                .addrTown(inquiry.getCustomer().getAddrTown())
                                .addrState(inquiry.getCustomer().getAddrState())
                                .build();
                        reviewResponseDtos.add(responseDto);
                    } else {
                        log.warn("review null");
                    }
                } else {
                    log.warn("reservation null");

                }
            }
        } else {
            log.warn("inquiries null");
        }

        if (customer.getOffers() != null) {
            for (Offer offer : customer.getOffers()) {
                Reservation reservation = offer.getReservation();
                if (reservation != null) {
                    Review review = reservation.getReview();
                    if (review != null) {
                        // 소요시간
                        Long seconds = Duration.between(reservation.getStartDt(), reservation.getEndDt()).getSeconds();
                        Long hour = seconds / 3600;
                        Long min = seconds % 3600 / 60;

                        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                                .dogwalkerId(offer.getDogwalker().getId())
                                .reviewId(review.getId())
                                .star(review.getStar())
                                .context(review.getContext())
                                .dirName(review.getDirName())
                                .fileName(review.getFileName())
                                .extension(review.getExtension())
                                .customerId(offer.getCustomer().getId())
                                .customerNick(offer.getCustomer().getNick())
                                .breed(offer.getCommision().getBreed())
                                .dogType(offer.getCommision().getDogType())
                                .month(offer.getCommision().getMonth())
                                .day(offer.getCommision().getDay())
                                .hour(hour)
                                .min(min)
                                .commentCnt(review.getComments().size())
                                .addrTown(offer.getCustomer().getAddrTown())
                                .addrState(offer.getCustomer().getAddrState())
                                .build();
                        reviewResponseDtos.add(responseDto);
                    } else {
                        log.warn("review null");
                    }
                } else {
                    log.warn("reservation null");

                }
            }
        } else {
            log.warn("offers null");
        }

        return reviewResponseDtos;

    }

    public List<ReviewResponseDto> getGoodReviews() {
        List<ReviewResponseDto> dtos = new ArrayList<>();
        List<Review> candidates = new ArrayList<>();
        List<Review> reviews = reviewRepositoryImpl.findGoodReviews(); // 4.5이상 최신순
        System.out.println("getGoodReviews length : "+reviews.size());

        if (reviews.size() > 0) {
            if (reviews.size() >= 4) {
                candidates.add(reviews.get(0));
                candidates.add(reviews.get(1));
                candidates.add(reviews.get(2));
                candidates.add(reviews.get(3));
            }else if (reviews.size() == 3){
                candidates.add(reviews.get(0));
                candidates.add(reviews.get(1));
                candidates.add(reviews.get(2));
            } else if (reviews.size() == 2) {
                candidates.add(reviews.get(0));
                candidates.add(reviews.get(1));
            } else {
                candidates.add(reviews.get(0));
            }
            for (Review review : candidates) {
                dtos.add(review.toDto());
            }
        }

        return dtos;
    }
    public ReviewResponseDto getReviewById(Long id) throws Exception {
        ReviewResponseDto responseDto = null;
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("id에 해당하는 review가 없습니다"));
        Reservation reservation = review.getReservation();
        // 소요시간
        Long seconds = Duration.between(reservation.getStartDt(), reservation.getEndDt()).getSeconds();
        Long hour = seconds / 3600;
        Long min = seconds % 3600 / 60;

        if (reservation.getInquiry() != null) {
            Inquiry inquiry = reservation.getInquiry();
            Dogwalker dogwalker = inquiry.getDogwalker();
            responseDto = ReviewResponseDto.builder()
                    .dogwalkerId(dogwalker.getId())
                    .reviewId(review.getId())
                    .star(review.getStar())
                    .context(review.getContext())
                    .dirName(review.getDirName())
                    .fileName(review.getFileName())
                    .extension(review.getExtension())
                    .customerId(inquiry.getCustomer().getId())
                    .customerNick(inquiry.getCustomer().getNick())
                    .breed(inquiry.getCommision().getBreed())
                    .dogType(inquiry.getCommision().getDogType())
                    .month(inquiry.getCommision().getMonth())
                    .day(inquiry.getCommision().getDay())
                    .hour(hour)
                    .min(min)
                    .commentCnt(review.getComments().size())
                    .build();
            return responseDto;

        } else if (reservation.getOffer() != null) {
            Offer offer = reservation.getOffer();
            Dogwalker dogwalker = offer.getDogwalker();
            responseDto = ReviewResponseDto.builder()
                    .dogwalkerId(dogwalker.getId())
                    .reviewId(review.getId())
                    .star(review.getStar())
                    .context(review.getContext())
                    .dirName(review.getDirName())
                    .fileName(review.getFileName())
                    .extension(review.getExtension())
                    .customerId(offer.getCustomer().getId())
                    .customerNick(offer.getCustomer().getNick())
                    .breed(offer.getCommision().getBreed())
                    .dogType(offer.getCommision().getDogType())
                    .month(offer.getCommision().getMonth())
                    .day(offer.getCommision().getDay())
                    .hour(hour)
                    .min(min)
                    .commentCnt(review.getComments().size())
                    .build();
            return responseDto;
        } else {
            throw new Exception("inquiry, offer 둘 중 하나만 존재해야 합니다");
        }
    }

}
