import {getMetaMaskLoginUserAddress} from "@/api/metamask_utils";
import {erc20Abi,ArkreenRewardAbi,ArkreenNTFAbi} from "@/api/contract.json";
import {ethers} from 'ethers';

// eslint-disable-next-line no-undef
const provider = new ethers.providers.Web3Provider(ethereum)
//提现合约地址
const withdrawContractAddress = '0x139C33cA681b57aC53A288F16726a558E912f6d9'
//Erc20合约地址
const tokenContractAddress = '0xf2D4C9C2A9018F398b229D812871bf2B316D50E1'
//NTF合约地址
const ntfContractAddress = '0xe980d0b94D62372c63821Acf7436Ae124276dd8f'

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
export async function etherWithdraw(value,nonce,v,r,s){
    const arkreenContract = new ethers.Contract(withdrawContractAddress, ArkreenRewardAbi.withdraw, provider.getSigner());
    const address = getMetaMaskLoginUserAddress();
    let result = await arkreenContract.withdraw(address,value,nonce,v,r,s,{gasPrice: '0x9af8da43', gasLimit: '0x186a0'})
    return result.hash;
}

/**
 * 获取账户最新nonce
 * @returns nonces
 */
export async function etherNonces(){
    const arkreenContract = new ethers.Contract(withdrawContractAddress, ArkreenRewardAbi.nonces, provider);
    return await arkreenContract.nonces(getMetaMaskLoginUserAddress())
}

/**
 * 查询交易状态
 * @param hash
 * @returns status
 */
export async function getTransactionStatus(hash){
    let receipt = await provider.waitForTransaction(hash);
    return receipt.status;
}

/**
 * 查询交易详情
 * @param hash
 * @returns TransactionReceipt
 */
export async function getTransactionReceipt(hash){
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
export async function etherGameMinerOnboard(owner,miner,bAirDrop,deadline,sigRegister){
    const arkreenContract = new ethers.Contract(ntfContractAddress, ArkreenNTFAbi.GameMinerOnboard, provider.getSigner());
    const address = getMetaMaskLoginUserAddress();
    let result = await arkreenContract.GameMinerOnboard(address,miner,bAirDrop,deadline,sigRegister,{gasPrice: '0x9af8da43', gasLimit: '0x186a0'})
    return result.hash;
}

