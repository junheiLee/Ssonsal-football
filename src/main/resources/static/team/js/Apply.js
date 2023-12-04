function redirect(error){

    if(error.status === 401){
        alert(error.responseJSON.message);
        location.replace('/login');
    } else if(error.status === 404){
        alert(error.responseJSON.message);
        location.replace('/teams');
    } else if(error.status === 403){
        alert(error.responseJSON.message);
        location.replace('/teams');
    } else if(error.status === 400){
        alert(error.responseJSON.message);
        location.replace('/teams');
    } else if(error.status === 409){
        alert(error.responseJSON.message);
        location.replace('/teams');
    } else {
        alert("알 수 없는 오류발생 => "+error.status);
        location.replace('/');
    }
}


function teamApplyCancel(teamId){

    if(confirm("신청을 취소하시겠습니까?")){
         $.ajax({
            method : 'DELETE',
            url :'/members/'+teamId+'/applications/users',
         }).done(function(success) {
               alert('신청 취소 완료');
               location.href='/teams';
         }).fail(function(error){
                redirect(error);
         });
    }
}

function teamApply(teamId){

    if(confirm("팀에 신청하시겠습니까?")){
         $.ajax({
            method : 'POST',
            url :'/members/'+teamId+'/users',
         }).done(function(success) {
            alert('신청 완료');
            location.href='/teams/'+teamId;
         }).fail(function(error){
                redirect(error);
         });
    }

}

function userAccept(teamId,userId){

    if(confirm("신청을 수락하시겠습니까?")){
        $.ajax({
          method : 'POST',
          url :'/members/'+teamId+'/applications/'+userId,
        }).done(function(success) {
          alert(success+'님이 가입 승인되었습니다.');
          location.href='/teams/'+teamId+'/managers';
        }).fail(function(error){
                redirect(error);
        });
    }
}

function userReject(teamId,userId){

    if(confirm("신청을 거절하시겠습니까?")){
          $.ajax({
              method : 'DELETE',
              url :'/members/'+teamId+'/applications/'+userId,
          }).done(function(success) {
              alert(success+'님이 가입 거절되었습니다.');
              location.href='/teams/'+teamId+'/managers';
          }).fail(function(error){
                redirect(error);
          });
    }
}

function teamLeave(teamId){

    	if(confirm("팀을 탈퇴하시겠습니까?")){
           $.ajax({
             method : 'DELETE',
             url :'/members/'+teamId+'/users',
           }).done(function(success) {
             alert(success+'팀 탈퇴 완료');
             location.href='/teams';
           }).fail(function(error){
                redirect(error);
           });
    	}
}

function leaderDelegate(teamId,userId){

    	if(confirm("정말 팀장을 위임하시겠습니까?")){
           $.ajax({
           method : 'PATCH',
           url :'/members/'+teamId+'/managers/'+userId,
           }).done(function(success) {
              alert(success +'님에게 팀장 위임 완료');
              location.replace('/teams');
           }).fail(function(error){
                redirect(error);
           });
    	}
}

function userBan(teamId,userId){

    	if(confirm("정말 밴하시겠습니까?")){
           $.ajax({
           method : 'DELETE',
           url :'/members/'+teamId+'/managers/'+userId,
           }).done(function(success) {
           alert(success+'님을 밴하였습니다.');
           location.href='/teams/'+teamId+'/managers';
           }).fail(function(error){
                redirect(error);
           });
        }

}