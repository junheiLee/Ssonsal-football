package com.ssonsal.football.game.controller;

import com.ssonsal.football.game.dto.request.ApprovalSubRequestDto;
import com.ssonsal.football.game.dto.response.SubsResponseDto;
import com.ssonsal.football.game.service.SubService;
import com.ssonsal.football.global.account.Account;
import com.ssonsal.football.global.account.CurrentUser;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssonsal.football.game.util.GameConstant.CREATED_SUB_ID;
import static com.ssonsal.football.game.util.GameConstant.SUBS;
import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;
import static com.ssonsal.football.global.util.transfer.Transfer.longIdToMap;
import static com.ssonsal.football.global.util.transfer.Transfer.toMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/match-teams")
@Tag(name = "Sub", description = "Sub API")
public class SubController {

    private final SubService subService;

    /**
     * 신청한 용병을 승인하는 api
     *
     * @param approvalSubDto     subApplicantsId
     * @param matchApplicationId 해당 신청 팀 식별자
     * @return 승인된 용병 아이디
     */
    @PostMapping("/{matchApplicationId}/subs")
    public ResponseEntity<ResponseBodyFormatter> acceptSub(@RequestBody ApprovalSubRequestDto approvalSubDto,
                                                           @PathVariable Long matchApplicationId,
                                                           @CurrentUser Account account) {

        Long createdSubId = subService.acceptSub(account.getId(), matchApplicationId, approvalSubDto);

        return DataResponseBodyFormatter.put(SUCCESS, longIdToMap(CREATED_SUB_ID, createdSubId));
    }

    /**
     * 해당 신청 팀에 대한 용병 목록 반환 api
     *
     * @param matchApplicationId 해당 신청 팀 식별자
     * @return 승인된 용병 목록
     */
    @GetMapping("/{matchApplicationId}/subs")
    public ResponseEntity<ResponseBodyFormatter> findSubs(@PathVariable Long matchApplicationId) {

        List<SubsResponseDto> subs = subService.getTeamSubList(matchApplicationId);
        return DataResponseBodyFormatter.put(SUCCESS, toMap(SUBS, subs));
    }
}
