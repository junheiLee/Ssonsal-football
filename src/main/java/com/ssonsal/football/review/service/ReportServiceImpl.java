package com.ssonsal.football.review.service;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.review.dto.request.ReportRequestDto;
import com.ssonsal.football.review.dto.response.ReportResponseDto;
import com.ssonsal.football.review.etity.Report;
import com.ssonsal.football.review.etity.Review;
import com.ssonsal.football.review.exception.ReviewErrorCode;
import com.ssonsal.football.review.repository.ReportRepository;
import com.ssonsal.football.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<ReportResponseDto> getAllReports() {
        List<Report> reports = reportRepository.findAll();

        if(reports.isEmpty()){
            log.error("신고된 리뷰가 없습니다.");
            throw new CustomException(ReviewErrorCode.REPORT_NOT_FOUND);
        }

        return reports.stream()
                .map(ReportResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ReportResponseDto createReport(ReportRequestDto reportRequestDto) {

        Review review = reviewRepository.findById(reportRequestDto.getReviewId())
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        Report report = Report.builder()
                .review(review)
                .reason(reportRequestDto.getReason())
                .build();

        reportRepository.save(report);

        return ReportResponseDto.fromEntity(report);
    }
}
