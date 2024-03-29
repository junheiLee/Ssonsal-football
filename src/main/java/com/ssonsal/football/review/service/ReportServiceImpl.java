package com.ssonsal.football.review.service;

import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.ErrorCode;
import com.ssonsal.football.review.dto.request.ReportRequestDto;
import com.ssonsal.football.review.dto.response.ReportResponseDto;
import com.ssonsal.football.review.entity.Report;
import com.ssonsal.football.review.entity.Review;
import com.ssonsal.football.review.exception.ReviewErrorCode;
import com.ssonsal.football.review.repository.ReportRepository;
import com.ssonsal.football.review.repository.ReviewRepository;
import com.ssonsal.football.user.entity.User;
import com.ssonsal.football.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public List<ReportResponseDto> getAllReports() {
        List<Report> reports = reportRepository.findByReportCode(0);

        return reports.stream()
                .map(report -> new ReportResponseDto(report))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportResponseDto createReport(ReportRequestDto reportRequestDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewRepository.findById(reportRequestDto.getReviewId()).orElseThrow(
                () -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        Report report = Report.builder()
                .review(review)
                .reason(reportRequestDto.getReason())
                .user(user)
                .build();

        Report newReport = reportRepository.save(report);

        return new ReportResponseDto(newReport);
    }

    @Transactional
    public void updateDeleteCode(Long reportId, Long reviewId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.ID_NOT_FOUND));

        int reportCode = 1;

        reviewRepository.updateDeleteCodeByReviewId(reviewId);
        report.updateReport(reportCode);
    }
}
