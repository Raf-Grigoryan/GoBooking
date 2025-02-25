const theme = {
    primary: "var(--dashui-primary)",
    secondary: "var(--dashui-secondary)",
    success: "var(--dashui-success)",
    info: "var(--dashui-info)",
    warning: "var(--dashui-warning)",
    danger: "var(--dashui-danger)",
    dark: "var(--dashui-dark)",
    light: "var(--dashui-light)",
    white: "var(--dashui-white)",
    gray100: "var(--dashui-gray-100)",
    gray200: "var(--dashui-gray-200)",
    gray300: "var(--dashui-gray-300)",
    gray400: "var(--dashui-gray-400)",
    gray500: "var(--dashui-gray-500)",
    gray600: "var(--dashui-gray-600)",
    gray700: "var(--dashui-gray-700)",
    gray800: "var(--dashui-gray-800)",
    gray900: "var(--dashui-gray-900)",
    black: "var(--dashui-black)",
    transparent: "transparent"
};
window.theme = theme, function () {
    "use strict";
    var e;
    document.getElementById("perfomanceChart") && (e = {
        series: [21, 78, 89],
        chart: {height: 320, type: "radialBar"},
        colors: [window.theme.primary, window.theme.success, window.theme.danger],
        stroke: {lineCap: "round"},
        plotOptions: {
            radialBar: {
                startAngle: -168,
                endAngle: -450,
                hollow: {size: "55%"},
                track: {background: "transaprent"},
                dataLabels: {show: !1}
            }
        }
    }, new ApexCharts(document.querySelector("#perfomanceChart"), e).render()), document.getElementById("revenueChart") && (e = {
        series: [{
            name: "Current Week",
            data: [31, 40, 28, 51, 42, 109, 100]
        }, {name: "Past Week", data: [11, 32, 45, 32, 34, 52, 41]}],
        labels: ["Jan", "Feb", "March", "April", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        chart: {height: 350, type: "area", toolbar: {show: !1}},
        dataLabels: {enabled: !1},
        markers: {size: 5, hover: {size: 6, sizeOffset: 3}},
        colors: ["#624bff", "#0dcaf0"],
        stroke: {curve: "smooth", width: 2},
        grid: {borderColor: window.theme.gray300},
        xaxis: {
            labels: {
                show: !0,
                align: "right",
                minWidth: 0,
                maxWidth: 160,
                style: {
                    fontSize: "12px",
                    fontWeight: 400,
                    colors: [window.theme.gray600],
                    fontFamily: '"Inter", "sans-serif"'
                }
            },
            axisBorder: {show: !0, color: window.theme.gray300, height: 1, width: "100%", offsetX: 0, offsetY: 0},
            axisTicks: {show: !0, borderType: "solid", color: window.theme.gray300, height: 6, offsetX: 0, offsetY: 0}
        },
        legend: {labels: {colors: window.theme.gray600, useSeriesColors: !1}},
        yaxis: {
            labels: {
                show: !0,
                align: "right",
                minWidth: 0,
                maxWidth: 160,
                style: {
                    fontSize: "12px",
                    fontWeight: 400,
                    colors: window.theme.gray600,
                    fontFamily: '"Inter", "sans-serif"'
                }
            }
        }
    }, new ApexCharts(document.querySelector("#revenueChart"), e).render()), document.getElementById("totalSale") && (e = {
        series: [45, 38, 28, 15],
        labels: ["Direct", "Affiliate", "Sponsored", "E-mail"],
        colors: ["#624BFF", "#0EA5E9", "#DC3545", "#F59E0B"],
        chart: {type: "donut", height: 377},
        legend: {show: !1},
        dataLabels: {enabled: !0},
        plotOptions: {pie: {donut: {size: "70%"}}},
        stroke: {width: 0},
        responsive: [{breakpoint: 1400, options: {chart: {type: "donut", width: 290, height: 330}}}]
    }, new ApexCharts(document.querySelector("#totalSale"), e).render()), document.getElementById("locationmap") && new jsVectorMap({
        map: "world",
        selector: "#locationmap",
        zoomOnScroll: !0,
        zoomButtons: !0,
        markersSelectable: !0,
        markers: [{name: "United Kingdom", coords: [53.613, -11.6368]}, {
            name: "India",
            coords: [20.7504374, 73.7276105]
        }, {name: "United States", coords: [37.2580397, -104.657039]}, {
            name: "Sweden",
            coords: [-25.0304388, 115.2092761]
        }],
        markerStyle: {initial: {fill: ["#624BFF"]}},
        markerLabelStyle: {
            initial: {
                fontFamily: "Inter",
                fontSize: 13,
                fontWeight: 500,
                cursor: "default",
                fill: ["#161C24"]
            }
        },
        labels: {markers: {render: e => e.name}}
    }), document.getElementById("chartGraphics") && (e = {
        series: [{
            name: "Net Profit",
            data: [44, 55, 57, 56, 61, 58]
        }, {name: "Revenue", data: [76, 85, 101, 98, 87, 105]}],
        chart: {type: "bar", height: 260, toolbar: {show: !1}},
        colors: ["#624bff", "#f59e0b"],
        plotOptions: {bar: {horizontal: !1, columnWidth: "65%", endingShape: "rounded"}},
        dataLabels: {enabled: !1},
        legend: {show: !1},
        stroke: {show: !0, width: 2, colors: ["transparent"]},
        xaxis: {
            categories: ["18-35", "25-34", "35-44", "45-54", "55-64", "65+"],
            axisBorder: {show: !1},
            axisTicks: {show: !1},
            labels: {
                show: !0,
                align: "right",
                minWidth: 0,
                maxWidth: 160,
                style: {fontSize: "12px", fontWeight: 400, colors: "#475569", fontFamily: '"Inter", "sans-serif"'}
            }
        },
        yaxis: {
            labels: {
                show: !0,
                align: "right",
                minWidth: 0,
                maxWidth: 160,
                style: {fontSize: "12px", fontWeight: 400, colors: "#475569", fontFamily: '"Inter", "sans-serif"'}
            }
        },
        grid: {show: !1, borderColor: "#e2e8f0"},
        fill: {opacity: 1},
        tooltip: {y: {}}
    }, new ApexCharts(document.querySelector("#chartGraphics"), e).render()), document.getElementById("socialTraffic") && (e = {
        series: [83, 24, 12, 10, 8, 4],
        labels: ["Quara", "Twitter", "Facebook", "Yourube", "Linkedin", "Reddit"],
        chart: {width: 250, type: "donut"},
        plotOptions: {pie: {donut: {size: "80%"}}},
        colors: ["#624bff", "#f59e0b", "#198754", "#0ea5e9", "#dc3545", "#20c997"],
        dataLabels: {enabled: !1},
        legend: {show: !1},
        stroke: {show: !0, colors: [window.theme.transparent]},
        responsive: [{breakpoint: 768, options: {chart: {width: 200}, legend: {position: "bottom"}}}]
    }, new ApexCharts(document.querySelector("#socialTraffic"), e).render()), document.getElementById("chartCampaign") && (e = {
        series: [{
            name: "Campaign Sent",
            data: [44, 35, 57, 26, 61, 38, 54, 78, 84, 32]
        }],
        chart: {type: "bar", height: 60, sparkline: {enabled: !0}},
        colors: ["#624BFF"],
        plotOptions: {bar: {columnWidth: "60%"}},
        xaxis: {crosshairs: {width: 1}},
        tooltip: {fixed: {enabled: !1}, x: {show: !1}}
    }, new ApexCharts(document.querySelector("#chartCampaign"), e).render()), document.getElementById("chartLead") && (e = {
        series: [{
            name: "Leads",
            data: [25, 66, 41, 89, 63, 25, 44, 12, 36, 9, 54]
        }],
        chart: {type: "line", height: 60, sparkline: {enabled: !0}},
        stroke: {width: 2, curve: "smooth"},
        markers: {size: 0},
        colors: ["#198754"],
        tooltip: {fixed: {enabled: !1}, x: {show: !1}}
    }, new ApexCharts(document.querySelector("#chartLead"), e).render()), document.getElementById("chartDeals") && (e = {
        series: [{
            name: "Deals",
            data: [44, 105, 57, 99, 71, 48, 54, 88, 65, 44]
        }],
        chart: {type: "bar", height: 60, sparkline: {enabled: !0}},
        colors: ["#f59e0b"],
        plotOptions: {bar: {columnWidth: "60%"}},
        xaxis: {crosshairs: {width: 1}},
        tooltip: {fixed: {enabled: !1}, x: {show: !1}}
    }, new ApexCharts(document.querySelector("#chartDeals"), e).render()), document.getElementById("chartBooked") && (e = {
        series: [{
            name: "Revenue",
            data: [44, 105, 57, 99, 71, 48, 54, 88, 65, 44]
        }],
        chart: {type: "bar", height: 60, sparkline: {enabled: !0}},
        colors: ["#0ea5e9"],
        plotOptions: {bar: {columnWidth: "60%"}},
        xaxis: {crosshairs: {width: 1}},
        tooltip: {fixed: {enabled: !1}, x: {show: !1}}
    }, new ApexCharts(document.querySelector("#chartBooked"), e).render()), document.getElementById("chartCampaignEmail") && (e = {
        series: [55, 33, 12],
        labels: ["Total Sent", "Reached", "Opened"],
        chart: {width: 350, type: "donut"},
        colors: ["#f59e0b", "#198754", "#624BFF"],
        plotOptions: {pie: {donut: {size: "74%"}}},
        dataLabels: {enabled: !1},
        legend: {show: !1},
        stroke: {show: !0, colors: [window.theme.transparent]},
        responsive: [{breakpoint: 768, options: {chart: {width: 200}, legend: {position: "bottom"}}}]
    }, new ApexCharts(document.querySelector("#chartCampaignEmail"), e).render()), document.getElementById("chartRevenue") && (e = {
        series: [{
            name: "Current Month",
            type: "area",
            data: [44, 55, 41, 67, 22, 43, 21, 41, 56, 27, 43]
        }, {name: "Previous Month", type: "line", data: [30, 25, 36, 30, 45, 35, 64, 52, 59, 36, 39]}],
        chart: {height: 300, type: "line", stacked: !1, toolbar: {show: !1}},
        legend: {show: !1},
        grid: {borderColor: window.theme.gray300},
        fill: {type: "gradient", gradient: {shadeIntensity: 1, opacityFrom: .7, opacityTo: .9, stops: [0, 90, 100]}},
        colors: ["#624bff", "#198754"],
        stroke: {width: [2, 2], curve: "smooth", colors: ["#624BFF", "#198754"]},
        plotOptions: {bar: {columnWidth: "50%"}},
        labels: ["Dec 01", "Dec 02", "Dec 03", "Dec 04", "Dec 05", "Dec 06", "Dec 07", "Dec 08", "Dec 09", "Dec 10", "Dec 11"],
        markers: {size: 0},
        xaxis: {
            labels: {
                style: {
                    fontSize: "14px",
                    fontWeight: 400,
                    colors: "#1e293b",
                    fontFamily: '"Inter", "sans-serif"'
                }
            },
            axisBorder: {show: !0, color: window.theme.gray300, height: 1, width: "100%", offsetX: 0, offsetY: 0},
            axisTicks: {show: !0, borderType: "solid", color: window.theme.gray300, height: 6, offsetX: 0, offsetY: 0}
        },
        yaxis: {
            labels: {
                style: {
                    fontSize: "14px",
                    fontWeight: 400,
                    colors: "#1e293b",
                    fontFamily: '"Inter", "sans-serif"'
                }
            }
        },
        tooltip: {
            shared: !0, intersect: !1, y: {
                formatter: function (e) {
                    return void 0 !== e ? e.toFixed(0) + " points" : e
                }
            }
        }
    }, new ApexCharts(document.querySelector("#chartRevenue"), e).render()), document.getElementById("progressChart") && (e = {
        series: [75],
        chart: {height: 350, type: "radialBar", toolbar: {show: !1}},
        colors: [window.theme.primary, window.theme.success],
        plotOptions: {
            radialBar: {
                startAngle: -135,
                endAngle: 225,
                hollow: {
                    margin: 0,
                    size: "70%",
                    background: window.theme.white,
                    image: void 0,
                    imageOffsetX: 0,
                    imageOffsetY: 0,
                    position: "front",
                    dropShadow: {enabled: !0, top: 3, left: 0, blur: 4, opacity: .24}
                },
                track: {
                    background: window.theme.white,
                    strokeWidth: "67%",
                    margin: 0,
                    dropShadow: {enabled: !0, top: -3, left: 0, blur: 4, opacity: .35}
                },
                dataLabels: {
                    showOn: "always", name: {show: !1}, value: {
                        formatter: function (e) {
                            return parseInt(e) + "%"
                        }, color: window.theme.gray800, fontSize: "48px", fontWeight: "700", show: !0
                    }
                }
            }
        },
        fill: {
            type: "gradient",
            gradient: {
                shade: "dark",
                type: "horizontal",
                shadeIntensity: .5,
                gradientToColors: [window.theme.info],
                inverseColors: !1,
                opacityFrom: 1,
                opacityTo: 1,
                stops: [0, 100]
            }
        },
        stroke: {lineCap: "round"}
    }, new ApexCharts(document.querySelector("#progressChart"), e).render()), document.getElementById("taskSummary") && (e = {
        series: [{
            name: "Closed",
            type: "column",
            data: [12, 18, 20, 32, 19, 25, 30]
        }, {name: "New", type: "line", data: [20, 32, 28, 50, 38, 35, 49]}],
        chart: {height: 350, type: "line", toolbar: {show: !1}},
        plotOptions: {bar: {horizontal: !1, columnWidth: "40%", borderRadius: 5}},
        markers: {colors: ["#161C24"], fillColor: "#F4F6F8"},
        colors: [window.theme.gray300, window.theme.primary],
        legend: {show: !1},
        stroke: {width: [0, 4], colors: [window.theme.gray300]},
        dataLabels: {enabled: !0, enabledOnSeries: [1]},
        labels: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        grid: {borderColor: window.theme.gray300},
        xaxis: {
            labels: {
                style: {
                    fontSize: "13px",
                    fontWeight: 400,
                    colors: window.theme.gray800,
                    fontFamily: '"Inter", "sans-serif"'
                }
            },
            axisBorder: {show: !0, color: window.theme.gray300, height: 1, width: "100%", offsetX: 0, offsetY: 0},
            axisTicks: {show: !0, borderType: "solid", color: window.theme.gray300, height: 6, offsetX: 0, offsetY: 0}
        },
        yaxis: {
            labels: {
                style: {
                    fontSize: "13px",
                    fontWeight: 400,
                    colors: window.theme.gray700,
                    fontFamily: '"Inter", "sans-serif"'
                }
            }
        }
    }, new ApexCharts(document.querySelector("#taskSummary"), e).render()), document.getElementById("taskStatus") && (e = {
        dataLabels: {enabled: !1},
        series: [75, 25],
        labels: ["Completed", "Incomplete"],
        colors: [window.theme.primary, window.theme.info],
        chart: {width: 480, type: "donut"},
        stroke: {show: !0, colors: [window.theme.transparent]},
        plotOptions: {pie: {expandOnClick: !1, donut: {size: "75%"}}},
        legend: {
            position: "bottom",
            fontFamily: '"Inter", "sans-serif"',
            fontWeight: 500,
            fontSize: "14px",
            labels: {colors: window.theme.gray500, useSeriesColors: !1},
            markers: {
                width: 8,
                height: 8,
                strokeWidth: 0,
                strokeColor: window.theme.gray600,
                fillColors: void 0,
                radius: 12,
                customHTML: void 0,
                onClick: void 0,
                offsetX: -2,
                offsetY: 1
            },
            itemMargin: {horizontal: 8, vertical: 0}
        },
        tooltip: {theme: "light", marker: {show: !0}, x: {show: !1}},
        states: {hover: {filter: {type: "none"}}},
        responsive: [{breakpoint: 1400, options: {chart: {type: "donut", width: 290, height: 410}}}]
    }, new ApexCharts(document.querySelector("#taskStatus"), e).render()), document.getElementById("taskSectionchart") && (e = {
        series: [44, 65, 89, 75],
        chart: {height: 400, type: "radialBar"},
        legend: {
            show: !0,
            fontSize: "14px",
            fontFamily: "Inter",
            fontWeight: 500,
            position: "bottom",
            itemMargin: {horizontal: 8, vertical: 0},
            labels: {colors: window.theme.gray800, useSeriesColors: !1},
            markers: {width: 8, height: 8, offsetY: 1, offsetX: -2}
        },
        plotOptions: {
            radialBar: {
                dataLabels: {
                    name: {},
                    value: {
                        fontSize: "24px", fontWeight: 600, color: window.theme.gray800, formatter: function (e) {
                            return e
                        }
                    },
                    total: {
                        show: !0,
                        label: "Total",
                        fontSize: "12px",
                        color: window.theme.gray600,
                        formatter: function (e) {
                            return 273
                        }
                    }
                }, track: {background: window.theme.gray400, margin: 10}
            }
        },
        labels: ["Design", "Frontend", "Development", "Issues"],
        colors: ["#624BFF", "#198754", "#0ea5e9", "#f59e0b"]
    }, new ApexCharts(document.querySelector("#taskSectionchart"), e).render()), document.getElementById(".taskPrototypeChart") && (e = {
        series: [75],
        chart: {height: 40, width: 40, type: "radialBar"},
        grid: {show: !1, padding: {left: -15, right: -15, top: -12, bottom: -15}},
        colors: [window.theme.info],
        plotOptions: {
            radialBar: {
                hollow: {size: "30%"},
                dataLabels: {
                    showOn: "always",
                    name: {show: !0, fontSize: "11px", fontFamily: void 0, fontWeight: 600, color: void 0, offsetY: 4},
                    value: {show: !1}
                },
                track: {background: window.theme.gray400}
            }
        },
        stroke: {lineCap: "round"},
        labels: ["75%"]
    }, new ApexCharts(document.querySelector(".taskPrototypeChart"), e).render()), document.getElementById(".taskContentChart") && (e = {
        series: [85],
        chart: {height: 40, width: 40, type: "radialBar"},
        grid: {show: !1, padding: {left: -15, right: -15, top: -12, bottom: -15}},
        colors: [window.theme.danger],
        plotOptions: {
            radialBar: {
                hollow: {size: "30%"},
                dataLabels: {
                    showOn: "always",
                    name: {show: !0, fontSize: "11px", fontFamily: void 0, fontWeight: 600, color: void 0, offsetY: 4},
                    value: {show: !1}
                },
                track: {background: window.theme.gray400}
            }
        },
        stroke: {lineCap: "round"},
        labels: ["85%"]
    }, new ApexCharts(document.querySelector(".taskContentChart"), e).render()), document.getElementById(".taskFigmaChart") && (e = {
        series: [40],
        chart: {height: 40, width: 40, type: "radialBar"},
        grid: {show: !1, padding: {left: -15, right: -15, top: -12, bottom: -15}},
        colors: [window.theme.warning],
        plotOptions: {
            radialBar: {
                hollow: {size: "30%"},
                dataLabels: {
                    showOn: "always",
                    name: {show: !0, fontSize: "11px", fontFamily: void 0, fontWeight: 600, color: void 0, offsetY: 4},
                    value: {show: !1}
                },
                track: {background: window.theme.gray400}
            }
        },
        stroke: {lineCap: "round"},
        labels: ["40%"]
    }, new ApexCharts(document.querySelector(".taskFigmaChart"), e).render()), document.getElementById(".taskInterfaceChart") && (e = {
        series: [35],
        chart: {height: 40, width: 40, type: "radialBar"},
        grid: {show: !1, padding: {left: -15, right: -15, top: -12, bottom: -15}},
        colors: [window.theme.primary],
        plotOptions: {
            radialBar: {
                hollow: {size: "30%"},
                dataLabels: {
                    showOn: "always",
                    name: {show: !0, fontSize: "11px", fontFamily: void 0, fontWeight: 600, color: void 0, offsetY: 4},
                    value: {show: !1}
                },
                track: {background: window.theme.gray400}
            }
        },
        stroke: {lineCap: "round"},
        labels: ["35%"]
    }, new ApexCharts(document.querySelector(".taskInterfaceChart"), e).render()), document.getElementById(".taskWireframeChart") && (e = {
        series: [65],
        chart: {height: 40, width: 40, type: "radialBar"},
        grid: {show: !1, padding: {left: -15, right: -15, top: -12, bottom: -15}},
        colors: [window.theme.success],
        plotOptions: {
            radialBar: {
                hollow: {size: "30%"},
                dataLabels: {
                    showOn: "always",
                    name: {show: !0, fontSize: "11px", fontFamily: void 0, fontWeight: 600, color: void 0, offsetY: 4},
                    value: {show: !1}
                },
                track: {background: window.theme.gray400}
            }
        },
        stroke: {lineCap: "round"},
        labels: ["65%"]
    }, new ApexCharts(document.querySelector(".taskWireframeChart"), e).render()), document.getElementById("budgetExpenseChart") && (e = {
        series: [{
            name: "Series 1",
            data: [90, 32, 30, 40, 100, 20]
        }],
        stroke: {show: !0, width: 2, colors: [window.theme.primary], dashArray: 0},
        fill: {colors: "#754ffe", opacity: .2},
        colors: [window.theme.primary],
        dataLabels: {enabled: !0, background: {enabled: !0, borderRadius: 2}},
        chart: {height: 350, type: "radar", toolbar: {show: !1}},
        plotOptions: {
            radar: {
                size: 150,
                offsetX: 0,
                offsetY: 0,
                polygons: {
                    strokeColors: window.theme.gray300,
                    strokeWidth: 1,
                    connectorColors: window.theme.gray300,
                    fill: {colors: void 0}
                }
            }
        },
        xaxis: {
            categories: ["Design", "SaaS Services", "Development", "SEO", "Entertainment", "Marketing"],
            labels: {
                show: !0,
                style: {colors: window.theme.primary, fontSize: "14px", fontFamily: "Inter", fontWeight: 600}
            }
        }
    }, new ApexCharts(document.querySelector("#budgetExpenseChart"), e).render()), document.getElementById("storeOne") && (e = {
        series: [{data: [1, 6, 14, 48, 18, 15, 47, 45, 65, 19, 22]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#624BFF"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeOne"), e).render()), document.getElementById("storeTwo") && (e = {
        series: [{data: [45, 20, 8, 42, 30, 5, 35, 79, 22, 54, 64]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#dc3545"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeTwo"), e).render()), document.getElementById("storeThree") && (e = {
        series: [{data: [2, 14, 2, 47, 42, 15, 35, 75, 20, 67, 89]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#f59e0b"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeThree"), e).render()), document.getElementById("storeFour") && (e = {
        series: [{data: [26, 15, 48, 12, 47, 19, 35, 19, 85, 68, 50]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#198754"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeFour"), e).render()), document.getElementById("storeFive") && (e = {
        series: [{data: [60, 67, 12, 49, 6, 78, 63, 51, 33, 8, 16]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#0ea5e9"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeFive"), e).render()), document.getElementById("storeSix") && (e = {
        series: [{data: [78, 63, 51, 33, 8, 16, 60, 67, 12, 49]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#fd7e14"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeSix"), e).render()), document.getElementById("storeSeven") && (e = {
        series: [{data: [12, 14, 2, 47, 42, 15, 35, 75, 20, 67, 89]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#624bff"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeSeven"), e).render()), document.getElementById("storeEight") && (e = {
        series: [{data: [26, 15, 48, 12, 47, 19, 35, 19, 85, 68, 50]}],
        chart: {type: "area", height: 80, sparkline: {enabled: !0}},
        fill: {
            type: "gradient",
            gradient: {
                shadeIntensity: 1,
                inverseColors: !1,
                opacityFrom: .45,
                opacityTo: .09,
                stops: [20, 100, 100, 100]
            }
        },
        stroke: {curve: "smooth", width: 2},
        colors: ["#637381"],
        tooltip: {
            fixed: {enabled: !1}, x: {show: !1}, y: {
                title: {
                    formatter: function (e) {
                        return ""
                    }
                }
            }, marker: {show: !1}
        }
    }, new ApexCharts(document.querySelector("#storeEight"), e).render()), document.getElementById("visitorBlog") && (e = {
        series: [{name: "Visitor", data: [4, 8, 12, 18, 33, 24, 21, 28, 92, 42, 88, 36]}, {
            name: "Visitor",
            data: [13, 23, 20, 8, 13, 27, 4, 8, 12, 18, 33, 24]
        }],
        chart: {toolbar: {show: !1}, type: "bar", height: 480, stacked: !0},
        legend: {show: !1},
        colors: [window.theme.primary, window.theme.gray300],
        plotOptions: {bar: {horizontal: !1, columnWidth: "40%", borderRadius: 4, endingShape: "rounded"}},
        dataLabels: {enabled: !1},
        stroke: {show: !0, width: 1, colors: ["transparent"]},
        grid: {borderColor: window.theme.gray300, strokeDashArray: 2, xaxis: {lines: {show: !1}}},
        xaxis: {
            categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            axisBorder: {show: !1},
            axisTicks: {show: !0, borderType: "solid", color: window.theme.gray400, width: 6, offsetX: 0, offsetY: 0},
            labels: {
                offsetX: 0,
                offsetY: 5,
                style: {
                    fontSize: "13px",
                    fontWeight: 400,
                    fontFamily: '"Inter", "sans-serif"',
                    colors: [window.theme.gray800]
                }
            }
        },
        grid: {
            borderColor: window.theme.gray300,
            strokeDashArray: 3,
            xaxis: {lines: {show: !1}},
            yaxis: {lines: {show: !0}},
            padding: {top: 0, right: 0, bottom: 0, left: -10}
        },
        yaxis: {
            title: {text: void 0},
            plotOptions: {bar: {horizontal: !1, endingShape: "rounded", columnWidth: "80%"}},
            labels: {
                style: {
                    fontSize: "13px",
                    fontWeight: 400,
                    fontFamily: '"Inter", "sans-serif"',
                    colors: [window.theme.gray800]
                }, offsetX: -10
            }
        },
        fill: {opacity: 1},
        tooltip: {
            y: {
                formatter: function (e) {
                    return e + " sales "
                }
            }, marker: {show: !0}
        }
    }, new ApexCharts(document.querySelector("#visitorBlog"), e).render()), document.getElementById("blogTrafficChart") && (e = {
        series: [42, 27, 32],
        labels: ["Search ", "Referral", "Direct "],
        chart: {type: "donut", width: 300},
        legend: {show: !1},
        colors: ["#624bff", "#198754", "#0ea5e9"],
        dataLabels: {
            style: {
                fontSize: "12px",
                fontFamily: '"Inter", "sans-serif"',
                fontWeight: "bold",
                colors: [window.theme.gray200]
            }, dropShadow: {enabled: !1}
        },
        stroke: {show: !0, colors: [window.theme.transparent]},
        responsive: [{breakpoint: 480, options: {chart: {width: 200}}}]
    }, new ApexCharts(document.querySelector("#blogTrafficChart"), e).render()), document.getElementById("cashFlowChart") && (e = {
        series: [45, 38],
        labels: ["Income", "Expenses"],
        colors: [window.theme.primary, window.theme.warning],
        chart: {type: "donut", height: 240},
        legend: {show: !1},
        dataLabels: {enabled: !1},
        plotOptions: {pie: {donut: {size: "80%"}}},
        stroke: {width: 0},
        responsive: [{breakpoint: 1400, options: {chart: {type: "donut", width: 290, height: 330}}}]
    }, new ApexCharts(document.querySelector("#cashFlowChart"), e).render()), document.getElementById("dealRoportChart") && (e = {
        series: [{
            name: "Won",
            data: [80, 50, 30, 40, 100, 20]
        }, {name: "Pending", data: [20, 30, 40, 80, 20, 80]}, {name: "Loss", data: [44, 76, 78, 13, 43, 10]}],
        chart: {height: 350, type: "radar", toolbar: {show: !1}, dropShadow: {enabled: !0, blur: 1, left: 1, top: 1}},
        legend: {
            show: !0,
            fontWeight: 500,
            offsetX: 0,
            offsetY: 0,
            markers: {width: 8, height: 8, radius: 6},
            itemMargin: {horizontal: 10, vertical: 0}
        },
        plotOptions: {
            radar: {
                polygons: {
                    strokeColors: window.theme.gray300,
                    strokeWidth: 1,
                    connectorColors: window.theme.gray300,
                    fill: {colors: void 0}
                }
            }
        },
        markers: {size: 0},
        markers: {size: 0},
        colors: ["#624bff", "#dc3545", "#198754"],
        fill: {opacity: .2},
        xaxis: {categories: ["2011", "2012", "2013", "2014", "2015", "2016"]},
        grid: {borderColor: window.theme.gray300, xaxis: {lines: {show: !1}}}
    }, new ApexCharts(document.querySelector("#dealRoportChart"), e).render()), document.getElementById("salesForecastChart") && (e = {
        series: [{
            name: "Goal",
            data: [37]
        }, {name: "Pending Forcast", data: [12]}, {name: "Revenue", data: [18]}],
        chart: {type: "bar", height: 350, toolbar: {show: !1}},
        plotOptions: {bar: {horizontal: !1, columnWidth: "55%"}},
        stroke: {show: !0, width: 5, colors: ["transparent"]},
        grid: {borderColor: window.theme.gray300},
        xaxis: {
            categories: [""],
            axisTicks: {show: !1, borderType: "solid", color: window.theme.gray400, height: 6, offsetX: 0, offsetY: 0},
            axisBorder: {show: !0, color: window.theme.gray400, offsetX: 0, offsetY: 0},
            title: {
                text: "Total Forecasted Value",
                offsetX: 0,
                offsetY: -30,
                style: {
                    color: window.theme.gray400,
                    fontSize: "12px",
                    fontWeight: 400,
                    fontFamily: '"Inter", "sans-serif"'
                }
            }
        },
        yaxis: {
            labels: {
                formatter: function (e) {
                    return "$" + e
                }
            }, tickAmount: 4, min: 0
        },
        fill: {opacity: 1},
        legend: {
            show: !0,
            position: "bottom",
            horizontalAlign: "center",
            fontWeight: 500,
            offsetX: 0,
            offsetY: -14,
            itemMargin: {horizontal: 8, vertical: 0},
            markers: {width: 10, height: 10}
        },
        colors: ["#624bff", "#198754", "#0ea5e9"]
    }, new ApexCharts(document.querySelector("#salesForecastChart"), e).render())
}();