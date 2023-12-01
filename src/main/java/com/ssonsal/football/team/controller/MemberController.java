package com.ssonsal.football.team.controller;

import com.ssonsal.football.team.service.MemberService;
import com.ssonsal.football.team.service.TeamApplyService;
import com.ssonsal.football.team.service.TeamRejectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final TeamRejectService teamRejectService;
    private final TeamApplyService teamApplyService;
    private final MemberService memberService;


}
