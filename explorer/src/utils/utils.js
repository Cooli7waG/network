import BigNumber from 'bignumber.js'

export  function hexToBytes(hex) {
    let bytes = []
    for ( let c = 0; c < hex.length; c += 2)
        bytes.push(parseInt(hex.substr(c, 2), 16));
    return bytes;
}
export function bytesToHex(bytes) {
    for (var hex = [], i = 0; i < bytes.length; i++) {
        hex.push((bytes[i] >>> 4).toString(16));
        hex.push((bytes[i] & 0xF).toString(16));
    }
    return hex.join("");
}

export function toUint8Arr(str) {
    const buffer = []
    for (let i of str) {
        const _code = i.charCodeAt(0)
        if (_code < 0x80) {
            buffer.push(_code)
        } else if (_code < 0x800) {
            buffer.push(0xc0 + (_code >> 6))
            buffer.push(0x80 + (_code & 0x3f))
        } else if (_code < 0x10000) {
            buffer.push(0xe0 + (_code >> 12))
            buffer.push(0x80 + (_code >> 6 & 0x3f))
            buffer.push(0x80 + (_code & 0x3f))
        }
    }
    return Uint8Array.from(buffer)
}

export function toEther(value,decimalPlaces) {
    let bigNumberValue = new BigNumber(value)
    let newBigNumber=bigNumberValue.div(1000000000000000000).toFixed(decimalPlaces)
    return numberToCurrencyNo(newBigNumber)
}

export function numberToCurrencyNo(value) {
    if (!value) return 0
    const intPart = Math.trunc(value)
    const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
    let floatPart = ''
    const valueArray = value.toString().split('.')
    if (valueArray.length === 2) { // 有小数部分
        floatPart = valueArray[1].toString() // 取得小数部分
        return intPartFormat + '.' + floatPart
    }
    return intPartFormat + floatPart
}
