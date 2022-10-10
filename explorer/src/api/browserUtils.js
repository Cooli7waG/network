export function getExplorerInfo(){
    //判断浏览器版本
    const userAgent = navigator.userAgent;
    console.log("userAgent:"+userAgent)
    let info;
    const isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    const isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    const isEdge = userAgent.toLowerCase().indexOf("edg") > -1; //判断是否IE的Edge浏览器
    const isIE11 = (userAgent.toLowerCase().indexOf("trident") > -1 && userAgent.indexOf("rv") > -1);
    let tempArray;
    if (/[Ff]irefox(\/\d+\.\d+)/.test(userAgent)) {
        tempArray = /([Ff]irefox)\/(\d+\.\d+)/.exec(userAgent);
        info = tempArray[1];
    } else if (isIE) {
        let version = "";
        const reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        const fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion == 7) {
            version = "IE7";
        } else if (fIEVersion == 8) {
            version = "IE8";
        } else if (fIEVersion == 9) {
            version = "IE9";
        } else if (fIEVersion == 10) {
            version = "IE10";
        } else {
            version = "0"
        }
        info = version;
    } else if (isEdge) {
        info = "Edge";
    } else if (isIE11) {
        info = "IE11";
    } else if (/[Cc]hrome\/\d+/.test(userAgent)) {
        tempArray = /([Cc]hrome)\/(\d+)/.exec(userAgent);
        info = tempArray[1];
    } else if (/[Vv]ersion\/\d+\.\d+\.\d+(\.\d)* *[Ss]afari/.test(userAgent)) {
        tempArray = /[Vv]ersion\/(\d+\.\d+\.\d+)(\.\d)* *([Ss]afari)/.exec(userAgent);
        info = tempArray[3];
    } else if (/[Oo]pera.+[Vv]ersion\/\d+\.\d+/.test(userAgent)) {
        tempArray = /([Oo]pera).+[Vv]ersion\/(\d+)\.\d+/.exec(userAgent);
        info = tempArray[1];
    } else {
        info = "unknown";
    }
    console.log("INFO:"+info)
    return info;
}

export function getMinersPageSize() {
    let pageSize = window.localStorage.getItem('MinersPageSize')
    return pageSize==undefined?20:pageSize;
}

export function saveMinersPageSize(pageSize) {
    window.localStorage.setItem('MinersPageSize', pageSize);
}

export function getMinersCurrentPage() {
    let currentPage = window.localStorage.getItem('MinersCurrentPage')
    return currentPage==undefined?1:currentPage;
}

export function saveMinersCurrentPage(currentPage) {
    window.localStorage.setItem('MinersCurrentPage', currentPage);
}

export function getTxsPageSize() {
    let pageSize = window.localStorage.getItem('TxsPageSize')
    return pageSize==undefined?20:pageSize;
}

export function saveTxsPageSize(pageSize) {
    window.localStorage.setItem('TxsPageSize', pageSize);
}

export function getTxsCurrentPage() {
    let currentPage = window.localStorage.getItem('TxsCurrentPage')
    return currentPage==undefined?1:currentPage;
}

export function saveTxsCurrentPage(currentPage) {
    window.localStorage.setItem('TxsCurrentPage', currentPage);
}

export function getMinerPageInfo() {
    let pageInfo = window.localStorage.getItem('MinerPageInfo')
    try {
        JSON.parse(pageInfo)
        return pageInfo==undefined?{pageSize:25,currentPage:1,type:"report"}:JSON.parse(pageInfo);
    }catch (err){
        return {pageSize:25,currentPage:1,type:"report"};
    }
}

export function setMinerPageInfo(pageSize,currentPage,type) {
    window.localStorage.setItem('MinerPageInfo', '{"pageSize":'+pageSize+',"currentPage":'+currentPage+',"type":"'+type+'"}');
}

export function getWalletMinerPageInfo() {
    let pageInfo = window.localStorage.getItem('WalletMinerPageInfo')
    try {
        JSON.parse(pageInfo)
        return pageInfo==undefined?{pageSize:25,currentPage:1,type:"report"}:JSON.parse(pageInfo);
    }catch (err){
        return {pageSize:25,currentPage:1,type:"report"};
    }
}

export function setWalletMinerPageInfo(pageSize,currentPage,type) {
    window.localStorage.setItem('WalletMinerPageInfo', '{"pageSize":'+pageSize+',"currentPage":'+currentPage+',"type":"'+type+'"}');
}
