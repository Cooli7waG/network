export function formatDate(value, format) {
    //value: 需要格式化的数据
    //format: 指定格式 yyyy-MM-dd hh:mm:ss

    let date = new Date(value);
    // 获取年份
    let year = date.getFullYear();

    if (/(y+)/.test(format)) {
        // 获取匹配组的内容
        let content = RegExp.$1;
        format = format.replace(content, year.toString().slice(4 - content.length));
    }

    let o = {
        // y: date.getFullYear(),  // 用这一句也行，但只适用于四位数显示时候用
        M: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        m: date.getMinutes(),
        s: date.getSeconds()
    };

    for (let key in o) {
        // 构造动态正则
        let reg = new RegExp(`(${key}+)`);

        if (reg.test(format)) {
            // 获取匹配组的内容
            let content = RegExp.$1;
            let k = o[key] >= 10 ? o[key] : content.length == 2 ? '0' + o[key] : o[key];
            format = format.replace(content, k);
        }
    }
    return format;
}

export function formatString(value, index) {
    if(value==null || value==undefined){
        return value;
    }
    if(index==null || index == undefined || index<1){
        index = 20;
    }
    if(value.length>index) {
        return value.substring(0,index)+"..."
    }else {
        return value;
    }
}

export function formatPower(value) {
    if(value==null || value==undefined){
        return "0 W";
    }
    if(value>10000) {
        return (value/1000)+" kW";
    }else {
        return value+" W";
    }
}

export function formatElectricity(value) {
    if(value==null || value==undefined){
        return "0 Wh";
    }
    if(value>10000) {
        return (value/1000)+" kWh";
    }else {
        return value+" Wh";
    }
}

export function formatNumber(value) {
    if(value==null || value==undefined){
        return 0;
    }
    if(value.toString().concat(".")){
        let str = value.toString();
        let strArr = str.split(".");
        return Number(strArr[0]).toLocaleString()+"."+strArr[1];
    }else {
        return value.toLocaleString()
    }
}
