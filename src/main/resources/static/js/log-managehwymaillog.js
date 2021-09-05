
$("#managemailinputBtn").click(function(e){
    e.preventDefault();
    window.location.href = "/hwymaillog/active/";
});


function loadmaillogdetail(logID){
	window.location.href = "/hwymaillog/active/"+logID;
}

loadprojectready();

function loadprojectready(){
    if (document.getElementById("logID") == null){
    } else {
      loadScript("/js/jexcel.js", inispreadsheetview);
    }
}






$("#printPreviewBtn").click(function(e){
    e.preventDefault();
    seqArr = new Array();

    var totalcount=document.getElementsByName('printNum[]').length;

    for(var i=0; i<totalcount; i++){
     if (document.getElementsByName('printNum[]')[i].checked==true){
        seqArr.push(document.getElementsByName('printNum[]')[i].value);
     }
    }
    if (seqArr.length>0){
    $("#maskloader").show();
    $("#reportinfoModalTitle").empty();
    $("#reportinfoModalTitle").append("<h4 class='modal-title'><i class='fa fa-print'></i> </span>Mail Log Report </h4> ")
    $("#reportinfoModal").modal('show');
	$("#reportinfopdf").css("height",$(window).height()-$(window).height()/4);
	$('#reportinfopdf').html("<iframe name='your_frame_name' src='/mailreport/"+seqArr+"'  height=100% width=100%> </iframe>")
    $("#maskloader").hide();
    }
});





///////////////////////////////////////////
////Code////
//////////////////////////////////////////
function initcodearray_by(code1,codepart){
defaultArray=new Array();
    $.ajax({
        url: "/code/codeList",
        data: {code1: code1,codepart: codepart},
        dataType: "json",
        async:false,
        success: function (data) {
           defaultArray=data;
        }
	});
return 	defaultArray;
}


///////////////////////////////////////////
////ActionTaken////
//////////////////////////////////////////
function actiontakenList_by(logID){
defaultArray=new Array();
    $.ajax({
        url: "/hwymaillog/actiontakenList/"+logID,
        dataType: "json",
        xhrFields: {withCredentials: true},
        async:false,
        success: function (data) {
        if (data==null){
        data=0;
        }
        if (data!=0){
            for(var i=0; i<data.length; i++){
            subArray=new Array();
            subArray.push(data[i]["logActionId"],data[i]["actionType"],data[i]["formDate"],data[i]["actionTaken"],data[i]["deleted"]);
            defaultArray.push(subArray);
            }
        } else {
            defaultArray=[['','','','']];
        }
        }
	});
return 	defaultArray;
}
function actiontakenList_update(logID,logActionId,ActionType,formDate,actionTaken,Delete){
    if (ActionType!="" && formDate!=""  && actionTaken!=""){
        if (logActionId==""){
            logActionId=0;
        }

        $.ajax({
            url: "/hwymaillog/actiontakenwrite/"+logID,
            dataType: "json",
            data: {"logActionId": logActionId,"ActionType":ActionType,"formDate":formDate,"actionTaken":actionTaken,"Delete":Delete},
            xhrFields: {withCredentials: true},
            complete: function (data) {
            if (data=="01"){
            $('#successalert01').toast('show');
            }
            $('#logactionTakenTable').jexcel('setData', actiontakenList_by(logID));
            }
        });
	}
}






function inispreadsheetview() {
    inispreadsheetactionTaken();
}




/* Action Taken */
function inispreadsheetactionTaken() {
var ActionType = initcodearray_by(9,"");
data03=actiontakenList_by($("#logID").val());


      var changed03 = function(instance, cell, x,y) {
          logActionId=instance.jexcel.options.data[y][0];
          ActionType=instance.jexcel.options.data[y][1];
          formDate=instance.jexcel.options.data[y][2];
          actionTaken=instance.jexcel.options.data[y][3];
          Delete=instance.jexcel.options.data[y][4];
          actiontakenList_update($("#logID").val(),logActionId,ActionType,formDate,actionTaken,Delete);
       };
       if ($("#logID").val()=="0"){
       $('#logactionTakenTable').html("<p style='text-align:center;padding:4rem;'>Please enter the basic information first.</p>");
       $('#loggactionTakenmemo').empty();
       } else {
       $('#loggactionTakenmemo').empty();
       $('#loggactionTakenmemo').append("* In/Out ,Date and Action must be entered for registration. It is automatically saved.");
        $('#logactionTakenTable').jexcel({
            data:data03,
            width:"100%",
            minSpareRows:1,
            allowDeleteRow:false,
            allowDeleteColumn:false,
            allowDeleteRow:false,
            defaultColAlign:"center",
            columns:[
                {
                    type: 'hidden',
                    title: 'logActionId',
                    name: 'logActionId',
                },
                { type: 'dropdown',
                    title:'IN/OUT',
                    name: 'ActionType',
                    width:'110',
                    source:ActionType
                },
                {
                    type:'calendar',
                    title: 'Date',
                    name: 'formDate',
                    options: {format:'MM/DD/YYYY'},
                    width: '130'
                },
                {
                    type:'text',
                    title: 'Action',
                    name: 'actionTaken',
                    align:'left',
                    width: '310'
                },
                {
                    type: 'checkbox',
                    name: 'Delete',
                    title: 'Delete',
                    width: '70'
                }
            ],
            onchange: changed03
        });
        }
}



// Delete sample card
$("#deldataBtn").click(function(e){
    e.preventDefault();
    if (confirm("Are you sure you want to delete this item?")) {
    var logID=$("#logID").val();
	$("#maskloader").show();
    $.ajax({
        url: "/hwymaillog/delete/"+logID,
        xhrFields: {withCredentials: true},
        complete: function (res) {
            window.location.href = "/hwymaillog/list";
			$("#maskloader").hide();
        }
	});
	}
});



























