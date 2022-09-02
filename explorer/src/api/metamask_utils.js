import Buffer from "vue-buffer";

export function getMetaMaskLoginUserAddress() {
    return  window.localStorage.getItem('MateMaskAddress')
}

export function saveMetaMaskUserAddress(userAddress) {
    window.localStorage.setItem('MateMaskAddress', userAddress);
}

export function removeMetaMaskUserAddress() {
    window.localStorage.removeItem('MateMaskAddress')
}

export async function loginWithMetaMask() {
    const newAccounts = await ethereum.request({
        method: 'eth_requestAccounts',
    });
    saveMetaMaskUserAddress(newAccounts[0]);
    return newAccounts[0];
}

export async function personalSign(message) {
    //console.log("personalSign message:"+message)
    const msg = `0x${Buffer.from(message, 'utf8').toString('hex')}`;
    //console.log("msg:"+msg)
    let address = getMetaMaskLoginUserAddress();
    //console.log("personalSign address:"+address)
    const sign = await ethereum.request({
        method: 'personal_sign',
        params: [msg,address, ''],
    });
    //console.log("personalSign result:" + sign);
    return sign;
}

export async function personalEcRecover(message,sign) {
    const ecRecoverAddress = await ethereum.request({
        method: 'personal_ecRecover',
        params: [message, sign],
    });
    return ecRecoverAddress;
}

export async function ddd(){
    const chainId = parseInt("0x89",16);
    const msgParams = {
        types: {
            EIP712Domain: [
                { name: 'name', type: 'string' },
                { name: 'version', type: 'string' },
                { name: 'chainId', type: 'uint256' },
                { name: 'verifyingContract', type: 'address' },
            ],
            Person: [
                { name: 'name', type: 'string' },
                { name: 'wallet', type: 'address' },
            ],
            Mail: [
                { name: 'from', type: 'Person' },
                { name: 'to', type: 'Person' },
                { name: 'contents', type: 'string' },
            ],
        },
        primaryType: 'Mail',
        domain: {
            name: 'Ether Mail',
            version: '1',
            chainId,
            verifyingContract: '0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC',
        },
        message: {
            from: {
                name: 'Cow',
                wallet: '0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826',
            },
            to: {
                name: 'Bob',
                wallet: '0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB',
            },
            contents: 'Hello, Bob!',
        },
    };
    try {
        const from = getMetaMaskLoginUserAddress();
        const sign = await ethereum.request({
            method: 'eth_signTypedData_v3',
            params: [from, JSON.stringify(msgParams)],
        });
        return sign;
    } catch (err) {
        console.error(err);
        return null;
    }
}
