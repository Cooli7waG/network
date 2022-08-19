package com.aitos.xenon.core.constant;


public interface BusinessConstants
{
    /**
     * 基金会
     */
    interface ArgonFoundation{
        String web3PublicKey="0xD909b78898AE965C90bb57056da4B18a00582d0E";
        String web3PrivateKey="fcaf466fe5337fb6259245ed23076592d560636924e2f3706ca855f0234d559a";
    }

    /**
     * 设备类型
     */
    interface DeviceMinerType{
        int GAME_MINER =1;
        int VIRTUAL_MINER=2;
        int API_MINER=3;
        int LITE_PV_MINER=4;
        int STANDARD_PV_MINER=5;
    }

    /**
     * 设备空投状态
     */
    interface DeviceAirdropStatus{
        /**
         * 未申领
         */
        int NOT_CLAIM =0;
        /**
         * 已申领
         */
        int CLAIMED=1;
    }

    interface AccountType{
        /**
         * owner账户
         */
        int WALLET =1;
        /**
         * miner账户
         */
        int MINER=2;
        /**
         * 基金会
         */
        int NETWORK =3;

        int VALIDATOR=4;

        int MAKER=5;
    }

    /**
     * 交易类型
     */
    interface TXType{
        /**
         * TX_Register_Miner
         */
        int TX_REGISTER_MINER=1;

        /**
         * TX_Onboard_Miner
         */
        int TX_ONBOARD_MINER=2;

        /**
         * TX_Transfer_Miner
         */
        int TX_TRANSFER_MINER=3;

        /**
         * TX_Terminate_Miner
         */
        int TX_TERMINATE_MINER=4;

        /**
         * TX_Airdrop_Miner
         */
        int TX_AIRDROP_MINER=5;

        /**
         * TX_Claim_Miner
         */
        int TX_CLAIM_MINER=6;

        /**
         * TX_Commit_PoGG
         */
        int TX_COMMIT_POGG=7;

        /**
         * TX_Report_PoGG
         */
        int TX_REPORT_POGG=8;

        /**
         * TX_Reward_PoGG
         */
        int TX_REWARD_POGG=9;

        /**
         * TX_Transfer
         */
        int TX_TRANSFER=10;

    }
    /**
     * pogg commit 状态
     */
    interface POGGCommitStatus{
        /**
         * 未结束
         */
        int NOT_OVER=0;
        /**
         * 已结束
         */
        int OVER=1;
        /**
         * 奖励已计算
         */
        int REWARD_CALCULATED=2;
    }
    /**
     * POGG奖励发放状态
     */
    interface POGGRewardStatus{
        /**
         * 未发放
         */
        int UN_ISSUED=0;
        /**
         * 已发放
         */
        int ISSUED=1;
        /**
         * 发放失败
         */
        int ISSUED_FAILED=2;
    }

    interface MakerInfo {
        String GAME_MINER = "aitos";
    }
}
