export default {
    common:{
        msg:{
            timeout:"System interface request timeout",
            networkError:"Backend interface connection exception",
            metaMaskNotFound:"Please install metamask wallet first",
            metaMaskNetWorkNotFound:"Please add or switch to Matic Mumbai network first!",
            airdropEventNotStart:"The airdrop event will start on September 1, please look forward to it!",
            notLoginWithMetaMask:"Please login to metamask and try again!",
            LoginWithMetaMaskFailed:"login with metamask failed, please try again!",
            PleaseEnterYourEmail:"Please enter your email",
            PleaseEnterTheCorrectEmailAddress:"Please enter the correct email address",
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
                10:"TX_Transfer",
                11:"TX_Withdraw"
            },
        }
    },
    menus:{
        dashboard:"Dashboard",
        miners:"Miners",
        accounts:"Accounts",
        blocks:"Blocks",
        txs:"Transactions",
        withdraw:"Withdraw",
        login:"Login with MetaMask",
    },
    dashboard: {
        miners: 'Solar Miners',
        totalPowerLow: 'Total Green Power',
        totalChargeVol: 'Total Green Energy Generation',

        uSDBmtMarketPrice: 'AKRE Market Price',
        totalBMTMarket: 'AKRE Market Cap',
        tokenSupply: 'Circulating Supply/Max Supply',
        tokenCirculatingSupply: 'Circulating Supply',
        tokenMaxSupply: 'Max Supply'
    },
    footer:'© 2022 Arkreen Network',
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
            amount:"Amount",
            nonce:"Nonce",
            status:"Status",
            txTime:"Timestamp",
            operation:"Operation"
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
            txTime:"Timestamp",
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
            earningMint:"Reward",
            earningService:"Income",
            power:"Power(kWp)",
            totalEnergyGeneration:"Energy(kWh)",
            createTime:"Create Time",
            /*earningMint:"Total Mining Revenue",
            earningService:"Total Service Revenue",
            power:"Average power generation (kWp)",
            totalEnergyGeneration:"Total Power Generation (kWh)",
            createTime:"Create Time",*/
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
            maker:"Miner Maker",
            locationType:"Location Type",
            latlog:"Latitude Longitude",
            h3index:"H3 Index",
            terminate:"Terminate",
            earningMint:"Total Mining Revenue",
            earningService:"Total Service Revenue",
            power:"Average Output Power",
            totalEnergyGeneration:"Cumulative Energy Generated",
            createTime:"Onboarding Time",
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
