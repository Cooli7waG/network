import Buffer from "vue-buffer";
import {tokenContractAddress} from "@/api/contract_utils";

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
    // eslint-disable-next-line no-undef
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
    // eslint-disable-next-line no-undef
    const sign = await ethereum.request({
        method: 'personal_sign',
        params: [msg,address, ''],
    });
    //console.log("personalSign result:" + sign);
    return sign;
}

export async function ethSign(message){
    try {
        // eslint-disable-next-line no-undef
        const ethResult = await ethereum.request({
            method: 'eth_sign',
            params: [getMetaMaskLoginUserAddress(), message],
        });
        return ethResult;
    } catch (err) {
        console.error(err);
        return null;
    }
}

export async function personalEcRecover(message,sign) {
    // eslint-disable-next-line no-undef
    const ecRecoverAddress = await ethereum.request({
        method: 'personal_ecRecover',
        params: [message, sign],
    });
    return ecRecoverAddress;
}

export async function getNetworkAndChainId() {
    try {
        // eslint-disable-next-line no-undef
        const chainId = await ethereum.request({
            method: 'eth_chainId',
        });
        // eslint-disable-next-line no-undef
        const networkId = await ethereum.request({
            method: 'net_version',
        });
        const chainInfo = {
            "chainId":chainId,
            "networkId":networkId
        }
        return chainInfo;
    } catch (err) {
        console.error(err);
        return undefined;
    }
}

export async function signTypedDataV3(){
    let chainInfo = await getNetworkAndChainId();
    const chainId = parseInt(chainInfo.chainId, 10);
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
        // eslint-disable-next-line no-undef
        const sign = await ethereum.request({
            method: 'eth_signTypedData_v3',
            params: [from, JSON.stringify(msgParams)],
        });
        return sign.toString();
    } catch (err) {
        console.error(err);
        return null;
    }
}

export async function signTypedDataV4(){
    let chainInfo = await getNetworkAndChainId();
    const networkId = parseInt(chainInfo.networkId, 10);
    const chainId = parseInt(chainInfo.chainId, 16) || networkId;
    const msgParams = {
        domain: {
            chainId: chainId.toString(),
            name: 'Ether Mail',
            verifyingContract: '0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC',
            version: '1',
        },
        message: {
            contents: 'Hello, Bob!',
            from: {
                name: 'Cow',
                wallets: [
                    '0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826',
                    '0xDeaDbeefdEAdbeefdEadbEEFdeadbeEFdEaDbeeF',
                ],
            },
            to: [
                {
                    name: 'Bob',
                    wallets: [
                        '0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB',
                        '0xB0BdaBea57B0BDABeA57b0bdABEA57b0BDabEa57',
                        '0xB0B0b0b0b0b0B000000000000000000000000000',
                    ],
                },
            ],
        },
        primaryType: 'Mail',
        types: {
            EIP712Domain: [
                { name: 'name', type: 'string' },
                { name: 'version', type: 'string' },
                { name: 'chainId', type: 'uint256' },
                { name: 'verifyingContract', type: 'address' },
            ],
            Group: [
                { name: 'name', type: 'string' },
                { name: 'members', type: 'Person[]' },
            ],
            Mail: [
                { name: 'from', type: 'Person' },
                { name: 'to', type: 'Person[]' },
                { name: 'contents', type: 'string' },
            ],
            Person: [
                { name: 'name', type: 'string' },
                { name: 'wallets', type: 'address[]' },
            ],
        },
    };
    try {
        const from = getMetaMaskLoginUserAddress();
        // eslint-disable-next-line no-undef
        const sign = await ethereum.request({
            method: 'eth_signTypedData_v4',
            params: [from, JSON.stringify(msgParams)],
        });
        return sign.toString();
    } catch (err) {
        console.error(err);
        return null;
    }
}

export async function addNetwork(chainCfg){
    // eslint-disable-next-line no-undef
    const result = await ethereum.request({
        method: 'wallet_addEthereumChain', // Metamask的api名称
        params: [chainCfg]
    })
    console.info("addNetwork result:"+JSON.stringify(result))
}

export async function switchNetwork(cid){
    try {
        //判断当前网络节点
        const chainInfo = await getNetworkAndChainId();
        if(chainInfo.chainId == cid){
            return true;
        }
        console.info("当前网络["+chainInfo.chainId+"]不一致：需要切换！");
        // eslint-disable-next-line no-undef
        await ethereum.request({
            method: "wallet_switchEthereumChain",
            params: [{ chainId: cid }],
        });
        return true;
    }catch (e){
        const err = e;
        console.log(err);
        if (err.code === 4902) {
            try {// 添加
                await addNetwork(ChainCfg["80001"])
                return true;
            } catch (addError) {
                console.error(addError);
                return false
            }
        } else {
            console.error("switch metamask network failed:"+err.message)
            return false
        }
    }
}

export async function addToken(){
    // eslint-disable-next-line no-undef
    const result = await ethereum.request({
        method: 'wallet_watchAsset',
        params: {
            type: 'ERC20',
            options: {
                address:TokenInfo.address,
                symbol: TokenInfo.tokenSymbol,
                decimals: TokenInfo.decimalUnits
            },
        },
    });
    return result
}

export function isPrivacyPolicy(){
    let isPrivacyPolicy = window.localStorage.getItem('isPrivacyPolicy')
    return isPrivacyPolicy == "true"?true:false;
}

export function setPrivacyPolicy(flag){
    window.localStorage.setItem('isPrivacyPolicy', flag?"true":"false");
}

export function isCheckBrowser(){
    let isCheckBrowser = window.localStorage.getItem('isCheckBrowser')
    return isCheckBrowser == "true"?true:false;
}

export function setCheckBrowser(flag){
    window.localStorage.setItem('isCheckBrowser', flag?"true":"false");
}

/**
 * Token 信息
 * @type {{image: string, address: number, tokenSymbol: string, decimalUnits: number}}
 */
const TokenInfo = {
    address : tokenContractAddress,
    decimalUnits : 18,
    tokenSymbol : 'AKRE'
}

/**
 * 网络信息
 * @type {{"1": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "56": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "3": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "137": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "1088": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "2088": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "42": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}, "97": {chainName: string, blockExplorerUrls: string[], chainId: string, nativeCurrency: {symbol: string, decimals: number, name: string}, rpcUrls: string[]}}}
 */
const ChainCfg = {
    1: {
        chainId: '0x1',
        chainName: 'Ethereum Mainnet',
        nativeCurrency: {
            name: 'ETH',
            symbol: 'ETH',
            decimals: 18,
        },
        rpcUrls: ['https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161'],
        blockExplorerUrls: ['https://etherscan.io'],
    },
    3: {
        chainId: '0x3',
        chainName: 'Ropsten testNet',
        nativeCurrency: {
            name: 'ETH',
            symbol: 'ETH',
            decimals: 18,
        },
        rpcUrls: ['https://ropsten.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161'],
        blockExplorerUrls: ['https://ropsten.etherscan.io'],
    },
    42: {
        chainId: '0x2a',
        chainName: 'Kovan testNet',
        nativeCurrency: {
            name: 'ETH',
            symbol: 'ETH',
            decimals: 18,
        },
        rpcUrls: ['https://kovan.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161'],
        blockExplorerUrls: ['https://kovan.etherscan.io'],
    },
    56: {
        chainId: '0x38',
        chainName: 'Binance Smart Chain',
        nativeCurrency: {
            name: 'BNB',
            symbol: 'BNB',
            decimals: 18,
        },
        rpcUrls: ['https://bsc-dataseed.binance.org/'],
        blockExplorerUrls: ['https://bscscan.com/'],
    },
    97: {
        chainId: '0x61',
        chainName: 'Binance Smart Chain - TestNet',
        nativeCurrency: {
            name: 'BNB',
            symbol: 'BNB',
            decimals: 18,
        },
        rpcUrls: ['https://data-seed-prebsc-1-s1.binance.org:8545/'],
        blockExplorerUrls: ['https://testnet.bscscan.com/'],
    },
    1088: {
        chainId: '0x440',
        chainName: 'Maas - TestNet',
        nativeCurrency: {
            name: 'Maas',
            symbol: 'Maas',
            decimals: 18,
        },
        rpcUrls: ['https://maas-test-node.onchain.com/'],
        blockExplorerUrls: ['https://maas-test-explorer.onchain.com/'],
    },
    2088: {
        chainId: '0x828',
        chainName: 'Maas',
        nativeCurrency: {
            name: 'Maas',
            symbol: 'Maas',
            decimals: 18,
        },
        rpcUrls: ['https://maas-node.onchain.com/'],
        blockExplorerUrls: ['https://maas-explorer.onchain.com/'],
    },
    137: {
        chainId: '0x89',
        chainName: 'Polygon Mainnet',
        nativeCurrency: {
            name: 'MATIC',
            symbol: 'MATIC',
            decimals: 18,
        },
        rpcUrls: ['https://polygon-rpc.com/'],
        blockExplorerUrls: ['https://polygonscan.com/'],
    },
    80001: {
        chainId: '0x13881',
            chainName: 'Matic Mumbai',
            nativeCurrency: {
            name: 'MATIC',
                symbol: 'MATIC',
                decimals: 18,
        },
        rpcUrls: ['https://rpc-mumbai.maticvigil.com/'],
        blockExplorerUrls: ['https://mumbai.polygonscan.com/'],
    }
};
