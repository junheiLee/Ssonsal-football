package com.ssonsal.football.admin.controller;

import com.ssonsal.football.admin.exception.AdminErrorCode;
import com.ssonsal.football.admin.exception.AdminSuccessCode;
import com.ssonsal.football.admin.service.UserService;
import com.ssonsal.football.global.exception.CustomException;
import com.ssonsal.football.global.util.formatter.DataResponseBodyFormatter;
import com.ssonsal.football.global.util.formatter.ResponseBodyFormatter;
import com.ssonsal.football.team.exception.TeamErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.ssonsal.football.global.util.SuccessCode.SUCCESS;

@RestController
@Slf4j
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserRestController {


    private final UserService userService;

    /**
     * 관리자 권한 부여시 호출되는 api
     * @param requestData
     * requestData는 선택한 모든 회원 id를 가져온다
     * @return 성공 코드와 선택된 회원은 관리자로 변경된다
     */
    @PostMapping("/changeRole")
    public ResponseEntity<ResponseBodyFormatter> updateUserRole(@RequestBody Map<String, Object> requestData) {

        List<Integer> userIds = (List<Integer>) requestData.get("userIds");
        Long user = 1L;


            if (user == null) {
                throw new CustomException(AdminErrorCode.USER_NOT_AUTHENTICATION);
            } else if(userIds == null) {
               throw new CustomException(AdminErrorCode.USER_SELECTED_FAILED);
            }

            userService.updateRoles(userIds);
            return DataResponseBodyFormatter.put(AdminSuccessCode.USER_ALTER_ROLE,userIds);

        }
    }

