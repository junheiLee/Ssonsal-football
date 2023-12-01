( function ( $ ) {
    "use strict";

     //JSON.parse로 문자열을 객체로 전환
    var confirmedGameCount = document.getElementById('confirmedGameCount').textContent;
    var waitingGameCount = document.getElementById('waitingGameCount').textContent;
        console.log(confirmedGameCount);
    console.log('aaa');
    //bar chart
    var ctx = document.getElementById( "barChart" );

    var myChart = new Chart( ctx, {
        type: 'bar',
        data: {
            labels: [ "11.1", "11.2", "11.3", "11.4", "11.5", "11.6", "11.7",
            "11.8","11.9","11.10","11.11","11.12","11.13","11.14","11.15","11.16",
            "11.17","11.18","11.19","11.21","11.22","11.23","11.24","11.25","11.26",
            "11.27","11.28","11.29","11.30","11.31"],
            datasets: [
                            {
                                label: "매치된 경기 수",
                                data: confirmedGameCount,
                                borderColor: "rgba(0, 194, 146, 0.9)",
                                borderWidth: "0",
                                backgroundColor: "rgba(0, 194, 146, 0.5)"
                            },
                            {
                                label: "취소된 경기 수",
                                data: waitingGameCount,
                                borderColor: "rgba(0,0,0,0.09)",
                                borderWidth: "0",
                                backgroundColor: "rgba(0,0,0,0.07)"
                            }
                        ]
           },
        options: {
            scales: {
                yAxes: [ {
                    ticks: {
                        beginAtZero: true
                    }
                                } ]
            }
        }
    } );

  
} )( jQuery );