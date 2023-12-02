

    function setThumbnail(event) {
        var reader = new FileReader();
        var logoInput = document.getElementById("input-file");

        // 파일이 선택되지 않은 경우
        if (logoInput.files.length === 0) {
            return;
        }

        var fileName = logoInput.files[0].name;

        // 허용된 확장자인지 확인
        if (!isValidFileType(fileName)) {
            alert("허용되지 않는 파일 확장자입니다.");
            // 파일 선택을 초기화
            logoInput.value = "";
            // 썸네일 표시를 초기화
            document.querySelector("div#image_container").innerHTML = "";
            return;
        }

        reader.onload = function (e) {
            var img = document.createElement("img");
            img.setAttribute("src", e.target.result);
            img.setAttribute("class", "img-fluid");
            document.querySelector("div#image_container").innerHTML = "";
            document.querySelector("div#image_container").appendChild(img);
        };

        reader.readAsDataURL(logoInput.files[0]);
    }

    function isValidFileType(fileName) {
        var allowedExtensions = /(\.png|\.jpg|\.jpeg)$/i;
        return allowedExtensions.test(fileName);
    }


    function createTeam(event,status){
        event.preventDefault();
        var requestType = "";

        if (status == 1) {
            requestType = "POST";
        } else if (status == 2) {
            requestType = "PATCH";
        }

        var recruitValue = document.getElementById('recruit').checked ? 1 : 0;

        var formData = new FormData(document.getElementById('teamForm'));
        formData.append('recruit', recruitValue);

        $.ajax({
        	type:requestType,
            url:'/teams',
            async: false,
            data:formData,
            processData:false,
            contentType:false,
            cache:false,
        }).done(function(success) {
            alert(success.responseJSON.name+'팀 생성 완료!');
            location.replace('/teams/'+success.responseJSON.id);
        }).fail(function(error){

             if(error.status === 401){
               alert(error.responseJSON.message);
               location.replace('/login');
             } else if (error.status === 400){
               alert(error.responseJSON.message);
             } else if (error.status === 409){
               alert(error.responseJSON.message);
             } else {
               alert('알 수 없는 오류가 발생했습니다.');
              location.replace('/');
             }
    });

    }








