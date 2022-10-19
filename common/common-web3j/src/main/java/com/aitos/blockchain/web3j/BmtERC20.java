package com.aitos.blockchain.web3j;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class BmtERC20 extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b50604051620017fb380380620017fb8339818101604052810190620000379190620000eb565b6012600a620000479190620002ad565b81620000549190620002fe565b60038190555033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600354600681905550506200035f565b600080fd5b6000819050919050565b620000c581620000b0565b8114620000d157600080fd5b50565b600081519050620000e581620000ba565b92915050565b600060208284031215620001045762000103620000ab565b5b60006200011484828501620000d4565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60008160011c9050919050565b6000808291508390505b6001851115620001ab578086048111156200018357620001826200011d565b5b6001851615620001935780820291505b8081029050620001a3856200014c565b945062000163565b94509492505050565b600082620001c6576001905062000299565b81620001d6576000905062000299565b8160018114620001ef5760028114620001fa5762000230565b600191505062000299565b60ff8411156200020f576200020e6200011d565b5b8360020a9150848211156200022957620002286200011d565b5b5062000299565b5060208310610133831016604e8410600b84101617156200026a5782820a9050838111156200026457620002636200011d565b5b62000299565b62000279848484600162000159565b925090508184048111156200029357620002926200011d565b5b81810290505b9392505050565b600060ff82169050919050565b6000620002ba82620000b0565b9150620002c783620002a0565b9250620002f67fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8484620001b4565b905092915050565b60006200030b82620000b0565b91506200031883620000b0565b9250817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff04831182151516156200035457620003536200011d565b5b828202905092915050565b61148c806200036f6000396000f3fe608060405234801561001057600080fd5b50600436106100f55760003560e01c8063459d2e2111610097578063ad166aa011610066578063ad166aa014610266578063ae1f566214610282578063b2bdfa7b146102a0578063f8e4dce1146102be576100f5565b8063459d2e21146101de57806346996ce5146101fc57806386299b0e1461021a5780638af1e0a714610236576100f5565b80632a068f57116100d35780632a068f571461015457806332424aa31461017257806335ee5f87146101905780633eaaf86b146101c0576100f5565b806315e08202146100fa57806318160ddd14610118578063280cb26314610136575b600080fd5b6101026102da565b60405161010f9190610a6d565b60405180910390f35b610120610300565b60405161012d9190610aa1565b60405180910390f35b61013e61030a565b60405161014b9190610aa1565b60405180910390f35b61015c610310565b6040516101699190610aa1565b60405180910390f35b61017a610316565b6040516101879190610ad8565b60405180910390f35b6101aa60048036038101906101a59190610c4d565b61031b565b6040516101b79190610aa1565b60405180910390f35b6101c861038d565b6040516101d59190610aa1565b60405180910390f35b6101e6610393565b6040516101f39190610aa1565b60405180910390f35b610204610399565b6040516102119190610aa1565b60405180910390f35b610234600480360381019061022f9190610e6b565b6103a3565b005b610250600480360381019061024b9190610ee3565b6105ca565b60405161025d9190610fea565b60405180910390f35b610280600480360381019061027b9190611038565b61070e565b005b61028a610874565b6040516102979190610aa1565b60405180910390f35b6102a861088b565b6040516102b59190610a6d565b60405180910390f35b6102d860048036038101906102d39190611078565b6108b1565b005b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600354905090565b60045481565b60055481565b601281565b6000610328826040610a08565b610367576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161035e90611131565b60405180910390fd5b60008260405161037791906111cb565b9081526020016040518091039020549050919050565b60035481565b60065481565b6000600554905090565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610433576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161042a9061122e565b60405180910390fd5b805182511461044157600080fd5b60148251111561045057600080fd5b60008060005b845181101561059b576104848582815181106104755761047461124e565b5b60200260200101516040610a08565b6104c3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104ba906112c9565b60405180910390fd5b60008582815181106104d8576104d761124e565b5b60200260200101516040516104ed91906111cb565b90815260200160405180910390205492508381815181106105115761051061124e565b5b6020026020010151826105249190611318565b91508381815181106105395761053861124e565b5b60200260200101518361054c9190611318565b60008683815181106105615761056061124e565b5b602002602001015160405161057691906111cb565b90815260200160405180910390208190555080806105939061136e565b915050610456565b506005548111156105ab57600080fd5b80600560008282546105bd91906113b6565b9250508190555050505050565b60606014825111156105db57600080fd5b6000825167ffffffffffffffff8111156105f8576105f7610b22565b5b6040519080825280602002602001820160405280156106265781602001602082028036833780820191505090505b50905060005b83518110156107045761065a84828151811061064b5761064a61124e565b5b60200260200101516040610a08565b610699576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161069090611131565b60405180910390fd5b60008482815181106106ae576106ad61124e565b5b60200260200101516040516106c391906111cb565b9081526020016040518091039020548282815181106106e5576106e461124e565b5b60200260200101818152505080806106fc9061136e565b91505061062c565b5080915050919050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461079e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161079590611436565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff16600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146107f957600080fd5b60065481111561080857600080fd5b81600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508060048190555080600581905550806006600082825461086991906113b6565b925050819055505050565b600060055460045461088691906113b6565b905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610941576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016109389061122e565b60405180910390fd5b60055481111561095057600080fd5b61095b826040610a08565b61099a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610991906112c9565b60405180910390fd5b600080836040516109ab91906111cb565b908152602001604051809103902054905081600560008282546109ce91906113b6565b9250508190555081816109e19190611318565b6000846040516109f191906111cb565b908152602001604051809103902081905550505050565b60008083905082815103610a20576001915050610a26565b60009150505b92915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610a5782610a2c565b9050919050565b610a6781610a4c565b82525050565b6000602082019050610a826000830184610a5e565b92915050565b6000819050919050565b610a9b81610a88565b82525050565b6000602082019050610ab66000830184610a92565b92915050565b600060ff82169050919050565b610ad281610abc565b82525050565b6000602082019050610aed6000830184610ac9565b92915050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610b5a82610b11565b810181811067ffffffffffffffff82111715610b7957610b78610b22565b5b80604052505050565b6000610b8c610af3565b9050610b988282610b51565b919050565b600067ffffffffffffffff821115610bb857610bb7610b22565b5b610bc182610b11565b9050602081019050919050565b82818337600083830152505050565b6000610bf0610beb84610b9d565b610b82565b905082815260208101848484011115610c0c57610c0b610b0c565b5b610c17848285610bce565b509392505050565b600082601f830112610c3457610c33610b07565b5b8135610c44848260208601610bdd565b91505092915050565b600060208284031215610c6357610c62610afd565b5b600082013567ffffffffffffffff811115610c8157610c80610b02565b5b610c8d84828501610c1f565b91505092915050565b600067ffffffffffffffff821115610cb157610cb0610b22565b5b602082029050602081019050919050565b600080fd5b6000610cda610cd584610c96565b610b82565b90508083825260208201905060208402830185811115610cfd57610cfc610cc2565b5b835b81811015610d4457803567ffffffffffffffff811115610d2257610d21610b07565b5b808601610d2f8982610c1f565b85526020850194505050602081019050610cff565b5050509392505050565b600082601f830112610d6357610d62610b07565b5b8135610d73848260208601610cc7565b91505092915050565b600067ffffffffffffffff821115610d9757610d96610b22565b5b602082029050602081019050919050565b610db181610a88565b8114610dbc57600080fd5b50565b600081359050610dce81610da8565b92915050565b6000610de7610de284610d7c565b610b82565b90508083825260208201905060208402830185811115610e0a57610e09610cc2565b5b835b81811015610e335780610e1f8882610dbf565b845260208401935050602081019050610e0c565b5050509392505050565b600082601f830112610e5257610e51610b07565b5b8135610e62848260208601610dd4565b91505092915050565b60008060408385031215610e8257610e81610afd565b5b600083013567ffffffffffffffff811115610ea057610e9f610b02565b5b610eac85828601610d4e565b925050602083013567ffffffffffffffff811115610ecd57610ecc610b02565b5b610ed985828601610e3d565b9150509250929050565b600060208284031215610ef957610ef8610afd565b5b600082013567ffffffffffffffff811115610f1757610f16610b02565b5b610f2384828501610d4e565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b610f6181610a88565b82525050565b6000610f738383610f58565b60208301905092915050565b6000602082019050919050565b6000610f9782610f2c565b610fa18185610f37565b9350610fac83610f48565b8060005b83811015610fdd578151610fc48882610f67565b9750610fcf83610f7f565b925050600181019050610fb0565b5085935050505092915050565b600060208201905081810360008301526110048184610f8c565b905092915050565b61101581610a4c565b811461102057600080fd5b50565b6000813590506110328161100c565b92915050565b6000806040838503121561104f5761104e610afd565b5b600061105d85828601611023565b925050602061106e85828601610dbf565b9150509250929050565b6000806040838503121561108f5761108e610afd565b5b600083013567ffffffffffffffff8111156110ad576110ac610b02565b5b6110b985828601610c1f565b92505060206110ca85828601610dbf565b9150509250929050565b600082825260208201905092915050565b7f6163636f756e742061646472657373206c656e677468206572726f7221000000600082015250565b600061111b601d836110d4565b9150611126826110e5565b602082019050919050565b6000602082019050818103600083015261114a8161110e565b9050919050565b600081519050919050565b600081905092915050565b60005b8381101561118557808201518184015260208101905061116a565b83811115611194576000848401525b50505050565b60006111a582611151565b6111af818561115c565b93506111bf818560208601611167565b80840191505092915050565b60006111d7828461119a565b915081905092915050565b7f6e6f6c7920666f756e646174696f6e7320636f756c6420646f20746869732100600082015250565b6000611218601f836110d4565b9150611223826111e2565b602082019050919050565b600060208201905081810360008301526112478161120b565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f6d696e65722061646472657373206c656e677468206572726f72210000000000600082015250565b60006112b3601b836110d4565b91506112be8261127d565b602082019050919050565b600060208201905081810360008301526112e2816112a6565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600061132382610a88565b915061132e83610a88565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff03821115611363576113626112e9565b5b828201905092915050565b600061137982610a88565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036113ab576113aa6112e9565b5b600182019050919050565b60006113c182610a88565b91506113cc83610a88565b9250828210156113df576113de6112e9565b5b828203905092915050565b7f6f6e6c79206f776e657220636f756c6420646f20746869732100000000000000600082015250565b60006114206019836110d4565b915061142b826113ea565b602082019050919050565b6000602082019050818103600083015261144f81611413565b905091905056fea2646970667358221220354551ec4053181c26313c00b8f1d6999964ed6736e13b8e0b4778db672f52bc64736f6c634300080d0033";

    public static final String FUNC__DECIMALS = "_decimals";

    public static final String FUNC__FOUNDATIONS = "_foundations";

    public static final String FUNC__FOUNDATIONS_LEFT = "_foundations_left";

    public static final String FUNC__INIT_FOUNDATIONS_SUPPLY = "_init_foundations_supply";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC__OWNER_LEFT = "_owner_left";

    public static final String FUNC__TOTALSUPPLY = "_totalSupply";

    public static final String FUNC_ALREADYREWARD = "alreadyReward";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BALANCEOF_MULTI = "balanceOf_multi";

    public static final String FUNC_FOUNDATIONSBALANCELEFT = "foundationsBalanceLeft";

    public static final String FUNC_INITFOUNDATIONS = "initFoundations";

    public static final String FUNC_REWARDMINER = "rewardMiner";

    public static final String FUNC_REWARDMINER_MULTI = "rewardMiner_multi";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    @Deprecated
    protected BmtERC20(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BmtERC20(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BmtERC20(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BmtERC20(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> _decimals() {
        final Function function = new Function(FUNC__DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> _foundations() {
        final Function function = new Function(FUNC__FOUNDATIONS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> _foundations_left() {
        final Function function = new Function(FUNC__FOUNDATIONS_LEFT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> _init_foundations_supply() {
        final Function function = new Function(FUNC__INIT_FOUNDATIONS_SUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> _owner() {
        final Function function = new Function(FUNC__OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> _owner_left() {
        final Function function = new Function(FUNC__OWNER_LEFT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> _totalSupply() {
        final Function function = new Function(FUNC__TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> alreadyReward() {
        final Function function = new Function(FUNC_ALREADYREWARD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> balanceOf_multi(List<String> accounts) {
        final Function function = new Function(FUNC_BALANCEOF_MULTI, 
                Arrays.<Type>asList(new DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(accounts, org.web3j.abi.datatypes.Utf8String.class))), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> foundationsBalanceLeft() {
        final Function function = new Function(FUNC_FOUNDATIONSBALANCELEFT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> initFoundations(String foundations, BigInteger value) {
        final Function function = new Function(
                FUNC_INITFOUNDATIONS, 
                Arrays.<Type>asList(new Address(160, foundations),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> rewardMiner(String miner, BigInteger value) {
        final Function function = new Function(
                FUNC_REWARDMINER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(miner), 
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> rewardMiner_multi(List<String> miners, List<BigInteger> values) {
        final Function function = new Function(
                FUNC_REWARDMINER_MULTI, 
                Arrays.<Type>asList(new DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(miners, org.web3j.abi.datatypes.Utf8String.class)), 
                new DynamicArray<Uint256>(
                        Uint256.class,
                        org.web3j.abi.Utils.typeMap(values, Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static BmtERC20 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BmtERC20(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BmtERC20 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BmtERC20(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BmtERC20 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BmtERC20(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BmtERC20 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BmtERC20(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BmtERC20> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(totalSupply_)));
        return deployRemoteCall(BmtERC20.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<BmtERC20> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(totalSupply_)));
        return deployRemoteCall(BmtERC20.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BmtERC20> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(totalSupply_)));
        return deployRemoteCall(BmtERC20.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BmtERC20> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(totalSupply_)));
        return deployRemoteCall(BmtERC20.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
