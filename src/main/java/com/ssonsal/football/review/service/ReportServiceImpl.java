package com.ssonsal.football.review.service;

import com.ssonsal.football.review.dto.request.ReportRequestDto;
import com.ssonsal.football.review.dto.response.ReportResponseDto;
import com.ssonsal.football.review.etity.Report;
import com.ssonsal.football.review.etity.Review;
import com.ssonsal.football.review.repository.ReportRepository;
import com.ssonsal.football.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<ReportResponseDto> getAllReports() {
        List<Report> reports = reportRepository.findAll();

        return reports.stream()
                .map(ReportResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ReportResponseDto createReport(ReportRequestDto reportRequestDto) {

        Review review = reviewRepository.findById(reportRequestDto.getReviewId()).get();
        if (review == null) {
            throw new RuntimeException("해당 리뷰를 찾을 수 없습니다. id: " + reportRequestDto.getReviewId());
        }

        Report report = Report.builder()
                .review(review)
                .reason(reportRequestDto.getReason())
                .build();

        reportRepository.save(report);

        return ReportResponseDto.fromEntity(report);
    }
}
