import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";
import {erc20Abi, ArkreenRewardAbi, ArkreenNTFAbi} from "@/api/contract.json";
import {ethers} from 'ethers';

let provider = null;
let withdrawContractAddress = null;
export let tokenContractAddress = null;
let ntfContractAddress = null;

if (window.ethereum) {
    // eslint-disable-next-line no-undef
    provider = new ethers.providers.Web3Provider(window.ethereum)
    //提现合约地址
    withdrawContractAddress = '0x26EF44535eE3F4eC77C9Bb3eDEdBD23563AA445a'
    //Erc20合约地址
    tokenContractAddress = '0x58a9EDa0b47d81f5151f7B28d91462C323345f7B'
    //NTF合约地址
    ntfContractAddress = '0xc4f795514586275c799729af5ae7113bdb7ccc86'
}


/**
 * 查询原生币余额
 * @returns {Promise<void>}
 */
export async function getBalance() {
    let balance = await provider.getBalance(getMetaMaskLoginUserAddress());
    console.log("provider balance:" + balance);
}

/**
 * 查询代币余额
 * @returns balance
 */
export async function balanceOf() {
    const tokenContract = new ethers.Contract(tokenContractAddress, erc20Abi.balanceOf, provider);
    return tokenContract.balanceOf(getMetaMaskLoginUserAddress());
}

/**
 * 用户提现
 * @param value
 * @param nonce
 * @param v
 * @param r
 * @param s
 * @returns txhash
 */
export async function etherWithdraw(value, nonce, v, r, s) {
    console.log("withdrawContractAddress:" + withdrawContractAddress)
    const arkreenContract = new ethers.Contract(withdrawContractAddress, ArkreenRewardAbi.withdraw, provider.getSigner());
    const address = getMetaMaskLoginUserAddress();
    let result = await arkreenContract.withdraw(address, value, nonce, v, r, s)
    return result.hash;
}

/**
 * 获取账户最新nonce
 * @returns nonces
 */
export async function etherNonces() {
    const arkreenContract = new ethers.Contract(withdrawContractAddress, ArkreenRewardAbi.nonces, provider);
    return await arkreenContract.nonces(getMetaMaskLoginUserAddress())
}

/**
 * 查询交易状态
 * @param hash
 * @returns status
 */
export async function getTransactionStatus(hash) {
    let receipt = await provider.waitForTransaction(hash);
    return receipt.status;
}

/**
 * 查询交易详情
 * @param hash
 * @returns TransactionReceipt
 */
export async function getTransactionReceipt(hash) {
    return await provider.waitForTransaction(hash);
}

/**
 * NFT铸造
 * @param owner
 * @param miner
 * @param bAirDrop
 * @param deadline
 * @param sigRegister
 * @returns {Promise<*>}
 * @constructor
 */
export async function etherGameMinerOnboard(owner, miner, bAirDrop, deadline, sigRegister) {
    const arkreenContract = new ethers.Contract(ntfContractAddress, ArkreenNTFAbi.GameMinerOnboard, provider.getSigner());
    let result = await arkreenContract.GameMinerOnboard(owner, miner, bAirDrop, deadline, sigRegister)
    return result.hash;
}

export async function getMinerTokenID(miner) {
    const arkreenContract = new ethers.Contract(ntfContractAddress, ArkreenNTFAbi.getMinerTokenID, provider);
    let result = await arkreenContract.getMinerTokenID(getMetaMaskLoginUserAddress(), miner)
    if(result == 115792089237316195423570985008687907853269984665640564039457584007913129639935){
        return undefined;
    }
    let nftInfo = {
        tokenId:result,
        url:"https://testnets.opensea.io/assets/mumbai/"+ntfContractAddress+"/"+result
    }
    return nftInfo;
}

export async function getNftBalanceOf() {
    const arkreenContract = new ethers.Contract(ntfContractAddress, ArkreenNTFAbi.balanceOf, provider);
    let result = await arkreenContract.balanceOf(getMetaMaskLoginUserAddress())
    return result.toString();
}

//let gas = {gasPrice: '0x9af8da43', gasLimit: '0x186a0'}

