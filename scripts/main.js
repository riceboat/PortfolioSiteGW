
document.querySelector('#timeButton').addEventListener("click", function(){
	d3.request("getDate").post("" , function readData(data) {
			data = JSON.parse(data.response);
			document.getElementById('timeText').innerHTML = data.date;
		});
});
document.querySelector('#calculateButton').addEventListener("click", function(){
	var num1 = document.getElementById('num1').value;
	var num2 = document.getElementById('num2').value;
	d3.request("multiply").post("num1=" + num1 + "&num2=" + num2 , function readData(data) {
			data = JSON.parse(data.response);
			document.getElementById('result').innerHTML = data.result;
		});
});