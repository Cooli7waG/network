export default {
    common:{
        msg:{
            timeout:"系统接口请求超时",
            networkError:"后端接口连接异常",
        },
        button:{
            back:"返回"
        },
        constant:{
            accountType: {
                1:"WALLET",
                2:"Miner",
                3:"NETWORK",
                4:"VALIDATOR",
                5:"MAKER"
            },
            minerType:{
                1:"GAME_MINER",
                2:"VIRTUAL_MINER",
                3:"API_MINER",
                4:"LITE_PV_MINER",
                5:"STANDARD_PV_MINER",
            },
            tXType:{
                1:"TX_REGISTER_MINER",
                2:"TX_ONBOARD_MINER",
                3:"TX_TRANSFER_MINER",
                4:"TX_TERMINATE_MINER",
                5:"TX_Airdrop_Miner",
                6:"TX_Claim_Miner",
                7:"TX_Commit_PoGG",
                8:"TX_Report_PoGG",
                9:"TX_Reward_PoGG",
                10:"TX_Transfer"
            },
        }
    },
    menus:{
        dashboard:"Dashboard",
        miners:"Miners",
        accounts:"Accounts",
        blocks:"Blocks",
        txs:"Transactions",
    },
    dashboard: {
        miners: '全网矿机数量',
        totalPowerLow: '全网总功率',
        totalChargeVol: '全网总发电量',

        uSDBmtMarketPrice: '美元计BMT市场价格',
        totalBMTMarket: 'BMT市场总量',
        tokenSupply: 'BMT总流通量'
    },
    txs: {
        path:{
            home:"Home",
            txs:"Transaction list"
        },
        query:{
            searchButton:"搜索",
            searchPlaceHolder:"请输入交易hash或块高"
        },
        table:{
            hash:"交易Hash",
            height:"块高",
            txType:"交易类型",
            txTime:"交易时间"
        },
        msg:{

        }
    },
    txinfo: {
        path:{
            home:"Home",
            txs:"Transaction list",
            info:"交易详情",
        },
        info:{
            hash:"hash",
            status:"状态",
            height:"Block高",
            txType:"交易类型",
            txTime:"交易时间",
            txData:"交易数据"
        },
        msg:{
            noData:"没有找到数据",
        }
    },
    miners: {
        path:{
            home:"Home",
            miners:"Miner list"
        },
        query:{
            searchButton:"搜索",
            searchPlaceHolder:"请输入Miners地址"
        },
        table:{
            address:"Address",
            ownerAddress:"Owner Address",
            minerType:"Miner Type",
            earningMint:"总挖矿收入",
            earningService:"总服务收入",
            power:"平均发电功率(kWp)",
            totalEnergyGeneration:"总发电量（kWh）",
            createTime:"Create Time",
        },
        msg:{

        }
    },
    minerinfo: {
        path:{
            home:"Home",
            miners:"Miner list",
            info:"Miner 详情",
        },
        info:{
            address:"Address",
            minerType:"Miner Type",
            ownerAddress:"Owner Address",
            maker:"Maker",
            locationType:"Location Type",
            latlog:"Latitude Longitude",
            h3index:"H3 Index",
            terminate:"是否终止",
            earningMint:"总挖矿收入",
            earningService:"总服务收入",
            power:"平均发电功率(kWp)",
            totalEnergyGeneration:"总发电量（kWh）",
            createTime:"Create Time",
        },
        msg:{
            noData:"没有找到数据",
        }
    },
    account: {
        path:{
            home:"Home",
            accounts:"Account list"
        },
        query:{
            searchButton:"搜索",
            searchPlaceHolder:"请输入Account地址"
        },
        table:{
            address:"Address",
            accountType:"Account Type",
            balance:"Balance",
            earningMint:"earningMint",
            earningService:"earningService",
            amountMiner:"amount Miner",
            createTime:"Create Time",
        },
        msg:{

        }
    },
    accountinfo: {
        path:{
            home:"Home",
            accounts:"acount list",
            info:"acount 详情",
        },
        info:{
            address:"Address",
            accountType:"Account Type",
            balance:"Balance",
            earningMint:"earningMint",
            earningService:"earningService",
            amountMiner:"amount Miner",
            createTime:"Create Time",
        },
        msg:{
            noData:"没有找到数据",
        }
    },
    block: {
        path:{
            home:"Home",
            blocks:"Block list"
        },
        query:{
            searchButton:"搜索",
            searchPlaceHolder:"请输入block地址"
        },
        table:{
            height:"height",
            amountTransaction:"Transaction 数量",
            blockTime:"blockTime",
            earningMint:"earningMint",
            earningService:"earningService",
            amountMiner:"amount Miner",
            createTime:"Create Time",
        },
        msg:{

        }
    },
}
