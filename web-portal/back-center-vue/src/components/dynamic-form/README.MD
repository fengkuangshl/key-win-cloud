(val)=>{
        debugger ;
        var todoTaskMain = document.querySelector("#todoTaskMain").__vue__;
        var vueDiv = document.querySelector("#dynamicForm").parentElement.__vue__;
        var formItems = vueDiv.formItems;
        var map = new Map();
        for (var item in formItems) {
            var formItem = formItems[item];
            map.set(formItem.model, formItem);
        }
        if (val === 'TT') {
            map.get('FormProperty_23taa76').isShowControl = true;
            todoTaskMain.trunTaskBtn = true;
            todoTaskMain.delegatelTaskBtn = false;
            todoTaskMain.approvalTaskBtn = false;
        } else if(val === 'DT'){
            map.get('FormProperty_23taa76').isShowControl = true;
            todoTaskMain.trunTaskBtn = false;
            todoTaskMain.delegatelTaskBtn = true;
            todoTaskMain.approvalTaskBtn = false;
        }else{
            map.get('FormProperty_23taa76').isShowControl = false;
            todoTaskMain.trunTaskBtn = false;
            todoTaskMain.delegatelTaskBtn = false;
            todoTaskMain.approvalTaskBtn = true;
        }
    }


FormProperty_0ueitp2-_!类型-_!名称-_!默认值-_!是否参数-_!是否只读-_!是否展示控件-_!控件选项-_!控件事件

FormProperty_06q23s6-_!string-_!姓名-_!请输入姓名-_!f
FormProperty_07nklqv-_!radio-_!审批-_!无-_!s-_!f-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB

默认值：无、字符常量、FormProperty_开头定义过的控件ID
是否参数：f为不是参数，s是字符，t是时间(不需要int，因为这里int等价于string)
是否只读:t只读，f:可写(默认f，可以不写)
注：类型是可以获取到的，但是为了统一配置原则，都配置到

-----------------------------------------------------
FormProperty_2dks78e-_!string-_!姓名-_!请输入姓名-_!f
FormProperty_3jb9mk5-_!long-_!请假天数-_!1-_!s
FormProperty_31v8qlb-_!textarea-_!请假事由-_!请输入请假事由-_!f

FormProperty_31v7bd5-_!string-_!请假人姓名-_!FormProperty_2dks78e-_!f
FormProperty_3ce90sb-_!long-_!请假天数-_!FormProperty_3jb9mk5-_!f
FormProperty_2vrhtoo-_!textarea-_!请假事由-_!FormProperty_31v8qlb-_!f

FormProperty_2f9jccm-_!string-_!请假人姓名-_!FormProperty_2dks78e-_!f
FormProperty_16j37c9-_!long-_!请假天数-_!FormProperty_3jb9mk5-_!f
FormProperty_32r44md-_!textarea-_!请假事由-_!FormProperty_31v8qlb-_!f


FormProperty_07ba83t-_!string-_!请假人姓名-_!FormProperty_2dks78e-_!f
FormProperty_05hs96c-_!long-_!请假天数-_!FormProperty_3jb9mk5-_!f
FormProperty_0mi1oq5-_!textarea-_!请假事由-_!FormProperty_31v8qlb-_!f

FormProperty_3k2796d-_!radio-_!审批-_!无-_!s-_!f-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB
FormProperty_085ah6l-_!textarea-_!备注-_!无-_!f-_!f

FormProperty_212amr4-_!radio-_!审批-_!无-_!s-_!f-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB
FormProperty_3u4v0od-_!textarea-_!备注-_!无-_!f

FormProperty_07nklqv-_!radio-_!审批-_!无-_!s-_!f-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB
FormProperty_0jh5dho-_!textarea-_!备注-_!无-_!f



FormProperty_07nklqv-_!radio-_!审批-_!无-_!s-_!false-_!true-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB-_!change!_!(val)=>{alert(val)}


FormProperty_07nklqv-_!radio-_!审批-_!Y-_!s-_!false-_!true-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB!_!转办-_-TT!_!委派-_-DT-_!change!_!(val)=>{        debugger ;        var todoTaskMain = document.querySelector("#todoTaskMain").__vue__;        var vueDiv = document.querySelector("#dynamicForm").parentElement.__vue__;        var formItems = vueDiv.formItems;        var map = new Map();        for (var item in formItems) {            var formItem = formItems[item];            map.set(formItem.model, formItem);        }        if (val === 'TT') {            map.get('FormProperty_23taa76').isShowControl = true;            todoTaskMain.trunTaskBtn = true;            todoTaskMain.delegatelTaskBtn = false;            todoTaskMain.approvalTaskBtn = false;        } else if(val === 'DT'){            map.get('FormProperty_23taa76').isShowControl = true;            todoTaskMain.trunTaskBtn = false;            todoTaskMain.delegatelTaskBtn = true;            todoTaskMain.approvalTaskBtn = false;        }else{            map.get('FormProperty_23taa76').isShowControl = false;            todoTaskMain.trunTaskBtn = false;            todoTaskMain.delegatelTaskBtn = false;            todoTaskMain.approvalTaskBtn = true;        }    }
FormProperty_23taa76-_!user_list-_!请选择用户-_!f-_!false-_!true



(val) => {
	debugger;
	var todoTaskMain = document.querySelector("#todoTaskMain").__vue__;
	var vueDiv = document.querySelector("#dynamicForm").parentElement.__vue__;
  todoTaskMain.controlEventType = '';
	var formItems = vueDiv.formItems;
	var map = new Map();
	for (var item in formItems) {
		var formItem = formItems[item];
		map.set(formItem.model, formItem);
	}
  const radioModel =  map.get('FormProperty_07nklqv');
  if(radioModel !== undefined && radioModel !== null){
    const options =  radioModel.opts;
    for (var item in options) {
		var opt = options[item];
        if(opt.value === val){
            todoTaskMain.controlEventType = opt.label;
        }
	}
  }
	if (val === 'TT') {
		map.get('FormProperty_23taa76')
			.isShowControl = true;
		todoTaskMain.trunTaskBtn = true;
		todoTaskMain.delegatelTaskBtn = false;
		todoTaskMain.approvalTaskBtn = false;
	} else if (val === 'DT') {
		map.get('FormProperty_23taa76')
			.isShowControl = true;
		todoTaskMain.trunTaskBtn = false;
		todoTaskMain.delegateTaskBtn = true;
		todoTaskMain.approvalTaskBtn = false;
	} else {
		map.get('FormProperty_23taa76')
			.isShowControl = false;
		todoTaskMain.trunTaskBtn = false;
		todoTaskMain.delegatelTaskBtn = false;
		todoTaskMain.approvalTaskBtn = true;
	}
}

FormProperty_07nklqv-_!radio-_!审批-_!Y-_!s-_!false-_!true-_!同意-_-Y!_!拒绝-_-N!_!驳回-_-GB!_!转办-_-TT!_!委派-_-DT-_!change!_!(val) => {	debugger;	var todoTaskMain = document.querySelector("#todoTaskMain").__vue__;	var vueDiv = document.querySelector("#dynamicForm").parentElement.__vue__;  todoTaskMain.controlEventType = '';	var formItems = vueDiv.formItems;	var map = new Map();	for (var item in formItems) {		var formItem = formItems[item];		map.set(formItem.model, formItem);	}  const radioModel =  map.get('FormProperty_07nklqv');  if(radioModel !== undefined && radioModel !== null){    const options =  radioModel.opts;    for (var item in options) {		var opt = options[item];        if(opt.value === val){            todoTaskMain.controlEventType = opt.label;        }	}  }	if (val === 'TT') {		map.get('FormProperty_23taa76')			.isShowControl = true;		todoTaskMain.trunTaskBtn = true;		todoTaskMain.delegatelTaskBtn = false;		todoTaskMain.approvalTaskBtn = false;	} else if (val === 'DT') {		map.get('FormProperty_23taa76')			.isShowControl = true;		todoTaskMain.trunTaskBtn = false;		todoTaskMain.delegatelTaskBtn = true;		todoTaskMain.approvalTaskBtn = false;	} else {		map.get('FormProperty_23taa76')			.isShowControl = false;		todoTaskMain.trunTaskBtn = false;		todoTaskMain.delegatelTaskBtn = false;		todoTaskMain.approvalTaskBtn = true;	}}