
// Script for check and uncheck start

function setChecked(val, name ,nameOne , nameTwo) {
    form = $('#comments');
    var inputs = $('#comments input');
    var len = inputs.length;
    var i=0;
    for( i=0 ; i<len ; i++) 
    {
    	var currentInput = inputs[i];
        if (currentInput.name == name) { 
        	currentInput.checked=val;
        }
        else if(currentInput.name == nameOne){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
        else if(currentInput.name == nameTwo){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
    }
}

function setCheckedNone(val, name, form) {
    len = form.elements.length;
    var i=0;
    for( i=0 ; i<len ; i++) {
        if (form.elements[i].name == name) { 
           form.elements[i].checked=val;
        }	
    }
}


// Script for check and uncheck end

function chckChecked(ObjId,otherObjId){
	
	var spanId = document.getElementById(ObjId);
	var inputTag = spanId.getElementsByTagName("input");
	if(inputTag[0] != undefined){		
		if(inputTag[0].checked == true){
				inputTag[0].checked = false;
		}
	}
	var delSpanId = document.getElementById(otherObjId);
	if(delSpanId != undefined)
	{
		var delInputTag = delSpanId.getElementsByTagName("input");
		if(delInputTag[0] != undefined){
			if(delInputTag[0].checked == true){
					delInputTag[0].checked = false;
			}
		}	
	}
}

function setCheckedAbusive(val, name ,nameOne) {
    form = $('offensive');
    var inputs = $('#offensive input');
    var len = inputs.length;
    var i=0;
    for( i=0 ; i<len ; i++) {
    	var currentInput = inputs[i];
        if (currentInput.name == name) { 
        	currentInput.checked=val;
        }
        else if(currentInput.name == nameOne){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
    }
}

function setCheckedComment(val, name ,nameOne , nameTwo, nameThree, nameFour, nameFive) {
	
    form = $('#comments');
    var inputs = $('#comments input');
    var len = inputs.length;
    var i=0;
    for( i=0 ; i<len ; i++) {
    	var currentInput = inputs[i];
        if (currentInput.name == name) { 
           currentInput.checked=val;
        }
        else if(currentInput.name == nameOne){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
        else if(currentInput.name == nameTwo){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
        else if(currentInput.name == nameThree){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
        else if(currentInput.name == nameFour){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
        else if(currentInput.name == nameFive){
			if(currentInput.checked==true)
				currentInput.checked=false;
		}
    }
}
function chckCheckedFour(ObjId,otherObjId, otherObjIdSecond, otherObjIdThird){
	
	var spanId = document.getElementById(ObjId);
	var inputTag = spanId.getElementsByTagName("input");
	if(inputTag[0] != undefined){		
		if(inputTag[0].checked == true){
				inputTag[0].checked = false;
		}
	}
	var delSpanId = document.getElementById(otherObjId);
	if(delSpanId != undefined)
	{
		var delInputTag = delSpanId.getElementsByTagName("input");
		if(delInputTag[0] != undefined){
			if(delInputTag[0].checked == true){
					delInputTag[0].checked = false;
			}
		}
	}
	var attSpanId = document.getElementById(otherObjIdSecond);
	if(attSpanId != undefined)
	{
		var attInputTag = attSpanId.getElementsByTagName("input");
		if(attInputTag[0] != undefined){
			if(attInputTag[0].checked == true){
					attInputTag[0].checked = false;
			}
		}
	}
	var spamSpanId = document.getElementById(otherObjIdThird);
	var spamInputTag = spamSpanId.getElementsByTagName("input");
	if(spamInputTag[0] != undefined){
		if(spamInputTag[0].checked == true){
				spamInputTag[0].checked = false;
		}
	}	
}

/* LogOut Screen*/
function shareIcon(obj){
	var id = obj.id;
	//alert($("#showHiddenIcon" + id).css("display"));
	if($("#showHiddenIcon" + id).css("display")=="none")
	{
		$("#showHiddenIcon" + id).css('display','block');
		$(".share" + id).addClass("shareIconBlock");
	}
	else
	{
		$("#showHiddenIcon" + id).css('display','none');
		$(".share" + id).removeClass("shareIconBlock");
	}
	
	$("#showHiddenIcon" + id).click(function() {
			$(".share" + id).addClass("shareIconBlock");
			$("#showHiddenIcon" + id).css('display','block');
	});
}

$(document).bind("mouseup", hideDiv);

function hideDiv(){
	$("#showHiddenIcon111").css('display','none');
	$(".share111").removeClass("shareIconBlock");
}
// for share link end
