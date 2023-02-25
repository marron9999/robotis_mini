var host;
var port;
var voice = 0;
var debug = 0;
var expand = 0;

function E(id) {
	return document.getElementById(id);
}

window.onload = function() {
	host = window.location.hostname;
	port = window.location.port;
	if(port == null
	|| port == "") port = "8080";
	ws = new WebSocket("ws://" + host + ":" + port + "/robotis/ws");
	ws["motions"] = motions;
	ws["motion"] = motion;
	ws["message"] = message;
	ws["ready"] = ready;
	ws["connect"] = connect;
	ws["disconnect"] = disconnect;
	ws.onopen = function() {
	};
	ws.onmessage = function(e) {
		let m = e.data;
		let p = m.indexOf(" ");
		let o = m.substr(0, p);
		m = m.substr(p + 1);
		ws[o](m);
	}
	ws.onerror = function(e) {
		message(e);
	}
}

function _connect() {
	E("open").style.display = "none";
	E("log").innerHTML = "";
	mc = 0;
	message("Search robotis");
	ws.send("open");
}
function _disconnect() {
	E("close").style.display = "none";
	ws.send("close");
}
function ready(/*m*/) {
	E("open").style.display = "inline-block";
	ws.send("motions");
}
function connect(m) {
	E("open").style.display = null;
	E("close").style.display = "inline-block";
	document.title = m;
}
function disconnect(/*m*/) {
	E("close").style.display = null;
	E("open").style.display = "inline-block";
}

let mc = 0;
function message(m) {
	let e = E("log");
	let t = "<div>" + m + "</div>";
	m = e.innerHTML + t;
	mc++;
	if(mc > 100) {
		mc--;
		let p = m.indexOf("</div>");
		m = m.substr(p+2+3+1);
	}
	e.innerHTML = m;
	e.scrollBy(0, 99999999);
}

let ns = "";
let nc = 3;
function motions(m) {
	if(m == "[") {
		ns = "";
		nc = 3;
		return;
	}
	if(m == "]") {
		if(ns.length > 0) {
			ns += "</tr>";
		}
		E("motion").innerHTML = "<table cellspacing=8>" + ns + "</table>";
		return;
	}
	let p = m.indexOf(" ");
	let o = m.substr(0, p);
	m = m.substr(p + 1);
	nc = (nc + 1) % 4;
	if(nc == 0) {
		if(ns.length > 0) {
			ns += "</tr>";
		}
		ns += "<tr>";
	}
	ns += "<td class=m id=m" + o + " onclick='_motion(this)'>" + m + "</td>";
}

function motion(m) {
	if(m == "end") {
		E("logoa").className = "z";
		return;
	}
	E("logoa").className = "a";
	message("motion " + m);
}

function _motion(e) {
	ws.send("motion " + e.id.substr(1));
}

function _voice() {
	voice = (voice + 1) % 2;
	E("logo").style.background = (voice == 0)? "white" : "#fcc";
}

function _logd() {
	debug = (debug + 1) % 2;
	E("logd").innerHTML = (debug == 0)? "☆" : "★";
	ws.send("verbose " + debug);
}

function _loge() {
	expand = (expand + 1) % 2;
	if(expand == 0) {
		E("loge").innerHTML = "▽";
		E("loga").style.height = null;
		E("log").scrollBy(0, 99999999);
		return;
	}
	E("loge").innerHTML = "▲";
	E("loga").style.height = "calc(100%)";
	E("log").scrollBy(0, 99999999);
}
