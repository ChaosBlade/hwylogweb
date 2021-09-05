/////////////////////////////////////////////////
pageSetUp(); //Do Not Remove
function printreportModal(reportId){
    $("#maskloader").show();

    $("#reportinfoModalTitle").empty();
    $("#reportinfoModalTitle").append("<h4 class='modal-title'><i class='fa fa-print'></i> </span> Report </h4> ")
    $("#reportinfoModal").modal('show');
	$("#reportinfopdf").css("height",$(window).height()-$(window).height()/4);
	$('#reportinfopdf').html("<iframe name='your_frame_name' src='/report/"+reportId+"'  height=100% width=100%> </iframe>")
    $("#maskloader").hide();
}



function openNav() {
  document.getElementById("mySidenav").style.width = "330px";

}

function closeNav() {
  document.getElementById("mySidenav").style.width = "0";
}





































