/**
 * @description 全局功能封装
 * @author zr
 * @type {{registerFileDrop(*, *): void, saveBpmn(Object): void, handleDragOver(*): void, setColor(Object): void, downLoad(Object): void, upload(Object, Object, Object): void, handleFileSelect(*): void, setEncoded(Object, string, string): void, openFromUrl(Object, Object, Object, string): void, createDiagram(string, Object, Object): Promise<void>, getUrlParam: tools.getUrlParam}}
 */

import $ from 'jquery';

const accessToken = 'access_token';
const publicUrl = 'http://127.0.0.1:9200/api-activiti/';
const tools = {
    registerFileDrop(container, callback) {
        container.get(0).addEventListener('dragover', tools.handleDragOver, false);
        container.get(0).addEventListener('drop', tools.handleFileSelect, false);
    },
    /**
     * 获取地址栏参数
     * @param {string} value
     */
    getUrlParam: function (url) {
        var object = {};
        if (url.indexOf("?") != -1) {
            var str = url.split("?")[1];
            var strs = str.split("&");
            for (var i = 0; i < strs.length; i++) {
                object[strs[i].split("=")[0]] = strs[i].split("=")[1]
            }
            return object
        }
        return object[url];
    },
    /**
     * 通过xml创建bpmn
     * @param {string} xml 创建bpms xml
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     */
    async createDiagram(xml, bpmnModeler, container) {
        try {
            await bpmnModeler.importXML(xml);
            container.removeClass('with-error').addClass('with-diagram');
        } catch (err) {
            container.removeClass('with-diagram').addClass('with-error');
            container.find('.error pre').text(err.message);
            console.error(err);
        }
    },
    /**
     * 通过Json设置颜色
     * @param {object} json json 字符串
     */
    setColor(json, bpmnModeler) {
        var modeling = bpmnModeler.get('modeling');
        var elementRegistry = bpmnModeler.get('elementRegistry')
        var elementToColor = elementRegistry.get(json.name);
        if (elementToColor) {
            modeling.setColor([elementToColor], {
                stroke: json.stroke,
                fill: json.fill
            });
        }
    },
    getToken() {
        return window.localStorage.getItem(accessToken);
    },
    post(path, param, successFun) {
        tools.ajax(path, 'POST', 'json', param, successFun);
    },
    get(path, param, successFun) {
        tools.ajax(path, 'GET', 'json', param, successFun);
    },
    ajax(path, method, dataType, param, successFun, errorFun) {
        param = param || {};
        var token = tools.getToken();
        if (token) {
            param.access_token = token.access_token;
        }
        $.ajax({
            url: publicUrl + path,
            type: method,
            dataType: dataType,
            data: param,
            //headers:{'Content-Type':'application/json;charset=utf8'},
            beforeSend: function (xhr) {
                var token = tools.getToken();
                if (token) {
                    // xhr.setRequestHeader('Authorization', 'Basic ' + token.access_token);
                    xhr.setRequestHeader('Authorization', 'Bearer ' + token.access_token);
                }
            },
            success: function (result) {
                if (tools.isFunction(successFun)) {
                    successFun(result);
                }
            },
            error: function (err) {
                console.log(err)
                if (tools.isFunction(errorFun)) {
                    errorFun(err);
                }
            }
        });
    },
    /**
     * 保存bpmn对象
     * @param {object} bpmnModeler bpmn对象
     */
    saveBpmn(bpmnModeler) {
        bpmnModeler.saveXML({format: true}, function (err, xml) {
            if (err) {
                return console.error('保存失败，请重试', err);
            }
            console.log(xml)
            var param = {
                "stringBPMN": xml
            }
            var path = 'processDefinitionCtrl/addDeploymentByString';
            tools.post(path, param, function (result) {
                if (result.code == 200) {
                    tools.syhide('alert')
                    alert('保存成功')
                } else {
                    alert(result.msg)
                }
            });
        });
    },
    /**
     * 下载bpmn
     * @param {object} bpmnModeler bpmn对象
     */
    downLoad(bpmnModeler) {
        var downloadLink = $("#downloadBpmn")
        bpmnModeler.saveXML({format: true}, function (err, xml) {
            if (err) {
                return console.error('could not save BPMN 2.0 diagram', err);
            }
            tools.setEncoded(downloadLink, 'diagram.bpmn', err ? null : xml);
        });
    },
    /**
     * 转码xml并下载
     * @param {object} link 按钮
     * @param {string} name 下载名称
     * @param {string} data base64XML
     */
    setEncoded(link, name, data) {
        var encodedData = encodeURIComponent(data);
        if (data) {
            link.addClass('active').attr({
                'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
                'download': name
            });
        } else {
            link.removeClass('active');
        }
    },
    /**
     * 上传bpmn
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     */
    upload(bpmnModeler, container) {
        var FileUpload = document.myForm.uploadFile.files[0];
        var fm = new FormData();
        fm.append('processFile', FileUpload);
        $.ajax({
            url: publicUrl + 'processDefinitionCtrl/upload',
            type: 'POST',
            data: fm,
            async: false,
            contentType: false, //禁止设置请求类型
            processData: false, //禁止jquery对DAta数据的处理,默认会处理
            beforeSend: function (xhr) {
                var token = tools.getToken();
                if (token) {
                    // xhr.setRequestHeader('Authorization', 'Basic ' + token.access_token);
                    xhr.setRequestHeader('Authorization', 'Bearer ' + token.access_token);
                }
            },
            success: function (result) {
                tools.openFromUrl(bpmnModeler, container, result.data)
            },
            error: function (err) {
                console.log(err)
            }
        });
    },
    /**
     * 打开xml  Url 地址
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     * @param {string} url url地址
     */
    openFromUrl(bpmnModeler, container, url) {
        $.ajax(url, {dataType: 'text'}).done(async function (xml) {
            try {
                await bpmnModeler.importXML(xml);
                container.removeClass('with-error').addClass('with-diagram');
            } catch (err) {
                console.error(err);
            }
        });
    },
    /**
     * 打开弹出框
     * @param id
     */
    syopen(id) {
        var dom = $("#" + id);
        this.sycenter(dom);
        dom.addClass(name);
        dom.show();
        var that = this;
        $(".sy-mask").fadeIn(300)
        setTimeout(function () {
            dom.removeClass(name)
        }, 300);

    },
    /**
     * 隐藏弹出框
     * @param id
     */
    syhide(id) {
        if (typeof id == "undefined") {
            var dom = $(".sy-alert")
        } else {
            var dom = $("#" + id)
        }
        var name = dom.attr("sy-leave");
        dom.addClass(name);
        $(".sy-mask").fadeOut(300);
        setTimeout(function () {
            dom.hide();
            dom.removeClass(name);
        }, 300)
    },
    /**
     * 弹出框居中
     * @param dom
     */
    sycenter(dom) {
        var mgtop = parseFloat(dom.height() / 2);
        dom.css({
            "top": "50%",
            "margin-top": "-" + mgtop + "px"
        })
    },
    /**
     * 判断是否是数组
     * @param value
     * @returns {arg is Array<any>|boolean}
     */
    isArrayFn(value) {
        if (typeof Array.isArray === "function") {
            return Array.isArray(value);
        } else {
            return Object.prototype.toString.call(value) === "[object Array]";
        }
    },
    /**
     * 根据数据设置颜色
     * @param data
     * @returns {Array}
     */
    getByColor(data) {

        var ColorJson = []
        if (data) {
            for (var k in data['highLine']) {
                var par = {
                    "name": data['highLine'][k],
                    "stroke": "green",
                    "fill": "green"
                }
                ColorJson.push(par)
            }
            for (var k in data['highPoint']) {
                var par = {
                    "name": data['highPoint'][k],
                    "stroke": "gray",
                    "fill": "#eae9e9"

                }
                ColorJson.push(par)
            }
            for (var k in data['iDo']) {
                var par = {
                    "name": data['iDo'][k],
                    "stroke": "green",
                    "fill": "#a3d68e"
                }
                ColorJson.push(par)
            }
            for (var k in data['waitingToDo']) {
                var par = {
                    "name": data['waitingToDo'][k],
                    "stroke": "green",
                    "fill": "yellow"
                }
                ColorJson.push(par)
            }
        }
        return ColorJson
    },
    isFunction(fn) {
        if (fn) {
            if (typeof (fn) == 'function') {
                return true;
            } else {
                throw new Error(fn + '不是一个正确的Function!');
            }
        }
    },
    loadDefinitionXML(bpmnModeler, param, fn, container) {
        //加载后台方法获取xml
        var path = 'processDefinitionCtrl/getDefinitionXML';
        tools.ajax(path, 'GET', 'text', param, function (result) {
            if (result.code == 200) {
                var newXmlData = result.data
                tools.createDiagram(newXmlData, bpmnModeler, container);
                if (tools.isFunction(fn)) {
                    fn();
                }
            }
        })
    },
    loadHighLine(bpmnModeler, param, param1, container) {
        var path = 'activitiHistoryCtrl/getHighLine';
        tools.ajax(path, 'GET', 'json', param1, function (result) {
            if (result.code == 200) {
                var ColorJson = tools.getByColor(result.data.obj)
                tools.loadDefinitionXML(bpmnModeler, param, function () {
                    setTimeout(function () {
                        for (var i in ColorJson) {
                            tools.setColor(ColorJson[i], bpmnModeler)
                        }
                    }, 200)
                }, container)
            }
        });
    }
}


export default tools