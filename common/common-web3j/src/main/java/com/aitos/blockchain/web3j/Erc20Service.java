package com.aitos.blockchain.web3j;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
 * <p>Generated with web3j version 4.9.4.
 */
@SuppressWarnings("rawtypes")
public class Erc20Service extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b506040516200177b3803806200177b833981016040819052620000349162000220565b825162000049906003906020860190620000ad565b5081516200005f906004906020850190620000ad565b506200006e6012600a620003a8565b6200007a9082620003c0565b6002819055600580546001600160a01b03191633908117909155600090815260208190526040902055506200041f915050565b828054620000bb90620003e2565b90600052602060002090601f016020900481019282620000df57600085556200012a565b82601f10620000fa57805160ff19168380011785556200012a565b828001600101855582156200012a579182015b828111156200012a5782518255916020019190600101906200010d565b50620001389291506200013c565b5090565b5b808211156200013857600081556001016200013d565b634e487b7160e01b600052604160045260246000fd5b600082601f8301126200017b57600080fd5b81516001600160401b038082111562000198576200019862000153565b604051601f8301601f19908116603f01168101908282118183101715620001c357620001c362000153565b81604052838152602092508683858801011115620001e057600080fd5b600091505b83821015620002045785820183015181830184015290820190620001e5565b83821115620002165760008385830101525b9695505050505050565b6000806000606084860312156200023657600080fd5b83516001600160401b03808211156200024e57600080fd5b6200025c8783880162000169565b945060208601519150808211156200027357600080fd5b50620002828682870162000169565b925050604084015190509250925092565b634e487b7160e01b600052601160045260246000fd5b600181815b80851115620002ea578160001904821115620002ce57620002ce62000293565b80851615620002dc57918102915b93841c9390800290620002ae565b509250929050565b6000826200030357506001620003a2565b816200031257506000620003a2565b81600181146200032b5760028114620003365762000356565b6001915050620003a2565b60ff8411156200034a576200034a62000293565b50506001821b620003a2565b5060208310610133831016604e8410600b84101617156200037b575081810a620003a2565b620003878383620002a9565b80600019048211156200039e576200039e62000293565b0290505b92915050565b6000620003b960ff841683620002f2565b9392505050565b6000816000190483118215151615620003dd57620003dd62000293565b500290565b600181811c90821680620003f757607f821691505b602082108114156200041957634e487b7160e01b600052602260045260246000fd5b50919050565b61134c806200042f6000396000f3fe608060405234801561001057600080fd5b506004361061012c5760003560e01c8063829e617f116100ad578063ae1f566211610071578063ae1f56621461028c578063b2bdfa7b14610294578063bb21852e146102a7578063dd62ed3e146102ba578063f5e86ed6146102cd57600080fd5b8063829e617f1461023657806395d89b411461024b578063a457c2d714610253578063a9059cbb14610266578063ad166aa01461027957600080fd5b806323b872dd116100f457806323b872dd146101cf5780632e07925c146101e2578063313ce567146101eb57806339509351146101fa57806370a082311461020d57600080fd5b806306fdde0314610131578063095ea7b31461014f57806315e082021461017257806318160ddd1461019d57806323768416146101af575b600080fd5b6101396102e0565b6040516101469190610ddc565b60405180910390f35b61016261015d366004610e48565b610372565b6040519015158152602001610146565b600654610185906001600160a01b031681565b6040516001600160a01b039091168152602001610146565b6002545b604051908152602001610146565b6101c26101bd366004610f4f565b61038c565b6040516101469190610f8c565b6101626101dd366004610fd0565b61048c565b6101a160075481565b60405160128152602001610146565b610162610208366004610e48565b6104b0565b6101a161021b36600461100c565b6001600160a01b031660009081526020819052604090205490565b61024961024436600461102e565b6104d2565b005b6101396105bc565b610162610261366004610e48565b6105cb565b610162610274366004610e48565b610646565b610249610287366004610e48565b610654565b6101a16107bc565b600554610185906001600160a01b031681565b6102496102b536600461100c565b610872565b6101a16102c83660046110e9565b6109bf565b6102496102db366004610e48565b6109ea565b6060600380546102ef9061111c565b80601f016020809104026020016040519081016040528092919081815260200182805461031b9061111c565b80156103685780601f1061033d57610100808354040283529160200191610368565b820191906000526020600020905b81548152906001019060200180831161034b57829003601f168201915b5050505050905090565b600033610380818585610a99565b60019150505b92915050565b6006546060906001600160a01b031633146103c25760405162461bcd60e51b81526004016103b990611157565b60405180910390fd5b6000825167ffffffffffffffff8111156103de576103de610e72565b604051908082528060200260200182016040528015610407578160200160208202803683370190505b50905060005b83518110156104835760008085838151811061042b5761042b61118e565b60200260200101516001600160a01b03166001600160a01b03168152602001908152602001600020548282815181106104665761046661118e565b60209081029190910101528061047b816111ba565b91505061040d565b5090505b919050565b60003361049a858285610bbd565b6104a5858585610c37565b506001949350505050565b6000336103808185856104c383836109bf565b6104cd91906111d5565b610a99565b6006546001600160a01b031633146104fc5760405162461bcd60e51b81526004016103b990611157565b805182511461054d5760405162461bcd60e51b815260206004820152601c60248201527f696e707574206172726179206c656e677468206e6f7420657175616c0000000060448201526064016103b9565b60005b82518110156105b75760065483516105a5916001600160a01b03169085908490811061057e5761057e61118e565b60200260200101518484815181106105985761059861118e565b6020026020010151610c37565b806105af816111ba565b915050610550565b505050565b6060600480546102ef9061111c565b600033816105d982866109bf565b9050838110156106395760405162461bcd60e51b815260206004820152602560248201527f45524332303a2064656372656173656420616c6c6f77616e63652062656c6f77604482015264207a65726f60d81b60648201526084016103b9565b6104a58286868403610a99565b600033610380818585610c37565b6005546001600160a01b031633146106aa5760405162461bcd60e51b81526020600482015260196024820152786f6e6c79206f776e657220636f756c6420646f20746869732160381b60448201526064016103b9565b6006546001600160a01b0316156107035760405162461bcd60e51b815260206004820152601c60248201527f666f756e646174696f6e732061646472657373206973206e6f7420300000000060448201526064016103b9565b60006107116012600a6112d1565b61071b90836112e0565b6005546001600160a01b03166000908152602081905260409020549091508111156107885760405162461bcd60e51b815260206004820152601f60248201527f71756f7461206973206c6172676572207468616e206f776e6572206c6566740060448201526064016103b9565b600680546001600160a01b0319166001600160a01b038581169190911790915560078290556005546105b791168483610c37565b6006546001600160a01b031660009081526020819052604081205460075410156108455760405162461bcd60e51b815260206004820152603460248201527f696e697469616c20737570706c7920697320736d616c6c6572207468616e2062604482015273616c616e6365206f6620666f756e646174696f7360601b60648201526084016103b9565b6006546001600160a01b031660009081526020819052604090205460075461086d91906112ff565b905090565b6005546001600160a01b031633146108c85760405162461bcd60e51b81526020600482015260196024820152786f6e6c79206f776e657220636f756c6420646f20746869732160381b60448201526064016103b9565b6001600160a01b03811661091e5760405162461bcd60e51b815260206004820152601c60248201527f6e657720666f756e646174696f6e73206164647265737320697320300000000060448201526064016103b9565b6006546001600160a01b03166109765760405162461bcd60e51b815260206004820152601c60248201527f6f6c6420666f756e646174696f6e73206164647265737320697320300000000060448201526064016103b9565b6006546001600160a01b031660008181526020819052604090205461099d91908390610c37565b600680546001600160a01b0319166001600160a01b0392909216919091179055565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b6006546001600160a01b03163314610a145760405162461bcd60e51b81526004016103b990611157565b6006546001600160a01b0316600090815260208190526040902054811115610a7e5760405162461bcd60e51b815260206004820152601c60248201527f6e6f7420656e6f75676820746f6b656e20666f7220726577617264210000000060448201526064016103b9565b600654610a95906001600160a01b03168383610c37565b5050565b6001600160a01b038316610afb5760405162461bcd60e51b8152602060048201526024808201527f45524332303a20617070726f76652066726f6d20746865207a65726f206164646044820152637265737360e01b60648201526084016103b9565b6001600160a01b038216610b5c5760405162461bcd60e51b815260206004820152602260248201527f45524332303a20617070726f766520746f20746865207a65726f206164647265604482015261737360f01b60648201526084016103b9565b6001600160a01b0383811660008181526001602090815260408083209487168084529482529182902085905590518481527f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925910160405180910390a3505050565b6000610bc984846109bf565b90506000198114610c315781811015610c245760405162461bcd60e51b815260206004820152601d60248201527f45524332303a20696e73756666696369656e7420616c6c6f77616e636500000060448201526064016103b9565b610c318484848403610a99565b50505050565b6001600160a01b038316610c9b5760405162461bcd60e51b815260206004820152602560248201527f45524332303a207472616e736665722066726f6d20746865207a65726f206164604482015264647265737360d81b60648201526084016103b9565b6001600160a01b038216610cfd5760405162461bcd60e51b815260206004820152602360248201527f45524332303a207472616e7366657220746f20746865207a65726f206164647260448201526265737360e81b60648201526084016103b9565b6001600160a01b03831660009081526020819052604090205481811015610d755760405162461bcd60e51b815260206004820152602660248201527f45524332303a207472616e7366657220616d6f756e7420657863656564732062604482015265616c616e636560d01b60648201526084016103b9565b6001600160a01b03848116600081815260208181526040808320878703905593871680835291849020805487019055925185815290927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef910160405180910390a350505050565b600060208083528351808285015260005b81811015610e0957858101830151858201604001528201610ded565b81811115610e1b576000604083870101525b50601f01601f1916929092016040019392505050565b80356001600160a01b038116811461048757600080fd5b60008060408385031215610e5b57600080fd5b610e6483610e31565b946020939093013593505050565b634e487b7160e01b600052604160045260246000fd5b604051601f8201601f1916810167ffffffffffffffff81118282101715610eb157610eb1610e72565b604052919050565b600067ffffffffffffffff821115610ed357610ed3610e72565b5060051b60200190565b600082601f830112610eee57600080fd5b81356020610f03610efe83610eb9565b610e88565b82815260059290921b84018101918181019086841115610f2257600080fd5b8286015b84811015610f4457610f3781610e31565b8352918301918301610f26565b509695505050505050565b600060208284031215610f6157600080fd5b813567ffffffffffffffff811115610f7857600080fd5b610f8484828501610edd565b949350505050565b6020808252825182820181905260009190848201906040850190845b81811015610fc457835183529284019291840191600101610fa8565b50909695505050505050565b600080600060608486031215610fe557600080fd5b610fee84610e31565b9250610ffc60208501610e31565b9150604084013590509250925092565b60006020828403121561101e57600080fd5b61102782610e31565b9392505050565b6000806040838503121561104157600080fd5b823567ffffffffffffffff8082111561105957600080fd5b61106586838701610edd565b935060209150818501358181111561107c57600080fd5b85019050601f8101861361108f57600080fd5b803561109d610efe82610eb9565b81815260059190911b820183019083810190888311156110bc57600080fd5b928401925b828410156110da578335825292840192908401906110c1565b80955050505050509250929050565b600080604083850312156110fc57600080fd5b61110583610e31565b915061111360208401610e31565b90509250929050565b600181811c9082168061113057607f821691505b6020821081141561115157634e487b7160e01b600052602260045260246000fd5b50919050565b6020808252601f908201527f6e6f6c7920666f756e646174696f6e7320636f756c6420646f20746869732100604082015260600190565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b60006000198214156111ce576111ce6111a4565b5060010190565b600082198211156111e8576111e86111a4565b500190565b600181815b8085111561122857816000190482111561120e5761120e6111a4565b8085161561121b57918102915b93841c93908002906111f2565b509250929050565b60008261123f57506001610386565b8161124c57506000610386565b8160018114611262576002811461126c57611288565b6001915050610386565b60ff84111561127d5761127d6111a4565b50506001821b610386565b5060208310610133831016604e8410600b84101617156112ab575081810a610386565b6112b583836111ed565b80600019048211156112c9576112c96111a4565b029392505050565b600061102760ff841683611230565b60008160001904831182151516156112fa576112fa6111a4565b500290565b600082821015611311576113116111a4565b50039056fea264697066735822122007b3fccdeb09f1cf49b28a051863ed5f81874d54b8b44c40f2dfe3ee9376513c64736f6c63430008090033";

    public static final String FUNC__FOUNDATIONS = "_foundations";

    public static final String FUNC__FOUNDATIONS_INITIAL_SUPPLY = "_foundations_initial_supply";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_ALREADYREWARD = "alreadyReward";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BALANCEOF_MULTI = "balanceOf_multi";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_INITFOUNDATIONS = "initFoundations";

    public static final String FUNC_MIGRATEFOUNDATIONS = "migrateFoundations";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_REWARDMINER = "rewardMiner";

    public static final String FUNC_REWARDMINER_MULTI = "rewardMiner_multi";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Erc20Service(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Erc20Service(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Erc20Service(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Erc20Service(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public static List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<String> _foundations() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__FOUNDATIONS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> _foundations_initial_supply() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__FOUNDATIONS_INITIAL_SUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> _owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner),
                        new org.web3j.abi.datatypes.Address(160, spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> alreadyReward() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALREADYREWARD,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> balanceOf_multi(List<String> accounts) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF_MULTI,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(accounts, org.web3j.abi.datatypes.Address.class))),
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

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> decreaseAllowance(String spender, BigInteger subtractedValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DECREASEALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(subtractedValue)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> increaseAllowance(String spender, BigInteger addedValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INCREASEALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(addedValue)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> initFoundations(String foundations, BigInteger initSupply) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITFOUNDATIONS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, foundations),
                        new org.web3j.abi.datatypes.generated.Uint256(initSupply)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateFoundations(String newFoundations) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MIGRATEFOUNDATIONS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newFoundations)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> rewardMiner(String miner, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REWARDMINER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, miner),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> rewardMiner_multi(List<String> miners, List<BigInteger> values) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REWARDMINER_MULTI,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(miners, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(values, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> symbol() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Erc20Service load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20Service(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Erc20Service load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20Service(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Erc20Service load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Erc20Service(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc20Service load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Erc20Service(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Erc20Service> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String name_, String symbol_, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_),
                new org.web3j.abi.datatypes.Utf8String(symbol_),
                new org.web3j.abi.datatypes.generated.Uint256(totalSupply_)));
        return deployRemoteCall(Erc20Service.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Erc20Service> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String name_, String symbol_, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_),
                new org.web3j.abi.datatypes.Utf8String(symbol_),
                new org.web3j.abi.datatypes.generated.Uint256(totalSupply_)));
        return deployRemoteCall(Erc20Service.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20Service> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String name_, String symbol_, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_),
                new org.web3j.abi.datatypes.Utf8String(symbol_),
                new org.web3j.abi.datatypes.generated.Uint256(totalSupply_)));
        return deployRemoteCall(Erc20Service.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20Service> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String name_, String symbol_, BigInteger totalSupply_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name_),
                new org.web3j.abi.datatypes.Utf8String(symbol_),
                new org.web3j.abi.datatypes.generated.Uint256(totalSupply_)));
        return deployRemoteCall(Erc20Service.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}
