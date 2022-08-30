
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
    if (value == null || value == undefined) {
        return value;
    }
    if (index == null || index == undefined || index < 1) {
        index = 20;
    }
    if (value.length > index) {
        return value.substring(0, index) + "..."
    } else {
        return value;
    }
}

export function formatPower(value) {
    if (value == null || value == undefined) {
        return "0 W";
    }
    if (value > 10000) {
        return (value / 1000) + " kW";
    } else {
        return value + " W";
    }
}

export function formatElectricity(value) {
    if (value == null || value == undefined) {
        return "0 Wh";
    }
    if (value > 10000) {
        return (value / 1000) + " kWh";
    } else {
        return value + " Wh";
    }
}

export function formatNumber(value) {
    if (value == null || value == undefined) {
        return 0;
    }
    let str = value.toString();
    let strArr = str.split(".");
    if(strArr[1] == undefined){
        return Number(strArr[0]).toLocaleString();
    }
    return Number(strArr[0]).toLocaleString() + "." + strArr[1];

}

export function formatLocation(locationType, latitude, longitude) {
    if (latitude == null || latitude == undefined) {
        return "Unknown";
    }
    if (longitude == null || longitude == undefined) {
        return "Unknown";
    }
    if (locationType == null || locationType == undefined) {
        locationType = 1;
    }
    if (locationType == 1) {
        return latitude + "," + longitude
    } else {
        return latitude + "," + longitude
    }
}

export function formatToken(value) {
    if (value == null || value == undefined || value==0) {
        return 0;
    }
    let fixed = getTokenFixed(value);
    const num = Number(value).toFixed(fixed)
    return formatNumber(num);
}

export function getTokenFixed(value) {
    let fixed = 0;
    if (value == null || value == undefined || value==0) {
        return fixed;
    }
    if(value>1000){
        fixed = 0;
    }else if(value>100 && value <=1000){
        fixed = 1;
    }else if(value>10 && value <=100){
        fixed = 2;
    }else if(value>1 && value <=10){
        fixed = 4;
    }else if(value>0.1 && value <=1){
        let str = value.toString();
        let strArr = str.split(".");
        if(strArr[1] == undefined){
            fixed = 0;
        }else if(strArr[1].length>8){
            fixed = 4;
        }else {
            fixed = strArr[1].length;
        }
    }else{
        let str = value.toString();
        let strArr = str.split(".");
        if(strArr[1] == undefined){
            fixed = 0;
        }else if(strArr[1].length>8){
            fixed = 8;
        }else {
            fixed = strArr[1].length;
        }
    }
    return fixed;
}
