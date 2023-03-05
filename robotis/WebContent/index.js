let host;
let port;
let ws = null;
let voice = 0;
let debug = 0;
let expand = 0;

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
	recognition.parse = _recognition;
	recognition._onend = recognition.onend;
	recognition.onend = function (e) {
		recognition.parse = _recognition;
		recognition._onend(e);
	}
}

function _connect() {
	E("open").style.display = "none";
	E("log").innerHTML = "";
	mc = 0;
	message("Connect robotis");
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
	E("open").style.display = "none";
	E("close").style.display = "inline-block";
	document.title = m;
}
function disconnect(/*m*/) {
	E("close").style.display = "none";
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

let _motions = {};
let ns = "";
let nc = 3;
function motions(m) {
	if(m == "[") {
		_motions = {};
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
	let s = "";
	for(p = 0; p<m.length; p++)
		s += "<n>" + m[p] + "</n>";
	ns += "<td class=m id=m" + o + " onclick='_motion(this)'>";
	ns += "<table border=0><tr><td><img class=i src=icon/" + o + ".png></td>";
	ns += "<td class=t id=t" + o + ">" + s + "</td></tr></table></td>";
	_motions[o] = m;
}

function motion(m) {
	if(m == "end") {
		E("logoa").className = "z";
		_reset();
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
	if(voice == 0) {
		E("reset").style.display = "none";
		E("logo").style.background = "white";
		recognition.repeat = false;
		recognition.abort();
		recognition.stop();
		return;
	}
	E("reset").style.display = "inline-block";
	E("logo").style.background = "#fcc";
	recognition.repeat = true;
	recognition.start();
}

function _resete(ee, c) {
	if(c == undefined) c = "m";
	let bb = ee;
	if(ee.id.charAt(0) == 't')
		bb = E(ee.id.replace("t", "m"));
	else 
		ee = E(ee.id.replace("m", "t"));
	bb.className = c;
	let m = ee.textContent;
	let s = "";
	if(c == "s") {
		for(let p = 0; p<m.length; p++)
			s += "<r>" + m[p] + "</r>";
	} else {
		for(let p = 0; p<m.length; p++)
			s += "<n>" + m[p] + "</n>";
	}
	ee.innerHTML = s;
}
function _reset(c) {
	if(c == undefined) c = "m";
	for(let ei = 0; ; ei ++) {
		let ee = E("t" + ei);
		if(ee == null) break;
		_resete(ee, c);
	}
}

let modify = {
	"抗体" : "後退",
	"交代" : "後退",
	"交替" : "後退",
	"光体" : "後退",
	"全身" : "前進",
	"先進" : "前進",
	"専心" : "前進",
	"最新" : "前進",
	"精神" : "前進",
	"蹴る" : "蹴り",
	"下痢" : "蹴り",
	"ケリ" : "蹴り",
	"防ぐ" : "防御",
	"防ぎ" : "防御",
	"転がる" : "転がり",
	"入江" : "右へ",
	"エブリ" : "手振り",
	"デブリ" : "手振り",
	"右側" : "右",
	"左側" : "左",
	"右に" : "右へ",
	"左に" : "左へ",
	"早い" : "速い",
	"位置" : "1",
	"荷" : "2",
	"規律" : "起立",
	"キリツ" : "起立",
	"見る" : "見ろ",
	"ミロ" : "見ろ",
	"止ま" : "停止",
	"クック" : "フック",
	"ジャム" : "ジャブ",
	"じゃあ" : "ジャブ",
	"嫉妬" : "シット",
	" " : "",
}

function _recognition(s) {
	if(debug)
		message(s);
	for(tx in modify) {
		s = s.replace(tx, modify[tx]);
	}
	let test = function(e, s) {
		let t = e.textContent;
		if(s.length == 1) {
			let p = t.indexOf(s);
			if(p >= 0) {
				t = e.innerHTML.substr(0, p * 8);
				t += "<r>" + s + "</r>";
				e.innerHTML = t + e.innerHTML.substr(p * 8 + 8);
				return true;
			}
			return false;
		}
		let b = false;
		for(let si=0; si<s.length - 1; si++) {
			let ss = s.substr(si, 2);
			let p = t.indexOf(ss);
			if(p >= 0) {
				b = true;
				let x = e.innerHTML.substr(0, p * 8);
				x += "<r>" + ss[0] + "</r>";
				x += "<r>" + ss[1] + "</r>";
				e.innerHTML = x + e.innerHTML.substr(p * 8 + 16);
			}
		}
		return b;
	};
	if(s.indexOf("リセ") >= 0
	|| s.indexOf("セット") >= 0
	) {
		recognition.parse = function() {};
		_reset();
		message("リセット");
		return;
	}
	if(s.indexOf("接続") >= 0) {
		if(E("open").style.display != "none") {
			recognition.parse = function() {};
			recognition.abort();
			_reset();
			message("接続");
			_connect();
			return;
		}
	}
	if(s.indexOf("切断") >= 0) {
		if(E("close").style.display != "none") {
			recognition.parse = function() {};
			recognition.abort();
			_reset();
			message("切断");
			_disconnect();
			return;
		}
	}
	let select = function(es, s) {
		let b = false;
		for(let ei = 0; ei < es.length; ei++) {
			if(test(es[ei], s)) {
				if( ! b) {
					b = true;
					for(let ej = 0; ej < ei; ej++) {
						_resete(es[ej], "n");
					}
				}
				E(es[ei].id.replace("t", "m")).className = "s";
			} else {
				if(b) {	
					_resete(es[ei], "n");
				}
			}
		}
	};
	let ess = [];
	let ems = [];
	for(let ei = 0; ; ei ++) {
		let ee = E("t" + ei);
		if(ee == null) break;
		ems[ei] = ee;
		if(ee.className == "s")
			ess.push(ee);
	}
	if(ess.length > 0) {
		select(ess, s);
	} else {
		select(ems, s);
	}
	ess = E("motion").getElementsByClassName("s");
	if(ess.length > 0) {
		for(let ei = 0; ei<ess.length; ei ++) {
			if(ess[ei].innerHTML.indexOf("<n>") < 0) {
				recognition.parse = function() {};
				recognition.abort();
				let ee = ess[ei];
				_reset("n");
				_resete(ee, "s");
				_motion(ee);
				return;
			}
		}
		if(ess.length == 1) {
			recognition.parse = function() {};
			recognition.abort();
			let ee = ess[0];
			_reset("n");
			_resete(ee, "s");
			_motion(ee);
			return;
		}
	}
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
