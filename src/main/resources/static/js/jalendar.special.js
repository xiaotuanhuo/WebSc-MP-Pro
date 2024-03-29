!function(a) {
    a.fn.jalendar = function(t) {
        function e() {
            T[1] = A(C);
            var t = new Date;
            t.setFullYear(E, w, 0);
            var e = t.getDay();
            v.find(".header h1").html(o[s.lang][w] + " " + E + '<div class="total-bar"></div>'),
            v.find(".day").html("&nbsp;").removeAttr("data-date").removeClass("this-month have-event");
            for (var d = 0; d < 42 - (e + T[w]); d++) v.find(".day").eq(e + T[w] + d).html("<span>" + (d + 1) + "</span>");
            for (var d = 0; e > d; d++) {
                var i = void 0 == T[w - 1] ? T[11] : T[w - 1];
                v.find(".day").eq(d).html("<span>" + (i - e + (d + 1)) + "</span>")
            }
            for (var d = 1; d <= T[w]; d++) {
                e++;
                var r;
                "dd-mm-yyyy" == s.dateType ? r = d + "-" + (w + 1) + "-" + E: "mm-dd-yyyy" == s.dateType ? r = w + 1 + "-" + d + "-" + E: "yyyy-mm-dd" == s.dateType ? r = E + "-" + (w + 1) + "-" + d: "yyyy-dd-mm" == s.dateType && (r = E + "-" + d + "-" + (w + 1)),
                "linker" == s.type ? v.find(".day").eq(e - 1).addClass("this-month").attr("data-date", r).html('<span><a href="' + s.customUrl + r + '">' + d + "</a></span>") : v.find(".day").eq(e - 1).addClass("this-month").attr("data-date", r).html("<span>" + d + "</span>");
                if ("" == s.type) {
                	if (E < new Date().getFullYear()) {
                		v.find(".day").eq(e - 1).addClass("disable-selecting");
                	} else if (E == new Date().getFullYear() && w < new Date().getMonth()) {
                		v.find(".day").eq(e - 1).addClass("disable-selecting");
                	} else if (E == new Date().getFullYear() && w == new Date().getMonth() && d < new Date().getDate()) {
                		v.find(".day").eq(e - 1).addClass("disable-selecting");
                	} else {
                		v.find(".day").eq(e - 1).removeClass("disable-selecting");
                	}
                }
            }
            w == k.getMonth() ? v.find(".day.this-month").removeClass("today").eq(b - 1).addClass("today") : v.find(".day.this-month").removeClass("today").attr("style", ""),
            v.find(".days").attr("data-month", w + 1).attr("data-year", E),
            v.find(".added-event").each(function(t) {
                //a(this).attr("data-id", t),
                v.find('.this-month[data-date="' + a(this).attr("data-date") + '"]').append(c("div", "event-single").attr("data-id", a(this).attr("data-id")).append(c("p", "").text(a(this).attr("data-title"))).append(c("small", "fa fa-trash-o ml-10"))),
                v.find(".day").has(".event-single").addClass("have-event"),
                v.find(".day").has(".event-single").removeClass("disable-selecting")
            }),
            n(),
            null != s.dayColor && v.find(".day span, .day span a").css({
                color: s.dayColor
            }),
            null != s.titleColor && v.find(".header h1, .header .prv-m, .header .nxt-m, .event-single p, h3, .close-button-sp").css({
                color: s.titleColor
            }),
            null != s.weekColor && v.find("h2").css({
                color: s.weekColor
            }),
            null != s.todayColor && v.find(".day.this-month.today span, .day.this-month.today span a").css({
                color: s.todayColor
            }),
            ("#fff" == s.color || "#ffffff" == s.color || "white" == s.color) && v.find(".header h1, .header .prv-m, .header .nxt-m, .day.today span, h2, .event-single p, h3, .close-button-sp").css({
                "text-shadow": "none"
            })
        }
        function n() {
            var t = v.find(".this-month .event-single").length;
            0 == t && v.find(".total-bar").hide(0),
            v.find(".total-bar").text(t),
            v.find(".events h3 span").text(a(".jalendar .day.selected .event-single").length)
        }
        function d() {
            v.find(".day").removeClass("selected").removeAttr("style"),
            v.find(".add-event").removeClass("selected").height(0)
        }
        function i() {
            if (v.find(".day").removeClass("first-range range last-range"), null != j) if (0 == v.find('[data-date="' + z.val() + '"]').length) {
                if (M < v.find(".days").attr("data-month") && I >= v.find(".days").attr("data-year") || I < v.find(".days").attr("data-year") ? j = 0 : (M > v.find(".days").attr("data-month") && I <= v.find(".days").attr("data-month") || I > v.find(".days").attr("data-year")) && (j = 42), null != D) {
                    if (I == N && M == S) return ! 1;
                    //console.log("ilk secili tarihin yili: " + I + " — su anki yil: " + v.find(".days").attr("data-year") + " — son secili tarihin yili: " + N + "\nilk secili tarihin ayi: " + M + " — su anki ay: " + v.find(".days").attr("data-month") + " — son secili tarihin ayi: " + S),
                    (I < parseInt(v.find(".days").attr("data-year")) && N > parseInt(v.find(".days").attr("data-year")) || M < parseInt(v.find(".days").attr("data-month")) && I == parseInt(v.find(".days").attr("data-year")) && S > parseInt(v.find(".days").attr("data-month")) && N == parseInt(v.find(".days").attr("data-year")) || M < parseInt(v.find(".days").attr("data-month")) && N > parseInt(v.find(".days").attr("data-year")) && I == parseInt(v.find(".days").attr("data-year")) || M < parseInt(v.find(".days").attr("data-month")) && I == parseInt(v.find(".days").attr("data-year")) && S > parseInt(v.find(".days").attr("data-month")) && N >= parseInt(v.find(".days").attr("data-year"))) && v.find(".day").addClass("range")
                }
            } else j = v.find('[data-date="' + z.val() + '"]').index()
        }
        function r() {
            v.find('.day[data-date="' + z.val() + '"]').addClass("first-range"),
            v.find('.day[data-date="' + R.val() + '"]').addClass("last-range"),
            v.find('.day[data-date="' + z.val() + '"]').nextUntil('.day[data-date="' + R.val() + '"]').addClass("range"),
            v.find('.day[data-date="' + R.val() + '"]').length > 0 && (v.find(".day.first-range").length > 0 ? v.find(".day.first-range").nextUntil(".day.last-range").addClass("range") : v.find(".day.last-range").prevUntil(".day:eq(0)").addClass("range"))
        }
        var s = a.extend({
            customDay: new Date,
            color: "#207cd1",
            color2: "",
            lang: "EN",
            type: "",
            customUrl: "#",
            dateType: "yyyy-mm-dd",
            dayColor: null,
            titleColor: null,
            weekColor: null,
            todayColor: null,
            done: null
        }, t),
        l = {},
        o = {},
        f = {},
        y = {},
        p = {},
        h = {},
        u = {};
        l.EN = new Array("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        l.CN = new Array("一", "二", "三", "四", "五", "六", "日"),
        o.EN = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"),
        o.CN = new Array("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"),
        f.EN = "Add New Event",
        f.CN = "新增",
        y.EN = "All Day",
        y.CH = "全天",
        p.EN = "Total Events in This Month: ",
        p.CN = "本月总计",
        h.EN = "Event(s)",
        h.CN = "备休",
        u.EN = "Close",
        u.CN = "关闭";
        
        var schedule = {};
        
        var v = a(this),
        c = function(t, e) {
            return a(document.createElement(t)).addClass(e)
        },
        m = "" == s.color2 ? s.color: s.color2;
        //v.append(a('<input type="hidden" class="data1" /><input type="hidden" class="data2" />'), c("div", "jalendar-container").append(c("div", "jalendar-pages").append(c("div", "header").append(c("a", "prv-m").append(c("i", "fa fa-angle-left")), c("h1"), c("a", "nxt-m").append(c("i", "fa fa-angle-right")), c("div", "day-names")), c("div", "days"), c("div", "add-event").append(c("div", "events").append(c("h3", "").html("<span></span> " + h[s.lang]), c("div", "events-list")), c("div", "close-button-sp").text(u[s.lang]))).attr("style", "background-color:" + s.color + "; background: -webkit-gradient(linear, left top, left bottom, from(" + s.color + "), to(" + m + ")); background: -webkit-linear-gradient(top, " + s.color + ", " + m + "); background : -moz-linear-gradient(top, " + s.color + ", " + m + "); background: -ms-linear-gradient(top, " + s.color + ", " + m + "); background: -o-linear-gradient(top, " + s.color + ", " + m + ");"))),
        v.append(a('<input type="hidden" class="data1" /><input type="hidden" class="data2" />'), c("div", "jalendar-container").append(c("div", "jalendar-pages").append(c("div", "header").append(c("a", "prv-m").append(c("i", "fa fa-angle-left")), c("h1"), c("a", "nxt-m").append(c("i", "fa fa-angle-right")), c("div", "day-names")), c("div", "days"), c("div", "add-event").append(c("div", "events").append(c("h3", "").html("<span></span> " + h[s.lang]), c("div", "events-list")), c("div", "add-new").text(f[s.lang]).attr("data-popup", "formPopup"), c("div", "close-button-sp").text(u[s.lang])      )).attr("style", "background-color:" + s.color + "; background: -webkit-gradient(linear, left top, left bottom, from(" + s.color + "), to(" + m + ")); background: -webkit-linear-gradient(top, " + s.color + ", " + m + "); background : -moz-linear-gradient(top, " + s.color + ", " + m + "); background: -ms-linear-gradient(top, " + s.color + ", " + m + "); background: -o-linear-gradient(top, " + s.color + ", " + m + ");"))),
        "range" == s.type && v.find(".jalendar-pages").addClass("range").append(c("input", "first-range-data").attr({
            type: "hidden"
        }), c("input", "last-range-data").attr({
            type: "hidden"
        }));
        for (var g = 0; 42 > g; g++) v.find(".days").append(c("div", "day"));
        for (var g = 0; 7 > g; g++) v.find(".day-names").append(c("h2").text(l[s.lang][g]));
        var C, k = new Date(s.customDay),
        E = k.getFullYear(),
        b = k.getDate(),
        w = k.getMonth(),
        x = function(a) {
            var t = new Date;
            return t.setYear(a),
            t.setMonth(1),
            t.setDate(29),
            29 == t.getDate()
        },
        A = function(a) {
            return a = x(E) === !0 ? 29 : 28
        },
        T = new Array(31, A(C), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31),
        j = null,
        D = null,
        M = null,
        S = null,
        I = null,
        N = null;
        e();
        var q = new Array(v.find(".prv-m"), v.find(".nxt-m")),
        z = (v.find(".jalendar .close-button-sp"), v.find("input.first-range-data")),
        R = v.find("input.last-range-data");
        q[1].on("click", function() {
            w >= 11 ? (w = 0, E++) : w++,
            e(),
            d(),
            "range" == s.type && (i(), r())
        }),
        q[0].on("click", function() {
            dayClick = v.find(".this-month"),
            0 === w ? (w = 11, E--) : w--,
            e(),
            d(),
            "range" == s.type && (i(), r())
        }),
        v.on("click", ".close-button-sp", function(a) {
            a.preventDefault(),
            v.find(".add-event").removeClass("selected").height(0),
            v.find(".this-month.selected").removeClass("selected")
        }),
        v.on("click", ".add-new", function(a) {
        	var today = new Date().toLocaleDateString();
        	var selectedDay = (new Date(Date.parse(v.find("input.data1").val()))).toLocaleDateString();
//        	if (selectedDay < today) {
//        		return false;
//        	}
        	a.preventDefault();
        	$("h6.popup-title").text(v.find("input.data1").val());
        	$("#picktime_begin").val("00:00");
        	$("#picktime_end").val("23:59");
        	$(".radioCheck").removeAttr("checked");
			$("#all").prop("checked", true);
			$("#timeBtn").val("all");
        }),
        $(".orange").on("click", function() {
        	var today = v.find("input.data1").val();
        	var begin = $("#picktime_begin").val();
        	var end = $("#picktime_end").val();
        	var timeBtn = $("#timeBtn").val();
        	var dataJson = "{\"day\":\"" + today + "\", \"begin\":\"" + begin  + "\", \"end\":\"" + end + "\", \"timeBtn\":\"" + timeBtn + "\"}";
			$.ajax({
				type : "POST",
				data : "calendarInfo=" + dataJson,
				dataType : "json",
				url : "addCalendar",
				success: function(result) {
					if (result.code == 0) {
						var listDom2 = document.getElementById("yourId");
			        	var divDom2 = document.createElement("div");
			        	divDom2.setAttribute("class", "added-event");
			        	divDom2.setAttribute("data-date", today);
			        	divDom2.setAttribute("data-title", result.data.title);
			        	divDom2.setAttribute("data-id", result.data.calendarId);
						
						listDom2.appendChild(divDom2);
			        	e();
			            v.find(".add-event").removeClass("selected").height(0),
			            v.find(".this-month.selected").removeClass("selected")
					} else {
						alert(data.msg);
					}
				},
				error: function() {
					alert("网络错误，无法正常登录！");
				}
			});
        });
        /* delete event */
        v.on("click", ".event-single .fa-trash-o", function() {
        	var restId;
        	var len = $('div[data-id=' + $(this).parents(".event-single").attr("data-id") + ']').length;
        	var step = 1;
        	var justOnce = false;
        	$('div[data-id=' + $(this).parents(".event-single").attr("data-id") + ']').animate({'height': 0}, function(a) { 
        		restId = $(this).attr("data-id")
        		$(this).remove();
        		var eventCount = v.find('.this-month .event-single').length;
        		0 == eventCount && v.find(".total-bar").hide(),
                v.find('.total-bar').text(eventCount);
                var vvv = $(this).parent().parent();
                v.find('.events h3 span').text(v.find('.events .event-single').length);
                if (v.find('.events .event-single').length == 0) {
                	v.find('.this-month.selected').removeClass("have-event");
                }
        		//v.find(".add-event").height(v.find(".add-event ").height() -50);
        		//v.find(".add-event").removeClass("selected").height(0);
                //v.find(".this-month.selected").removeClass("selected");
        		if (step == len) {
        			schedule.type = "del";
        			schedule.calendarId = restId;
        			s.done(schedule);
        		} else {
        			step++;
        		}
            });
        }),
        v.on("click", ".this-month", function() {
            function t(a) {
                a.parent().find(".day").removeClass("first-range").removeClass("range").removeClass("last-range"),
                a.addClass("first-range"),
                z.val(a.attr("data-date")),
                j = v.find('[data-date="' + v.find(".first-range").attr("data-date") + '"]').index(),
                M = v.find(".days").attr("data-month"),
                I = v.find(".days").attr("data-year"),
                D = null,
                R.val("")
            }
            function e(a) {
                a.addClass("last-range"),
                R.val(a.attr("data-date")),
                D = v.find(".last-range").index(),
                S = v.find(".days").attr("data-month"),
                N = v.find(".days").attr("data-year")
            }
            if ("selector" == s.type) return v.find("input.data1").val(a(this).data("date")),
            a(this).parent().find(".selected").removeClass("selected"),
            a(this).addClass("selected"),
            v.parent().is(".jalendar-input") && (v.parent().find("input").removeClass("selected"), v.parent(".jalendar-input").find("input").val(a(this).data("date"))),
            null != s.done && s.done.call(this),
            !1;
            if ("range" == s.type) {
                a(this).parent().find(".first-range"),
                a(this).parent().find(".last-range");
                if (null != j) if (null != D) t(a(this));
                else {
                    if (j > a(this).index()) return t(a(this)),
                    !1;
                    e(a(this)),
                    v.find("input.data1").val(z.val()),
                    v.find("input.data2").val(R.val()),
                    v.parent().is(".jalendar-input") && (v.parent().find("input").removeClass("selected"), v.parent(".jalendar-input").find("input").val(v.find("input.data1").val() + ", " + v.find("input.data2").val())),
                    null != s.done && s.done.call(this)
                } else t(a(this));
                return v.on({
                    mouseenter: function() {
                        return null == j ? !1 : void("" == R.val() && (v.find(".day").removeClass("range last-range"), a(this).index() > j && a(this).hasClass("this-month") && (a(this).addClass("last-range"), a(this).parent().find(".day:eq(" + j + ")").nextUntil(".this-month.last-range").addClass("range"))))
                    },
                    mouseleave: function() {
                        "" == R.val() && a(this).parent().find(".day").removeClass("last-range").removeClass("range")
                    }
                }, ".range .day.this-month"), !1
            }
            var n = a(this).find(".event-single");
            v.find(".events .event-single").remove(),
            d(),
            //"" == s.type && (v.find("input.data1").val(a(this).data("date")), a(this).addClass("selected"), v.find(".add-event").find(".events-list").html(n.clone()), v.parent().is(".jalendar-input") && v.parent(".jalendar-input").find("input").val(a(this).data("date")), v.find(".events .event-single").length >= 0 && v.find(".events h3 span").html(v.find(".events .event-single").size()), v.find(".add-event").addClass("selected").height(v.find(".add-event .events").height() + 59))
            "" == s.type && !$(this).hasClass("disable-selecting") && (v.find("input.data1").val(a(this).data("date")), a(this).addClass("selected"), v.find(".add-event").find(".events-list").html(n.clone()), v.parent().is(".jalendar-input") && v.parent(".jalendar-input").find("input").val(a(this).data("date")), v.find(".events .event-single").length >= 0 && v.find(".events h3 span").html(v.find(".events .event-single").size()), v.find(".add-event").addClass("selected").attr("style", "height: auto;"));
        }),
        v.parent().is(".jalendar-input") && v.parent(".jalendar-input").find('input[type="text"], .jalendar').on("click", function(t) {
            t.stopPropagation(),
            a(this).addClass("selected")
        }),
        a("html").on("click", function() {
            a(".jalendar-input input").removeClass("selected")
        })
    }
} (jQuery);