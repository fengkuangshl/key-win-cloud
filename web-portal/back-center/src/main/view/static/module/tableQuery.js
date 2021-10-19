layui.define(['config', 'table', 'layer'], function (exports) {
    var config = layui.config;
    var table = layui.table;
    var layer = layui.layer;
    var tableQuery = {
        /**
         * {
                elem: '#edi-table',
                // url: config.base_server + 'edi_query.json',
                url: config.base_server + 'api-web-internet/edic/queryByPaged',
                method: 'POST',
                where: {
                    access_token: config.getToken().access_token
                },
                request: {
                    pageName: 'pageNo',   // 页码的参数名称，默认：page
                    limitName: 'pageSize'   // 每页数据量的参数名，默认：limit
                },
                page: true,
                cols: [[
                    {type: 'numbers'},
                    {field: 'tradeName', sort: true, title: '行业'},
                    {field: 'enterpriseTypeName', sort: true, title: '企业类型'},
                    {field: 'name', sort: true, title: '字段名称'},
                    {field: 'dataType', sort: true, title: '数据类型'},
                    {sort: true, title: '是否需要验证',width: 150,templet:function (d) {
                            return d.sex==1?"是":"否"
                        }},
                    {field: 'verifyExpress', sort: true, title: '验证表达式',width: 150},
                    {field: 'errorMsg', sort: true, title: '被验证的提示信息'},
                    {field: 'valueList', sort: true, title: '值列表'},
                    {
                        sort: true, templet: function (d) {
                            return util.toDateString(d.createTime);
                        }, title: '创建时间'
                    },
                    {align: 'center', toolbar: '#edi-table-bar', title: '操作'}
                ]]
            }
         */
        table: function (element, url, method, cols, where,doneCallFun, isPage, widthNum, heightNum) {
            isPage = isPage || true;
            where = where || {};
            var whereNew = where || {};
            if (isPage &&where.pageNo === undefined && where.pageSize === undefined) {
                whereNew = {
                    t: where
                };
            }

            var param = {
                elem: element,
                url: config.base_server + url,
                method: method,
                contentType: 'application/json',
                where: whereNew,
                headers: {},
                parseData: function (res) {
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.message, //解析提示文本
                        "count": res.count, //解析数据长度
                        "data": res.data //解析数据列表
                    };
                },
                request: {
                    pageName: 'pageNo',   // 页码的参数名称，默认：page
                    limitName: 'pageSize'   // 每页数据量的参数名，默认：limit
                },
                page: isPage,
                cols: cols,
                width: widthNum,
                height: heightNum,
                done: doneCallFun
            };
            var token = config.getToken();
            if (token) {
                if (method === 'POST') {
                    param.headers.Authorization = 'Bearer ' + token.access_token
                } else {
                    param.where.access_token = token.access_token
                }
            }
            param.error = function (result, status, xhr) {
                // 判断登录过期和没有权限
                if(result && result.status && result.status == 401){
                    config.removeToken();
                    layer.msg('登录过期', {icon: 2, time: 1500}, function () {
                        location.replace('/login.html');
                    }, 1000);
                    return;
                }else if(result && result.status && result.status == 403){
                    layer.msg('没有权限', {icon: 2});
                    return;
                }
            };
            table.render(param);
        },
        post: function (element, url, cols, where, isPage, widthNum, heightNum) {
            tableQuery.table(element, url, 'POST', cols, where, isPage, widthNum, heightNum)
        },
        get: function (element, url, cols, where, isPage, widthNum, heightNum) {
            tableQuery.table(element, url, 'GET', cols, where, isPage, widthNum, heightNum)
        },
        parseJSON: function (str) {
            if (typeof str == 'string') {
                try {
                    var obj = JSON.parse(str);
                    if (typeof obj == 'object' && obj) {
                        return obj;
                    }
                } catch (e) {
                }
            }
        }
    };
    exports('tableQuery', tableQuery);
});
