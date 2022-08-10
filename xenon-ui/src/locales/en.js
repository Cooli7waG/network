export default {
    common:{
        msg:{
            timeout:"System interface request timeout",
            networkError:"Backend interface connection exception",
        },
        button:{
            back:"Back"
        },
        constant:{
            accountType: {
                1:"Wallet",
                2:"Miner",
                3:"Network",
                4:"Validator",
                5:"Maker"
            },
            minerType:{
                1:"Game Miner",
                2:"Virtual Miner",
                3:"Api Miner",
                4:"Lite Pv Miner",
                5:"Standard Pv Miner",
            },
            tXType:{
                1:"TX_Register_miner",
                2:"TX_Onboard_Miner",
                3:"TX_Transfer_Miner",
                4:"TX_Terminate_Miner",
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
        miners: 'The number of mining machines in the whole network',
        totalPowerLow: 'The total power of the whole network',
        totalChargeVol: 'The total power generation of the whole network',

        uSDBmtMarketPrice: 'BMT market price in USD',
        totalBMTMarket: 'Total BMT Market',
        tokenSupply: 'Total BMT Circulation'
    },
    txs: {
        path:{
            home:"Home",
            txs:"Transaction List"
        },
        query:{
            searchButton:"Search",
            searchPlaceHolder:"Please enter transaction hash or block height",
            searchType:{
                1:"Tx Hash",
                2:"Height",
                3:"Address",
            }
        },
        table:{
            hash:"Transaction Hash",
            height:"Block Height",
            txType:"Transaction Type",
            txTime:"Transaction Hour"
        },
        msg:{

        }
    },
    txinfo: {
        path:{
            home:"Home",
            txs:"Transaction list",
            info:"Transaction Details",
        },
        info:{
            hash:"Transaction Hash",
            status:"Status",
            height:"Block Height",
            txType:"Transaction Type",
            txTime:"Transaction Hour",
            txData:"Transaction Data"
        },
        msg:{
            noData:"No data found",
        }
    },
    miners: {
        path:{
            home:"Home",
            miners:"Miner list"
        },
        query:{
            searchButton:"Search",
            searchPlaceHolder:"Please enter Miners address"
        },
        table:{
            address:"Address",
            ownerAddress:"Owner Address",
            minerType:"Miner Type",
            earningMint:"Total Mining Revenue",
            earningService:"Total Service Revenue",
            power:"Average power generation (kWp)",
            totalEnergyGeneration:"Total Power Generation (kWh)",
            createTime:"Create Time",
        },
        msg:{

        }
    },
    minerinfo: {
        path:{
            home:"Home",
            miners:"Miner list",
            info:"Miner Details",
        },
        info:{
            address:"Address",
            minerType:"Miner Type",
            ownerAddress:"Owner Address",
            maker:"Maker",
            locationType:"Location Type",
            latlog:"Latitude Longitude",
            h3index:"H3 Index",
            terminate:"Terminate",
            earningMint:"Total Mining Revenue",
            earningService:"Total Service Revenue",
            power:"Average power generation (kWp)",
            totalEnergyGeneration:"Total Power Generation (kWh)",
            createTime:"Create Time",
        },
        msg:{
            noData:"No data found",
        }
    },
    account: {
        path:{
            home:"Home",
            accounts:"Account list"
        },
        query:{
            searchButton:"Search",
            searchPlaceHolder:"Please enter Account address"
        },
        table:{
            address:"Address",
            accountType:"Account Type",
            balance:"Balance",
            earningMint:"Earning Mint",
            earningService:"Earning Service",
            amountMiner:"Amount Miner",
            createTime:"Create Time",
        },
        msg:{

        }
    },
    accountinfo: {
        path:{
            home:"Home",
            accounts:"Account list",
            info:"Account Details",
        },
        info:{
            address:"Address",
            accountType:"Account Type",
            balance:"Balance",
            earningMint:"Earning Mint",
            earningService:"Earning Service",
            amountMiner:"Amount Miner",
            createTime:"Create Time",
        },
        msg:{
            noData:"No data found",
        }
    },
    block: {
        path:{
            home:"Home",
            blocks:"Block list"
        },
        query:{
            searchButton:"Search",
            searchPlaceHolder:"Please enter the block address"
        },
        table:{
            height:"height",
            amountTransaction:"Transaction quantity",
            blockTime:"Block Time",
            createTime:"Create Time",
        },
        msg:{

        }
    },
}
