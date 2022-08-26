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
    console.log("personalSign message:"+message)
    const msg = `0x${Buffer.from(message, 'utf8').toString('hex')}`;
    const sign = await ethereum.request({
        method: 'personal_sign',
        params: [msg, getMetaMaskLoginUserAddress(), ''],
    });
    console.log("personalSign result:" + sign);
    return sign;
}
